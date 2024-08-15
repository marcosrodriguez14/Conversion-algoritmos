package utils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.function.Function;
import java.util.stream.Collectors;

import us.lsi.common.Files2;
import us.lsi.common.List2;

public class FicherosListas {
	
	private static Random rr = new Random(System.nanoTime()); // para inicializarlo una sola vez y compartirlo con los métodos que lo requieran

	public record PropiedadesListas(Integer sizeMin, Integer sizeMax, Integer numSizes, Integer minValue, Integer maxValue, Integer numListPerSize, Integer numCasesPerList, Random rr) {
		public static PropiedadesListas of(Integer sizeMin, Integer sizeMax, Integer numSizes, Integer minValue, Integer maxValue, Integer numListPerSize, Integer numCasesPerList, Random rr) {
			return new PropiedadesListas(sizeMin, sizeMax, numSizes, minValue, maxValue, numListPerSize, numCasesPerList, rr);
		}
	}
	
	/**
	 * Devuelve una lista de enteros de tamaño sizeList y con valores en el rango [minValue, maxValue)
	 * @param rr 
	 * @param sizeList 
	 * @return 
	 */
	public static List<Integer> generaListaEnteros(Integer sizeList, PropiedadesListas p) {
		List <Integer> ls = new ArrayList<Integer>();
		for (int i=0;i<sizeList;i++) {
			ls.add(p.minValue()+p.rr().nextInt(p.maxValue()-p.minValue()));
		}
		return ls;
	}
	
	
	public static <E> List<E> listaOrdenada(List<E> ls, Comparator<?> comparator) {
		List<E> lc = List2.copy(ls);
		lc.sort((Comparator<? super E>) comparator);
		return lc;
	}
	
	public static <E> List<E> listaOrdenada(List<E> ls) {
		return listaOrdenada(ls, Comparator.naturalOrder());
	}
	


	/**
	 * Elemento aleatorio de la lista
	 * @param le
	 * @param rr
	 * @return
	 */
	public static Integer elemBusquedaSI(List<Integer> le, Random rr) {
		Integer ir = rr.nextInt(le.size());
		return le.get(ir);
	}
	
	/**
	 * Se generará una posición aleatoria entre [0, listaOrd.size()] 
	 * y el elemento generado será el primer entero menor que listaOrd[pos] que no está 
	 * en la lista, o el primer entero mayor que listaOrd[pos] si pos==listaOrd.size()
	 * 
	 * @param le
	 * @param rr
	 * @return
	 */
	public static Integer elemBusquedaNO(List<Integer> le, Random rr) {
		Integer ir = rr.nextInt(le.size()+1);
		Integer elem = null;
		if (ir < le.size()) {
			elem = le.get(ir);
			while (elem>=le.get(0) && ir>=0) {
				Integer ix = buscaMenorPosicion(elem,le,ir);
				if (ix == -1) { // elem no esta en le
					break; 
				} else { // la menor posici�n ocupada por elem es ix
					elem--;
					ir = ix-1;
				}
			}
		} else {
			 elem = le.get(le.size()-1)+1;
		}
		return elem;
	}
	
	public static Integer buscaMenorPosicion(Integer elem, List<Integer> le, Integer ir) {
		boolean enc = false;
		Integer i = ir;
		while (i>=0 && !enc) {
			if (le.get(i)<elem) enc=true; // i es la primera posici�n con valor menor que elem
			else i--;
		}
		return (elem==le.get(i+1))?i+1:-1;
	}


	public static Integer elemBusquedaProb(List<Integer> leo, double p) {
		return (rr.nextDouble()<p) ? elemBusquedaSI(leo, rr) : elemBusquedaNO(leo, rr); 
	}
	

	
	public static void generaFicherosDatos(PropiedadesListas p, String ficheroListas, 
			String ficheroListasOrdenadas, 
			String ficheroElementosSI, 
			String ficheroElementosNO,
			String ficheroElementosProb, 
			Double... probs) {
		rr = new Random(System.nanoTime()); 
		generaFicheroListasEnteros(ficheroListas, p);
		generaFicheroListasEnterosOrdenadas(ficheroListas, ficheroListasOrdenadas);		
		generaFicherosElementosBusquedaSI(ficheroListas,
				ficheroElementosSI, p);
		generaFicherosElementosBusquedaNO(ficheroListas,
				ficheroElementosNO, p);
		for (int i=0; i<probs.length; i++) {
			String fichero = ficheroElementosProb
					.replace(".txt", String.format("_%d.txt",i));
			generaFicherosElementosBusquedaProb(ficheroListas, fichero, probs[i], p);
		}
	}
	

