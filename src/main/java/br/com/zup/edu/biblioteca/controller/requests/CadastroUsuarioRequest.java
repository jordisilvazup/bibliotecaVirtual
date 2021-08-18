package br.com.zup.edu.biblioteca.controller.requests;

import br.com.zup.edu.biblioteca.model.TipoUsuario;
import br.com.zup.edu.biblioteca.model.Usuario;
import br.com.zup.edu.biblioteca.validators.ExisteEntradaNoEnum;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotBlank;

public class CadastroUsuarioRequest {

    @JsonProperty
    @NotBlank
    private String nome;

    @ExisteEntradaNoEnum(targetClassType = TipoUsuario.class)
    @NotBlank
    @JsonProperty
    private String tipoUsuario;

    public CadastroUsuarioRequest(String nome, String tipoUsuario) {
        this.nome=nome;
        this.tipoUsuario=tipoUsuario;
    }

    public Usuario paraUsuario(){
        return new Usuario(nome,TipoUsuario.valueOf(tipoUsuario.toUpperCase()));
    }
}
