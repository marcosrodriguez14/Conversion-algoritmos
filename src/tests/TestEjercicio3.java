package tests;


import java.util.List;
import ejercicios.Ejercicio3;
import us.lsi.common.Files2;
import us.lsi.common.Pair;
import us.lsi.common.Preconditions;
import us.lsi.tiposrecursivos.BinaryTree;
import us.lsi.tiposrecursivos.Tree;


public class TestEjercicio3 {
	public static void main(String[] args) {
		testsEjercicio3();

	}

	public static void testsEjercicio3() {

		String file = "ficheros/Ejercicio3DatosEntradaBinario.txt";
		String file2 = "ficheros/Ejercicio3DatosEntradaNario.txt";
		

		List<Pair<BinaryTree<Character>, Character>> inputs = Files2.streamFromFile(file)
				.map(linea -> {
			String aux[] = linea.split("#");
			Preconditions.checkArgument(aux.length == 2);
			return Pair.of(BinaryTree.parse(aux[0], s -> s.charAt(0)), aux[1].charAt(0));
			
		}).toList();
		
		List<Pair<Tree<Character>, Character>> inputs2 = Files2.streamFromFile(file2)
				.map(linea -> {
			String aux[] = linea.split("#");
			Preconditions.checkArgument(aux.length == 2);
			return Pair.of(Tree.parse(aux[0], s -> s.charAt(0)), aux[1].charAt(0));
			
		}).toList();
		
		
		

		System.out.println("\n -----------------------------------------------------------------------------------");
		System.out.println("Ejercicio 3 Arbol Binario");
		System.out.println("----------------------------------------------------------------------------------- \n");

		inputs.stream().forEach(par -> {
			BinaryTree<Character> tree = par.first();
			Character chars = par.second();

		//	System.out.println("Arbol: " + tree + "  caracter: " + chars );
			System.out.println("Arbol: " + tree + "Caracter " + chars + "\t["
					+ Ejercicio3.arbolBinario(tree, chars) + "] \n");
		});
		
		System.out.println("\n -----------------------------------------------------------------------------------");
		System.out.println("Ejercicio 3 Arbol Nario");
		System.out.println("----------------------------------------------------------------------------------- \n");	
		
		inputs2.stream().forEach(par -> {
			Tree<Character> tree = par.first();
			Character chars = par.second();

		//	System.out.println("Arbol: " + tree + "  caracter: " + chars );
			System.out.println("Arbol: " + tree + "Caracter " + chars + "\t["
					+ Ejercicio3.arbolNario(tree, chars) + "] \n");
		});

	}
	
}
