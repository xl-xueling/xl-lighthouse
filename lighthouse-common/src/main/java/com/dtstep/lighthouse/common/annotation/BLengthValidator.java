package com.dtstep.lighthouse.common.annotation;

import com.dtstep.lighthouse.common.util.StringUtil;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class BLengthValidator implements ConstraintValidator<BLengthValidation, String> {

    private int max;

    private int min;

    @Override
    public void initialize(BLengthValidation constraintAnnotation) {
        max = constraintAnnotation.max();
        min = constraintAnnotation.min();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        int length = StringUtil.getBLen(value);
        return length >= min && length <= max;
    }
}
