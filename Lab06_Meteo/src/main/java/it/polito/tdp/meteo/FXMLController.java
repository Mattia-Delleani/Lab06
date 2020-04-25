/**
 * Sample Skeleton for 'Scene.fxml' Controller Class
 */

package it.polito.tdp.meteo;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import it.polito.tdp.meteo.model.Model;
import it.polito.tdp.meteo.model.Rilevamento;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;

public class FXMLController {
	
	private Model model;

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="boxMese"
    private ChoiceBox<Integer> boxMese; // Value injected by FXMLLoader

    @FXML // fx:id="btnUmidita"
    private Button btnUmidita; // Value injected by FXMLLoader

    @FXML // fx:id="btnCalcola"
    private Button btnCalcola; // Value injected by FXMLLoader

    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader

    @FXML
    void doCalcolaSequenza(ActionEvent event) {
    	txtResult.clear();
    	
    	if(boxMese.getValue()==null) {
    		txtResult.setText("Selezionare un mese!");
    	}
    	
    	int mese = boxMese.getValue();
    	txtResult.appendText("La sequenza migliore per il mese "+ mese + " e':\n");
    	for(Rilevamento rTemp: this.model.trovaSequenza(mese)) {
    		txtResult.appendText(String.format("%-11s %-10s %2d", rTemp.getData(), rTemp.getLocalita(), rTemp.getUmidita())+"%\n");
    		
    	}
    	txtResult.appendText("\nCon un costo di: " + this.model.getCosto()+" euro");
    	
    }

    @FXML
    void doCalcolaUmidita(ActionEvent event) {
    	txtResult.clear();
    	
    	if(boxMese.getValue()==null) {
    		txtResult.setText("Selezionare un mese!");
    	}
    	
    	int mese = boxMese.getValue();
    	
		txtResult.appendText(this.model.getUmiditaMedia(mese));

    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert boxMese != null : "fx:id=\"boxMese\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnUmidita != null : "fx:id=\"btnUmidita\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnCalcola != null : "fx:id=\"btnCalcola\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Scene.fxml'.";

    }
    
    public void setModel(Model model) {
    	
    	List<Integer> mesi = new ArrayList<>();
    	
    	for(int i=0 ; i<12;i++)
    		mesi.add(i+1);
    	
    	boxMese.getItems().addAll(mesi);
    	this.model = model;
    }
}

