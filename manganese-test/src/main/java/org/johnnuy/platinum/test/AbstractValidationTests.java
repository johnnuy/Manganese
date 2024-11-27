package org.johnnuy.platinum.test;


import java.util.Iterator;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

@TestInstance(Lifecycle.PER_CLASS)
public abstract class AbstractValidationTests {

	protected Validator validator = null;

	@BeforeAll
	public void init() {
		validator = Validation.buildDefaultValidatorFactory().getValidator();
	}

	protected <T> void assertContains(String message, Set<ConstraintViolation<T>> results) {
		Iterator<ConstraintViolation<T>> iter = results.iterator();
		while (iter.hasNext()) {
			ConstraintViolation<?> cv = iter.next();
			if (StringUtils.equals(message, cv.getMessageTemplate())) {
				return;
			}
		}
		Assertions.fail(message + " was not contained in " + results);
	}	
}
