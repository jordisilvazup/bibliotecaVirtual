package br.com.zup.edu.biblioteca.controller.requests;

import br.com.zup.edu.biblioteca.model.*;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotNull;

public class CadastroEmprestimoDeExemplarRequest {

    @JsonProperty
    private Integer tempoDeEmprestimoEmDias;

    @NotNull
    @JsonProperty
    private Long idLivro;

    @NotNull
    @JsonProperty
    private Long idUsuario;

    public CadastroEmprestimoDeExemplarRequest(Integer tempoDeEmprestimoEmDias, Long idLivro, Long idUsuario) {
        this.tempoDeEmprestimoEmDias = tempoDeEmprestimoEmDias;
        this.idLivro = idLivro;
        this.idUsuario = idUsuario;
    }

    public EmprestimoDeExemplar paraEmprestimoDeExemplar(Exemplar exemplar, Usuario usuario){
        if(usuario.getTipoUsuario().equals(TipoUsuario.PESQUISADOR)){
            return new EmprestimoDeExemplar(exemplar,usuario,60);
        }
        return new EmprestimoDeExemplar(exemplar,usuario,tempoDeEmprestimoEmDias);
    }

    public Integer getTempoDeEmprestimoEmDias() {
        return tempoDeEmprestimoEmDias;
    }

    public Long getIdLivro() {
        return idLivro;
    }

    public Long getIdUsuario() {
        return idUsuario;
    }
}
