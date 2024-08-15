package tests;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;


import ejemplos.Ejemplo3;
import us.lsi.common.Files2;
import us.lsi.common.List2;
import us.lsi.common.Pair;
import us.lsi.curvefitting.DataCurveFitting;
import utils.FicherosListas;
import utils.FicherosListas.PropiedadesListas;
import utils.GraficosAjuste;
import utils.Resultados;
import utils.TipoAjuste;

public class TestEjemplo3 {
	
	// Parámetros de generación de las listas
	private static Integer sizeMin = 100; // tamaño mínimo de lista
	private static Integer sizeMax = 100000; // tamaño máximo de lista
	private static Integer numSizes = 50; // número de tamaños de listas
	private static Integer minValue = 0; 
	private static Integer maxValue = 1000000;
	private static Integer numListPerSize = 1; // número de listas por cada tamaño  (UTIL???) 
	private static Integer numCasesPerList = 30; // número de casos (elementos a buscar) por cada lista 
	private static Random rr = new Random(System.nanoTime()); // para inicializarlo una sola vez y compartirlo con los métodos que lo requieran

	// Parámetros de medición
	private static Integer numMediciones = 5; // número de mediciones de tiempo de cada caso (número de experimentos)
	private static Integer numIter = 50; // número de iteraciones para cada medición de tiempo
	private static Integer numIterWarmup = 100000; // número de iteraciones para warmup
	
	public static void main(String[] args) {
		// Generación de Listas
		PropiedadesListas props = PropiedadesListas.of(sizeMin, sizeMax, numSizes, minValue, maxValue,numListPerSize, numCasesPerList, rr);
		//generaFicherosDatos(props);
		
		//generaFicherosTiempoEjecucion();
		 muestraGraficas();
	}
	

	
	// Dos tipos de listas: ordenadas y no ordenadas
	private static List<String> ficheroListaEntrada = List.of("ficheros/Listas.txt",
			"ficheros/ListasOrdenadas.txt");
	private static List<String> etiquetaListaEntrada = List.of("NoOrdenada",
			"Ordenada");
	
	// Tres tipos de listas de elementos a buscar en las listas: 
	// 		- Elementos que SÍ están todos en las listas
	//		- Elementos que NO están en las listas
	//		- Elementos que están en las listas con una cierta probabilidad
	private static List<String> ficheroElemEntrada = List.of("ficheros/ElementosSI.txt",
			"ficheros/ElementosNO.txt",
			"ficheros/ElementosProb_0.txt");
	private static List<String> etiquetaElemEntrada = List.of("SI",
			"No",
			"MedioProb_0"
			);
	
