package com.alura.literatura.repository;

import com.alura.literatura.model.Autor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface AutorRepository extends JpaRepository<Autor, Long> {

    Optional<Autor> findByNombre(String nombre);

    @Query("SELECT a FROM a LEFT JOIN FETCH a.libros WHERE (a.anoFallecimiento IS NULL OR a.anofallecimiento >:ano) AND a.anoNacimiento <=:ano")
    List<Autor> findAutoresVivosEnAnoConLibros(@Param("ano") int ano);

    @Query("SELECT a FROM Autor a LEFT JOIN FETCH a.libros")
    List<Autor> findAllConLibros();

}
