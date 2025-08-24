package com.backend.crame.domain.user.entitiy.terms;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Terms {
		private boolean phonePolicy;
		private boolean creditPolicy;
		private boolean privacyPolicy;
		private boolean marketing;
}
