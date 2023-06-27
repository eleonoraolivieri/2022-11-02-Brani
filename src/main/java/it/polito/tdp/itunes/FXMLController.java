/**
 * Sample Skeleton for 'Scene.fxml' Controller Class
 */

package it.polito.tdp.itunes;

import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;

import it.polito.tdp.itunes.model.Album;
import it.polito.tdp.itunes.model.Model;
import it.polito.tdp.itunes.model.Track;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class FXMLController {

	private Model model;
	
    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="btnCreaGrafo"
    private Button btnCreaGrafo; // Value injected by FXMLLoader

    @FXML // fx:id="btnPlaylist"
    private Button btnPlaylist; // Value injected by FXMLLoader

    @FXML // fx:id="cmbGenere"
    private ComboBox<String> cmbGenere; // Value injected by FXMLLoader

    @FXML // fx:id="txtDTOT"
    private TextField txtDTOT; // Value injected by FXMLLoader

    @FXML // fx:id="txtMax"
    private TextField txtMax; // Value injected by FXMLLoader

    @FXML // fx:id="txtMin"
    private TextField txtMin; // Value injected by FXMLLoader

    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader

    @FXML
    void doCalcolaPlaylist(ActionEvent event) {
    	
    	int dTot;
    	try {
    		dTot = Integer.parseInt(txtDTOT.getText());
    	}catch (NumberFormatException e) {
    		txtResult.appendText("Inseririci un valore numerico per la soglia");
    		return ;
    	}
    	
    	if(!this.model.grafoCreato()) {
    		txtResult.appendText("Crea prima il grafo!");
    		return ;
    	}
    	
    	txtResult.appendText("\n  PLAYLIST MIGLIORE: \n");
    	
    		txtResult.appendText(this.model.cercaLista(dTot) + "\n");
    	

    }

    @FXML
    void doCreaGrafo(ActionEvent event) {
    	
    	int min = 0;
    	int max = 0;
    	String genere = this.cmbGenere.getValue();
    	if(genere==null) {
    		this.txtResult.setText("Please select a genre");
    		return;
    	}
    	try {
    		min = Integer.parseInt( this.txtMin.getText() );
    		max = Integer.parseInt( this.txtMax.getText() );
    	}catch(NumberFormatException e) {
    		txtResult.setText("Min e Max devono essere dei numeri");
    		return;
    	}
    	if (min<0) {
    		txtResult.setText("Il min deve essere un numero positivo.");
    		return;
    	}
    	if (max<0) {
    		txtResult.setText("Il max deve essere un numero positivo.");
    		return;
    	}
    	
    	this.model.creaGrafo(genere, min, max);
    	
    	List<Track> vertici = this.model.getVertici();
    	
    	this.txtResult.setText("Grafo creato, con " + vertici.size() + " vertici e " + this.model.getNArchi()+ " archi\n");
    	
    	List<Set<Track>> componentiConnesse = this.model.componentiConnesse();
    	int nComponenti = componentiConnesse.size();
    	
    	this.txtResult.appendText(this.model.componenti());
    	

    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert btnCreaGrafo != null : "fx:id=\"btnCreaGrafo\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnPlaylist != null : "fx:id=\"btnPlaylist\" was not injected: check your FXML file 'Scene.fxml'.";
        assert cmbGenere != null : "fx:id=\"cmbGenere\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtDTOT != null : "fx:id=\"txtDTOT\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtMax != null : "fx:id=\"txtMax\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtMin != null : "fx:id=\"txtMin\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Scene.fxml'.";

    }
    
    public void setModel(Model model) {
    	this.model = model;
    	List<String> genere = this.model.getGeneri();
    	cmbGenere.getItems().addAll(genere);
    }

}
