package br.com.zup.edu.biblioteca.controller;

import br.com.zup.edu.biblioteca.controller.requests.CadastroExemplarRequest;
import br.com.zup.edu.biblioteca.model.Exemplar;
import br.com.zup.edu.biblioteca.model.Livro;
import br.com.zup.edu.biblioteca.model.TipoCirculacao;
import br.com.zup.edu.biblioteca.repository.LivroRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/livros")
public class CadastrarNovoExemplarController {
    private final LivroRepository repository;

    public CadastrarNovoExemplarController(LivroRepository repository) {
        this.repository = repository;
    }

    @PostMapping("/{isbn}/exemplares")
    public ResponseEntity<?> cadastrarNovoExemplar(@PathVariable String isbn, @RequestBody @Valid CadastroExemplarRequest cadastroExemplarRequest){

        Optional<Livro> possivelLivro=repository.findByIsbn(isbn);

        if(possivelLivro.isEmpty()){
            return ResponseEntity.notFound().build();
        }

        Livro livro = possivelLivro.get();
        Exemplar exemplar = new Exemplar(TipoCirculacao.valueOf(cadastroExemplarRequest.getTipoCirculacao()), livro);
        livro.associar(exemplar);
        repository.save(livro);

        URI location= UriComponentsBuilder.fromUriString("/api/v1/livros/{isbn}/exemplares/{id}").buildAndExpand(isbn,exemplar.getId()).toUri();

        return ResponseEntity.created(location).build();

    }
}
