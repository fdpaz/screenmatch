package com.aluracursos.screenmatach.principal;

import com.aluracursos.screenmatach.model.DatosEpisodio;
import com.aluracursos.screenmatach.model.DatosSerie;
import com.aluracursos.screenmatach.model.DatosTemporada;
import com.aluracursos.screenmatach.model.Episodio;
import com.aluracursos.screenmatach.service.ConsumoAPI;
import com.aluracursos.screenmatach.service.ConvierteDatos;

import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;


public class Principal {

    private Scanner teclado = new Scanner(System.in);
    private ConsumoAPI consumoApi = new ConsumoAPI();
    private final String URL_BASE = "http://www.omdbapi.com/?t=";
    private final String API_KEY = "&apikey=58e4a172";
    private ConvierteDatos conversor= new ConvierteDatos();

    public void muestraElMenu(){
        //Busca los datos generales de la serie
        System.out.println("Por favor escribe el nombre de la serie que deseas buscar");
        var nombreSerie = teclado.nextLine();
        var json = consumoApi.obtenerDatos(	URL_BASE + nombreSerie.replace(" ", "+") + API_KEY);
        var datos = conversor.obtenerDatos(json, DatosSerie.class);
        System.out.println(datos);

        //Buscar los datos de todas las temporadas
        List<DatosTemporada> temporadas = new ArrayList<>();
        for (int i = 1; i <= datos.totalDeTemporadas(); i++) {
            json= consumoApi.obtenerDatos(URL_BASE+nombreSerie.replace(" ","+")+"&Season="+i+API_KEY);
            var datosTemporadas = conversor.obtenerDatos(json, DatosTemporada.class);
            temporadas.add(datosTemporadas);
        }
        temporadas.forEach(System.out::println);

        //Mostrar solo el titulo de los episodios para las temporadas
//        for (int i = 0; i < datos.totalDeTemporadas() ; i++) {
//            List<DatosEpisodio> episodiosTemporada = temporadas.get(i).episodios();
//            for (int j = 0; j < episodiosTemporada.size() ; j++) {
//                System.out.println(episodiosTemporada.get(j).titulo());
//            }
//        }
        //Con Funcion Lambda
//        temporadas.forEach(t -> {
//            t.episodios().forEach(e -> {
//                System.out.println(e.titulo());
//            });
//        });
        //Convertir todas las informaciones a una lista del tipo DatosEpisodios

        List<DatosEpisodio> datosEpisodios = temporadas.stream()
                .flatMap(t-> t.episodios().stream())
                .collect(Collectors.toList());

        // Top 5 episodios
//        System.out.println("Top 5 Episodios");
//        datosEpisodios.stream()
//                .filter(e-> !e.evaluacion().equalsIgnoreCase("N/A"))
//                .peek(e-> System.out.println("Primer Filtro (N/A)"+e))
//                .sorted(Comparator.comparing(DatosEpisodio::evaluacion).reversed())
//                .peek(e-> System.out.println("Segundo Filtro (N>m)"+e))
//                .map(e-> e.titulo().toUpperCase())
//                .peek(e-> System.out.println("Tercer Filtro Mayúscula(n>M)"+e))
//                .limit(5)
//                .forEach(System.out::println);

        //Convirtiendo los datos a una lista del tipo episodio
        List<Episodio> episodios = temporadas.stream()
                .flatMap(t-> t.episodios().stream()
                .map(d->new Episodio(t.numero(),d)))
                .collect(Collectors.toList());

        //episodios.forEach(System.out::println);

        //Busqueda de episodios a partir de x año

//        System.out.println("Por favor indica el año a partir del cual deseas ver los episodios ");
//        var fecha = teclado.nextLine();
//        teclado.nextLine();
//          LocalDate fechaBusqueda = LocalDate.of(Integer.parseInt(fecha),1,1);

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
//        episodios.stream()
//                .filter( e -> e.getFechaDeLanzamiento() != null && e.getFechaDeLanzamiento()
//                        .isAfter(fechaBusqueda))
//                .forEach(e-> System.out.println(
//                        "Temporada" + e.getTemporada()+
//                                "Episodio" + e.getTitulo()+
//                                "Fecha de Lanzamiento" + e.getFechaDeLanzamiento().format(dtf)
//                ));

        //Busca episodio por pedazo de titutlo

//        System.out.println("Por favor escriba el titulo del episodio que desea ver");
//        var pedazoTitulo = teclado.nextLine();
//        Optional<Episodio> episodioBuscado = episodios.stream()
//                .filter(e -> e.getTitulo().toUpperCase().contains(pedazoTitulo.toUpperCase()))
//                .findFirst();
//
//        if(episodioBuscado.isPresent()){
//            System.out.println("Episodio encontrado");
//            System.out.println("Los datos son:"+episodioBuscado.get());
//        }else{
//            System.out.println("Episodio no encontrado");
//        }
        Map<Integer, Double>evaluacionesPorTemporada = episodios.stream()
                .filter(e -> e.getEvaluacion() > 0.0)
                .collect(Collectors.groupingBy(Episodio::getTemporada,
                        Collectors.averagingDouble(Episodio::getEvaluacion)));
        System.out.println(evaluacionesPorTemporada);

        DoubleSummaryStatistics est= episodios.stream()
                .filter(e -> e.getEvaluacion() > 0.0)
                .collect(Collectors.summarizingDouble(Episodio::getEvaluacion));
        System.out.println("Media de evaluaciones: "+est.getAverage());
        System.out.println("Episodio mejor evaluado:"+est.getMax());
        System.out.println("Episodio peor evaluado:"+est.getMin());
    }
}