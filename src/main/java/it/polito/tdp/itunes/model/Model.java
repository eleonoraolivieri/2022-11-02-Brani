package it.polito.tdp.itunes.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.alg.connectivity.ConnectivityInspector;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;


import it.polito.tdp.itunes.db.ItunesDAO;

public class Model {
	
	private ItunesDAO dao;
	private Graph<Track, DefaultEdge> grafo;
	private Map<Integer, Track> idMap;
	private List<String> generi ;
	private List<Track> track;
	private List<Track> listaMigliore;
	
	public Model() {
		dao = new ItunesDAO();
		idMap = new HashMap<Integer,Track>();
		dao.getAllTracks(idMap);
	}
	

	public List<String> getGeneri() {
		if(this.generi==null) {
			ItunesDAO dao = new ItunesDAO() ;
			this.generi = dao.getGeneri();
		}
		return this.generi;
	
	
	}
	
	public void creaGrafo(String genere, int min, int max) {
		
		this.grafo = new SimpleGraph<>(DefaultEdge.class);
		
		this.track = dao.getVertici(genere,min,max,idMap);
		Graphs.addAllVertices(grafo, track);
		
		for(Adiacenza a : dao.getAdiacenze(genere,min,max,idMap)) {
			if(this.grafo.containsVertex(a.getV1()) && 
					this.grafo.containsVertex(a.getV2())) {
				DefaultEdge e = this.grafo.getEdge(a.getV1(), a.getV2());
				if(e == null) 
					Graphs.addEdgeWithVertices(grafo, a.getV1(), a.getV2());
			}
		}
		
		System.out.println("VERTICI: " + this.grafo.vertexSet().size());
		System.out.println("ARCHI: " + this.grafo.edgeSet().size());
	}
	
	public List<Set<Track>> componentiConnesse() {
		ConnectivityInspector<Track, DefaultEdge> inspector = new ConnectivityInspector<Track, DefaultEdge>(this.grafo);
		return inspector.connectedSets();
	}
	
	public String componenti() {
		ConnectivityInspector<Track, DefaultEdge> inspector = new ConnectivityInspector<Track, DefaultEdge>(this.grafo);
		List<Set<Track>> cc=  inspector.connectedSets();
		int tot = 0;
		String s = "";
		
		for(Set<Track> c: cc) {
			for(Track c1: c) {
				tot = this.grafo.edgesOf(c1).size();
			
			}
			s = s+ "\n Componente connessa con " + c.size() + " inseriti in: " + tot +" vertici";
		}
		return s;
	}
	
	public List<Track> getVertici(){
		return new ArrayList<Track>(this.grafo.vertexSet());
	}
	
	public int getNVertici(){
		return this.grafo.vertexSet().size();
	}
	
	
	/**
	 * Metodo che restituisce il numero di archi del grafo
	 * @return
	 */
	public int getNArchi(){
		return this.grafo.edgeSet().size();
	}
	
	public List<Track> cercaLista( int dTot){
		//recupero la componenete connessa di c
		Set<Track> componenteConnessa;
		ConnectivityInspector<Track, DefaultEdge> ci = 
				new ConnectivityInspector<>(this.grafo);
		
		for(Track t: this.grafo.vertexSet() ) {
			componenteConnessa = ci.connectedSetOf(t);
			List<Track> trackValidi = new ArrayList<Track>();
			trackValidi.add(t);
			componenteConnessa.remove(t);
			trackValidi.addAll(componenteConnessa);
			List<Track> parziale = new ArrayList<>();
			listaMigliore = new ArrayList<>();
			parziale.add(t);
			cerca(parziale,trackValidi,dTot, 1);
			
			
			
		}
		
		
		
		return listaMigliore;
	}
	
	private void cerca(List<Track> parziale, List<Track> trackValidi, int dTot, int L) {
		
		if(sommaMemoria(parziale) > dTot)
			return;
		
		//parziale è valida
		if(parziale.size() > listaMigliore.size()) {
			listaMigliore = new ArrayList<>(parziale);
		}
		
		if(L == trackValidi.size())
			return;
		
		parziale.add(trackValidi.get(L));
		cerca(parziale, trackValidi,dTot, L +1);
		parziale.remove(trackValidi.get(L));
		cerca(parziale,trackValidi,dTot, L+1);
	}
	
	private double sommaMemoria (List<Track> track) {
		double somma = 0;
		for(Track t: track) {
			somma += t.getMilliseconds()/1000/60;
		}
		return somma;
	}
	
	public boolean grafoCreato() {
		if(this.grafo == null)
			return false;
		else 
			return true;
	}
	
}