package br.com.zup.edu.biblioteca.controller.requests;

import br.com.zup.edu.biblioteca.model.Livro;
import br.com.zup.edu.biblioteca.validators.UniqueValue;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.ISBN;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;

public class CadastroLivroRequest {
    @JsonProperty @NotBlank
    private String titulo;

    @JsonProperty @NotNull @PositiveOrZero
    private BigDecimal preco;

    @JsonProperty
    @NotBlank
    @UniqueValue(domainClass= Livro.class, domainAtribute="isbn")
    @ISBN
    private String isbn;

    public CadastroLivroRequest(String titulo, BigDecimal preco, String isbn) {
        this.titulo = titulo;
        this.preco = preco;
        this.isbn = isbn;
    }

    public Livro paraLivro(){
        return new Livro(titulo,preco,isbn);
    }

}
