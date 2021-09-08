package br.com.zup.edu.biblioteca.model;

import javax.persistence.*;

@Entity
public class Devolucao {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    private EmprestimoDeExemplar emprestimo;

    @ManyToOne
    private Usuario resposavel;

    public Devolucao(EmprestimoDeExemplar emprestimo, Usuario resposavel) {
        this.emprestimo = emprestimo;
        this.resposavel = resposavel;
        emprestimo.associarDevolucao(this);
    }

    @Deprecated
    public Devolucao() {
    }

    public Long getId() {
        return this.id;
    }
}