	/**
	 * Genera un fichero con listas de enteros. En cada línea va una lista de elementos 
	 * separados por comas, precedida por su tamaño y el símbolo #.
	 * Los tamaños de las listas están en el rango [sizeMin, sizeMax].
	 * El número de diferentes tamaños es numSizes.
	 * Para cada tamaño hay repSizes listas
	 * Los valores de los elementos de las listas están en el rango [minValue, maxValue)
	 * @param fichero
	 */
	public static void generaFicheroListasEnteros(String fichero, PropiedadesListas p) {
		for (int i=0; i<p.numSizes(); i++) {
			int tam = p.sizeMin() + i*(p.sizeMax()-p.sizeMin())/(p.numSizes()-1);
			for (int j=0; j<p.numListPerSize(); j++) {
				List<Integer> ls = generaListaEnteros(tam, p);
				String sls = ls.stream().map(x->x.toString()).collect(Collectors.joining(",")); 
				Resultados.toFile(String.format("%d#%s",tam,sls), fichero, false);
			}
		}
	}
	
	public static void generaFicheroListasEnterosOrdenadas(String ficheroNoOrd, String ficheroOrd) {
		List<String> lineas = Files2.linesFromFile(ficheroNoOrd);
		for (int i=0; i<lineas.size(); i++) {
			String linea = lineas.get(i);
			List<String> ls = List2.parse(linea, "#", Function.identity());
			Integer tam = Integer.parseInt(ls.get(0)); 
			List<Integer> le = List2.parse(ls.get(1), ",", Integer::parseInt);
			List<Integer> leo = listaOrdenada(le, Comparator.naturalOrder());
			String sleo = leo.stream().map(x->x.toString()).collect(Collectors.joining(",")); 
			Resultados.toFile(String.format("%d#%s",tam,sleo), ficheroOrd, false);
		}
	}
	
	
	/**
	 * Genera un fichero con los elementos a buscar en las listas del fichero ficheroListas,
	 * cumpliéndose que los elementos NO pertenecen a las listas correspondientes
	 * El número de elementos a buscar para cada lista es numProbSameList
	 * Se generará una posición aleatoria entre [0, listaOrd.size()] 
	 * y el elemento generado será el primer entero menor que listaOrd[pos] que no está 
	 * en la lista, o el primer entero mayor que listaOrd[pos] si pos==listaOrd.size()
	 * @param ficheroListas
	 * @param ficheroElementos
	 * @param numProbSameList
	 */
	public static void generaFicherosElementosBusquedaNO(String ficheroListas, String ficheroElementos, PropiedadesListas p) {
		List<String> lineas = Files2.linesFromFile(ficheroListas);
		Random rr = new Random(System.nanoTime());
		for (int i=0; i<lineas.size(); i++) {
			String linea = lineas.get(i);
			List<String> ls = List2.parse(linea, "#", Function.identity());
			Integer tam = Integer.parseInt(ls.get(0)); 
			List<Integer> le = List2.parse(ls.get(1), ",", Integer::parseInt);
			List<Integer> leo = listaOrdenada(le);
			for (int j=0; j<p.numCasesPerList(); j++) {
				Resultados.toFile(String.format("%d#%d", tam, elemBusquedaNO(leo, rr)), ficheroElementos, false);
			}
		}
	}

	
	/**
	 * Genera un fichero con los elementos a buscar en las listas del fichero ficheroListas,
	 * cumpliéndose que los elementos SÍ pertenecen a las listas correspondientes
	 * El número de elementos a buscar para cada lista es numProbSameList
	 * @param rr 
	 * @param ficheroListas
	 * @param ficheroElementos
	 * @param numProbSameList
	 */
	public static void generaFicherosElementosBusquedaSI(String ficheroListas, String ficheroElementos, PropiedadesListas p) {
		List<String> lineas = Files2.linesFromFile(ficheroListas);
		if (rr==null) rr = new Random(System.nanoTime());
		for (int i=0; i<lineas.size(); i++) {
			String linea = lineas.get(i);
			List<String> ls = List2.parse(linea, "#", Function.identity());
			Integer tam = Integer.parseInt(ls.get(0)); 
			List<Integer> le = List2.parse(ls.get(1), ",", Integer::parseInt);
			for (int j=0; j<p.numCasesPerList(); j++) {
				Resultados.toFile(String.format("%d#%d", tam, elemBusquedaSI(le, rr)), ficheroElementos, false);
			}
		}
	}

	public static void generaFicherosElementosBusquedaProb(String ficheroListas, String ficheroElementos, double prob, PropiedadesListas p) {
		List<String> lineas = Files2.linesFromFile(ficheroListas);
		Random rr = new Random(System.nanoTime());
		for (int i=0; i<lineas.size(); i++) {
			String linea = lineas.get(i);
			List<String> ls = List2.parse(linea, "#", Function.identity());
			Integer tam = Integer.parseInt(ls.get(0)); 
			List<Integer> le = List2.parse(ls.get(1), ",", Integer::parseInt);
			List<Integer> leo = listaOrdenada(le);
			for (int j=0; j<p.numCasesPerList(); j++) {
				Resultados.toFile(String.format("%d#%d", tam, elemBusquedaProb(leo, prob)), ficheroElementos, false);
			}
		}
	}
}
