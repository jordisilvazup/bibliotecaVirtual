package br.com.zup.edu.biblioteca.controller.requests;

import br.com.zup.edu.biblioteca.model.Livro;
import br.com.zup.edu.biblioteca.validators.UniqueValue;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;

public class CadastroLivroRequest {
    @JsonProperty @NotBlank
    private String titulo;

    @JsonProperty @NotNull @PositiveOrZero
    private BigDecimal preco;

    @JsonProperty @NotBlank @UniqueValue(domainClass= Livro.class, domainAtribute="ISBN")
    private String ISBN;

    public Livro paraLivro(){
        return new Livro(titulo,preco,ISBN);
    }

}
