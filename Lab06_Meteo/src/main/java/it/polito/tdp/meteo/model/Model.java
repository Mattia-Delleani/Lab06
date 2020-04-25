package it.polito.tdp.meteo.model;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import it.polito.tdp.meteo.DAO.MeteoDAO;

public class Model {
	
	private final static int COST = 100;
	private final static int NUMERO_GIORNI_CITTA_CONSECUTIVI_MIN = 3;
	private final static int NUMERO_GIORNI_CITTA_MAX = 6;
	private final static int NUMERO_GIORNI_TOTALI = 15;
	private int contaSoluzioniPossibili = 0;
	
	private String[] localita = {"Torino", "Genova", "Milano"};
	private MeteoDAO dao;
	private List<Citta> cittaDisponibili;
	private List<Rilevamento> bestSoluzione;
	private int costo;

	public Model() {
		bestSoluzione = null;
		dao = new MeteoDAO();
		cittaDisponibili = new ArrayList<>();
		for(String sTemp: localita) {
			cittaDisponibili.add(new Citta(sTemp));
			//setto un costo esagerato.
			
		}
	}

	// of course you can change the String output with what you think works best
	public String getUmiditaMedia(int mese) {
		
		String result = "La media di umidita' nelle citta nel mese "+ mese +" e':\n";
		for(String sTemp: localita) {
			
			result += sTemp + " " + dao.getAvgRilevamentiLocalitaMese(mese, sTemp)+ "%\n";		
		}
			
		return result;
	}
	
	// of course you can change the String output with what you think works best
	public List<Rilevamento> trovaSequenza(int mese) {
		costo=10000000;
		//metto i parametri del mese nelle citta disponibili
		for(Citta cTemp: cittaDisponibili) {
			cTemp.setRilevamenti(dao.getAllRilevamentiLocalitaMese(mese, cTemp.getNome()));
		}
		
		int livello = 0;
		List<Rilevamento> parziale = new ArrayList<>();
		List <Rilevamento> rilevamenti = dao.getAllRilevamentiMese(mese);
		int contatore = 0;
		cerca(parziale, livello, rilevamenti, contatore);
		System.out.println("NUMERO SOLUZIONI POSSIBILI: "+contaSoluzioniPossibili);
		return bestSoluzione;
	}
	
	
	//funzine ricorsiva;
	
	public void cerca(List<Rilevamento> parziale, int livello, List<Rilevamento> rilevamenti, int contatore) {
		
		if(livello == NUMERO_GIORNI_TOTALI) {
			contaSoluzioniPossibili++;
			//stampa tutte le soluzioni parziali
			//stampaSoluzioni(parziale);
			
			int costoSoluzione = calcolaCosto(parziale);
			
			if(costoSoluzione < costo) {
				//stampo le soluzioni con il costo
				//stampaSoluzioni(parziale);
				//System.out.println("\n  costo: "+costoSoluzione);
				
				
				costo = costoSoluzione;
				
				bestSoluzione = new ArrayList<>(parziale);
			}
			
			return;
		}
		if(livello<NUMERO_GIORNI_TOTALI){
			for(Citta c: cittaDisponibili) {
				
				//Se non sono state inserite citta oppure se la nuova citta è diversa dall'ultima visitata allora è avvenuto 
				//un trasferimento--> deve stare almeno 3gg nella città nuova
				if(livello==0 ||((parziale.get(parziale.size()-1).getLocalita().equals(c.getNome())==false))) {
					c.setTrasferito(true);
				}
				
				if(c.getCounter()<NUMERO_GIORNI_CITTA_MAX) {
					//System.out.println("Citta entrata: " + c.getNome()+"\n");
					List<Rilevamento> rilevamentiPerCitta = new ArrayList<>(c.getRilevamenti());
					//contatore per il backtracking
					int contBack =0;
					//se è stato trasferito devo aggiungere 3 citta, rispettando i vincoli.
					if(c.isTrasferito()) {
						for(int i =contatore; i<(NUMERO_GIORNI_CITTA_CONSECUTIVI_MIN+contatore) && c.getCounter()<NUMERO_GIORNI_CITTA_MAX && livello<NUMERO_GIORNI_TOTALI; i++) {
					
							parziale.add(rilevamentiPerCitta.get(i));
							c.increaseCounter();
							livello++;
							contBack++;
						}
					
						c.setTrasferito(false);
					}else {
						//se non è stato trsferito dopo il 3° giorno
						parziale.add(rilevamentiPerCitta.get(contatore));
						c.increaseCounter();
						livello++;
						contBack++;
						
					}
					contatore = livello;
				
					cerca(parziale, livello, rilevamenti, contatore);

					
					/*
					 * BACKTRACKING: parto dall'ultimo che ha indice = contatore e ne tolgo tanti quanti sono stati aggiunti (backcont)
					 */
					for(int i = contatore; i> contatore-contBack ; i--) {
						
						parziale.remove(parziale.size()-1);
						c.decreaseCounter();
						livello--;
					}
					contatore = livello;
					
				
				}
				
			}
						
		}
		
		
	}

	private int calcolaCosto(List<Rilevamento> parziale) {
		int costo = parziale.get(0).getUmidita();;
		
		for(int i=1; i<parziale.size(); i++) {
			if(parziale.get(i).getLocalita().equals(parziale.get(i-1).getLocalita())) {
				costo+= parziale.get(i).getUmidita();
			}
			else {
				costo = costo + COST + parziale.get(i).getUmidita();
			}
			
		}
		
		return costo;
	}

	public int getContaSoluzioniPossibili() {
		return contaSoluzioniPossibili;
	}
	
	public void  stampaSoluzioni(List<Rilevamento> parziale) {
		for(int i =0; i< parziale.size(); i++) {
			System.out.println( (i+1)+" -->" + parziale.get(i).getLocalita()+ " con " +parziale.get(i).getUmidita()+"\n");
			
		}
		System.out.println("____________________________________________________________________________________");
	}

	public int getCosto() {
		return costo;
	}

	public void setBestSoluzione(List<Rilevamento> bestSoluzione) {
		this.bestSoluzione = bestSoluzione;
	} 
	
	

}
