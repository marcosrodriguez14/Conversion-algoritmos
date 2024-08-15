package ejercicios;

import java.util.List;

import us.lsi.common.List2;
import us.lsi.tiposrecursivos.BinaryTree;
import us.lsi.tiposrecursivos.BinaryTree.BEmpty;
import us.lsi.tiposrecursivos.BinaryTree.BLeaf;
import us.lsi.tiposrecursivos.BinaryTree.BTree;
import us.lsi.tiposrecursivos.Tree;
import us.lsi.tiposrecursivos.Tree.TEmpty;
import us.lsi.tiposrecursivos.Tree.TLeaf;
import us.lsi.tiposrecursivos.Tree.TNary;

public class Ejercicio3 {
	
	/*
	 * * 3. Dados un árbol binario de caracteres y un carácter, diseñe un algoritmo
	 * que devuelva una lista con todas las cadenas que se forman desde la raíz a
	 * una hoja no vacía, excluyendo aquellas cadenas que contengan dicho carácter.
	 * Proporcione una solución también para árboles n-arios.
	 */
	
	//------------------------ EJERCICIO 3 ARBOL BINARIO ------------------------------------------------- 
	
	
	public static List<String> arbolBinario(BinaryTree<Character> tree,  Character c) {
		List<String>list = List2.empty();
		String s = "";
		return recursivo(tree, c, s,list);
	}

	public static List<String> recursivo(BinaryTree<Character> tree, Character c, String s,List<String>ls) {
		
		return switch (tree) {	
		case BEmpty<Character> t -> ls; 	//Si hay un nodo vacio devuelvo la lista												//otra forma
		case BLeaf<Character> t -> {  		//Si el carácter no es igual al label lo sumo a la cadena y lo añado a la lista			//c==t.label() ? ls: añadeEtiqueta(t.label(),s,ls) ;
			if(c != t.label()) {
				s = s + t.label();
				ls.add(s);
			}
			yield ls;
		}
		case BTree<Character> t -> { //Si el carácter no es igual al label subo el label y hago llamada recursiva al árbol izq y der
			if(c!=t.label()) {
			s = s + t.label();
			recursivo(t.left(),c,s,ls);
			recursivo(t.right(),c,s,ls);}
			yield ls;}
		};
	}
	
	/*private static List<String>añadeEtiqueta(Character c,String s, List<String> ls) {
		
		String res = s + c;
		ls.add(res);
		return ls;
	}*/
	//------------------------ EJERCICIO 3 ARBOL NARIO ------------------------------------------------- 	
	
	public static List<String> arbolNario(Tree<Character> tree,  Character c) {
		List<String>list = List2.empty();
		String s = "";
		return recursivo(tree, c, s,list);
	}

	public static List<String> recursivo(Tree<Character> tree, Character c, String s,List<String>ls) {
		
		return switch (tree) {
		case TEmpty<Character> t -> ls; 			//igual al anterior solo que en este caso en la 
		case TLeaf<Character> t -> {  				//llamada recursiva llamo a todos sus hijos recursivamente
			if(c != t.label()) {
				s = s + t.label();
				ls.add(s);
			}
			yield ls;
		}
		case TNary<Character> t -> {
			if(c!=t.label()) {
			s = s + t.label();
			for(Tree<Character>hijo:t.elements()) {
				if(c!=t.label()) {
					recursivo(hijo, c, s, ls);
				}
			}}
			yield ls;}
		};
	}
}
