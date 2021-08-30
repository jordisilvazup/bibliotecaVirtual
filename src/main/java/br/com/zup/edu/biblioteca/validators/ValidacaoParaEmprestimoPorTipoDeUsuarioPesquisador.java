package br.com.zup.edu.biblioteca.validators;

import br.com.zup.edu.biblioteca.controller.requests.CadastroEmprestimoDeExemplarRequest;
import br.com.zup.edu.biblioteca.model.Exemplar;
import br.com.zup.edu.biblioteca.model.Livro;
import br.com.zup.edu.biblioteca.model.Usuario;
import br.com.zup.edu.biblioteca.util.ExecutorTransacional;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.Objects;

import static br.com.zup.edu.biblioteca.model.TipoUsuario.PESQUISADOR;
import static javax.persistence.LockModeType.READ;

@Component
//6
public class ValidacaoParaEmprestimoPorTipoDeUsuarioPesquisador implements ValidacaoEmprestimo {

    //1
    private final ExecutorTransacional execTransacional;

    private static final String BUSQUE_EXEMPLAR_LIVRE = "SELECT e FROM Exemplar e WHERE e.emprestado=:falso AND e.livro=:livro";

    public ValidacaoParaEmprestimoPorTipoDeUsuarioPesquisador(ExecutorTransacional execTransacional) {
        this.execTransacional = execTransacional;
    }

    @Override
    public Errors handler(Errors errors, CadastroEmprestimoDeExemplarRequest request) {

        final EntityManager manager = execTransacional.getManager();

        Usuario possivelResponsavelPeloEmprestimo = execTransacional.executor(() -> manager.find(Usuario.class, request.getIdUsuario()));

        if (!possivelResponsavelPeloEmprestimo.getTipoUsuario().equals(PESQUISADOR)) {
            return errors;
        }

        Livro livroDesejado = execTransacional.executor(() -> manager.find(Livro.class, request.getIdLivro()));

        TypedQuery<Exemplar> qtdExemplaresLivresQuery = manager.createQuery(BUSQUE_EXEMPLAR_LIVRE, Exemplar.class)
                .setParameter("falso", false)
                .setParameter("livro", livroDesejado)
                .setLockMode(READ);

        //1
        final Boolean naoExisteExemplaresDisponives = execTransacional.executor(() -> qtdExemplaresLivresQuery.getResultList().isEmpty());

        if (naoExisteExemplaresDisponives) {
            errors.rejectValue("idLivro", null, "Nao ah exemplares disponiveis");
            return errors;
        }

        return null;
    }


}
