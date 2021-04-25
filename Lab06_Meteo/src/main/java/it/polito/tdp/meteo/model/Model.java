package it.polito.tdp.meteo.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import it.polito.tdp.meteo.DAO.MeteoDAO;

public class Model {
	
	private final static int COST = 100;
	private final static int NUMERO_GIORNI_CITTA_CONSECUTIVI_MIN = 3;
	private final static int NUMERO_GIORNI_CITTA_MAX = 6;
	private final static int NUMERO_GIORNI_TOTALI = 15;	
	
	MeteoDAO dao;
	int costoMigliore;
	List<Rilevamento> soluzioneMigliore;
	
	public Model() {
		dao = new MeteoDAO();
	}

	public List<Mese> listaMesi(){
		List<Mese> lista = new ArrayList<>();
		lista.add(new Mese("Gennaio", 1));
		lista.add(new Mese("Febbraio", 2));
		lista.add(new Mese("Marzo", 3));
		lista.add(new Mese("Aprile", 4));
		lista.add(new Mese("Maggio", 5));
		lista.add(new Mese("Giugno", 6));
		lista.add(new Mese("Luglio", 7));
		lista.add(new Mese("Agosto", 8));
		lista.add(new Mese("Settembre", 9));
		lista.add(new Mese("Ottobre", 10));
		lista.add(new Mese("Novembre", 11));
		lista.add(new Mese("Dicembre", 12));
		return lista;
		
	}
	
	// of course you can change the String output with what you think works best
	public Map<String, Integer> getUmiditaMedia(int mese) {
		return this.dao.getMediaUmidita(mese);
	}
	
	// of course you can change the String output with what you think works best
	public List<Rilevamento> trovaSequenza(int mese) {
		
		costoMigliore=3000;	

		
		itera(1, 0, new ArrayList<Rilevamento>(), COST, dao.getAllRilevamentiLocalitaMese(mese, "Torino"),
				dao.getAllRilevamentiLocalitaMese(mese, "Milano"), dao.getAllRilevamentiLocalitaMese(mese, "Genova"));
		
		costoMigliore=3000;	

		
		return soluzioneMigliore;
	}
	
	public boolean verificaCitta(List<Rilevamento> rilevamenti) {
		int t=0;
		int m=0;
		int g=0;
		int consec=1;
		
		for(int i=0; i<rilevamenti.size(); i++) {
			if(rilevamenti.get(i).getLocalita().equals("Torino"))
				t++;
			if(rilevamenti.get(i).getLocalita().equals("Milano"))
				m++;
			if(rilevamenti.get(i).getLocalita().equals("Genova"))
				g++;
			
			if(i>0) {
				if(rilevamenti.get(i).getLocalita().equals(rilevamenti.get(i-1).getLocalita()))
					consec++;
				else if(consec>=NUMERO_GIORNI_CITTA_CONSECUTIVI_MIN)
					consec=1;
				else
					return false;
			}
			
			
		}
		
		if(t>0 && m>0 && g>0 && t<=NUMERO_GIORNI_CITTA_MAX && m<=NUMERO_GIORNI_CITTA_MAX && g<=NUMERO_GIORNI_CITTA_MAX)
			return true;
		
		return false;
	}
	
	public void itera(int giorniConsec, int livello, List<Rilevamento> parziale, int costo, List<Rilevamento> listT, List<Rilevamento> listM, List<Rilevamento> listG ) {
		if(livello==NUMERO_GIORNI_TOTALI) {
			if(costo<costoMigliore && verificaCitta(parziale)) {
				soluzioneMigliore = new ArrayList<>(parziale);
				costoMigliore=costo;
			}
			return;
		}
		if(costo>costoMigliore) {
			return;
		}
		
		boolean spostamento = false;
		
		Rilevamento rilT = listT.get(livello);
		int umT = rilT.getUmidita();
		costo+=umT;
		if(parziale.size()>0 && !parziale.get(livello-1).getLocalita().equals("Torino")) {
			costo+=COST;
			spostamento=true;
		}
		parziale.add(rilT);
		itera(giorniConsec, livello+1, parziale, costo, listT, listM, listG);
		parziale.remove(rilT);
		if(spostamento)
			costo-=COST;
		costo-=umT;
		spostamento = false;
		
		Rilevamento rilM = listM.get(livello);
		int umM = rilM.getUmidita();
		costo+=umM;
		if(parziale.size()>0 && !parziale.get(livello-1).getLocalita().equals("Milano")) {
			costo+=COST;
			spostamento=true;
		}
		parziale.add(rilM);
		itera(giorniConsec, livello+1, parziale, costo, listT, listM, listG);
		parziale.remove(rilM);
		if(spostamento)
			costo-=COST;
		costo-=umM;
		spostamento = false;

		
		Rilevamento rilG = listG.get(livello);
		int umG = rilG.getUmidita();
		costo+=umG;
		if(parziale.size()>0 && !parziale.get(livello-1).getLocalita().equals("Genova")) {
			costo+=COST;
			spostamento=true;
		}
		parziale.add(rilG);
		itera(giorniConsec, livello+1, parziale, costo, listT, listM, listG);
		parziale.remove(rilG);
		if(spostamento)
			costo-=COST;
		costo-=umG;
		spostamento = false;

	}
	

}