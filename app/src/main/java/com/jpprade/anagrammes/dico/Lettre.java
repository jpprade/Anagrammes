package com.jpprade.anagrammes.dico;

public class Lettre {
	
	private char lettre;
	private Lettre suffixes;
	private Lettre suivants;
	private boolean endWord;
	private Lettre parent;
	
	public Lettre getParent() {
		return parent;
	}

	public void setParent(Lettre parent) {
		this.parent = parent;
	}
	
	public boolean isEndWord() {
		return endWord;
	}

	public Lettre(char lettre, Lettre suffixes, Lettre suivants, boolean endWord) {
		super();
		this.lettre = lettre;
		this.suffixes = suffixes;
		this.suivants = suivants;
		this.endWord = endWord;
	}

	public void setEndWord(boolean endWord) {
		this.endWord = endWord;
	}

	public Lettre(char lettre, Lettre suffixes, Lettre suivants) {
		super();
		this.lettre = lettre;
		this.suffixes = suffixes;
		this.suivants = suivants;
	}

	public char getLettre() {
		return lettre;
	}

	public Lettre getSuffixes() {
		return suffixes;
	}

	public Lettre getSuivants() {
		return suivants;
	}

	public void setLettre(char lettre) {
		this.lettre = lettre;
	}

	public void setSuffixes(Lettre suffixes) {
		this.suffixes = suffixes;
	}

	public void setSuivants(Lettre suivants) {
		this.suivants = suivants;
	}

}
