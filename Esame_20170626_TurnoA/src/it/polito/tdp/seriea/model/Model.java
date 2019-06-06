package it.polito.tdp.seriea.model;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.seriea.db.SerieADAO;

public class Model {
	
	private Map<String, Team> idMap;
	private SimpleWeightedGraph<Team, DefaultWeightedEdge> grafo;
	
	public Model() {
		idMap = new HashMap<>();
		grafo = new SimpleWeightedGraph<Team, DefaultWeightedEdge>(DefaultWeightedEdge.class);
	}

	public String creaGrafo() {
		String risultato="";
		SerieADAO dao = new SerieADAO();
		dao.listTeams(idMap);
		List<PartiteStagione> partite = dao.getPartiteStagione(idMap);
		for(PartiteStagione p: partite) {
			if(!grafo.containsVertex(p.getT1())) {
				grafo.addVertex(p.getT1());
			}
			if(!grafo.containsVertex(p.getT2())) {
				grafo.addVertex(p.getT2());
			}
			DefaultWeightedEdge edge = grafo.getEdge(p.getT1(), p.getT2());
			if(edge==null) {
				Graphs.addEdgeWithVertices(grafo, p.getT1(), p.getT2(), p.getPeso());
			}else {
				grafo.setEdgeWeight(edge, p.getPeso());
			}
		}
		risultato+="Grafo creato! Vertici: "+grafo.vertexSet().size()+" Archi: "+grafo.edgeSet().size();
		return risultato;
	}

	public List<Team> getVertici() {
		List<Team> vertici = new LinkedList<>();
		for(Team t: grafo.vertexSet()) {
			vertici.add(t);
		}
		return vertici;
	}

	public String getAvversari(Team t) {
		String risultato="";
		List<Team> avversari = Graphs.neighborListOf(grafo, t);
		Collections.sort(avversari, new Comparator<Team>() {

			@Override
			public int compare(Team o1, Team o2) {
				DefaultWeightedEdge edge1 = grafo.getEdge(t, o1);
				double peso1 = grafo.getEdgeWeight(edge1);
				DefaultWeightedEdge edge2 = grafo.getEdge(t, o2);
				double peso2 = grafo.getEdgeWeight(edge2);
				return (int) (peso2-peso1);
			}
		});
		for(Team t1: avversari) {
			DefaultWeightedEdge edge = grafo.getEdge(t, t1);
			risultato+=t1.getTeam()+" "+grafo.getEdgeWeight(edge)+"\n";
		}
		return risultato;
	}

}
