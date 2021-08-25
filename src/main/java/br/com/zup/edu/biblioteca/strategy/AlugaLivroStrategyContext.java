package br.com.zup.edu.biblioteca.strategy;

import br.com.zup.edu.biblioteca.controller.requests.CadastroEmprestimoDeExemplarRequest;
import br.com.zup.edu.biblioteca.model.EmprestimoDeExemplar;
import br.com.zup.edu.biblioteca.model.Livro;
import br.com.zup.edu.biblioteca.model.TipoUsuario;
import br.com.zup.edu.biblioteca.model.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

@Component
public class AlugaLivroStrategyContext {
    @Autowired
    private final EntityManager manager;

    public AlugaLivroStrategyContext(EntityManager manager) {

        this.manager = manager;
    }

    @Transactional
    public EmprestimoDeExemplar execute(CadastroEmprestimoDeExemplarRequest request) {

        Usuario usuario = manager.find(Usuario.class, request.getIdUsuario());
        Livro livro = manager.find(Livro.class, request.getIdLivro());
        AlugaLivroResource alugaLivroResource= new AlugaLivroResource(usuario,livro, request.getTempoDeEmprestimoEmDias());

        return usuario.getTipoUsuario().getAlocacaoStrategy(manager).alocarLivro(alugaLivroResource);
    }
}
