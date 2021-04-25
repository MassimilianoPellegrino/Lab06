package it.polito.tdp.meteo.model;

public class Mese {
	private String nome;
	private int numero;
	public Mese(String nome, int numero) {
		super();
		this.nome = nome;
		this.numero = numero;
	}
	public String getNome() {
		return nome;
	}
	public int getNumero() {
		return numero;
	}
	@Override
	public String toString() {
		return nome;
	}
	
	

}
