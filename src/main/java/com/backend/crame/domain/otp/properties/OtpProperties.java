package com.backend.crame.domain.otp.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;

@Getter
@Configuration
@ConfigurationProperties(prefix = "otp")
public class OtpProperties {
	private int ttlSeconds;
	private int cooldownSeconds;
	private int maxAttempts;
	private Rate rate = new Rate();

	@Getter
	public static class Rate {
		private int windowSeconds;
		private int maxPerWindow;
		public void setWindowSeconds(int s){ this.windowSeconds = s; }
		public void setMaxPerWindow(int m){ this.maxPerWindow = m; }
	}

	public void setTtlSeconds(int s){ this.ttlSeconds = s; }
	public void setCooldownSeconds(int s){ this.cooldownSeconds = s; }
	public void setMaxAttempts(int m){ this.maxAttempts = m; }
}