package br.com.zup.edu.biblioteca.validators;

import br.com.zup.edu.biblioteca.controller.requests.CadastroDevolucaoLivroRequest;
import br.com.zup.edu.biblioteca.model.EmprestimoDeExemplar;
import br.com.zup.edu.biblioteca.model.Usuario;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;

@Component
public class DevolucaoDeEmprestimoValidator implements Validator {
    private final EntityManager manager;

    public DevolucaoDeEmprestimoValidator(EntityManager manager) {
        this.manager = manager;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return aClass.isAssignableFrom(CadastroDevolucaoLivroRequest.class);
    }

    @Override
    public void validate(Object o, Errors errors) {
        CadastroDevolucaoLivroRequest request= (CadastroDevolucaoLivroRequest) o;
        if(errors.hasErrors()){
            return;
        }

        Usuario usuario = manager.find(Usuario.class, request.getIdUsuario());
        EmprestimoDeExemplar emprestimo = manager.find(EmprestimoDeExemplar.class, request.getIdEmprestimo());

        if (emprestimo.getUsuario().equals(usuario)){
            errors.rejectValue("idUsuario",null,"Este emprestimo não pertence a este usuario.");
        }

        String devolucaoJaExecutada="SELECT nullif(d,true) FROM Devolucao d WHERE d.emprestimo=:emprestimo";

        final Boolean existeDevolucaoParaEsteEmprestimo = manager.createQuery(devolucaoJaExecutada, Boolean.class)
                .setParameter("emprestimo", emprestimo)
                .getSingleResult();

        if(existeDevolucaoParaEsteEmprestimo){
            errors.rejectValue("idEmprestimo",null,"Já existe devolucao para este emprestimo.");
        }


    }
}
