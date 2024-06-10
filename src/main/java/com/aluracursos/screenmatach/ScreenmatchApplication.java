package com.aluracursos.screenmatach;

import com.aluracursos.screenmatach.principal.Principal;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class ScreenmatchApplication implements CommandLineRunner {

	@Override
	public void run(String... args) throws Exception {
		Principal principal = new Principal();
		principal.muestraElMenu();
//		EjemploStream ejemploStreams = new EjemploStream();
//		ejemploStreams.muestraEjmplo();
	}
	public static void main(String[] args) {
		SpringApplication.run(ScreenmatchApplication.class, args);
	}


}
