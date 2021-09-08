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

    @OneToOne(mappedBy = "emprestimo", cascade = CascadeType.ALL)
    private Devolucao devolucao;

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

    public Usuario getUsuario() {
        return usuario;
    }

    public Integer getTempoDeEmprestimoEmDias() {
        return tempoDeEmprestimoEmDias;
    }

    public LocalDate getEmprestadoEm() {
        return emprestadoEm;
    }

    public boolean atrasado(){
        return LocalDate.now().isAfter(emprestadoEm.plusDays(tempoDeEmprestimoEmDias));
    }

    public void associarDevolucao(Devolucao devolucao) {
        exemplar.devolver();
        this.devolucao=devolucao;
    }
}
