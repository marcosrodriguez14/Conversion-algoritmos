package tests;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;

import ejemplos.Ejemplo1;
import us.lsi.common.Pair;
import us.lsi.common.Trio;
import us.lsi.curvefitting.DataCurveFitting;
import us.lsi.math.Math2;
import utils.GraficosAjuste;
import utils.Resultados;
import utils.TipoAjuste;

public class TestEjemplo1 {
	
	public static void main(String[] args) {
	//	generaFicherosTiempoEjecucion();
		muestraGraficas();
	}
	
	private static Integer nMin = 50; // n mínimo para el cálculo de potencia
	private static Integer nMax = 100000; // n máximo para el cálculo de potencia
	private static Integer numSizes = 50; // número de problemas (número de potencias distintas a calcular)
	private static Integer numMediciones = 10; // número de mediciones de tiempo de cada caso (número de experimentos)
	private static Integer numIter = 50; // número de iteraciones para cada medición de tiempo
	private static Integer numIterWarmup = 100000; // número de iteraciones para warmup
	
	// Trios de métodos a probar con su tipo de ajuste y etiqueta para el nombre de los ficheros
	private static List<Trio<BiFunction<Double, Integer, Number>, TipoAjuste, String>> metodos = 
			List.of(
				Trio.of(Ejemplo1::potenciaR, TipoAjuste.POWERANB, "PotenciaRecursiva(lineal)"), 
				Trio.of(Ejemplo1::potenciaIter,TipoAjuste.POWERANB, "PotenciaIterativa(lineal)"),
				Trio.of(Math2::pow, TipoAjuste.LOG2_0,"PotenciaRecursiva(log)"),
				Trio.of(Math2::powr, TipoAjuste.LOG2_0,"PotenciaIterativa(log)")
			);
	
	

	public static void generaFicherosTiempoEjecucion() {
		
		for (int i=0; i<metodos.size(); i++) { 
						
			String ficheroSalida = String.format("ficheros/Tiempos%s.csv",
					metodos.get(i).third());
			
			testTiemposEjecucion(nMin, nMax, 
						metodos.get(i).first(),
						ficheroSalida
						);
			}
	}

	
	public static void testTiemposEjecucion(Integer nMin, Integer nMax,
			BiFunction<Double, Integer, Number> funcion,
			String ficheroTiempos
			) {
		
		Map<Problema, Double> tiempos = new HashMap<Problema,Double>();
		Integer nMed = numMediciones; 
		for (int iter=0; iter<nMed; iter++) {
			for (int i=0; i<numSizes; i++) {
				Double r = Double.valueOf(nMax-nMin)/(numSizes-1);
				Integer tam = (Integer.MAX_VALUE/nMax > i) 
						? nMin + i*(nMax-nMin)/(numSizes-1)
						: nMin + (int) (r*i) ;
				Problema p = Problema.of(tam);
				warmup(funcion, 10.,10);
				Integer nIter = numIter;
				Double[] res = new Double[nIter];
				Long t0 = System.nanoTime();
				for (int z=0; z<nIter; z++) {
					res[z] = (Double) funcion.apply(Double.valueOf(tam), tam);
				}
				Long t1 = System.nanoTime();
				actualizaTiempos(tiempos, p, Double.valueOf(t1-t0)/nIter);
			}
			
		}
		
		Resultados.toFile(tiempos.entrySet().stream()
				.map(x->TResultD.of(x.getKey().tam(), 
									x.getValue()))
				.map(TResultD::toString),
				ficheroTiempos, true);
		
	}
	
	private static void actualizaTiempos(Map<Problema, Double> tiempos, Problema p, double d) {
		if (!tiempos.containsKey(p)) {
			tiempos.put(p, d);
		} else if (tiempos.get(p) > d) {
				tiempos.put(p, d);
		}
	}
	
	private static void warmup(BiFunction<Double,Integer, Number> pot, Double a, Integer n) {
		for (int i=0; i<numIterWarmup; i++) {
			pot.apply(a,n);
		}
	}

	record TResultD(Integer tam, Double t) {
		public static TResultD of(Integer tam, Double t){
			return new TResultD(tam, t);
		}
		
		public String toString() {
			return String.format("%d,%.0f", tam, t);
		}
	}

	record Problema(Integer tam) {
		public static Problema of(Integer tam){
			return new Problema(tam);
		}
	}
	
	
	public static void muestraGraficas() {
		System.out.println("a*n^b*(ln n)^c + d");
		List<String> ficherosSalida = new ArrayList<>();
		List<String> labels = new ArrayList<>();
		for (int i=0; i<metodos.size(); i++) { 
			
			String ficheroSalida = String.format("ficheros/Tiempos%s.csv",
					metodos.get(i).third());
			ficherosSalida.add(ficheroSalida);
			String label = metodos.get(i).third();
			System.out.println(label);
			
			TipoAjuste tipoAjuste = metodos.get(i).second();
			GraficosAjuste.show(ficheroSalida, tipoAjuste, label);

			// Obtener ajusteString para mostrarlo en gráfica combinada
			Pair<Function<Double, Double>, String> parCurve = GraficosAjuste.fitCurve(
					DataCurveFitting.points(ficheroSalida), tipoAjuste);
			String ajusteString = parCurve.second();
			labels.add(String.format("%s     %s", label, ajusteString));

					
		}

		GraficosAjuste.showCombined("Potencia", ficherosSalida, labels);
	}

}
