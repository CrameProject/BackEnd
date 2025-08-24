package com.backend.crame.domain.mail.service;

import reactor.core.publisher.Mono;

public interface MailSender {
	Mono<Void> send(String to, String subject, String body);
}