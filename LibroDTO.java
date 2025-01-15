package com.alura.literatura.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class LibroDTO {

    @JsonProperty("id")
    private  int id;

    @JsonProperty("titulo")
    private String titulo;

    @JsonProperty("authors")
    private List<AutorDTO> auores;

    @JsonProperty("languages")
    private List<String> idiomas;

    @JsonProperty("download_count")
    private int numeroDescargas;

    public static boolean isEmpty() {
        return false;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public List<AutorDTO> getAuores() {
        return auores;
    }

    public void setAuores(List<AutorDTO> auores) {
        this.auores = auores;
    }

    public List<String> getIdiomas() {
        return idiomas;
    }

    public void setIdiomas(List<String> idiomas) {
        this.idiomas = idiomas;
    }

    public int getNumeroDescargas() {
        return numeroDescargas;
    }

    public void setNumeroDescargas(int numeroDescargas) {
        this.numeroDescargas = numeroDescargas;
    }
}


