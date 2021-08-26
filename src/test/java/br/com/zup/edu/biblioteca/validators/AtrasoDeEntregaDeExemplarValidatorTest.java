package br.com.zup.edu.biblioteca.validators;

import br.com.zup.edu.biblioteca.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class AtrasoDeEntregaDeExemplarValidatorTest {
    @PersistenceContext
    private EntityManager manager;
    private AtrasoDeEntregaDeExemplarValidator validator;
    private Usuario jordi;
    private Livro dddDaMassa;
    private Exemplar exemplar;
    private EmprestimoDeExemplar emp;

    @BeforeEach
    void setUp() {

        this.validator=new AtrasoDeEntregaDeExemplarValidator(manager);

        this.jordi = new Usuario("Jordi", TipoUsuario.PESQUISADOR);
        manager.persist(jordi);

        this.dddDaMassa = new Livro("DDD da massa", new BigDecimal("230.0"), "978-8550806037");
        manager.persist(dddDaMassa);

        this.exemplar=new Exemplar(TipoCirculacao.LIVRE,dddDaMassa);
        manager.persist(exemplar);

        this.emp=new EmprestimoDeExemplar(exemplar,jordi,1);
        manager.persist(emp);



    }

    @Test
    void name() {

    }
}