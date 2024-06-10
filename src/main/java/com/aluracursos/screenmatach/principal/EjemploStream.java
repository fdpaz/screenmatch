package com.aluracursos.screenmatach.principal;

import java.util.Arrays;
import java.util.List;

public class EjemploStream {
    public void muestraEjmplo(){
        List<String> nombres = Arrays.asList("Brenda","Franco","David","Felix");

        nombres.stream()
                .sorted()
                .limit(5)
                .filter(n -> n.startsWith("D"))
                .map(n-> n.toUpperCase())
                .forEach(System.out::println);
    }
}
