package br.com.zup.edu.biblioteca.controller;

import br.com.zup.edu.biblioteca.controller.requests.CadastroLivroRequest;
import br.com.zup.edu.biblioteca.model.Livro;
import br.com.zup.edu.biblioteca.repository.LivroRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping("/api/v1/livros")
public class CadastrarNovoLivroController {
    private final  LivroRepository repository;

    public CadastrarNovoLivroController(LivroRepository repository) {
        this.repository = repository;
    }

    @PostMapping
    public ResponseEntity<?> cadastrarNovoLivro(@RequestBody @Valid CadastroLivroRequest livroRequest){

        Livro novoLivro = livroRequest.paraLivro();
        repository.save(novoLivro);

        URI location = UriComponentsBuilder.fromUriString("/api/v1/livros/{id}").buildAndExpand(novoLivro.getId()).toUri();

        return ResponseEntity.created(location).build();
    }
}
