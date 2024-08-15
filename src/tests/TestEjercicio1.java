package tests;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import ejercicios.Ejercicio1;
import tests.TestEjemplo1.Problema;
import tests.TestEjemplo1.TResultD;
import us.lsi.common.Pair;
import us.lsi.common.Trio;
import us.lsi.curvefitting.DataCurveFitting;
import utils.GraficosAjuste;
import utils.Resultados;
import utils.TipoAjuste;

public class TestEjercicio1 {

	public static void main(String[] args) {


		//generaFicherosTiempoEjecucion();
		muestraGraficas();
	}
	private static Integer nMin = 100; // n mínimo
	private static Integer nMax = 100000; // n máximo para el factorial recursivo sin memoria 
	private static Integer nMaxBigInt = 10000; // n máximo para el factorial no exponencial
	private static Integer numSizes = 100; // número de problemas
	private static Integer numMediciones = 10; //10; // número de mediciones de tiempo de cada caso (número de experimentos)
												// para exponencial se puede reducir 
	private static Integer numIter = 50; //50; // número de iteraciones para cada medición de tiempo
											// para exponencial se puede reducir 
	private static Integer numIterWarmup = 1000; // número de iteraciones para warmup
	
	private static List<Trio<Function<Double, Number>, TipoAjuste, String>> metodosDouble = 
			List.of(
					Trio.of(Ejercicio1::factorialRecursivoDouble, TipoAjuste.POWERANB, "FactRec_Double"), 
					Trio.of(Ejercicio1::factorialIterativoDouble, TipoAjuste.POWERANB ,"FactIter_Double")
			);
	
	private static List<Trio<Function<Integer, Number>, TipoAjuste, String>> metodosBigInteger = 
			List.of(
					Trio.of(Ejercicio1::factorialRecursivoBigInteger, TipoAjuste.POWERANB, "FactRec_BigInteger"), 
					Trio.of(Ejercicio1::factorialIterativoBigInteger, TipoAjuste.POWERANB, "FactIter_BigInteger")
			);
	
	

	public static void generaFicherosTiempoEjecucion() {
		

		generaFicherosTiempoEjecucionMetodos(metodosDouble);
		generaFicherosTiempoEjecucionMetodos(metodosBigInteger);
	}
	private static void warmup (Function<Double,Double>metodo,Double n) {
		for(int i = 0; i<numIterWarmup;i++) {
			metodo.apply(n);
		}
	}
	
	private static <E> void generaFicherosTiempoEjecucionMetodos(List<Trio<Function<E, Number>, TipoAjuste,String>> metodos) {
		for (int i=0; i<metodos.size(); i++) { 
			
			String ficheroSalida = String.format("ficheros/Ejercicio1%s.csv",
					metodos.get(i).third());
			System.out.println(ficheroSalida);
			
			
			int numMax = ficheroSalida.contains("BigInt") ? nMaxBigInt : nMax; 

			testTiemposEjecucion(nMin, numMax, 
						metodos.get(i).first(),
						ficheroSalida);
			}
	}
	@SuppressWarnings("unchecked")
	public static <E> void testTiemposEjecucion(Integer nMin, Integer nMax,
			Function<E, Number> funcion,
			String ficheroTiempos) {
		
		Map<Problema, Double> tiempos = new HashMap<Problema,Double>();
		Integer nMed = numMediciones; 
		for (int iter=0; iter<nMed; iter++) {
			System.out.println(iter);
			for (int i=0; i<numSizes; i++) {
				Double r = Double.valueOf(nMax-nMin)/(numSizes-1);
				Integer tam = (Integer.MAX_VALUE/nMax > i) 
						? nMin + i*(nMax-nMin)/(numSizes-1)
						: nMin + (int) (r*i) ;
				Problema p = Problema.of(tam);
				warmup(Ejercicio1::factorialRecursivoDouble, 10.);
				Integer nIter = numIter;
				Number[] res = new Number[nIter];
				Long t0 = System.nanoTime();
				for (int z=0; z<nIter; z++) {
					if(ficheroTiempos.contains("Long")) {
						res[z] = funcion.apply((E) Long.valueOf(tam));
					}
					else if (ficheroTiempos.contains("Double")) {
						res[z] = funcion.apply((E) Double.valueOf(tam));
					}
					else {
						res[z] = funcion.apply((E) tam);
					}
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
	public static void muestraGraficas() {
		List<String> ficherosSalida = new ArrayList<>();
		List<String> labels = new ArrayList<>();
		
		muestraGraficasMetodos(metodosBigInteger, ficherosSalida, labels);
		muestraGraficasMetodos(metodosDouble, ficherosSalida, labels);
		
		GraficosAjuste.showCombined("Factorial", ficherosSalida, labels);
	}
	public static <E> void muestraGraficasMetodos(List<Trio<Function<E, Number>, TipoAjuste, String>> metodos, List<String> ficherosSalida, List<String> labels) {
		for (int i=0; i<metodos.size(); i++) { 
			
			String ficheroSalida = String.format("ficheros/Ejercicio1%s.csv",
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
	}

}