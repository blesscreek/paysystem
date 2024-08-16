package com.bless.paysystempayment.service;

import com.bless.paysystemcore.exception.BizException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.Set;

/**
 * @Author bless
 * @Version 1.0
 * @Description 通用 Validator
 * @Date 2024-08-10 11:30
 */
@Service
public class ValidateService {
    @Autowired
    private Validator validator;

    public void validate(Object obj) {
        Set<ConstraintViolation<Object>> resultSet = validator.validate(obj);
        if(resultSet == null || resultSet.isEmpty()){
            return ;
        }
        resultSet.stream().forEach(item -> {throw new BizException(item.getMessage());});
    }

}
