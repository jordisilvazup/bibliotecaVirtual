package br.com.zup.edu.biblioteca.validators;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;

public class EnumValidator implements ConstraintValidator<ExisteEntradaNoEnum,String> {

    private  Class<? extends Enum> enumSelected;

    @Override
    public void initialize(ExisteEntradaNoEnum targetEnum) {

        this.enumSelected = targetEnum.targetClassType();

    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {

        return Arrays.stream(enumSelected.getEnumConstants())
                .anyMatch(e -> e.name().equals(value));
    }
}
