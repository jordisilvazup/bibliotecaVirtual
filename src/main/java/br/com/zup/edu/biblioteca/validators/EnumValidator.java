package br.com.zup.edu.biblioteca.validators;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.EnumSet;
import java.util.List;
import java.util.stream.Collectors;

public class EnumValidator implements ConstraintValidator<ExisteCirculacao,String> {
    private List<String> allowedValues;
    private  Class<? extends Enum> enumSelected;

    @Override
    public void initialize(ExisteCirculacao targetEnum) {
        this.enumSelected = targetEnum.targetClassType();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        allowedValues =  (List<String>) EnumSet.allOf(enumSelected).stream().map(e -> ((Enum<? extends Enum<?>>) e).name()).collect(Collectors.toList());
        return allowedValues.contains(value);
    }
}
