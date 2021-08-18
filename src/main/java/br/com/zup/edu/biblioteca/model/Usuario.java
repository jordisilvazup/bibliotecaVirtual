package br.com.zup.edu.biblioteca.model;

import javax.persistence.*;

@Entity
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;

    @Enumerated(EnumType.STRING)
    private TipoUsuario tipoUsuario;

    public Usuario(String nome, TipoUsuario tipoUsuario) {
        this.nome = nome;
        this.tipoUsuario = tipoUsuario;
    }

    @Deprecated
    public Usuario() {
    }

    public Long getId() {
        return id;
    }
}
