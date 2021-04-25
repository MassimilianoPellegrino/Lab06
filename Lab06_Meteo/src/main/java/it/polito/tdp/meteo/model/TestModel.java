package it.polito.tdp.meteo.model;

public class TestModel {

	public static void main(String[] args) {
		
		Model m = new Model();
		
		System.out.println(m.getUmiditaMedia(11));
		
		for(Rilevamento r: m.trovaSequenza(11)) {
			System.out.print(r.getLocalita()+"\t");
		}
		
		
		

	}

}
