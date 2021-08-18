package br.com.zup.edu.biblioteca.controller.requests;

import br.com.zup.edu.biblioteca.model.TipoCirculacao;
import br.com.zup.edu.biblioteca.validators.ExisteEntradaNoEnum;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class CadastroExemplarRequest {
    @JsonProperty
    @ExisteEntradaNoEnum(targetClassType = TipoCirculacao.class)
    private String tipoCirculacao;

    @JsonCreator
    public CadastroExemplarRequest(String tipoCirculacao) {
        this.tipoCirculacao = tipoCirculacao;
    }

    public String getTipoCirculacao() {
        return tipoCirculacao.toUpperCase();
    }
}
