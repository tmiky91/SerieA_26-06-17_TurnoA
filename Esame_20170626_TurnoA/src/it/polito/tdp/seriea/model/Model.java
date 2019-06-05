package it.polito.tdp.seriea.model;

import java.util.HashMap;
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

	public Object getVertici() {
		// TODO Auto-generated method stub
		return null;
	}

}
