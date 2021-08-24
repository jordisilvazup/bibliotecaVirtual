package br.com.zup.edu.biblioteca.model;

import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Entity
public class EmprestimoDeExemplar {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = CascadeType.MERGE)
    private Exemplar exemplar;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Usuario usuario;

    @NotNull
    @Max(60)
    @Column(nullable = false)
    private Integer tempoDeEmprestimoEmDias;


    @Column(nullable = false)
    private LocalDate emprestadoEm=LocalDate.now();

    public EmprestimoDeExemplar(Exemplar exemplar, Usuario usuario, Integer tempoDeEmprestimoEmDias) {

        this.exemplar = exemplar;
        this.exemplar.associarEmprestimo(this);
        this.usuario = usuario;
        this.tempoDeEmprestimoEmDias = tempoDeEmprestimoEmDias;

    }

    @Deprecated
    public EmprestimoDeExemplar() {

    }

    public Long getId() {
        return id;
    }
}