	// Seis métodos distintos de búsqueda a probar
	// Para cada método hay 3 experimentos distintos (elementos SI, NO, Prob)
	// Los dos primeros métodos funcionan sobre listas no ordenadas
	// Los cuatro restantes sobre listas ordenadas
	private static List<BiFunction<List<Integer>, Integer, Integer>> metodosBusqueda = 
			List.of(Ejemplo3::busquedaLineal, 
					Ejemplo3::busquedaLinealR,
					Ejemplo3::busquedaLinealOrdI, 
					Ejemplo3::busquedaLinealOrdR,
					Ejemplo3::busquedaBinaria,
					Ejemplo3::busquedaBinariaR);
	private static List<String> etiquetaMetodo = List.of("LinealI",
			"LinealR",
			"LinealOrdI",
			"LinealOrdR",
			"BinariaI",
			"BinariaR");
	

	
	public static void muestraGraficas() {
		List<String> ficherosSalida = new ArrayList<>();
		List<String> labels = new ArrayList<>();
		
		for (int i=0; i<ficheroListaEntrada.size(); i++) { 
			for (int j=0; j<ficheroElemEntrada.size(); j++) {
				int kini = i==0 ? 0 : 2; 		// búsqueda lineal, listas no ordenadas 
				int kfin = i==0 ? 2 : 6;		// búsqueda lineal Ord y binaria, listas ordenadas
				for (int k=kini; k<kfin; k++) { 
					String ficheroMediosSalida = String.format("ficheros/TiemposMed%s%s%s.csv",
							etiquetaMetodo.get(k),
							etiquetaListaEntrada.get(i),
							etiquetaElemEntrada.get(j));
					
					String label = etiquetaMetodo.get(k)+
							etiquetaListaEntrada.get(i)+
							etiquetaElemEntrada.get(j);
					

					
					TipoAjuste tipoAjuste = label.contains("Lineal")? TipoAjuste.POWERANB:TipoAjuste.LOG2_0;
					
					GraficosAjuste.show(ficheroMediosSalida, tipoAjuste, label);
					
					// Obtener ajusteString para mostrarlo en gráfica combinada
					Pair<Function<Double, Double>, String> parCurve = GraficosAjuste.fitCurve(
							DataCurveFitting.points(ficheroMediosSalida), tipoAjuste);
					String ajusteString = parCurve.second();
					
					// Elegir filtro para gráfica combinada para evitar
					// que salgan los 18 experimentos en una única gráfica
//					if(etiquetaElemEntrada.get(j).equals("SI")) {
//					if(etiquetaMetodo.get(k).contains("LinealOrdI") || 
//							etiquetaMetodo.get(k).contains("LinealI")) {
					if(!etiquetaListaEntrada.get(i).contains("NoOrdenada")
							&& !etiquetaElemEntrada.get(j).contains("Prob")) {
						ficherosSalida.add(ficheroMediosSalida);
						labels.add(String.format("%s     %s", label, ajusteString));
					}

				}
			}
		}			
		GraficosAjuste.showCombined("Búsqueda listas", ficherosSalida, labels);

	}

	
	public static void generaFicherosDatos(PropiedadesListas p) {
		Resultados.cleanFiles(List.of("ficheros/Listas.txt",
				"ficheros/ListasOrdenadas.txt",
				"ficheros/ElementosSI.txt",
				"ficheros/ElementosNO.txt",
				"ficheros/ElementosProb_0.txt"));
		
		FicherosListas.generaFicherosDatos(p,"ficheros/Listas.txt",
				"ficheros/ListasOrdenadas.txt",
				"ficheros/ElementosSI.txt",
				"ficheros/ElementosNO.txt",
				"ficheros/ElementosProb.txt",
				0.5
		);
	}
	
	
	public static void generaFicherosTiempoEjecucion() {
		for (int i=0; i<ficheroListaEntrada.size(); i++) { 
			for (int j=0; j<ficheroElemEntrada.size(); j++) {
				int kini = i==0 ? 0 : 2; 		// búsqueda lineal, listas no ordenadas 
				int kfin = i==0 ? 2 : 6;		// búsqueda lineal Ord y binaria, listas ordenadas
				for (int k=kini; k<kfin; k++) { 
					String ficheroSalida = String.format("ficheros/Tiempos%s%s%s.csv",
							etiquetaMetodo.get(k),
							etiquetaListaEntrada.get(i),
							etiquetaElemEntrada.get(j));
					System.out.println(ficheroSalida);
					String ficheroMediosSalida = String.format("ficheros/TiemposMed%s%s%s.csv",
							etiquetaMetodo.get(k),
							etiquetaListaEntrada.get(i),
							etiquetaElemEntrada.get(j));
					testTiemposEjecucionBusqueda(ficheroListaEntrada.get(i),
						ficheroElemEntrada.get(j),
						metodosBusqueda.get(k),
						ficheroSalida,
						ficheroMediosSalida);
				}
			}
		}
	}

