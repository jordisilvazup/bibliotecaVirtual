package br.com.zup.edu.biblioteca.validators;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD,ElementType.ANNOTATION_TYPE})
@Constraint(validatedBy = EnumValidator.class)
public @interface ExisteEntradaNoEnum {
    String message() default "Informado não existe";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
    Class<? extends Enum<?>> targetClassType() ;

}
