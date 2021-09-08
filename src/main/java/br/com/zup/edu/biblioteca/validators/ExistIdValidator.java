package br.com.zup.edu.biblioteca.validators;

import javax.persistence.EntityManager;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ExistIdValidator implements ConstraintValidator<ExistId,Long> {
    private final EntityManager manager;
    private String domainClass;


    public ExistIdValidator(EntityManager manager) {
        this.manager = manager;
    }

    @Override
    public void initialize(ExistId constraintAnnotation) {
        this.domainClass=constraintAnnotation.domainClass().getSimpleName();
    }

    @Override
    public boolean isValid(Long id, ConstraintValidatorContext constraintValidatorContext) {
        String existeId="SELECT r FROM "+domainClass+" r WHERE r.id=:id";
        return !manager.createQuery(existeId)
                .setParameter("id", id)
                .getResultList()
                .isEmpty();

    }
}
