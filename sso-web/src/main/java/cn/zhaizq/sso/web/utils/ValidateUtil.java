package cn.zhaizq.sso.web.utils;

import cn.zhaizq.sso.common.exception.BusinessException;
import org.hibernate.validator.HibernateValidator;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Set;

public class ValidateUtil {
    private final static Validator validator;

    static {
        validator = Validation
                .byProvider(HibernateValidator.class)
                .configure()
                .failFast(true) // true:快速模式 false:普通模式
                .buildValidatorFactory()
                .getValidator();
    }

    public static void validate(Object value) throws BusinessException {
        Set<ConstraintViolation<Object>> result = validator.validate(value);

        if (result == null || result.isEmpty())
            return;

        for (ConstraintViolation<Object> c : result)
            throw new BusinessException("字段[" + c.getPropertyPath() + "]" + c.getMessage());
    }
}