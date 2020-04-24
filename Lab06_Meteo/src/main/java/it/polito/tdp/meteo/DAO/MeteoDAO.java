package it.polito.tdp.meteo.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import it.polito.tdp.meteo.model.Rilevamento;

public class MeteoDAO {
	
	public List<Rilevamento> getAllRilevamenti() {

		final String sql = "SELECT Localita, Data, Umidita FROM situazione ORDER BY data ASC";

		List<Rilevamento> rilevamenti = new ArrayList<Rilevamento>();

		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);

			ResultSet rs = st.executeQuery();

			while (rs.next()) {

				Rilevamento r = new Rilevamento(rs.getString("Localita"), rs.getDate("Data"), rs.getInt("Umidita"));
				rilevamenti.add(r);
			}

			conn.close();
			return rilevamenti;

		} catch (SQLException e) {

			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	public List<Rilevamento> getAllRilevamentiLocalitaMese(int mese, String localita) {
		
		final String sql ="SELECT Localita, DATA, Umidita FROM situazione " + 
				"WHERE MONTH(DATA)= ? AND localita = ? ORDER BY data ASC ";
		
		List<Rilevamento>rilevamenti = new ArrayList<>();
		
		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			
			st.setInt(1, mese);
			st.setString(2, localita);
			ResultSet rs = st.executeQuery();
			
			while(rs.next()) {
				
				rilevamenti.add(new Rilevamento(rs.getString("Localita"), rs.getDate("Data"), rs.getInt("Umidita")));
			}
			
			st.close();
			conn.close();
			return rilevamenti;
			
		}catch(SQLException sqle){
			
			sqle.printStackTrace();
			throw new RuntimeException(sqle);
		}

		
	}

	
public List<Rilevamento> getAllRilevamentiMese(int mese) {
		
		final String sql ="SELECT Localita, DATA, Umidita FROM situazione " + 
				"WHERE MONTH(DATA)= ?  ORDER BY data ASC ";
		
		List<Rilevamento>rilevamenti = new ArrayList<>();
		
		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			
			st.setInt(1, mese);
			ResultSet rs = st.executeQuery();
			
			while(rs.next()) {
				
				rilevamenti.add(new Rilevamento(rs.getString("Localita"), rs.getDate("Data"), rs.getInt("Umidita")));
			}
			
			st.close();
			conn.close();
			return rilevamenti;
			
		}catch(SQLException sqle){
			
			sqle.printStackTrace();
			throw new RuntimeException(sqle);
		}

		
	}
	public double getAvgRilevamentiLocalitaMese(int mese, String localita) {
		
		List<Rilevamento>rilevamenti = new ArrayList<>(this.getAllRilevamentiLocalitaMese(mese, localita));
		int somma = 0;
		for(Rilevamento tempR: rilevamenti ) {
			somma+= tempR.getUmidita();
		}
		double media = somma/rilevamenti.size();
		
		return media;
	}


}
