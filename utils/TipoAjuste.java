package utils;

public enum TipoAjuste {
	LINEAL, // a + n*b
	POLYNOMIALLOG, // a*n^b*(ln n)^c + d
	POWER, // a*n^b + c
	POWERANB, // a*n^b
	LOG2, // a*(ln n) + b
	LOG2_0, // a*(ln n)
	NLOGN, // a*n*(ln n) + b
	NLOGN_0, // a*n*(ln n)
	EXP, // a*b^(c*n) + d
	EXP2, // a*b^n + c
	EXP2_0	 // a*b^n

}
