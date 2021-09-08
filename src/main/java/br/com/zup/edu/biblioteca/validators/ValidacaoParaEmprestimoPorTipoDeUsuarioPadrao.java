package br.com.zup.edu.biblioteca.validators;

import br.com.zup.edu.biblioteca.controller.requests.CadastroEmprestimoDeExemplarRequest;
import br.com.zup.edu.biblioteca.model.Livro;
import br.com.zup.edu.biblioteca.model.Usuario;
import br.com.zup.edu.biblioteca.util.ExecutorTransacional;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.Objects;

import static br.com.zup.edu.biblioteca.model.TipoCirculacao.LIVRE;
import static br.com.zup.edu.biblioteca.model.TipoUsuario.PADRAO;
import static javax.persistence.LockModeType.READ;

//10
@Component
public class ValidacaoParaEmprestimoPorTipoDeUsuarioPadrao implements ValidacaoEmprestimo {

    //1
    private final ExecutorTransacional execTransacional;
    private static final String BUSQUE_AQUANTIDADE_DE_EXEMPLARES_LIVRES = "SELECT COUNT(e) FROM Exemplar " +
            "e WHERE e.tipoCirculacao=:livre AND e.emprestado=:falso and e.livro=:livro";

    private static final int QUANTIDADE_MAXIMA_DE_ALOCACAO_PARA_USUARIO_PADRAO = 5;
    private static final int QUANTIDADE_EM_DIAS_MAXIMA_DE_ALOCACAO = 60;

    public ValidacaoParaEmprestimoPorTipoDeUsuarioPadrao(ExecutorTransacional execTransacional) {
        this.execTransacional = execTransacional;
    }


    @Override
    public Errors handler(Errors errors, CadastroEmprestimoDeExemplarRequest request) {
        final EntityManager manager = execTransacional.getManager();

        Usuario possivelResponsavelPeloEmprestimo = execTransacional.executor(() -> manager.find(Usuario.class, request.getIdUsuario()));

        if (!possivelResponsavelPeloEmprestimo.getTipoUsuario().equals(PADRAO)) {
            return errors;
        }


        //1
        Livro livroDesejado = execTransacional.executor(() -> manager.find(Livro.class, request.getIdLivro()));


        String porQuantosExemplaresEsteUsuarioEResponsavel = "SELECT COUNT(e) FROM EmprestimoDeExemplar e WHERE e.usuario=:responsavel and e.exemplar.emprestado=true";

        TypedQuery<Long> query = manager.createQuery(porQuantosExemplaresEsteUsuarioEResponsavel, Long.class)
                .setParameter("responsavel", possivelResponsavelPeloEmprestimo);

        Integer quantidadeDeLivrosEmprestados = execTransacional.executor(() -> query.getSingleResult().intValue());

        //1

        //1
        if (Objects.isNull(request.getTempoDeEmprestimoEmDias())) {
            errors.rejectValue("tempoDeEmprestimoEmDias", null, "O Emprestimo de exemplares para usuarios do tipo padrao deve conter o tempo que pretende ficar com o livro");
            return errors;
        }
        //1
        else if (request.getTempoDeEmprestimoEmDias() > QUANTIDADE_EM_DIAS_MAXIMA_DE_ALOCACAO) {
            errors.rejectValue("tempoDeEmprestimoEmDias", null, "O Emprestimo de exemplares deve ter no maximo tempo de 60 dias");
            return errors;
        }
        //1
        else if (quantidadeDeLivrosEmprestados >= QUANTIDADE_MAXIMA_DE_ALOCACAO_PARA_USUARIO_PADRAO) {
            errors.rejectValue("idUsuario", null, "Usuarios do tipo padrão podem ter no maximo 5 livros emprestados");
            return errors;
        }


        TypedQuery<Long> qtdExemplaresLivresQuery = manager.createQuery(BUSQUE_AQUANTIDADE_DE_EXEMPLARES_LIVRES, Long.class)
                .setParameter("livre", LIVRE)
                .setParameter("livro", livroDesejado)
                .setParameter("falso", false)
                .setLockMode(READ);


        int qtdExemplaraaesLivres = execTransacional.executor(() -> qtdExemplaresLivresQuery.getSingleResult().intValue());

        Integer qtdExemplaresLivres = qtdExemplaraaesLivres;

        //1
        if (qtdExemplaresLivres < 1) {
            errors.rejectValue("idLivro", null, "Nao ah exemplares disponiveis");
            return errors;
        }

        return null;
    }


}
