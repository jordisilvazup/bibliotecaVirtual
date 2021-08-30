package br.com.zup.edu.biblioteca.controller;

import br.com.zup.edu.biblioteca.controller.requests.CadastroDevolucaoLivroRequest;
import br.com.zup.edu.biblioteca.model.Devolucao;
import br.com.zup.edu.biblioteca.model.EmprestimoDeExemplar;
import br.com.zup.edu.biblioteca.model.Usuario;
import br.com.zup.edu.biblioteca.validators.DevolucaoDeEmprestimoValidator;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.net.URI;

@RestController
@RequestMapping("/api/v1/devolucoes")
public class CadastrarNovaDevolucaoController {
    private final EntityManager manager;
    private final DevolucaoDeEmprestimoValidator devolucaoDeEmprestimoValidator;

    public CadastrarNovaDevolucaoController(EntityManager manager, DevolucaoDeEmprestimoValidator devolucaoDeEmprestimoValidator) {
        this.manager = manager;
        this.devolucaoDeEmprestimoValidator = devolucaoDeEmprestimoValidator;
    }

    @InitBinder
    public void binder(WebDataBinder binder){
        binder.addValidators(devolucaoDeEmprestimoValidator);

    }

    @PostMapping
    @Transactional
    public ResponseEntity<?> cadastrarNovaDevolucao(CadastroDevolucaoLivroRequest request){

        Usuario usuario = manager.find(Usuario.class, request.getIdUsuario());
        EmprestimoDeExemplar emprestimoDeExemplar = manager.find(EmprestimoDeExemplar.class, request.getIdEmprestimo());

        Devolucao devolucao = request.paraDevolucao(usuario, emprestimoDeExemplar);
        manager.persist(devolucao);

        final URI location = UriComponentsBuilder.fromUriString("/api/v1/devolucoes/{id}").buildAndExpand(devolucao.getId()).toUri();

        return ResponseEntity.ok().build();
    }
}
