package com.aluracursos.screenmatach.service;

public interface IConvierteDatos {
   <T> T obtenerDatos(String json, Class<T> classe);
}
