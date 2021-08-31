package br.com.zup.edu.biblioteca.validators;

import br.com.zup.edu.biblioteca.controller.requests.CadastroDevolucaoLivroRequest;
import br.com.zup.edu.biblioteca.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class DevolucaoDeEmprestimoValidatorTest {
    @PersistenceContext
    private EntityManager manager;
    private DevolucaoDeEmprestimoValidator validator;
    private Usuario jordi;
    private Livro dddDaMassa;
    private Exemplar exemplar1;
    private EmprestimoDeExemplar emp;


    @BeforeEach
    void setUp() {

        this.validator = new DevolucaoDeEmprestimoValidator(manager);

        this.jordi = new Usuario("Jordi", TipoUsuario.PESQUISADOR);
        manager.persist(jordi);

        this.dddDaMassa = new Livro("DDD da massa", new BigDecimal("230.0"), "978-8550806037");
        manager.persist(dddDaMassa);

        this.exemplar1 = new Exemplar(TipoCirculacao.LIVRE, dddDaMassa);
        manager.persist(exemplar1);

        this.emp = new EmprestimoDeExemplar(exemplar1, jordi, 1);
        manager.persist(emp);

    }

    @Test
    @Transactional
    void aDevolucaoNaoDeveSerFeitaCasoOUsarioQueEstejaSolicitandoNaoSejaOMesmoDoEmprestimo() {

        Usuario kaio=new Usuario("kaio",TipoUsuario.PADRAO);
        manager.persist(kaio);

        CadastroDevolucaoLivroRequest request= new CadastroDevolucaoLivroRequest(kaio.getId(), emp.getId());

        Errors errors = new BeanPropertyBindingResult(request, "request");
        validator.validate(request,errors);

        FieldError fieldError=errors.getFieldError();

        assertEquals("idUsuario",fieldError.getField());
        assertEquals("Este emprestimo não pertence a este usuario.",fieldError.getDefaultMessage());


    }
    @Test
    @Transactional
    void aDevolucaoNaoDeveSerFeitaCasoOEmprestimoJaFoiRealizado() {

        Devolucao devolucao= new Devolucao(emp,jordi);
        manager.persist(devolucao);


        CadastroDevolucaoLivroRequest request= new CadastroDevolucaoLivroRequest(jordi.getId(), emp.getId());

        Errors errors = new BeanPropertyBindingResult(request, "request");
        validator.validate(request,errors);

        FieldError fieldError=errors.getFieldError();

        assertEquals("idEmprestimo",fieldError.getField());
        assertEquals("Já existe devolucao para este emprestimo.",fieldError.getDefaultMessage());


    }
}