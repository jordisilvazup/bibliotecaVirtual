package br.com.zup.edu.biblioteca.controller;

import br.com.zup.edu.biblioteca.controller.requests.CadastroEmprestimoDeExemplarRequest;
import br.com.zup.edu.biblioteca.model.EmprestimoDeExemplar;
import br.com.zup.edu.biblioteca.strategy.StrategyContext;
import br.com.zup.edu.biblioteca.validators.EmprestimoDeExemplarValidator;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping("/api/v1/emprestimos")
public class CadastrarNovoEmprestimoController {

    //1
    private final StrategyContext strategyContext;
    //2
    private final EmprestimoDeExemplarValidator validator;

    public CadastrarNovoEmprestimoController(StrategyContext strategyContext, EmprestimoDeExemplarValidator validator) {
        this.strategyContext = strategyContext;
        this.validator = validator;
    }


    @InitBinder
    public void init(WebDataBinder binder){
        binder.addValidators(validator);
    }


    @PostMapping
    //3
    public ResponseEntity<?> cadastrarEmprestimo(@RequestBody @Valid CadastroEmprestimoDeExemplarRequest request) {

        EmprestimoDeExemplar emprestimoDeExemplar = strategyContext.execute(request);

        final URI location = UriComponentsBuilder.fromUriString("/api/v1/emprestimos/{id}").buildAndExpand(emprestimoDeExemplar.getId()).toUri();

        return ResponseEntity.created(location).build();
    }
}
