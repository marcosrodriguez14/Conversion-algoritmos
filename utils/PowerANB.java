package utils;

import java.util.List;

import org.apache.commons.math3.analysis.ParametricUnivariateFunction;
import org.apache.commons.math3.fitting.WeightedObservedPoint;

import us.lsi.curvefitting.PolinomialLog;

import org.apache.commons.math3.fitting.SimpleCurveFitter;

/**
 * @author migueltoro
 * 
 * Una curva de la forma a*n^b
 *
 */
public class PowerANB implements ParametricUnivariateFunction {
	
	private static PowerANB pl = null;
	
	public static PowerANB of() {
		if(pl == null) pl = new PowerANB();
		return pl;
	}
	
	public PowerANB() {
		super();
	}

	@Override
	public double[] gradient(double n, double... p) {
		Double a = p[0];
		Double b = p[1];
		Double a0 = Math.pow(n,b);
		Double b0 = a*Math.pow(n,b)*Math.log(n);
		double[] r = {a0,b0};
		return r;
	}

	@Override
	public double value(double n, double... p) {
		Double a = p[0];
		Double b = p[1];
		return a*Math.pow(n,b);
	}
	
	public double[] fit(List<WeightedObservedPoint> points, double[] start) {
		final SimpleCurveFitter fitter = SimpleCurveFitter.create(PowerANB.of(),start);
		return fitter.fit(points);
	}
	
	public void print(double n, double... p) {
		String r = String.format("Values: n = %.2f,a = %.2f,b = %.2f,",n, p[0],p[1]);
		System.out.println(r);
		System.out.println("Value = "+this.value(n, p));
		double[] g = this.gradient(n, p);
		System.out.println("Gradiente = "+String.format("%.2f,%.2f",g[0],g[1]));
	}

}
