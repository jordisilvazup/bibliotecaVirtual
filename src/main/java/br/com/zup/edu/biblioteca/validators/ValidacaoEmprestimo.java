package br.com.zup.edu.biblioteca.validators;

import br.com.zup.edu.biblioteca.controller.requests.CadastroEmprestimoDeExemplarRequest;
import org.springframework.validation.Errors;

public interface ValidacaoEmprestimo {

    Errors handler(Errors errors, CadastroEmprestimoDeExemplarRequest request);


}
