package utils;

import java.util.List;

import org.apache.commons.math3.analysis.ParametricUnivariateFunction;
import org.apache.commons.math3.fitting.WeightedObservedPoint;
import org.apache.commons.math3.fitting.SimpleCurveFitter;

/**
 * @author migueltoro
 * 
 * Una curva de la forma a*(ln n)
 *
 */
public class Log2_0 implements ParametricUnivariateFunction {
	
	private static Log2_0 pl = null;
	
	public static Log2_0 of() {
		if(pl == null) pl = new Log2_0();
		return pl;
	}
	
	public Log2_0() {
		super();
	}

	@Override
	public double[] gradient(double n, double... p) {
		Double a = p[0];
		Double a0 = Math.log(n);
		double[] r = {a0};
		return r;
	}

	@Override
	public double value(double n, double... p) {
		Double a = p[0];
		return a*Math.log(n);
	}
	
	public double[] fit(List<WeightedObservedPoint> points, double[] start) {
		final SimpleCurveFitter fitter = SimpleCurveFitter.create(Log2_0.of(),start);
		return fitter.fit(points);
	}
	
	public void print(double n, double... p) {
		String r = String.format("Values: n = %.2f,a = %.2f,b = %.2f,c = %.2f,d = %.2f",n, p[0],p[1],p[2],p[3]);
		System.out.println(r);
		System.out.println("Value = "+this.value(n, p));
		double[] g = this.gradient(n, p);
		System.out.println("Gradiente = "+String.format("%.2f,%.2f,%.2f,%.2f",g[0],g[1],g[2],g[3]));
	}

}
