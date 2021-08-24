package br.com.zup.edu.biblioteca.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Exemplar {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private TipoCirculacao tipoCirculacao;

    @ManyToOne
    private Livro livro;

    @OneToMany(mappedBy = "exemplar", cascade = CascadeType.ALL)
    private List<EmprestimoDeExemplar> emprestimos= new ArrayList<>();

    private boolean emprestado;

    @Version//lock otimista
    private Integer version;

    public Exemplar(TipoCirculacao tipoCirculacao, Livro livro) {
        this.tipoCirculacao = tipoCirculacao;
        this.livro = livro;
        this.emprestado=false;
    }

    @Deprecated
    public Exemplar(){

    }

    public Long getId() {
        return id;
    }

    public void associarEmprestimo(EmprestimoDeExemplar emprestimoDeExemplar) {
        this.emprestado=true;
        this.emprestimos.add(emprestimoDeExemplar);
    }
}
