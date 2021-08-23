package br.com.zup.edu.biblioteca.validators;

import br.com.zup.edu.biblioteca.controller.requests.CadastroEmprestimoDeExemplarRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.List;

@Component
public class EmprestimoDeExemplarValidator implements Validator {
    @Autowired
    private List<ValidacaoEmprestimo> validators;


    @Override
    public boolean supports(Class<?> aClass) {
        return aClass.isAssignableFrom(CadastroEmprestimoDeExemplarRequest.class);
    }

    @Override
    public void validate(Object o, Errors errors) {
        //1
        if (errors.hasErrors()) {
            return;
        }

        CadastroEmprestimoDeExemplarRequest request = (CadastroEmprestimoDeExemplarRequest) o;
        validators.forEach(c -> c.handler(errors, request));


    }
}
