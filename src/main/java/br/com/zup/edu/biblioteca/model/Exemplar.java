package br.com.zup.edu.biblioteca.model;

import javax.persistence.*;

@Entity
public class Exemplar {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private TipoCirculacao tipoCirculacao;

    @ManyToOne
    private Livro livro;

    public Exemplar(TipoCirculacao tipoCirculacao, Livro livro) {
        this.tipoCirculacao = tipoCirculacao;
        this.livro = livro;
    }

    @Deprecated
    public Exemplar(){

    }

    public Long getId() {
        return id;
    }
}
