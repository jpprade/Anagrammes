package com.jpprade.anagrammes.dico;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ApplicationState {
	
	public static Lettre root;
	
	public static List<String> resultat;
	
	public static String searched ;
	
	private static ExecutorService es = Executors.newFixedThreadPool(2);
	
	private static int minLetter;
	private static int maxLetter;

	public static String getSearched() {
		return searched;
	}

	public static void setSearched(String searched) {
		ApplicationState.searched = searched;
	}

	public static Lettre getRoot() {
		return root;
	}

	public static List<String> getResultat() {
		return resultat;
	}

	public static void setRoot(Lettre root) {
		ApplicationState.root = root;
	}

	public static void setResultat(List<String> resultat) {
		ApplicationState.resultat = resultat;
	}
	
	public static void run(Runnable r){
		es.execute(r);
	}

	public static void setMinLetter(int minLetter) {
		ApplicationState.minLetter = minLetter;
	}

	public static int getMinLetter() {
		return minLetter;
	}

	public static void setMaxLetter(int maxLetter) {
		ApplicationState.maxLetter = maxLetter;
	}

	public static int getMaxLetter() {
		return maxLetter;
	}

}
