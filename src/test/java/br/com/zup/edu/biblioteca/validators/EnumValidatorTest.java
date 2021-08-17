package br.com.zup.edu.biblioteca.validators;

import br.com.zup.edu.biblioteca.model.TipoCirculacao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;
import java.lang.annotation.Annotation;

import static org.junit.jupiter.api.Assertions.*;

class EnumValidatorTest {
    @Mock
    private ConstraintValidatorContext context;

    private EnumValidator validator;

    private EnumValidatorCaseTest caseTest;

    @BeforeEach
    void setUp() {
        this.validator=new EnumValidator();
        this.caseTest=new EnumValidatorCaseTest();

    }

    @Test
    public void deveExistirOTipoDeCirculacao(){
        this.validator.initialize(caseTest);
        final boolean valid = this.validator.isValid("LIVRE", context);
        assertTrue(valid);
    }
    @Test
    public void naoDeveExistirOTipoDeCirculacao(){
        this.validator.initialize(caseTest);
        final boolean valid = this.validator.isValid("DESCONHECIDO", context);
        assertFalse(valid);
    }

    public static class EnumValidatorCaseTest implements ExisteCirculacao{

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
        public Class<? extends Enum<?>> targetClassType() {
            return TipoCirculacao.class;
        }

        @Override
        public Class<? extends Annotation> annotationType() {
            return ExisteCirculacao.class;
        }
    }

}