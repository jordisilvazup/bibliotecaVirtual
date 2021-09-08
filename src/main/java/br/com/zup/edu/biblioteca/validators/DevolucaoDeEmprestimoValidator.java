package br.com.zup.edu.biblioteca.validators;

import br.com.zup.edu.biblioteca.controller.requests.CadastroDevolucaoLivroRequest;
import br.com.zup.edu.biblioteca.model.Devolucao;
import br.com.zup.edu.biblioteca.model.EmprestimoDeExemplar;
import br.com.zup.edu.biblioteca.model.Usuario;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.util.List;

@Component
public class DevolucaoDeEmprestimoValidator implements Validator {

    private final static String DEVOLUCAO_JA_EXECUTADA = "SELECT d FROM Devolucao d WHERE d.emprestimo=:emprestimo";
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

        CadastroDevolucaoLivroRequest request = (CadastroDevolucaoLivroRequest) o;

        Usuario usuario = manager.find(Usuario.class, request.getIdUsuario());
        EmprestimoDeExemplar emprestimo = manager.find(EmprestimoDeExemplar.class, request.getIdEmprestimo());

        if (!emprestimo.getUsuario().equals(usuario)) {
            errors.rejectValue("idUsuario", null, "Este emprestimo não pertence a este usuario.");
        }

        List<Devolucao> devolucoes = manager.createQuery(DEVOLUCAO_JA_EXECUTADA, Devolucao.class)
                .setParameter("emprestimo", emprestimo)
                .getResultList();


        if (!devolucoes.isEmpty()) {
            errors.rejectValue("idEmprestimo", null, "Já existe devolucao para este emprestimo.");
        }


    }
}
