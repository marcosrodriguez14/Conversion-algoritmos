package ejercicios;

import java.math.BigInteger;

public class Ejercicio1 {
	
	//--------------------------------------FACTORIAL RECURSIVO DOUBLE------------------------------
	
	public static Double factorialRecursivoDouble(Double i) {
		Double r;
		if (i == 0) {
			r = 1.;
		} else {
			r = factorialRecursivoDouble(i - 1) * i;
		}
		return r;
	}
	
	
	//--------------------------------------FACTORIAL RECURSIVO BIG INTEGER ------------------------
	public static BigInteger factorialRecursivoBigInteger(Integer i) {
		BigInteger r = null;
		if (i == 0) {
			r = BigInteger.ONE;
		} else {
			r = factorialRecursivoBigInteger(i - 1).multiply(BigInteger.valueOf(i));
		}
		return r;
	}
	
	//--------------------------------------FACTORIAL ITERATIVO DOUBLE------------------------------
	
	public static Double factorialIterativoDouble(Double n) {
		Double a = 1.;
		while (n != 0) {
			a = a * n; // Muy importante el orden de las asignaciones
			n = n - 1;
		}
		return a;
	}
	
	//--------------------------------------FACTORIAL ITERATIVO BIG INTEGER -----------------------
	public static BigInteger factorialIterativoBigInteger(Integer n) {
		BigInteger a = BigInteger.ONE;
		while (n != 0) {
			a = a.multiply(BigInteger.valueOf(n)); // Muy importante el orden de las asignaciones
			n = n - 1;
		}
		return a;
	}
	
	
	
	
}