	private static void testTiemposEjecucionBusqueda(String ficheroListas, 
			String ficheroElementos, 
			BiFunction<List<Integer>, Integer, Integer> busca,
			String ficheroTiempos,
			String ficheroTiemposMedios) {
		Map<Problema, Double> tiempos = new HashMap<Problema,Double>();
		Map<Integer, Double> tiemposMedios; // tiempos medios por cada tamaño
		List<String> lineasListas = Files2.linesFromFile(ficheroListas);
		List<String> lineasElem = Files2.linesFromFile(ficheroElementos);
		for (int iter=0; iter<numMediciones; iter++) {
			System.out.println(iter);
			for (int i=0; i<lineasListas.size(); i++) { // numSizes*numListPerSize
				String lineaLista = lineasListas.get(i);
				List<String> ls = List2.parse(lineaLista, "#", Function.identity());
				Integer tam = Integer.parseInt(ls.get(0)); 
				List<Integer> le = List2.parse(ls.get(1), ",", Integer::parseInt);
				for (int j=0; j<numCasesPerList; j++) {
					String lineaElem = lineasElem.get(i*numCasesPerList+j);
					List<String> lse = List2.parse(lineaElem, "#", Function.identity());
					Integer elem = Integer.parseInt(lse.get(1)); 
					Problema p = Problema.of(tam, i%numListPerSize, j);
					warmup(busca, lineasListas.get(0), lineasElem.get(0));
					Integer[] res = new Integer[numIter];
					Long t0 = System.nanoTime();
					for (int z=0; z<numIter; z++) {
						res[z] = busca.apply(le, elem);
					}
					Long t1 = System.nanoTime();
					actualizaTiempos(tiempos, p, Double.valueOf(t1-t0)/numIter);
				}
			}
		}
		
		tiemposMedios = tiempos.entrySet().stream()
				.collect(Collectors.groupingBy(x->x.getKey().tam(),
							Collectors.averagingDouble(x->x.getValue())
							)
						);
		
//		Resultados.toFile(tiempos.entrySet().stream()
//				.map(x->TResultD.of(x.getKey().tam(), 
//									x.getKey().numList(), 
//									x.getKey().numCase(), 
//									x.getValue()))
//				.map(TResultD::toString),
//			ficheroTiempos,
//			true);
		
		Resultados.toFile(tiemposMedios.entrySet().stream()
				.map(x->TResultMedD.of(x.getKey(),x.getValue()))
				.map(TResultMedD::toString),
			ficheroTiemposMedios,
			true);
		
	}
	
	private static void actualizaTiempos(Map<Problema, Double> tiempos, Problema p, double d) {
		if (!tiempos.containsKey(p)) {
			tiempos.put(p, d);
		} else if (tiempos.get(p) > d) {
				tiempos.put(p, d);
		}
	}


	private static int warmup(BiFunction<List<Integer>, Integer, Integer> busca, String lineaLista, String lineaElem) {
		int res=0;
		List<String> ls = List2.parse(lineaLista, "#", Function.identity());
		Integer tam = Integer.parseInt(ls.get(0)); 
		List<Integer> le = List2.parse(ls.get(1), ",", Integer::parseInt);
		List<String> lse = List2.parse(lineaElem, "#", Function.identity());
		Integer elem = Integer.parseInt(lse.get(1)); 
		Integer z = 0; 
		for (int i=0; i<numIterWarmup; i++) {
			z += busca.apply(le, elem);
		}
		res = z>0?z+tam:tam;
		return res;
	}



	record TResult(Integer tam, Integer numList, Integer numCase, Long t) {
		public static TResult of(Integer tam, Integer numList, Integer numCase, Long t){
			return new TResult(tam, numList, numCase, t);
		}
		
		public String toString() {
			return String.format("%d,%d,%d,%d", tam, numList, numCase, t);
		}
	}
	
	record TResultD(Integer tam, Integer numList, Integer numCase, Double t) {
		public static TResultD of(Integer tam, Integer numList, Integer numCase, Double t){
			return new TResultD(tam, numList, numCase, t);
		}
		
		public String toString() {
			return String.format("%d,%d,%d,%.0f", tam, numList, numCase, t);
		}
	}
	
	record TResultMedD(Integer tam, Double t) {
		public static TResultMedD of(Integer tam, Double t){
			return new TResultMedD(tam, t);
		}
		
		public String toString() {
			return String.format("%d,%.0f", tam, t);
		}
	}
	
	
	record Problema(Integer tam, Integer numList, Integer numCase) {
		public static Problema of(Integer tam, Integer numList, Integer numCase){
			return new Problema(tam, numList, numCase);
		}
	}
	

}
