package utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;

import org.apache.commons.math3.fitting.WeightedObservedPoint;
import com.github.sh0nk.matplotlib4j.Plot;
import com.github.sh0nk.matplotlib4j.PythonConfig;
import com.github.sh0nk.matplotlib4j.PythonExecutionException;

import us.lsi.common.Pair;
import us.lsi.curvefitting.DataCurveFitting;
import us.lsi.curvefitting.Exponential;
import us.lsi.curvefitting.PolinomialLog;
import us.lsi.curvefitting.Polynomial;

public class GraficosAjuste {
	
	
	// Este método devuelve un par: función para obtener puntos de la curva de ajuste, y String para mostrar coeficientes del ajuste
	public static Pair<Function<Double,Double>, String> fitCurve(List<WeightedObservedPoint> points, TipoAjuste tipoAjuste) {
		
		// Curva de ajuste 
		final double[] coeff;
		String ajusteString = null;
		Function<Double,Double> f = null;
		
		switch(tipoAjuste) {
			case LINEAL: // a + n*b
				// Cálculo de coeficientes
				coeff = Polynomial.of(1).fit(points, new double[]{1.,1.,1.,1.});
				System.out.println(String.format("Solutions = a = %.2f,b = %.3g",coeff[0],coeff[1]));
				// Título de la gráfica con los coeficientes
				ajusteString = String.format("%.2f + n*%.3g", coeff[0],coeff[1] );
				f = x->coeff[0] + coeff[1]*x;
				break;
			case POLYNOMIALLOG: // a*n^b*(ln n)^c + d
				coeff = PolinomialLog.of().fit(points, new double[]{1.,1.,1.,1.});
				System.out.println(String.format("Solutions = a = %.2f,b = %.2f,c = %.2f,d = %.2f",coeff[0],coeff[1],coeff[2],coeff[3]));
				ajusteString = String.format("%.2f * n^%.2f * (ln n)^%.2f + %.2f", 
				coeff[0],coeff[1],coeff[2],coeff[3]);
				f = x->PolinomialLog.of().value(x, coeff);
				break;
			case POWER: // a*n^b + c
				coeff = Power.of().fit(points, new double[]{1.,1.,1.});
				System.out.println(String.format("Solutions = a = %.2f,b = %.2f,c = %.2f",coeff[0],coeff[1],coeff[2]));
				ajusteString = String.format("%.2f * n^%.2f + %.2f", coeff[0],coeff[1],coeff[2]);
				f = x->Power.of().value(x, coeff);
				break;
			case POWERANB: // a*n^b
				coeff = PowerANB.of().fit(points, new double[]{1.,1.});
				System.out.println(String.format("Solutions = a = %.2f,b = %.2f",coeff[0],coeff[1]));
				ajusteString = String.format("%.3g * n^%.2f", 
				coeff[0],coeff[1]);
				f = x->PowerANB.of().value(x, coeff);
				break;
			case LOG2: // a*(ln n) + b
				coeff = Log2.of().fit(points,new double[]{1.,1.});
				System.out.println(String.format("Solutions = a = %.2f,b = %.2f",coeff[0],coeff[1]));
				ajusteString = String.format("%.2f * (ln n) + %.2f", 
				coeff[0],coeff[1]);
				f = x->Log2.of().value(x, coeff);
				break;
			case LOG2_0:
				coeff = Log2_0.of().fit(points,new double[]{1.});
				System.out.println(String.format("Solutions = a = %.2f",coeff[0]));
				ajusteString = String.format("%.2f * (ln n)", coeff[0]);
				f = x->Log2_0.of().value(x, coeff);
				break;
			case NLOGN: // a*n*(ln n) + b
				coeff = NLogN.of().fit(points,new double[]{1.,1.});
				System.out.println(String.format("Solutions = a = %.2f,b = %.2f",coeff[0],coeff[1]));
				ajusteString = String.format("%.2f * n * (ln n) + %.2f", 
				coeff[0],coeff[1]);
				f = x->NLogN.of().value(x, coeff);
				break;
			case NLOGN_0: // a*n*(ln n)
				coeff = NLogN_0.of().fit(points,new double[]{1.});
				System.out.println(String.format("Solutions = a = %.2f",coeff[0]));
				ajusteString = String.format("%.2f * n * (ln n)", coeff[0]);
				f = x->NLogN_0.of().value(x, coeff);
				break;
			case EXP: // a*b^(c*n) + d
				coeff = Exponential.of().fit(points,new double[]{1.,1.,1.,1.});
				System.out.println(String.format("Solutions = a = %.2f,b = %.2f,c = %.2f,d = %.2f",coeff[0],coeff[1],coeff[2],coeff[3]));
				ajusteString = String.format("%.2f * %.2f^(%.2f * n) + %.2f", 
						coeff[0],coeff[1],coeff[2],coeff[3] );
				f = x->Exponential.of().value(x,coeff);
				break;
			case EXP2: // a*b^n + c
				coeff = Exponential2.of().fit(points,new double[]{1.,1.,1.});
				System.out.println(String.format("Solutions = a = %.2f,b = %.2f,c = %.2f",coeff[0],coeff[1],coeff[2]));
				ajusteString = String.format("%.2f * %.2f^n + %.2f", 
						coeff[0],coeff[1],coeff[2] );
				f = x->Exponential2.of().value(x,coeff);
				break;
			case EXP2_0: // a*b^n
				coeff = Exponential2_0.of().fit(points,new double[]{1.,1.});
				System.out.println(String.format("Solutions = a = %.2f,b = %.2f",coeff[0],coeff[1]));
				ajusteString = String.format("%.2f * %.2f^n", 
						coeff[0],coeff[1] );
				f = x->Exponential2_0.of().value(x,coeff);
				break;

			default:
				break;
		}	
			
		return Pair.of(f, ajusteString);
		
	}
	
