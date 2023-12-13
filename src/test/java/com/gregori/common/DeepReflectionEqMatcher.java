package com.gregori.common;

import org.mockito.ArgumentMatcher;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.argThat;

public class DeepReflectionEqMatcher<T> implements ArgumentMatcher<T>
{
	private final T expected;
	private final String[] excludedFields;

	public DeepReflectionEqMatcher(T expected, String... excludeFields) {
		this.expected = expected;
		this.excludedFields = excludeFields;
	}

	@Override
	public boolean matches(Object argument) {
		try {
			assertThat(argument)
				.usingRecursiveComparison()
				.ignoringFields(excludedFields)
				.isEqualTo(expected);
			return true;
		} catch (Throwable e) {
			return false;
		}
	}

	public static <T> T deepRefEq(T value, String... excludeFields) {
		return argThat(new DeepReflectionEqMatcher<T>(value, excludeFields));
	}
}
