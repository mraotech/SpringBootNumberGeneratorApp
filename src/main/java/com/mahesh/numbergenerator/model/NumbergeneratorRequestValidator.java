package com.mahesh.numbergenerator.model;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Documented
@Constraint(validatedBy = {NumbergeneratorRequestValidatorImpl.class})
@Target({ TYPE })
@Retention(RUNTIME)
public @interface NumbergeneratorRequestValidator {
    String message() default "Step value must be less than the goal value";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
