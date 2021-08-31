package br.com.zup.edu.biblioteca.model;

import javax.persistence.*;
import java.util.Objects;

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

    public TipoUsuario getTipoUsuario() {
        return tipoUsuario;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Usuario usuario = (Usuario) o;
        return Objects.equals(id, usuario.id) && Objects.equals(nome, usuario.nome) && tipoUsuario == usuario.tipoUsuario;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nome, tipoUsuario);
    }
}
