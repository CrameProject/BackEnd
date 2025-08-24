package com.backend.crame.domain.otp.service;

import com.backend.crame.domain.mail.service.MailSender;
import com.backend.crame.domain.otp.dto.AuditEvent;
import com.backend.crame.domain.otp.dto.EmailVerificationAudit;
import com.backend.crame.domain.otp.dto.OtpDtos;
import com.backend.crame.domain.otp.properties.OtpProperties;
import com.backend.crame.domain.otp.repository.EmailVerificationAuditRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

@Slf4j
@Service
@RequiredArgsConstructor
public class OtpService {

	private final ReactiveStringRedisTemplate redis;
	private final MailSender mailSender;
	private final EmailVerificationAuditRepository auditRepo;
	private final OtpProperties props;
	private final ObjectMapper mapper = new ObjectMapper();
	private final PasswordEncoder encoder = new BCryptPasswordEncoder(12);

	private String otpKey(String email, String purpose) { return "otp:" + purpose + ":" + email.toLowerCase(); }
	private String cooldownKey(String email, String purpose) { return "otp:cooldown:" + purpose + ":" + email.toLowerCase(); }
	private String rateKeyEmail(String email) { return "otp:rate:email:" + email.toLowerCase(); }
	private String rateKeyIp(String ip) { return "otp:rate:ip:" + ip; }

	private Mono<Void> checkRateLimit(String email, String ip) {
		String ek = rateKeyEmail(email);
		String ik = rateKeyIp(ip == null ? "unknown" : ip);

		int window = props.getRate().getWindowSeconds();
		int max = props.getRate().getMaxPerWindow();

		Mono<Long> e = redis.opsForValue().increment(ek).flatMap(v -> v==1 ? redis.expire(ek, Duration.ofSeconds(window)).thenReturn(v) : Mono.just(v));
		Mono<Long> i = redis.opsForValue().increment(ik).flatMap(v -> v==1 ? redis.expire(ik, Duration.ofSeconds(window)).thenReturn(v) : Mono.just(v));

		return Mono.zip(e,i).flatMap(t -> {
			if (t.getT1() > max || t.getT2() > max) {
				return logAudit(email, null, AuditEvent.RATE_LIMIT, "exceed " + max, ip, null)
					.then(Mono.error(new IllegalStateException("요청이 너무 많습니다. 잠시 후 다시 시도해주세요.")));
			}
			return Mono.empty();
		});
	}

	public Mono<Void> requestCode(OtpDtos.Request req, String ip, String userAgent) {
		String email = req.email();
		String purpose = "SIGN_UP";

		String code = generateCode();
		String codeHash = encoder.encode(code);

		String mainKey = otpKey(email, purpose);
		String cdKey   = cooldownKey(email, purpose);

		int ttl  = props.getTtlSeconds();
		int cool = props.getCooldownSeconds();

		Map<String,String> payload = Map.of("codeHash", codeHash, "attempts", "0");
		String json;
		try { json = mapper.writeValueAsString(payload); } catch (Exception e) { return Mono.error(e); }

		return checkRateLimit(email, ip)
			// 원본 스트림: Mono<String>
			.then(redis.opsForValue().get(cdKey))
			// 타입 강제 제거 (굳이 <String> 붙이지 말기)
			.flatMap(v -> Mono.error(new IllegalStateException("조금 뒤에 다시 요청해주세요.")))
			// 비어있을 때 실행되는 분기도 최종적으로 Mono<String>을 반환하게 만든다
			.switchIfEmpty(
				redis.opsForValue().set(mainKey, json, Duration.ofSeconds(ttl))
					.then(redis.opsForValue().set(cdKey, "1", Duration.ofSeconds(cool)))
					.then(logAudit(email, purpose, AuditEvent.REQUESTED, null, ip, userAgent))
					.then() // ★ EmailVerificationAudit -> 버리기 (Mono<Void>로)
					.then(mailSender.send(
						email,
						"[이메일 인증] 코드 안내",
						"""
						<html><body style="font-family:system-ui,Arial,Apple SD Gothic Neo;">
						  <div style="max-width:560px;margin:0 auto;padding:24px;border:1px solid #eee;border-radius:12px;">
							<h2 style="margin:0 0 12px 0;">이메일 인증 코드</h2>
							<p style="color:#666;margin:0 0 16px 0;">아래 6자리 코드를 입력해 주세요.</p>
							<div style="font-size:28px;font-weight:700;letter-spacing:6px;padding:16px 12px;border:1px dashed #ccc;border-radius:8px;text-align:center;">
							  %s
							</div>
							<p style="color:#666;margin:16px 0 4px 0;">유효 시간: <b>%d분</b></p>
							<p style="color:#999;font-size:12px;margin:0;">본 메일은 발신 전용입니다.</p>
						  </div>
						</body></html>
						""".formatted(code, ttl/60)
					))
					.then(logAudit(email, purpose, AuditEvent.SENT, null, ip, userAgent))
					.then()
					.thenReturn("OK")
			)
			.then();
	}

