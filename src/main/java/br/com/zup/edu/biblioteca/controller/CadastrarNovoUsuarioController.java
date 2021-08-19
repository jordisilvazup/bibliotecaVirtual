package br.com.zup.edu.biblioteca.controller;

import br.com.zup.edu.biblioteca.controller.requests.CadastroUsuarioRequest;
import br.com.zup.edu.biblioteca.model.Usuario;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping("/api/v1/usuarios")
public class CadastrarNovoUsuarioController {
    private final EntityManager manager;

    public CadastrarNovoUsuarioController(EntityManager manager) {
        this.manager = manager;
    }

    @PostMapping
    @Transactional
    public ResponseEntity<?> cadastrarNovoUsuario(@RequestBody @Valid CadastroUsuarioRequest cadastroUsuarioRequest){

        Usuario usuario = cadastroUsuarioRequest.paraUsuario();

        manager.persist(usuario);

        URI location= UriComponentsBuilder.fromUriString("/api/v1/usuarios/{id}").buildAndExpand(usuario.getId()).toUri();

        return ResponseEntity.created(location).build();

    }
}

