package ejercicios;

import java.util.List;

import us.lsi.tiposrecursivos.BinaryTree;
import us.lsi.tiposrecursivos.BinaryTree.BEmpty;
import us.lsi.tiposrecursivos.BinaryTree.BLeaf;
import us.lsi.tiposrecursivos.BinaryTree.BTree;
import us.lsi.tiposrecursivos.Tree;
import us.lsi.tiposrecursivos.Tree.TEmpty;
import us.lsi.tiposrecursivos.Tree.TLeaf;
import us.lsi.tiposrecursivos.Tree.TNary;

public class Ejercicio4 {

	/*
	 * // * PI2 - Ejercicio 4. Dado un árbol binario de cadena de caracteres, diseñe
	 * un algoritmo que devuelva cierto si se cumple que, para todo nodo, el número
	 * total de vocales contenidas en el subárbol izquierdo es igual al del subárbol
	 * derecho. Proporcione una solución también para árboles n-arios
	 */

	// ------------------------ EJERCICIO 4 ARBOL BINARIO-------------------------------------------------

	public static Boolean Ejercicio4Binario(BinaryTree<String> tree) {
		return recursivo(tree, true);
	}

	private static Boolean recursivo(BinaryTree<String> tree, Boolean b) {

		return switch (tree) {
		case BEmpty<String> t -> false;  
		case BLeaf<String> t ->b;  //Cuando lleguemos a una hoja si el resultado del padre ha dado false también dará false.
		case BTree<String> t -> {
			if (b) { //si el boolean es positivo comparamos vocales de los hijos y hacemos llamadas recursivas
				Boolean res = null;
				if (!t.right().isEmpty() && !t.left().isEmpty()) { //si los árboles no están vacios
					res = comparaVocalesBinario(t.left(),t.right()); //comparamos si las vocales de izq y der son iguales
					res = recursivo(t.left(), res); //llamada recursiva izq
					res = recursivo(t.right(), res);//llamada recursiva arbol der
						}else {
							res= false; // si alguno de los dos árboles está vacio devolvemos false
						}
				yield res;
			} else {
				yield b;  //false es lo mismo
			}
		}
		};
	}

	// ------------------------ EJERCICIO 4 ARBOL NARIO-------------------------------------------------

	public static Boolean Ejercicio4Nario(Tree<String> tree) {
		return recursivo(tree, true);
	}

	private static Boolean recursivo(Tree<String> tree, Boolean b) {
//igual que el anterior pero en vez de comparar el árbol de izq con der se compara uno con todos
		return switch (tree) {
		case TEmpty<String> t -> false;
		case TLeaf<String> t -> b;

		case TNary<String> t -> {
			if (b) {
				Boolean res = null;
				if (!t.elements().contains(Tree.empty())) {
					res = comparaVocalesNario(t.elements()); //comparamos las vocales de todos los hijos
					for (Tree<String> hijo : t.elements()) { 
						if (res) {//desde el primer hijo hasta el último realizamos llamadas recursivas si las vocales son iguales
							res = recursivo(hijo, res);
						} else {
							res = false;
						}
					}
				}
				yield res;
			} else {
				yield b; //si ponemos false es lo mismo
			}
		}
		};
	}

//---------------------------------------------------------------------------------------------------------
	
	private static Integer cuentaVocales(String s) {
		List<Character> vocales = List.of('a', 'e', 'i', 'o', 'u');
		Integer res = 0;
		for (int i = 0; i < s.length(); i++) {
			Character c = s.charAt(i);
			if (vocales.contains(c)) {
				res += 1;
			}
		}
		return res;
	}
	private static Boolean comparaVocalesBinario(BinaryTree<String> t_izq,BinaryTree<String> t_der) {
		
	Integer vocalesIzq= cuentaVocales(t_izq.toString()); //contamos las vocales del árbol izquierdo
	Integer vocalesDer=cuentaVocales(t_der.toString());	 //contamos las vocales del árbol derecho
	
	return vocalesIzq ==vocalesDer;		//si son iguales devolvemos true

	}

	private static Boolean comparaVocalesNario(List<Tree<String>> arboles) {
		Boolean res = true;
		Integer arbol0vocales = cuentaVocales(arboles.get(0).toString()); //cogemos las vocales del primer árbol
		for (int i = 0; i < arboles.size() && res; i++) { //las comparamos con la de todos los árboles
			String arbol = arboles.get(i).toString();
			Integer vocales = cuentaVocales(arbol);
			res = arbol0vocales == vocales; //si todas salen true devolvemos true si sale alguna false devolvemos false
		}
		return res;

	}

}
