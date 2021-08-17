package br.com.zup.edu.biblioteca.repository;

import br.com.zup.edu.biblioteca.model.Livro;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LivroRepository extends JpaRepository<Livro,Long> {
    Optional<Livro> findByIsbn(String isbn);
}
