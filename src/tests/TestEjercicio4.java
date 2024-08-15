package tests;

import java.util.List;

import ejercicios.Ejercicio4;
import us.lsi.common.Files2;
import us.lsi.tiposrecursivos.BinaryTree;
import us.lsi.tiposrecursivos.Tree;


public class TestEjercicio4 {

	
	public static void main(String[] args) {
		
		testsEjercicio4Binario();
		testsEjercicio4Nario();
	}
	
	
	
public static void testsEjercicio4Binario() {
		
		String file = "ficheros/Ejercicio4DatosEntradaBinario.txt";
		
		List<BinaryTree<String>> inputs = Files2.streamFromFile(file).map(linea -> 
		BinaryTree.parse(linea, s -> s)).toList();
		
		System.out.println("\n -----------------------------------------------------------------------------------");
		System.out.println("EJERCICIO 4");
		System.out.println("----------------------------------------------------------------------------------- \n");

		inputs.stream().forEach(x -> System.out.println(x + ":" + Ejercicio4.Ejercicio4Binario(x)));
	
	
	}

public static void testsEjercicio4Nario() {
	
	String file2 = "ficheros/Ejercicio4DatosEntradaNario.txt";
	
	List<Tree<String>> inputs2 = Files2.streamFromFile(file2).map(linea -> 
	Tree.parse(linea, s -> s)).toList();
	
	System.out.println("\n -----------------------------------------------------------------------------------");
	System.out.println("EJERCICIO 4");
	System.out.println("----------------------------------------------------------------------------------- \n");

	inputs2.stream().forEach(x -> System.out.println(x + ":" + Ejercicio4.Ejercicio4Nario(x)));


}
}
