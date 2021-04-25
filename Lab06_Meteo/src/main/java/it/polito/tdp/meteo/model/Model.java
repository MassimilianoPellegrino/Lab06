package it.polito.tdp.meteo.model;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

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

	
	public Set<Citta> getSetCitta(int mese) {
		List<Rilevamento> rilevamenti = this.dao.getAllRilevamenti();
		Set<Citta> citta = new LinkedHashSet<>();
		for(Rilevamento r: rilevamenti) {
			citta.add(new Citta(r.getLocalita()));
		}
		for(Citta c: citta) {
			c.setRilevamenti(this.dao.getAllRilevamentiLocalitaMese(mese, c.getNome()));
		}
		return citta;
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
		
		itera(0, new ArrayList<Rilevamento>(), COST, this.getSetCitta(mese));
		
		costoMigliore=3000;
		
		return soluzioneMigliore;
	}
	
	public boolean verificaCitta(List<Rilevamento> rilevamenti, Set<Citta> citta) {
		
		for(Citta c: citta) {
			for(Rilevamento r: rilevamenti) {
				if(r.getLocalita().equals(c.getNome())) {
					c.increaseCounter();
				}
			}
			if(c.getCounter()==0 || c.getCounter()>NUMERO_GIORNI_CITTA_MAX) {
				c.setCounter(0);
				return false;
			}
			c.setCounter(0);
		}
		
		int consec=1;
		
		for(int i=0; i<rilevamenti.size(); i++) {
			if(i>0) {
				if(rilevamenti.get(i).getLocalita().equals(rilevamenti.get(i-1).getLocalita()))
					consec++;
				else if(consec>=NUMERO_GIORNI_CITTA_CONSECUTIVI_MIN)
					consec=1;
				else
					return false;
			}
		}
		
		return true;
	}
	
	public void itera(int livello, List<Rilevamento> parziale, int costo, Set<Citta> setCitta) {
		if(livello==NUMERO_GIORNI_TOTALI) {
			if(costo<costoMigliore && verificaCitta(parziale, setCitta)) {
				soluzioneMigliore = new ArrayList<>(parziale);
				costoMigliore=costo;
			}
			return;
		}
		if(costo>costoMigliore) {
			return;
		}
		
		for(Citta c: setCitta) {
			boolean spostamento = false;
			Rilevamento ril = c.getRilevamenti().get(livello);
			int um = ril.getUmidita();
			costo+=um;
			if(parziale.size()>0 && !parziale.get(livello-1).getLocalita().equals(c.getNome())) {
				costo+=COST;
				spostamento=true;
			}
			parziale.add(ril);
			itera(livello+1, parziale, costo, setCitta);
			parziale.remove(ril);
			if(spostamento)
				costo-=COST;
			costo-=um;
			spostamento = false;
		}

	}
	

}