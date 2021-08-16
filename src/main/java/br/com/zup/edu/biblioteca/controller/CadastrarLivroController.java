package br.com.zup.edu.biblioteca.controller;

import br.com.zup.edu.biblioteca.controller.requests.CadastroLivroRequest;
import br.com.zup.edu.biblioteca.model.Livro;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping("/api/v1/livros")
public class CadastrarLivroController {
    private final EntityManager manager;

    public CadastrarLivroController(EntityManager manager) {
        this.manager = manager;
    }

    @PostMapping
    @Transactional
    public ResponseEntity<?> cadastrarNovoLivro(@RequestBody @Valid CadastroLivroRequest livroRequest){
        final Livro novoLivro = livroRequest.paraLivro();
        manager.persist(novoLivro);
        URI location = UriComponentsBuilder.fromUriString("/api/v1/livros/{id}").buildAndExpand(novoLivro.getId()).toUri();
        return ResponseEntity.created(location).build();
    }
}
