package br.com.zup.edu.biblioteca.validators;

import br.com.zup.edu.biblioteca.model.Livro;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;
import java.lang.annotation.Annotation;
import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
@DataJpaTest
class UniqueValueValidatorTest {
    @PersistenceContext
    private EntityManager manager;
    @Mock
    private ConstraintValidatorContext context;

    private UniqueValueValidator validator;

    private UniqueValueCaseTest caseTest;

    @BeforeEach
    public void setup(){
        this.validator= new UniqueValueValidator(manager);
        this.caseTest=new UniqueValueCaseTest();
    }

    @Test
    public void oISBNDeveSerUnico(){
        validator.initialize(caseTest);
        final boolean valid = validator.isValid("978-8550800653", context);
        assertTrue(valid);

    }

    @Test
    @Transactional
    public void oISBNNaoDeveSerUnico(){
        final Livro tddComZupEdu = new Livro("TDD com zup edu", new BigDecimal("250.0"), "978-8550800653");
        manager.persist(tddComZupEdu);
        validator.initialize(caseTest);
        final boolean notValid = validator.isValid("978-8550800653", context);
        assertFalse(notValid);

    }

    private static class UniqueValueCaseTest implements UniqueValue{

        @Override
        public String message() {
            return null;
        }

        @Override
        public Class<?>[] groups() {
            return new Class[0];
        }

        @Override
        public Class<? extends Payload>[] payload() {
            return new Class[0];
        }

        @Override
        public Class<?> domainClass() {
            return Livro.class;
        }

        @Override
        public String domainAtribute() {
            return "ISBN";
        }

        @Override
        public Class<? extends Annotation> annotationType() {
            return UniqueValue.class;
        }
    }

}