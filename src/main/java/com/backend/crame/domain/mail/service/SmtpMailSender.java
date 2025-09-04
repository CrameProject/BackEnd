package com.backend.crame.domain.mail.service;


import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import com.backend.crame.domain.mail.properties.MailProperties;

import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Slf4j
@Component
@RequiredArgsConstructor
public class SmtpMailSender implements MailSender {

	private final JavaMailSender javaMailSender;
	private final MailProperties props;

	@Override
	public Mono<Void> send(String to, String subject, String body) {
		return Mono.fromRunnable(() -> {
			try {
				MimeMessage mime = javaMailSender.createMimeMessage();
				MimeMessageHelper helper = new MimeMessageHelper(mime, false, "UTF-8");

				if (props.getFromPersonal() != null && !props.getFromPersonal().isBlank()) {
					mime.setFrom(new InternetAddress(props.getFrom(), props.getFromPersonal(), "UTF-8"));
				} else {
					mime.setFrom(props.getFrom());
				}

				helper.setTo(to);
				helper.setSubject(subject);
				boolean html = looksLikeHtml(body);
				helper.setText(body, html);

				javaMailSender.send(mime);
				log.info("📧 Gmail SMTP sent to {}", to);
			} catch (Exception e) {
				log.error("SMTP send failed: {}", e.getMessage(), e);
				throw new RuntimeException("메일 발송 실패: " + e.getMessage(), e);
			}
		}).subscribeOn(Schedulers.boundedElastic()).then();
	}

	private boolean looksLikeHtml(String t) {
		if (t == null) return false;
		String s = t.toLowerCase();
		return s.contains("<html") || s.contains("</") || s.contains("<p") || s.contains("<br");
	}
}
