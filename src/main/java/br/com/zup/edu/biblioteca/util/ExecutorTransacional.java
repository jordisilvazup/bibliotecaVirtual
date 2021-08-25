package br.com.zup.edu.biblioteca.util;

import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.util.Map;
import java.util.function.Supplier;

@Component
public class ExecutorTransacional {
    private final EntityManager manager;

    public ExecutorTransacional(EntityManager manager) {
        this.manager = manager;
    }

    @Transactional
    public <T> T salvar(T object){
        manager.persist(object);
        return object;
    }

    @Transactional
    public <T> T atualizarECommitar(T object){
        manager.merge(object);
        return object;
    }


    public EntityManager getManager() {
        return manager;
    }

    @Transactional
    public <T> T  executor(Supplier<T> action){
        return action.get();
    }
}
