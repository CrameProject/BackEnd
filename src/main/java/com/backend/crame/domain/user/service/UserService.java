package com.backend.crame.domain.user.service;


import java.time.LocalDate;
import java.util.UUID;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.backend.crame.domain.user.dto.ChangeUserRequest;
import com.backend.crame.domain.user.dto.SignInRequest;
import com.backend.crame.domain.user.dto.SignUpRequest;
import com.backend.crame.domain.token.entity.CustomPrincipal;
import com.backend.crame.domain.user.dto.UserInfoResponse;
import com.backend.crame.domain.user.entitiy.Domain;
import com.backend.crame.domain.user.entitiy.User;
import com.backend.crame.domain.user.entitiy.UserRole;
import com.backend.crame.domain.user.entitiy.UserStatus;
import com.backend.crame.domain.user.repository.UserRepository;
import com.backend.crame.domain.user.utils.BirthdateParser;
import com.backend.crame.global.exception.BaseException;
import com.backend.crame.global.exception.ErrorCode;
import com.backend.crame.domain.token.dto.TokenResponse;
import com.backend.crame.domain.token.repository.RefreshTokenRepository;
import com.backend.crame.domain.token.service.JwtTokenProvider;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class UserService {

	private final UserRepository userRepository;
	private final RefreshTokenRepository refreshTokenRepository;
	private final JwtTokenProvider jwtTokenProvider;
	private final PasswordEncoder passwordEncoder;


	//일반 로그인
	public Mono<TokenResponse> localLogin(SignInRequest request){
		final String loginId = request.id();
		final String rawPassword = request.password();

		return userRepository.findByLoginId(loginId)
			.switchIfEmpty(Mono.defer(() -> {
				passwordEncoder.matches(rawPassword, "$2a$12$5bpwOA4iJcQ8G9l7q1lT3ORbH1s0e3F3m3s2b8m8cVZDRv2lKk2qC");
				return Mono.error(new BaseException(ErrorCode.LOGIN_FAIL));
			}))
			.flatMap(user -> {
				if (user.getDomain() != Domain.LOCAL) {
					return Mono.error(new BaseException(ErrorCode.NOT_LOCAL_ACCOUNT));
				}
				if (user.getStatus() == UserStatus.DELETED) {
					return Mono.error(new BaseException(ErrorCode.ALREADY_SIGNOUT_USER));
				}
				if (user.getStatus() == UserStatus.PENDING) {
					return Mono.error(new BaseException(ErrorCode.SIGNUP_INCOMPLETE));
				}

				if (!passwordEncoder.matches(rawPassword, user.getPassword())) {
					return Mono.error(new BaseException(ErrorCode.LOGIN_FAIL));
				}
				return jwtTokenProvider.createToken(user.getName(),user.getUser_uuid(),user.getUserRole().toString(),user.getDomain().toString());
			});
	}

	//토큰 재발급

	//유저 정보 조회
	public Mono<UserInfoResponse> getInfo(CustomPrincipal customPrincipal){
		return userRepository.findById(customPrincipal.getUserId())
			.map(user->{
				boolean isSocial = user.getDomain() != Domain.LOCAL;
				return new UserInfoResponse(user.getName(),user.getBirthDate(),user.getLoginId(),user.getEmail(),isSocial);
			});
	}

	//아이디 찾기 -> 아마 이름 + 생년월일로 찾아야하지 않을까

	//비밀번호 찾기 -> 이름 + 이메일? 로 찾기 아니면 이메일로만 찾기


	// 회원가입 완료 처리
	public Mono<TokenResponse> completeSignup(SignUpRequest request) {

		return userRepository.findByEmail(request.email())
			.switchIfEmpty(Mono.defer(() -> {
				return Mono.error(new BaseException(ErrorCode.SIGNUP_ERROR));
			}))
			.flatMap(user -> {
				user.setName(request.name());
				user.setWallet_uuid(request.walletUuid());
				user.setTerms(request.terms());
				user.setStatus(UserStatus.SUCCESS);
				user.setBirthDate(BirthdateParser.parseToLocalDate(request.birthNumber()));
				//이게 일반 로그인과의 차이 부분임-> 이거 고려해서 나중에 변경하던가 해도 됨
				user.setLoginId("");
				user.setPassword("");
				return userRepository.save(user);
			})
			.flatMap(user -> jwtTokenProvider.createToken(user.getName(),user.getUser_uuid(),user.getUserRole().toString(),user.getDomain().toString()));
	}

	//일반 회원가입하기
	public Mono<TokenResponse> signUp(SignUpRequest request) {
		final String uuid = UUID.randomUUID().toString();
		final String email = request.email();
		final LocalDate dob = BirthdateParser.parseToLocalDate(request.birthNumber());

		return userRepository.findByEmail(email)
			.flatMap(existing -> Mono.error(new BaseException(ErrorCode.ALREADY_REGISTERED_EMAIL)))
			.switchIfEmpty(Mono.defer(() -> {
				User newUser = User.builder()
					.user_uuid(uuid)
					.domain(Domain.LOCAL)
					.birthDate(dob)
					.email(email)
					.loginId(request.id())
					.name(request.name())
					.password(passwordEncoder.encode(request.password()))
					.select_model("")
					.status(UserStatus.SUCCESS)
					.terms(request.terms())
					.userRole(UserRole.ROLE_USER)
					.subscribe(false)
					.wallet_uuid(request.walletUuid())
					.build();

				return userRepository.save(newUser);
			}))
			.cast(User.class)
			.flatMap(saved -> jwtTokenProvider.createToken(saved.getName(), saved.getUser_uuid(),
				saved.getUserRole().toString(), saved.getDomain().toString()));
	}


	public Mono<Void> logOut(CustomPrincipal customPrincipal){
		return refreshTokenRepository.deleteByUserId(customPrincipal.getUserId())
			.switchIfEmpty(Mono.error(new BaseException(ErrorCode.LOGOUT_ERROR)))
			.map(null);
	}

	//회원탈퇴
	public Mono<Void> signOut(CustomPrincipal principal) {
		final String userId = principal.getUserId();

		return refreshTokenRepository.deleteByUserId(userId) // Mono<Void>
			.onErrorMap(e -> new BaseException(ErrorCode.SIGNOUT_ERROR))
			.then(userRepository.findById(userId))
			.switchIfEmpty(Mono.error(new BaseException(ErrorCode.USER_NOT)))
			.flatMap(user -> {
				if (user.getStatus() == UserStatus.DELETED) {
					return Mono.error(new BaseException(ErrorCode.ALREADY_SIGNOUT_USER));
				}
				user.setStatus(UserStatus.DELETED);
				return userRepository.save(user);
			})
			.then();
	}


	// 회원정보 수정
	public Mono<UserInfoResponse> changeUserInfo(CustomPrincipal principal, ChangeUserRequest request) {
		return userRepository.findById(principal.getUserId())
			.switchIfEmpty(Mono.error(new BaseException(ErrorCode.USER_NOT)))
			.flatMap(user -> {
				if (request.name() != null && !request.name().isBlank()) {
					user.setName(request.name().trim());
				}
				if (request.birthDate() != null) {
					user.setBirthDate(request.birthDate());
				}
				return userRepository.save(user);
			})
			.map(saved -> {
				boolean isSocial = saved.getDomain() != Domain.LOCAL;
				return new UserInfoResponse(
					saved.getUser_uuid(),
					saved.getBirthDate(),
					saved.getName(),
					saved.getEmail(),
					isSocial
				);
				}
			);
	}


}
