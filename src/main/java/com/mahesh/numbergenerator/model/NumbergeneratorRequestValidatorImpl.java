package com.mahesh.numbergenerator.model;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class NumbergeneratorRequestValidatorImpl implements ConstraintValidator<NumbergeneratorRequestValidator, NumbergeneratorRequest> {

    @Override
    public boolean isValid(NumbergeneratorRequest member, ConstraintValidatorContext context) {
        // Custom Validator Implementation to ensure that step is always less than goal
        boolean returnVal = true;
        if (member.getGoal() != null && member.getStep() != null) {
            if (member.getStep().intValue() > member.getGoal().intValue()) {
                returnVal = false;
            }
        }
        return returnVal;
    }

}
