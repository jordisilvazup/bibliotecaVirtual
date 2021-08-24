package br.com.zup.edu.biblioteca.validators;

import br.com.zup.edu.biblioteca.controller.requests.CadastroEmprestimoDeExemplarRequest;
import br.com.zup.edu.biblioteca.model.Exemplar;
import br.com.zup.edu.biblioteca.model.Livro;
import br.com.zup.edu.biblioteca.model.Usuario;
import br.com.zup.edu.biblioteca.util.ExecutorTransacional;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.Objects;

import static br.com.zup.edu.biblioteca.model.TipoUsuario.PESQUISADOR;
import static javax.persistence.LockModeType.READ;

@Component
//6
public  class ValidacaoParaEmprestimoPorTipoDeUsuarioPesquisador implements ValidacaoEmprestimo{

    //1
    private final ExecutorTransacional execTransacional;

    public ValidacaoParaEmprestimoPorTipoDeUsuarioPesquisador(ExecutorTransacional execTransacional) {
        this.execTransacional = execTransacional;
    }

    @Override
    public Errors handler(Errors errors, CadastroEmprestimoDeExemplarRequest request) {
        final EntityManager manager = execTransacional.getManager();
        Usuario possivelResponsavelPeloEmprestimo =execTransacional.executor(()-> manager.find(Usuario.class, request.getIdUsuario()));
        //1
        if(Objects.isNull(possivelResponsavelPeloEmprestimo)){
            errors.rejectValue("idUsuario",null,"Usuario nao cadastrado");
            return errors;
        }

        //1
        Livro livroDesejado= execTransacional.executor(()->manager.find(Livro.class,request.getIdLivro()));

        if(Objects.isNull(livroDesejado)){
            errors.rejectValue("idLivro",null,"Livro nao cadastrado");
            return errors;
        }


        //1
        if (possivelResponsavelPeloEmprestimo.getTipoUsuario().equals(PESQUISADOR)) {

            String busqueAquantidadeDeExemplaresLivres = "SELECT e FROM Exemplar e WHERE e.emprestado=:falso AND e.livro=:livro";
            TypedQuery<Exemplar> qtdExemplaresLivresQuery = manager.createQuery(busqueAquantidadeDeExemplaresLivres, Exemplar.class);
            qtdExemplaresLivresQuery.setParameter("falso", false);
            qtdExemplaresLivresQuery.setParameter("livro", livroDesejado);
            qtdExemplaresLivresQuery.setLockMode(READ);

            //1
            final Boolean naoExisteExemplaresDisponives = execTransacional.executor(() -> qtdExemplaresLivresQuery.getResultList().isEmpty());
            System.out.println(naoExisteExemplaresDisponives);
            if (naoExisteExemplaresDisponives) {
                 errors.rejectValue("idLivro",null,"Nao ah exemplares disponiveis");
                 return errors;
            }
        }
        return errors;
    }


}
