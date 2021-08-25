package br.com.zup.edu.biblioteca.model;

import br.com.zup.edu.biblioteca.strategy.AlocarLivroParaUsuarioPadraoStrategy;
import br.com.zup.edu.biblioteca.strategy.AlocarLivroParaUsuarioPesquisadorStrategy;
import br.com.zup.edu.biblioteca.strategy.AlocarLivroStrategy;

import javax.persistence.EntityManager;

public enum TipoUsuario {
    PADRAO{
        @Override
        public AlocarLivroStrategy getAlocacaoStrategy(EntityManager manager) {
            return new AlocarLivroParaUsuarioPadraoStrategy(manager);
        }
    },
    PESQUISADOR{
        @Override
        public AlocarLivroStrategy getAlocacaoStrategy(EntityManager manager) {
            return new AlocarLivroParaUsuarioPesquisadorStrategy(manager);
        }
    };
    public abstract AlocarLivroStrategy getAlocacaoStrategy(EntityManager manager);
}
