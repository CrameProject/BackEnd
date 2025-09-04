package com.backend.crame.domain.user.utils;

import java.time.LocalDate;

public class BirthdateParser {

	public static LocalDate parseToLocalDate(String input) {
		if (input == null) throw new IllegalArgumentException("input is null");

		// 숫자만 추출
		String digits = input.replaceAll("\\D", "");
		if (digits.length() < 7) {
			throw new IllegalArgumentException("형식 오류: 최소 7자리(YYMMDD + 뒷자리 한자리)가 필요합니다.");
		}

		String yymmdd = digits.substring(0, 6);
		char code = digits.charAt(6);

		int yy  = Integer.parseInt(yymmdd.substring(0, 2));
		int mm  = Integer.parseInt(yymmdd.substring(2, 4));
		int dd  = Integer.parseInt(yymmdd.substring(4, 6));
		int century;

		switch (code) {
			case '1': case '2': case '5': case '6':
				century = 1900; break;
			case '3': case '4': case '7': case '8':
				century = 2000; break;
			case '9': case '0':
				century = 1800; break;
			default:
				throw new IllegalArgumentException("알 수 없는 세기 코드: " + code);
		}

		int yyyy = century + yy;
		try {
			return LocalDate.of(yyyy, mm, dd);
		} catch (Exception e) {
			throw new IllegalArgumentException("유효하지 않은 날짜입니다: " + yyyy + "-" + mm + "-" + dd, e);
		}
	}
}
