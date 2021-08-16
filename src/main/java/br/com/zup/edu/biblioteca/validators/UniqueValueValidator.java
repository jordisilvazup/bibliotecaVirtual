package br.com.zup.edu.biblioteca.validators;

import javax.persistence.EntityManager;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class UniqueValueValidator implements ConstraintValidator<UniqueValue,String> {
    private final EntityManager manager;
    private String className;
    private String atributeName;

    public UniqueValueValidator(EntityManager manager) {
        this.manager = manager;
    }

    @Override
    public void initialize(UniqueValue constraintAnnotation) {
        this.className=constraintAnnotation.domainClass().getSimpleName();
        this.atributeName= constraintAnnotation.domainAtribute();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
        String jpql="SELECT r FROM "+className+" r where "+atributeName+" =: value";
        return manager.createQuery(jpql).setParameter("value",value).getResultList().isEmpty();
    }
}
