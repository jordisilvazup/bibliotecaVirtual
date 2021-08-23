package br.com.zup.edu.biblioteca.controller;

import br.com.zup.edu.biblioteca.controller.requests.CadastroEmprestimoDeExemplarRequest;
import br.com.zup.edu.biblioteca.model.EmprestimoDeExemplar;
import br.com.zup.edu.biblioteca.model.Exemplar;
import br.com.zup.edu.biblioteca.model.Livro;
import br.com.zup.edu.biblioteca.model.Usuario;
import br.com.zup.edu.biblioteca.util.ExecutorTransacional;
import br.com.zup.edu.biblioteca.validators.EmprestimoDeExemplarValidator;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.validation.Valid;

import java.net.URI;

import static br.com.zup.edu.biblioteca.model.TipoCirculacao.LIVRE;
import static br.com.zup.edu.biblioteca.model.TipoUsuario.PADRAO;
import static javax.persistence.LockModeType.READ;

@RestController
@RequestMapping("/api/v1/emprestimos")
public class CadastrarNovoEmprestimoController {

    //1
    private final ExecutorTransacional executorTransacional;
    private final EmprestimoDeExemplarValidator validator;


    public CadastrarNovoEmprestimoController(ExecutorTransacional executorTransacional, EmprestimoDeExemplarValidator validator) {
        this.executorTransacional = executorTransacional;
        this.validator = validator;
    }
    @InitBinder
    public void init(WebDataBinder binder){
        binder.addValidators(validator);
    }


    @PostMapping
    //3
    public ResponseEntity<?> cadastrarEmprestimo(@RequestBody @Valid CadastroEmprestimoDeExemplarRequest request) {
        final EntityManager manager = executorTransacional.getManager();

        Exemplar exemplar = null;


        Usuario locatario = manager.find(Usuario.class, request.getIdUsuario());
        Livro livroDeDesejo = manager.find(Livro.class, request.getIdLivro());

        //1
        if (locatario.getTipoUsuario().equals(PADRAO)) {
            String busqueAquantidadeDeExemplaresLivres = "SELECT  e FROM Exemplar e WHERE e.tipoCirculacao=:livre AND e.emprestado=:falso and e.livro=:livro";
            TypedQuery<Exemplar> qtdExemplaresLivresQuery = manager.createQuery(busqueAquantidadeDeExemplaresLivres, Exemplar.class);
            qtdExemplaresLivresQuery.setParameter("livre", LIVRE);
            qtdExemplaresLivresQuery.setParameter("falso", false);
            qtdExemplaresLivresQuery.setParameter("livro", livroDeDesejo);
            qtdExemplaresLivresQuery.setLockMode(READ);
           exemplar= executorTransacional.executor(()-> {
                return qtdExemplaresLivresQuery.setMaxResults(1).getSingleResult();
            });
//            exemplar = qtdExemplaresLivresQuery.setMaxResults(1).getSingleResult();
        }
        //1
        else {
            String busqueAquantidadeDeExemplaresLivres = "SELECT e FROM Exemplar e WHERE e.emprestado=:falso and e.livro=:livro";
            TypedQuery<Exemplar> qtdExemplaresLivresQuery = manager.createQuery(busqueAquantidadeDeExemplaresLivres, Exemplar.class);
            qtdExemplaresLivresQuery.setParameter("falso", false);
            qtdExemplaresLivresQuery.setParameter("livro", livroDeDesejo);
            qtdExemplaresLivresQuery.setLockMode(READ);
            exemplar= executorTransacional.executor(()-> {
                return qtdExemplaresLivresQuery.setMaxResults(1).getSingleResult();
            });
//            exemplar = qtdExemplaresLivresQuery.setMaxResults(1).getSingleResult();
        }

        EmprestimoDeExemplar emprestimoDeExemplar = request.paraEmprestimoDeExemplar(exemplar, locatario);

        executorTransacional.salvar(emprestimoDeExemplar);
        exemplar.associarEmprestimo(emprestimoDeExemplar);
        executorTransacional.atualizarECommitar(exemplar);

        final URI location = UriComponentsBuilder.fromUriString("api/v1/emprestimos/{id}").buildAndExpand(emprestimoDeExemplar.getId()).toUri();

        return ResponseEntity.created(location).build();
    }
}
