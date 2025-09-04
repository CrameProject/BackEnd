package com.backend.crame.domain.apikey.entitiy;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import com.backend.crame.domain.quant.entity.AiStrategy;
import com.backend.crame.domain.quant.entity.AlgorithmStrategy;
import com.backend.crame.domain.quant.entity.TradingType;
import com.backend.crame.global.utils.BaseTimeEntity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Document(collection = "apikey")
@Getter
@NoArgsConstructor(access =  AccessLevel.PROTECTED)
public class ApiKey extends BaseTimeEntity {
	@Id
	private String uuid;

	private String userId;

	@Setter
	private String nickname;

	@Field("public_key")
	private String publicKey;

	@Setter
	private String keyVersion;

	private String secretKey;

	@Setter
	@Field("trading_type")
	private TradingType tradingType;

	@Setter
	@Field("algorithm_strategy")
	private AlgorithmStrategy algorithmStrategy;

	@Setter
	@Field("ai_strategy")
	private AiStrategy aiStrategy;

	@Builder
	private ApiKey(String key_uuid, String user_uuid, String nickname, String publicKey, String secretKey,String keyVersion){
		this.uuid = key_uuid;
		this.userId = user_uuid;
		this.nickname = nickname;
		this.publicKey = publicKey;
		this.secretKey = secretKey;
		this.keyVersion = keyVersion;
	}

	// 전략 정보를 문자열로 반환하는 헬퍼 메서드
	public String getStrategyDisplayName() {
		if (tradingType == TradingType.ALGORITHM && algorithmStrategy != null) {
			return tradingType.getDisplayName() + " " + algorithmStrategy.getDisplayName();
		} else if (tradingType == TradingType.AI && aiStrategy != null) {
			return tradingType.getDisplayName() + " " + aiStrategy.getDisplayName();
		}
		return tradingType != null ? tradingType.getDisplayName() : "";
	}

}
