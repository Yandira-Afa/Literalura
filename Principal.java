package com.alura.literatura.principal;

import com.alura.literatura.dto.AutorDTO;
import com.alura.literatura.dto.LibroDTO;
import com.alura.literatura.dto.RespuestaLibrosDTO;
import com.alura.literatura.model.Autor;
import com.alura.literatura.model.Libro;
import com.alura.literatura.service.AutorService;
import com.alura.literatura.service.ConsumoApi;
import com.alura.literatura.service.ConvierteDatos;
import com.alura.literatura.service.LibroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.stream.Collectors;

@Component
public class Principal {

    @Autowired
    private LibroService libroService;

    @Autowired
    private AutorService autorService;

    @Autowired
    private ConsumoApi consumoApi;

    @Autowired
    private ConvierteDatos convierteDatos;

    private static final String BASE_URL =  "https://gutendex.com/books/";
    Scanner scanner = new Scanner(System.in);
    int opcion;
    do {
        System.out.println("Sistema de Biblioteca");
        System.out.println("1) Buscar Libros por Titulo");
        System.out.println("2) Listar Libros Registrados");
        System.out.println("3) Listar Autores Registrados");
        System.out.println("4 Listado de Autores Vivos por Año");
        System.out.println("5) Listar Libros por Idiomas");
        System.out.println("0) Salir");
        System.out.println("Seleccione una Opcion ");
        opcion= scanner.nextInt();
        scanner.nextLine();
    }

    public void mostrarMenu() {
        switch (opcion) {
            case 1:
                System.out.println("Ingrese el Titulo del Libro:");
                String titulo = scanner.nextLine();
                try {
                    String encodedTitulo = URLEncoder.encode(titulo, StandardCharsets.UTF_8);
                    String json = consumoApi.obtenerDatos(url:BASE_URL + "?search=" + encodedTitulo);
                    RespuestaLibrosDTO respuestaLibrosDTO = convierteDatos.obtenerDatos(json, RespuestaLibrosDTO.class);
                    List<LibroDTO> libroDTOS = respuestaLibrosDTO.getLibros();
                    if (LibroDTO.isEmpty()) {
                        System.out.println("Libro no encontrado");
                    } else {
                        boolean libroRegistrado = false;
                        for (LibroDTO libroDTO : libroDTOS) {
                            if (libroDTO.getTitulo().equalsIgnoreCase(titulo)) {
                                Optional<Libro> libroExistente = libroService.obtenerLibroPorTitulo(titulo);
                                if (libroExistente.isPresent()) {
                                    System.out.println("Detalle: Clave (titulo)= (" + titulo + ") ya existe");
                                    System.out.println("No se puede registrar dos veces el mismo libro");
                                    libroRegistrado = true;
                                    break;
                                } else {
                                    Libro libro = new Libro();
                                    libro.setTitulo(libroDTO.getTitulo());
                                    libro.setIdioma(libroDTO.getIdiomas().get(0));
                                    libro.setNumeroDescargas(libro.getNumeroDescargas());

                                    AutorDTO primerAutorDTO = libroDTO.getAuores().get(0);
                                    Autor autor = autorService.obtenerAutorPorNombre(primerAutorDTO.getNombre())
                                            .orElseGet(() -> {
                                                Autor nuevoAutor = new Autor();
                                                nuevoAutor.setNombre(primerAutorDTO.getNombre());
                                                nuevoAutor.setAnoNacimiento(primerAutorDTO.getAnoNacimiento());
                                                nuevoAutor.setAnoFallecimiento(primerAutorDTO.getAnoFallecimiento());
                                                return autorService.crearAutor(nuevoAutor);

                                            });
                                    libro.setAutor(autor);
                                    libroService.crearLibro(libro);
                                    System.out.println("Libro Registrado: " + libro.getTitulo());
                                    mostrarDetallesLibro(libroDTO);
                                    libroRegistrado = true;
                                    break;
                                }
                            }
                        }
                        if (!libroRegistrado) {
                            System.out.println("No se encontro libro con ese titulo" + titulo + "en la base de datos");
                        }
                    }
                } catch (Exception e) {
                    System.out.println("Error obteniendo datos de API:" + e.getMessage());
                    break;
                }
            case 2:
                libroService.listarLibros().forEach(libro -> {
                    System.out.println("Titulo del Libro;" + libro.getTitulo());
                    System.out.println("Autor:" + (libro.getAutor() != null ? libro.getAutor().getNombre().));
                    System.out.println("Idiomna:" + libro.getIdioma());
                    System.out.println("Numero de descargas:" + libro.getNumeroDescargas());
                });
                break;

            case 3:
                autorService.listarAutores().forEach(autor -> {
                    System.out.println("Autor: " + autor.getNombre());
                    System.out.println("Fecha de Nacimiento:" + autor.getAnoNacimiento());
                    System.out.println("Fecha de fallecimiento:" + (autor.getAnoFallecimiento()));
                    String libros = autor.getLibros().stream()
                            .map(Libro::getTitulo)
                            .collect(Collectors.joining(" , "));
                });
                break;
            case 4:
                System.out.println("Ingrese el Año de los Autores vivos que desea ver:");
                int ano = scanner.nextInt();
                scanner.nextLine();
                List<Autor> autoresVivos = autorService.listarAutoresVivosEnAno(ano);
                if (autoresVivos.isEmpty()) {
                    System.out.println("No se encontraron Autores vivos en ese año:" + ano);
                } else {
                    autoresVivos.forEach(autor -> {
                        System.out.println("Autor:" + autor.getNombre());
                        System.out.println("Fecha de nacimiento:" + autor.getAnoNacimiento());
                        System.out.println("Fecha de fallecimiento;" + autor.getAnoFallecimiento());
                        System.out.println("Libros:" + autor.getLibros().size());
                    });
                }
                break;
            case 5:
                System.out.println("Ingese el idioma:");
                System.out.println("Español");
                System.out.println("Ingles");
                System.out.println("Frances");
                System.out.println("Postuguez");

                String idioma = scanner.nextLine();
                if ("Español".equalsIgnoreCase(idioma) || "Ingles".equalsIgnoreCase(idioma) || "Frances".equalsIgnoreCase(idioma) || "Portuguez".equalsIgnoreCase(idioma)) {

                libroService.listarLibrosPorIdioma(idioma).forEach(libro -> {
                    System.out.println("Titulo:" + libro.getTitulo());
                    System.out.println("Autor:" + (libro.getAutor() != null ? libro.getAutor().getLibros()));
                    System.out.println("Idioma" + libro.getIdioma());
                    System.out.println("Numero de descargas:" + libro.getNumeroDescargas());
                });
            } else {
                    System.out.println("Idioma no valido...Intende de nuevo:");
                }
                break;
            case 0 :
                System.out.println("Muchas gracias por visitarnos...");
                break;
            default:
                System.out.println("Opcion incorrecta...Intente de nuevo");
        } while (opcion !=0);
        scanner.close();

    }

    private void mostrarDetallesLibro(LibroDTO libroDTO) {
        System.out.println("Titulo: " + libroDTO.getTitulo());
        System.out.println("Autor: " + (libroDTO.getAuores().isEmpty() ? "Desconcido" : libroDTO.getAuores()));
        System.out.println("Idioma: " + libroDTO.getIdiomas().get(0));
        System.out.println("Numero de descargas: " + libroDTO.getNumeroDescargas());

    }
}
