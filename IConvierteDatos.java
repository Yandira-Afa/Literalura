package com.alura.literatura.service;

public interface IConvierteDatos {
    <T> T obtenerDatos(String Json, Class <T> clase);
}
