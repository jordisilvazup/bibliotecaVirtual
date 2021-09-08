package br.com.zup.edu.biblioteca.controller;

import br.com.zup.edu.biblioteca.controller.requests.CadastroDevolucaoLivroRequest;
import br.com.zup.edu.biblioteca.model.Devolucao;
import br.com.zup.edu.biblioteca.model.EmprestimoDeExemplar;
import br.com.zup.edu.biblioteca.model.Usuario;
import br.com.zup.edu.biblioteca.validators.DevolucaoDeEmprestimoValidator;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import javax.validation.Valid;
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
    public void binder(WebDataBinder binder) {
        binder.addValidators(devolucaoDeEmprestimoValidator);
    }

    @PostMapping
    @Transactional
    public ResponseEntity<?> cadastrarNovaDevolucao(@RequestBody @Valid CadastroDevolucaoLivroRequest request) {

        Devolucao devolucao = request.paraDevolucao(manager);

        manager.persist(devolucao);

        URI location = UriComponentsBuilder.fromUriString("/api/v1/devolucoes/{id}").buildAndExpand(devolucao.getId()).toUri();

        return ResponseEntity.created(location).build();
    }
}