	public Mono<OtpDtos.BooleanResp> confirmCode(OtpDtos.Confirm req, String ip, String userAgent) {
		String email = req.email();
		String purpose = "SIGN_UP";
		String input = req.code();

		String mainKey = otpKey(email, purpose);

		return redis.opsForValue().get(mainKey)
			.switchIfEmpty(Mono.defer(() ->
				logAudit(email, purpose, AuditEvent.EXPIRED, "no key or expired", ip, userAgent)
					.then(Mono.error(new IllegalStateException("인증 요청이 없거나 만료되었습니다.")))))
			.flatMap(json -> {
				try {
					Map<String,String> m = mapper.readValue(json, new TypeReference<>() {});
					int attempts = Integer.parseInt(m.getOrDefault("attempts", "0"));

					if (attempts >= props.getMaxAttempts()) {
						return redis.delete(mainKey)
							.then(logAudit(email, purpose, AuditEvent.FAILED, "max attempts", ip, userAgent))
							.then(Mono.error(new IllegalStateException("시도 횟수를 초과했습니다.")));
					}

					boolean ok = encoder.matches(input, m.get("codeHash"));

					if (ok) {
						String vkey = verifiedKey(email, purpose);
						return redis.delete(mainKey)
							.then(redis.opsForValue().set(vkey, "1", Duration.ofMinutes(15)))
							.then(logAudit(email, purpose, AuditEvent.CONFIRMED, null, ip, userAgent))
							.thenReturn(new OtpDtos.BooleanResp(true, "인증 성공"));
					} else {
						m.put("attempts", String.valueOf(attempts + 1));
						String updated = mapper.writeValueAsString(m);
						return redis.opsForValue().set(mainKey, updated)  // TTL 유지
							.then(logAudit(email, purpose, AuditEvent.FAILED, "wrong code", ip, userAgent))
							.thenReturn(new OtpDtos.BooleanResp(false, "코드가 일치하지 않습니다."));
					}
				} catch (Exception e) {
					return Mono.error(e);
				}
			});
	}

	private String generateCode() {
		int x = ThreadLocalRandom.current().nextInt(0, 1_000_000);
		return String.format("%06d", x);
	}

	private Mono<EmailVerificationAudit> logAudit(
		String email, String purpose, AuditEvent event, String reason, String ip, String ua) {
		return auditRepo.save(EmailVerificationAudit.builder()
			.email(email)
			.purpose(purpose)
			.event(event)
			.reason(reason)
			.ip(ip)
			.userAgent(ua)
			.createdAt(Instant.now())
			.build());
	}

	private String verifiedKey(String email, String purpose) {
		return "otp:verified:" + purpose + ":" + email.toLowerCase();
	}
}
