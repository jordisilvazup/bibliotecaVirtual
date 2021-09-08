package br.com.zup.edu.biblioteca.controller.requests;

import br.com.zup.edu.biblioteca.model.Devolucao;
import br.com.zup.edu.biblioteca.model.EmprestimoDeExemplar;
import br.com.zup.edu.biblioteca.model.Usuario;
import br.com.zup.edu.biblioteca.validators.ExistId;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.EntityManager;
import javax.validation.constraints.NotNull;


public class CadastroDevolucaoLivroRequest {
    @JsonProperty
    @NotNull
    @ExistId(domainClass = Usuario.class)
    private Long idUsuario;
    @JsonProperty
    @NotNull
    @ExistId(domainClass = EmprestimoDeExemplar.class)
    private Long idEmprestimo;

    public CadastroDevolucaoLivroRequest(Long idUsuario, Long idEmprestimo) {
        this.idUsuario = idUsuario;
        this.idEmprestimo = idEmprestimo;
    }

    public Long getIdUsuario() {
        return idUsuario;
    }

    public Long getIdEmprestimo() {
        return idEmprestimo;
    }

    public Devolucao paraDevolucao(EntityManager manager) {
        Usuario usuario = manager.find(Usuario.class, idUsuario);
        EmprestimoDeExemplar emprestimo = manager.find(EmprestimoDeExemplar.class, idEmprestimo);
        return new Devolucao(emprestimo,usuario);
    }
}
