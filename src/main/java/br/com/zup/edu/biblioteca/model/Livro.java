package br.com.zup.edu.biblioteca.model;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
public class Livro {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String titulo;

    @Column(nullable = false)
    private BigDecimal preco;

    @Column(unique = true, nullable = false)
    private String ISBN;

    public Livro(String titulo, BigDecimal preco, String ISBN) {
        this.titulo = titulo;
        this.preco = preco;
        this.ISBN = ISBN;
    }

    @Deprecated
    public Livro() {
    }

    public Long getId() {
        return id;
    }
}
