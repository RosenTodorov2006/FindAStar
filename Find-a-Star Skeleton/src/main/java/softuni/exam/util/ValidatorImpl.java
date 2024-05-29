package softuni.exam.util;

import javax.validation.Validation;
import javax.validation.Validator;

public class ValidatorImpl implements ValidatorInterface{
    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
    @Override
    public <E> boolean isValid(E entity) {
        return validator.validate(entity).isEmpty();
    }
}
