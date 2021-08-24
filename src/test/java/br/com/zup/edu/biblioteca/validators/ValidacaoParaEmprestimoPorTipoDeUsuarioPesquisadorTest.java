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

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class ValidacaoParaEmprestimoPorTipoDeUsuarioPesquisadorTest {
    @Autowired
    private ExecutorTransacional executorTransacional;
    private ValidacaoParaEmprestimoPorTipoDeUsuarioPesquisador validator;
    private Usuario jordi;
    private Livro dddDaMassa;
    private Exemplar exemplar;
    private EmprestimoDeExemplar emp;

    @BeforeEach
    void setUp() {

        this.validator=new ValidacaoParaEmprestimoPorTipoDeUsuarioPesquisador(executorTransacional);

        this.jordi = new Usuario("Jordi", TipoUsuario.PESQUISADOR);
        executorTransacional.salvar(jordi);

        this.dddDaMassa = new Livro("DDD da massa", new BigDecimal("230.0"), "978-8550806037");
        executorTransacional.salvar(dddDaMassa);

        this.exemplar=new Exemplar(TipoCirculacao.LIVRE,dddDaMassa);
        executorTransacional.salvar(exemplar);

        this.emp=new EmprestimoDeExemplar(exemplar,jordi,54);
        executorTransacional.salvar(emp);



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
    public void naoDeveTerExemplaresDisponiveisParaEntrega() {


        CadastroEmprestimoDeExemplarRequest request = new CadastroEmprestimoDeExemplarRequest(45, dddDaMassa.getId(), jordi.getId());

        Errors errors = new BeanPropertyBindingResult(request, "request");
        validator.handler(errors, request);


        String field = "idLivro";
        String msg = "Nao ah exemplares disponiveis";
        FieldError fieldError = errors.getFieldError();

        assertEquals(field, fieldError.getField());
        assertEquals(msg, fieldError.getDefaultMessage());

    }

}