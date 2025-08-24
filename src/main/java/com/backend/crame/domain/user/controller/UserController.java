package com.backend.crame.domain.user.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.backend.crame.domain.user.dto.ChangeUserRequest;
import com.backend.crame.domain.user.dto.NewPasswordRequest;
import com.backend.crame.domain.user.dto.SignInRequest;
import com.backend.crame.domain.user.dto.SignUpRequest;
import com.backend.crame.domain.token.entity.CustomPrincipal;
import com.backend.crame.domain.user.dto.UserIdResponse;
import com.backend.crame.domain.user.dto.UserInfoResponse;
import com.backend.crame.domain.user.dto.UserUuidResponse;
import com.backend.crame.domain.user.service.UserService;
import com.backend.crame.global.response.BaseResponse;
import com.backend.crame.global.response.dto.ResponseDto;
import com.backend.crame.global.response.enums.SuccessCode;
import com.backend.crame.domain.token.dto.TokenResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;


@RestController
@RequestMapping("/api/v1/auth/user")
@Tag(name = "유저관련 API", description = "유저 관련 API입니다.")
@RequiredArgsConstructor
public class UserController {

	private final UserService userService;

	@PutMapping("/social/signup")
	@Operation(summary = "소셜 회원가입 API")
	public Mono<ResponseEntity<ResponseDto<TokenResponse>>> completeSignup(@RequestBody SignUpRequest request) {
		return userService.completeSignup(request)
			.map(data -> BaseResponse.success(SuccessCode.SIGNUP_SUCCESS, data));
	}

	@PostMapping("/local/signup")
	@Operation(summary = "일반 회원가입 API")
	public Mono<ResponseEntity<ResponseDto<TokenResponse>>> signUp(@RequestBody SignUpRequest request) {
		return userService.signUp(request)
			.map(data -> BaseResponse.success(SuccessCode.SIGNUP_SUCCESS, data));
	}

	//일반 로그인
	@PostMapping("/local/signIn")
	@Operation(summary = "일반 로그인 API")
	public Mono<ResponseEntity<ResponseDto<TokenResponse>>> signIn(@RequestBody SignInRequest request) {
		return userService.localLogin(request)
			.map(data -> BaseResponse.success(SuccessCode.LOGIN_SUCCESS, data));
	}

	//회원 정보 가져오기
	@GetMapping("/info")
	@Operation(summary = "사용자 정보 가져오기 API")
	public Mono<ResponseEntity<ResponseDto<UserInfoResponse>>> getInfo(@AuthenticationPrincipal CustomPrincipal customPrincipal) {
		return userService.getInfo(customPrincipal)
			.map(data -> BaseResponse.success(SuccessCode.GETINFO_SUCCESS, data));
	}

	//토큰 재발급
	@PostMapping("/newToken")
	@Operation(summary = "토큰 재발급 API")
	public Mono<ResponseEntity<ResponseDto<TokenResponse>>> makeNewToken(@AuthenticationPrincipal CustomPrincipal customPrincipal){
		return userService.getNewToken(customPrincipal)
			.map(data -> BaseResponse.success(SuccessCode.NEWTOKEN_SUCCESS, data));
	}

	//아이디 찾기
	//등록한 이메일로 찾기
	@GetMapping("/id")
	@Operation(summary = "ID 찾기 API")
	public Mono<ResponseEntity<ResponseDto<UserIdResponse>>> getId(@RequestParam String email){
		return userService.getId(email)
			.map(data -> BaseResponse.success(SuccessCode.IDFOUND_SUCCESS, data));
	}

	//비밀 번호 찾기 -> 얘는 제대로 된 사람인지 검증하고 -> 맞으면
	@GetMapping("/password/before")
	@Operation(summary = "비밀번호 찾기 API -> 이걸 통해서 등록된 유저인지 검증 -> /password/after로 비밀번호 변경")
	public Mono<ResponseEntity<ResponseDto<UserUuidResponse>>> getPassword(@RequestParam String name, @RequestParam String loginId){
		return userService.getPassword(name,loginId)
			.map(data -> BaseResponse.success(SuccessCode.PASSWORD_BEFORE_SUCCESS, data));
	}

	//비밀번호 변경
	@PostMapping("/password/after")
	@Operation(summary = "비밀번호 변경 API")
	public Mono<ResponseEntity<ResponseDto<Void>>> makeNewPassWord(@RequestBody NewPasswordRequest request){
		return userService.makeNewPassword(request)
			.map(data -> BaseResponse.success(SuccessCode.PASSWORD_BEFORE_SUCCESS, data));
	}

	@DeleteMapping("/logout")
	@Operation(summary = "로그아웃 API")
	public Mono<ResponseEntity<ResponseDto<Void>>> logout(@AuthenticationPrincipal CustomPrincipal customPrincipal){
		return userService.logOut(customPrincipal)
			.map(data->BaseResponse.success(SuccessCode.LOGOUT_SUCCESS,data));
	}


	@DeleteMapping("/signout")
	@Operation(summary = "회원탈퇴 API")
	public Mono<ResponseEntity<ResponseDto<Void>>> signOut(@AuthenticationPrincipal CustomPrincipal customPrincipal){
		return userService.signOut(customPrincipal)
			.map(data->BaseResponse.success(SuccessCode.SIGNOUT_SUCCESS,data));
	}

	@PatchMapping("")
	@Operation(summary = "회원정보 수정 API")
	public Mono<ResponseEntity<?>> changeUserInfo(@AuthenticationPrincipal CustomPrincipal customPrincipal,@RequestBody
		ChangeUserRequest request){
		return userService.changeUserInfo(customPrincipal,request)
			.map(data->BaseResponse.success(SuccessCode.INFOCHANGE_SUCCESS,data));
	}

}
