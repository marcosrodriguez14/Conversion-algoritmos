package utils;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.stream.Stream;

public class Resultados {
	
	/**
	 * Escribe en fichero, con la opci�n de a�adir al final
	 * @param s
	 * @param file
	 */
	public static void toFile(String s, String file, Boolean overwrite) {
		try {
			if(overwrite) {
				new PrintWriter(file).close();
			}
			final PrintWriter f = 
					new PrintWriter(new BufferedWriter(
							new FileWriter(file, true)));
			f.println(s);
			f.close();
		} catch (IOException e) {
			throw new IllegalArgumentException(
					"No se ha podido crear el fichero " + file);
		}
	}
	
	public static void cleanFile(String file) {
		try {
			new PrintWriter(file).close();
		} catch (IOException e) {
			throw new IllegalArgumentException(
					"No se ha podido limpiar el fichero " + file);
		}
	}
	
	public static void cleanFiles(List<String> files) {
		for(String file: files) {
			cleanFile(file);
		}
	}

	/**
	 * Escribe en fichero, con la opci�n de a�adir al final
	 * @param s
	 * @param file
	 */
	public static void toFile(Stream<String> s, String file, Boolean overwrite) {
		try {
			if(overwrite) {
				new PrintWriter(file).close();
			}
			final PrintWriter f = new PrintWriter(
									new BufferedWriter(
											new FileWriter(file,true)));
			s.forEach(x -> {
				f.println(x);
			});
			f.close();
		} catch (IOException e) {
			throw new IllegalArgumentException("No se ha podido crear el fichero " + file);
		}
	}

	

}
