package br.com.zup.edu.biblioteca.validators;

import br.com.zup.edu.biblioteca.controller.requests.CadastroEmprestimoDeExemplarRequest;
import br.com.zup.edu.biblioteca.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.assertEquals;
@SpringBootTest
class AtrasoDeEntregaDeExemplarValidatorTest {
    @PersistenceContext
    private EntityManager manager;
    private AtrasoDeEntregaDeExemplarValidator validator;
    private Usuario jordi;
    private Livro dddDaMassa;
    private Exemplar exemplar1;
    private Exemplar exemplar2;
    private EmprestimoDeExemplar emp;

    @BeforeEach
    void setUp() {

        this.validator = new AtrasoDeEntregaDeExemplarValidator(manager);

        this.jordi = new Usuario("Jordi", TipoUsuario.PESQUISADOR);
        manager.persist(jordi);

        this.dddDaMassa = new Livro("DDD da massa", new BigDecimal("230.0"), "978-8550806037");
        manager.persist(dddDaMassa);

        this.exemplar1 = new Exemplar(TipoCirculacao.LIVRE, dddDaMassa);
        this.exemplar2 = new Exemplar(TipoCirculacao.LIVRE, dddDaMassa);
        manager.persist(exemplar1);
        manager.persist(exemplar2);

        this.emp = new EmprestimoDeExemplar(exemplar1, jordi, 1);
        manager.persist(emp);


    }

    @Test
    @Transactional
    void naoDeveEmprestarUmLivroCasoOLocatarioEstejaComLivroAtrasados() {
        LocalDate antesDeOntem = LocalDate.now().minusDays(2);

        ReflectionTestUtils.setField(emp, "emprestadoEm", antesDeOntem);

        CadastroEmprestimoDeExemplarRequest request= new CadastroEmprestimoDeExemplarRequest(2, dddDaMassa.getId(), jordi.getId());
        Errors errors = new BeanPropertyBindingResult(request, "request");
        validator.handler(errors,request);
        String field = "idUsuario";
        String msg = "Usuarios com atraso não podem realizar novos emprestimos.";

        assertEquals(field,errors.getFieldError().getField());
        assertEquals(msg,errors.getFieldError().getDefaultMessage());

    }
}