	public static void show(String dataFile, TipoAjuste tipoAjuste, String title) {
		List<WeightedObservedPoint> points = DataCurveFitting.points(dataFile);
		// Es necesario ordenar los puntos para que se muestre correctamente
		List<WeightedObservedPoint> pointsSorted = new ArrayList<>(points);
		pointsSorted.sort(Comparator.comparing(x->x.getX()));

		// Tamaños de problema (Eje X)
		List<Double> lx = pointsSorted.stream().map(p->p.getX()).toList();
		
		// Puntos registrados de tiempo de ejecución (Eje Y)
		List<Double> datos = pointsSorted.stream().map(p->p.getY()).toList();		
		
		// Puntos de la curva de ajuste (Eje Y)
		Pair<Function<Double, Double>, String> parCurve = fitCurve(pointsSorted, tipoAjuste);
		Function<Double, Double> ajusteFuncion = parCurve.first();
		String ajusteString = parCurve.second();
		List<Double> curvaAjuste = pointsSorted.stream().map(p->ajusteFuncion.apply(p.getX())).toList();
		

		Plot plt = Plot.create();
		
		plt.plot()
		    .add(lx,curvaAjuste)
		    .label("ajuste")
		    .linestyle("-");
	    
		plt.plot()
			.add(lx,datos)
			.label("datos")
			.linestyle(":")
			.linewidth(3.5);
	
		plt.title(title + ":    " + ajusteString);
		plt.legend();	
		plt.xlabel("tamano");
		plt.ylabel("tiempo");
		try {
			plt.show();
			System.out.println(title);
			plt.savefig("figuras/" + title + ".png");
		} catch (IOException e) {
			e.printStackTrace();
		} catch (PythonExecutionException e) {
			e.printStackTrace();
		}
				
	}
	
	
	
	
	public static void showCombined(String header, List<String> dataFiles, 
			List<String> labels) {
		
		Plot plt = Plot.create();
		plt.title(header);
		for(int i=0; i<dataFiles.size(); i++) {
			String dataFile = dataFiles.get(i);
			String label = labels.get(i);
			
			List<WeightedObservedPoint> points = DataCurveFitting.points(dataFile);
			List<WeightedObservedPoint> pointsSorted = new ArrayList<>(points);
			pointsSorted.sort(Comparator.comparing(x->x.getX()));
			
			List<Double> lx = pointsSorted.stream().map(p->p.getX()).toList();
			List<Double> datos = pointsSorted.stream().map(p->p.getY()).toList();
			plt.plot()
			.add(lx,datos)
			.label(label)
			.linestyle("-");
		}	
		
		plt.legend();
		plt.xlabel("tamano");
		plt.ylabel("tiempo");
		try {
			plt.show();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (PythonExecutionException e) {
			e.printStackTrace();
		}
	}

}



