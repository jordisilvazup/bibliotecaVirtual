package br.com.zup.edu.biblioteca.strategy;

import br.com.zup.edu.biblioteca.model.Livro;
import br.com.zup.edu.biblioteca.model.Usuario;

public class AlugaLivroResource {
    private Usuario locatario;
    private Livro livro;
    private Integer tempoDeEmprestimoEmDias;

    public AlugaLivroResource(Usuario locatario, Livro livro, Integer tempoDeEmprestimoEmDias) {
        this.locatario = locatario;
        this.livro = livro;
        this.tempoDeEmprestimoEmDias = tempoDeEmprestimoEmDias;
    }

    public Usuario getLocatario() {
        return locatario;
    }

    public Livro getLivro() {
        return livro;
    }

    public Integer getTempoDeEmprestimoEmDias() {
        return tempoDeEmprestimoEmDias;
    }
}
