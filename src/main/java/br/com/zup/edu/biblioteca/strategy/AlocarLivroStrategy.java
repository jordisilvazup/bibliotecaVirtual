package br.com.zup.edu.biblioteca.strategy;

import br.com.zup.edu.biblioteca.controller.requests.CadastroEmprestimoDeExemplarRequest;
import br.com.zup.edu.biblioteca.model.EmprestimoDeExemplar;
import br.com.zup.edu.biblioteca.model.Livro;
import br.com.zup.edu.biblioteca.model.Usuario;

public interface AlocarLivroStrategy {
    public EmprestimoDeExemplar alocarLivro(Usuario locatario, Livro livroDesejado, Integer tempoEmDias);
}
