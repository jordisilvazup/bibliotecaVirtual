package br.com.zup.edu.biblioteca.validators;

import br.com.zup.edu.biblioteca.controller.requests.CadastroEmprestimoDeExemplarRequest;
import br.com.zup.edu.biblioteca.model.*;
import br.com.zup.edu.biblioteca.util.ExecutorTransacional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;


@SpringBootTest
class ValidacaoParaEmprestimoPorTipoDeUsuarioPadraoTest {
    @Autowired
    private ExecutorTransacional executorTransacional;
    private ValidacaoParaEmprestimoPorTipoDeUsuarioPadrao validator;
    private Usuario jordi;
    private Livro dddDaMassa;
    private Exemplar exemplar1;
    private Exemplar exemplar2;
    private Exemplar exemplar3;
    private Exemplar exemplar4;
    private Exemplar exemplar5;
    private EmprestimoDeExemplar emp1;
    private EmprestimoDeExemplar emp2;
    private EmprestimoDeExemplar emp3;
    private EmprestimoDeExemplar emp4;
    private EmprestimoDeExemplar emp5;
    private Errors errors;

    @BeforeEach
    void setUp() {
        this.validator = new ValidacaoParaEmprestimoPorTipoDeUsuarioPadrao(executorTransacional);

        this.jordi = new Usuario("Jordi", TipoUsuario.PADRAO);
        executorTransacional.salvar(jordi);

        dddDaMassa = new Livro("DDD da massa", new BigDecimal("230.0"), "978-8550811765");
        executorTransacional.salvar(dddDaMassa);

        this.exemplar1 = new Exemplar(TipoCirculacao.LIVRE, dddDaMassa);
        this.exemplar2 = new Exemplar(TipoCirculacao.LIVRE, dddDaMassa);
        this.exemplar3 = new Exemplar(TipoCirculacao.LIVRE, dddDaMassa);
        this.exemplar4 = new Exemplar(TipoCirculacao.LIVRE, dddDaMassa);
        this.exemplar5 = new Exemplar(TipoCirculacao.LIVRE, dddDaMassa);

        executorTransacional.salvar(exemplar1);
        executorTransacional.salvar(exemplar2);
        executorTransacional.salvar(exemplar3);
        executorTransacional.salvar(exemplar4);
        executorTransacional.salvar(exemplar5);


        this.emp1 = new EmprestimoDeExemplar(exemplar1, jordi, 45);
        executorTransacional.salvar(emp1);

        this.emp2 = new EmprestimoDeExemplar(exemplar2, jordi, 45);
        executorTransacional.salvar(emp2);

        this.emp3 = new EmprestimoDeExemplar(exemplar3, jordi, 45);
        executorTransacional.salvar(emp3);

        this.emp4 = new EmprestimoDeExemplar(exemplar4, jordi, 45);
        executorTransacional.salvar(emp4);

        this.emp5 = new EmprestimoDeExemplar(exemplar5, jordi, 45);
        executorTransacional.salvar(emp5);
    }

    @Test
    @Transactional
    public void oUsuarioNaoDeveEstaCadastrado() {

        CadastroEmprestimoDeExemplarRequest request = new CadastroEmprestimoDeExemplarRequest(null, dddDaMassa.getId(), 7L);

        Errors errors = new BeanPropertyBindingResult(request, "request");
        validator.handler(errors, request);

        String field = "idUsuario";
        String msg = "Usuario nao cadastrado";
        FieldError fieldError = errors.getFieldError();

        assertEquals(field, fieldError.getField());
        assertEquals(msg, fieldError.getDefaultMessage());

    }
    @Test
    @Transactional
    public void oLivroNaoDeveEstaCadastrado() {

        CadastroEmprestimoDeExemplarRequest request = new CadastroEmprestimoDeExemplarRequest(null, 9L, jordi.getId());

        Errors errors = new BeanPropertyBindingResult(request, "request");
        validator.handler(errors, request);

        String field = "idLivro";
        String msg = "Livro nao cadastrado";
        FieldError fieldError = errors.getFieldError();

        assertEquals(field, fieldError.getField());
        assertEquals(msg, fieldError.getDefaultMessage());

    }

    @Test
    @Transactional
    public void oTempoDeEmprestimoNaoDeveSerNulo() {

        CadastroEmprestimoDeExemplarRequest request = new CadastroEmprestimoDeExemplarRequest(null, dddDaMassa.getId(), jordi.getId());

        Errors errors = new BeanPropertyBindingResult(request, "request");
        validator.handler(errors, request);

        String field = "tempoDeEmprestimoEmDias";
        String msg = "O Emprestimo de exemplares para usuarios do tipo padrao deve conter o tempo que pretende ficar com o livro";
        FieldError fieldError = errors.getFieldError();

        assertEquals(field, fieldError.getField());
        assertEquals(msg, fieldError.getDefaultMessage());

    }

    @Test
    @Transactional
    public void oTempoDeEmprestimoNaoDeveSerMaiorQue60Dias() {

        CadastroEmprestimoDeExemplarRequest request = new CadastroEmprestimoDeExemplarRequest(61, dddDaMassa.getId(), jordi.getId());
        Errors errors = new BeanPropertyBindingResult(request, "request");
        validator.handler(errors, request);

        String field = "tempoDeEmprestimoEmDias";
        String msg = "O Emprestimo de exemplares deve ter no maximo tempo de 60 dias";

        FieldError fieldError = errors.getFieldError();

        assertEquals(field, fieldError.getField());
        assertEquals(msg, fieldError.getDefaultMessage());

    }

    @Test
    @Transactional
    public void oUsuarioNaoDeveTer5LivrosEmprestados() {

        CadastroEmprestimoDeExemplarRequest request = new CadastroEmprestimoDeExemplarRequest(31, dddDaMassa.getId(), jordi.getId());
        Errors errors = new BeanPropertyBindingResult(request, "request");
        validator.handler(errors, request);

        String field = "idUsuario";
        String msg = "Usuarios do tipo padrão podem ter no maximo 5 livros emprestados";

        FieldError fieldError = errors.getFieldError();
        assertEquals(field, fieldError.getField());
        assertEquals(msg, fieldError.getDefaultMessage());

    }

    @Test
    @Transactional
    public void naoDeveTer1ExemplarLiveParaEmprestimo() {

        Usuario jeje = new Usuario("jeje", TipoUsuario.PADRAO);
        executorTransacional.salvar(jeje);
        CadastroEmprestimoDeExemplarRequest request = new CadastroEmprestimoDeExemplarRequest(31, dddDaMassa.getId(), jeje.getId());
        Errors errors = new BeanPropertyBindingResult(request, "request");
        validator.handler(errors, request);

        String field = "idLivro";
        String msg = "Nao ah exemplares disponiveis";

        List<FieldError> fieldErrors = errors.getFieldErrors();
        assertEquals(1, fieldErrors.size());
        assertEquals(msg, fieldErrors.get(0).getDefaultMessage());
        assertEquals(field, fieldErrors.get(0).getField());


    }
}