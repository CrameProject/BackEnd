package com.backend.crame.domain.mail.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;

@Getter
@Configuration
@ConfigurationProperties(prefix = "app.mail")
public class MailProperties {
	private String from;
	private String fromPersonal;

	public void setFrom(String from) { this.from = from; }
	public void setFromPersonal(String fromPersonal) { this.fromPersonal = fromPersonal; }
}