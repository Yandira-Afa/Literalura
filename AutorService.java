package com.alura.literatura.service;


import com.alura.literatura.model.Autor;
import com.alura.literatura.repository.AutorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AutorService {

    @Autowired
    private AutorRepository autorrRepository;

    public List<Autor> listarAutores() {
        return autorrRepository.findAllConLibros();
    }
    public List<Autor> listarAutoresVivosEnAno(int ano) {
        return autorrRepository.findAutoresVivosEnAnoConLibros(ano);
    }

    public Autor crearAutor(Autor autor) {
        return autorrRepository.save(autor);
    }
    public Optional<Autor> obtenerAutorPorId(Long id) {
        return autorrRepository.findById(id);
    }

    public Optional<Autor> obtenerAutorPorNombre(String nombre) {
        return autorrRepository.findByNombre(nombre);
    }

    public Autor actualizarAutor(Long id, Autor autorDetalles) {
        Autor autor= autorrRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("Autor no Encontrado"));
        autor.setNombre(autorDetalles.getNombre());
        autor.setAnoNacimiento(autorDetalles.getAnoNacimiento());
        autor.setAnoFallecimiento(autorDetalles.getAnoFallecimiento());
        return autorrRepository.save(autor);
    }

    public void eliminarAutor(Long id) {
        autorrRepository.deleteById(id);
    }
}
