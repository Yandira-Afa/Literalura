package com.alura.literatura.repository;

import com.alura.literatura.model.Libro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface LibroRepository extends JpaRepository<Libro, Long> {

 Optional<Libro>finByTituloignoreCase(String titulo);

    @Query("SELECT l FROM libro l WHERE l.idioma = :idioma")
    List<Libro> finByIdioma(@Param("idioma")String idioma);

    Optional<Libro> findByTituloIgnoreCase(String titulo);
}
