package it.polito.tdp.artsmia.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.artsmia.db.ArtsmiaDAO;

public class Model {
	
	private ArtsmiaDAO dao;
	private Graph<Artist, DefaultWeightedEdge> grafo;
	private Map<Integer, Artist> idMap;
	private List<Adiacenza> adiacenze;
	private List<Artist> percorsoMigliore;
	
	public Model() {
		this.dao = new ArtsmiaDAO();
		this.idMap = new HashMap<>();
		this.dao.listArtists(this.idMap);
	}

	public List<String> getRuoli() {
		
		return this.dao.getRuoli();
	}

	public void creaGrafo(String ruolo) {
		
		this.grafo = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		this.adiacenze = new ArrayList<>();
		
		Graphs.addAllVertices(this.grafo, this.dao.getArtistiByRuolo(ruolo, this.idMap));
		
		adiacenze = this.dao.getAdiacenze(ruolo, idMap);
		for(Adiacenza a : adiacenze) {
			if(this.grafo.vertexSet().contains(a.getA1()) && this.grafo.vertexSet().contains(a.getA2())) {
				Graphs.addEdge(this.grafo, a.getA1(), a.getA2(), a.getPeso());
			}
		}
		
	}

	public int getNVertici() {
		
		return this.grafo.vertexSet().size();
	}
	
	public int getNArchi() {
		return this.grafo.edgeSet().size();
	}
	
	public List<Adiacenza> getAdiacenze() {
		Collections.sort(this.adiacenze);
		return this.adiacenze;
	}

	public boolean contieneId(Integer id) {
		Artist a = this.idMap.get(id);
		if(a!=null && this.grafo.vertexSet().contains(a))
			return true;
		return false;
	}

	public List<Artist> calcolaPercorso(Integer id) {
		
		this.percorsoMigliore = new ArrayList<>();
		
		Artist partenza = this.idMap.get(id);
		
		if(Graphs.neighborListOf(this.grafo, partenza).size()==0)
			return this.percorsoMigliore;
		
		List<Artist> parziale = new ArrayList<>();
		parziale.add(partenza);
		
		this.cerca(parziale, -1);
		
		return this.percorsoMigliore;
		
	}
	
	
	private void cerca(List<Artist> parziale, int peso) {
		
		
		// scorro i vicini dell'ultimo inserito e provo 1 a 1 ad aggiungere
		
		for (Artist vicino : Graphs.neighborListOf(this.grafo, parziale.get(parziale.size()-1))) {
			
			int pesoNuovo = (int) this.grafo.getEdgeWeight(this.grafo.getEdge(vicino, parziale.get(parziale.size()-1)));
			
			if(!parziale.contains(vicino) && peso == -1 ) {
				parziale.add(vicino);
				this.cerca(parziale, pesoNuovo);
				parziale.remove(parziale.size()-1);
			}
			else if(!parziale.contains(vicino) && peso == pesoNuovo ) { 
				parziale.add(vicino);
				this.cerca(parziale, peso);
				parziale.remove(parziale.size()-1);
			}
		
		}
		
		if(parziale.size()>this.percorsoMigliore.size())
			this.percorsoMigliore = new ArrayList<>(parziale);
	}
}
