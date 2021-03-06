package br.com.zup.edu.biblioteca.model;

import org.hibernate.validator.constraints.ISBN;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Livro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String titulo;

    @Column(nullable = false)
    private BigDecimal preco;

    @Column(unique = true, nullable = false)
    @ISBN
    private String isbn;

    @OneToMany(mappedBy = "livro", cascade = CascadeType.ALL)
    private List<Exemplar> exemplares= new ArrayList<>();

    public Livro(String titulo, BigDecimal preco,@ISBN String isbn) {
        this.titulo = titulo;
        this.preco = preco;
        this.isbn = isbn;
    }

    @Deprecated
    public Livro() {
    }

    public Long getId() {
        return id;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public void associar(Exemplar exemplar) {
        exemplares.add(exemplar);
    }
}
