package it.polito.tdp.artsmia;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import it.polito.tdp.artsmia.model.Adiacenza;
import it.polito.tdp.artsmia.model.Artist;
import it.polito.tdp.artsmia.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class ArtsmiaController {
	
	private Model model ;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button btnCreaGrafo;

    @FXML
    private Button btnArtistiConnessi;

    @FXML
    private Button btnCalcolaPercorso;

    @FXML
    private ComboBox<String> boxRuolo;

    @FXML
    private TextField txtArtista;

    @FXML
    private TextArea txtResult;

    @FXML
    void doArtistiConnessi(ActionEvent event) {
    	txtResult.clear();
    	this.txtArtista.clear();
    	txtResult.appendText("Calcola artisti connessi\n");
    	List<Adiacenza> result = this.model.getAdiacenze();
    	if(result.size()==0) {
    		this.txtResult.appendText("non ci sono coppie di artisti");
    	}
    	else {
    		for(Adiacenza a : result)
    			this.txtResult.appendText(a.toString());
    	}
    	
    	
    }

    @FXML
    void doCalcolaPercorso(ActionEvent event) {
    	txtResult.clear();
    	txtResult.appendText("Calcola percorso");
    	
    	try {
    	
    		Integer id = Integer.parseInt(this.txtArtista.getText());
    		if(!this.model.contieneId(id)) {
    			this.txtResult.setText("codice id non corrisponde a nessun vertice del grafo");
    			return;
    		}
    		
    		List<Artist> percorso = this.model.calcolaPercorso(id);
    		if(percorso.size()==0) {
    			this.txtResult.setText("nodo isolato");
    			return;
    		}
    		
    		this.txtResult.setText("percorso più lungo: "+percorso.size() +"\n");
    		
    		for(Artist a : percorso)
    			this.txtResult.appendText(a.toString());
    	}catch(NumberFormatException e) {
    		this.txtResult.setText("inserire un codice numerico");
    	}
    	
   
    }

    @FXML
    void doCreaGrafo(ActionEvent event) {
    	txtResult.clear();
    	this.txtArtista.clear();
    	txtResult.appendText("Crea grafo\n");
    	
    	String ruolo = this.boxRuolo.getValue();
    	
    	if(ruolo==null) {
    		this.txtResult.setText("selezionare un ruolo");
    		return;
    	}
    	
    	this.model.creaGrafo(ruolo);
    	
    	this.txtResult.appendText("#vertici: " +this.model.getNVertici()+"\n");
    	
    	this.txtResult.appendText("#archi: " +this.model.getNArchi()+"\n");
    	
    	this.btnArtistiConnessi.setDisable(false);
    	this.btnCalcolaPercorso.setDisable(false);
    }

    public void setModel(Model model) {
    	this.model = model;
    	this.boxRuolo.getItems().addAll(this.model.getRuoli());
    }

    
    @FXML
    void initialize() {
        assert btnCreaGrafo != null : "fx:id=\"btnCreaGrafo\" was not injected: check your FXML file 'Artsmia.fxml'.";
        assert btnArtistiConnessi != null : "fx:id=\"btnArtistiConnessi\" was not injected: check your FXML file 'Artsmia.fxml'.";
        assert btnCalcolaPercorso != null : "fx:id=\"btnCalcolaPercorso\" was not injected: check your FXML file 'Artsmia.fxml'.";
        assert boxRuolo != null : "fx:id=\"boxRuolo\" was not injected: check your FXML file 'Artsmia.fxml'.";
        assert txtArtista != null : "fx:id=\"txtArtista\" was not injected: check your FXML file 'Artsmia.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Artsmia.fxml'.";

    }
}
