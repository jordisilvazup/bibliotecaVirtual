package br.com.zup.edu.biblioteca.validators;

import br.com.zup.edu.biblioteca.controller.requests.CadastroEmprestimoDeExemplarRequest;
import br.com.zup.edu.biblioteca.model.EmprestimoDeExemplar;
import br.com.zup.edu.biblioteca.model.Usuario;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;

@Component
public class AtrasoDeEntregaDeExemplarValidator implements ValidacaoEmprestimo {
    private final EntityManager manager;

    public AtrasoDeEntregaDeExemplarValidator(EntityManager manager) {
        this.manager = manager;
    }

    @Override
    @Transactional
    public Errors handler(Errors errors, CadastroEmprestimoDeExemplarRequest request) {

        String todosEmprestimosDoUsuario = "SELECT e FROM EmprestimoDeExemplar e where e.usuario.id=:usuario";

        List<EmprestimoDeExemplar> emprestimoDeExemplarList = manager.createQuery(todosEmprestimosDoUsuario, EmprestimoDeExemplar.class)
                .setParameter("usuario", request.getIdUsuario())
                .getResultList();

        boolean existeAlgumLivroComAtraso = emprestimoDeExemplarList.stream()
                .anyMatch(e -> e.getEmprestadoEm()
                        .plusDays(e.getTempoDeEmprestimoEmDias().longValue())
                        .isAfter(LocalDate.now())
                );
        if(existeAlgumLivroComAtraso)
            errors.rejectValue("idUsuario",null,"Usuarios com atraso não podem realizar novos emprestimos.");

        return errors;
    }
}
