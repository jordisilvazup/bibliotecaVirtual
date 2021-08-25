package br.com.zup.edu.biblioteca.strategy;

import br.com.zup.edu.biblioteca.controller.requests.CadastroEmprestimoDeExemplarRequest;
import br.com.zup.edu.biblioteca.model.EmprestimoDeExemplar;
import br.com.zup.edu.biblioteca.model.Exemplar;
import br.com.zup.edu.biblioteca.model.Livro;
import br.com.zup.edu.biblioteca.model.Usuario;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import static br.com.zup.edu.biblioteca.model.TipoCirculacao.LIVRE;
import static javax.persistence.LockModeType.READ;

@Component
public class AlocarLivroParaUsuarioPesquisadorStrategy implements AlocarLivroStrategy {

    private final EntityManager manager;

    public AlocarLivroParaUsuarioPesquisadorStrategy(EntityManager manager) {
        this.manager = manager;
    }


    @Override
    public EmprestimoDeExemplar alocarLivro(AlugaLivroResource resource) {

        Usuario locatario= resource.getLocatario();
        Livro livroDesejado= resource.getLivro();
        Integer tempoEmDias= resource.getTempoDeEmprestimoEmDias();

        String busqueAquantidadeDeExemplaresLivres = "SELECT e FROM Exemplar e WHERE e.emprestado=:falso and e.livro=:livro";
        Exemplar exemplar = manager.createQuery(busqueAquantidadeDeExemplaresLivres, Exemplar.class)
                .setParameter("falso", false)
                .setParameter("livro", livroDesejado)
                .setLockMode(READ)
                .setMaxResults(1).getSingleResult();

        EmprestimoDeExemplar emprestimoDeExemplar = new EmprestimoDeExemplar(exemplar,locatario,60);
        manager.persist(emprestimoDeExemplar);
        return emprestimoDeExemplar;
    }
}
