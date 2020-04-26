package eu.su.mas.dedaleEtu.mas.knowledge;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.graphstream.algorithm.Dijkstra;
import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.SingleGraph;

import dataStructures.tuple.Couple;
import eu.su.mas.dedaleEtu.mas.agents.dummies.AbstractExploreMultiAgent;
import eu.su.mas.dedaleEtu.mas.knowledge.MapRepresentation.MapAttribute;

public class MapMessage implements Serializable {

	private static final long serialVersionUID = -1539136037779261196L;
	private HashMap<String, ArrayList<String>> listeDesNoeuds;
	private ArrayList<Couple<String, String>> listeDesArcs;
	
	
	public MapMessage() {
		listeDesNoeuds = new HashMap<String, ArrayList<String>>();
		listeDesNoeuds.put("open", new ArrayList<String>());
		listeDesNoeuds.put("closed", new ArrayList<String>());
		listeDesArcs = new ArrayList<Couple<String, String>>();
	}


	public HashMap<String, ArrayList<String>> getListeDesNoeuds() {
		return listeDesNoeuds;
	}


	public void setListeDesNoeuds(HashMap<String, ArrayList<String>> listeDesNoeuds) {
		this.listeDesNoeuds = listeDesNoeuds;
	}


	public ArrayList<Couple<String, String>> getListeDesArcs() {
		return listeDesArcs;
	}


	public void setListeDesArcs(ArrayList<Couple<String, String>> listeDesArcs) {
		this.listeDesArcs = listeDesArcs;
	}
	
	// On utilise pas cette méthode parce qu'elle n'était pas utile
	public MapRepresentation toMapRepresentation () {
		MapRepresentation mapRep = new MapRepresentation();
		mapRep.closeGuiPublic();
		
		MapRepresentation.MapAttribute open = MapRepresentation.MapAttribute.open;
		MapRepresentation.MapAttribute closed = MapRepresentation.MapAttribute.closed;
		for (String idNode : this.listeDesNoeuds.get("open")) {
			mapRep.addNode(idNode, open);
		}
		for (String idNode : this.listeDesNoeuds.get("closed")) {
			mapRep.addNode(idNode, closed);
		}
		for (Couple<String, String> arc : this.listeDesArcs) {
			mapRep.addEdge(arc.getLeft(), arc.getRight());
		}
		
		return mapRep;
	}
	
	public void mergeWith(MapMessage other) {
		if (other == null) { return; }
		for (String idNode : other.listeDesNoeuds.get("closed")) {
			if (this.listeDesNoeuds.get("closed").contains((Object)idNode)) { continue; }
			if (this.listeDesNoeuds.get("open").contains((Object)idNode)) {
				this.listeDesNoeuds.get("open").remove((Object)idNode);
			}
			this.listeDesNoeuds.get("closed").add(idNode);
		}
		for (String idNode : other.listeDesNoeuds.get("open")) {
			if (this.listeDesNoeuds.get("closed").contains((Object)idNode)) { continue; }
			if (this.listeDesNoeuds.get("open").contains((Object)idNode)) { continue; }
			this.listeDesNoeuds.get("open").add(idNode);
		}
		for (Couple<String, String> arc : other.listeDesArcs) {
			Couple<String, String> arc_inv = new Couple<String, String>(arc.getRight(), arc.getLeft());
			if (this.listeDesArcs.contains((Object)arc) || this.listeDesArcs.contains((Object)arc_inv)) { continue; }
			this.listeDesArcs.add(arc);
		}
	}
	
	public void mergeWith(MapRepresentation other) {
		if (other == null) { return; }
		for (Object nObject : other.getG().nodes().toArray()) {
			Node n = (Node)nObject;
			String idNode = n.getId();
			if ((n.getAttribute("ui.class").equals("closed")) || n.getAttribute("ui.class").equals("agent")) {
				if (this.listeDesNoeuds.get("closed").contains((Object)idNode)) { continue; }
				if (this.listeDesNoeuds.get("open").contains((Object)idNode)) {
					this.listeDesNoeuds.get("open").remove((Object)idNode);
				}
				this.listeDesNoeuds.get("closed").add(idNode);
			}
			else if (n.getAttribute("ui.class").equals("open")) {
				if (this.listeDesNoeuds.get("closed").contains((Object)idNode)) { continue; }
				if (this.listeDesNoeuds.get("open").contains((Object)idNode)) { continue; }
				this.listeDesNoeuds.get("open").add(idNode);
			}
		}
		for (Object arcObject : other.getG().edges().toArray()) {
			Edge e = (Edge)arcObject;
			Couple<String, String> arc = new Couple<String, String>(e.getNode0().getId(), e.getNode1().getId());
			Couple<String, String> arc_inv = new Couple<String, String>(arc.getRight(), arc.getLeft());
			if (this.listeDesArcs.contains((Object)arc) || this.listeDesArcs.contains((Object)arc_inv)) { continue; }
			this.listeDesArcs.add(arc);
		}
	}
	
	public List<String> getShortestPath(String idFrom, String idTo) {
		List<String> shortestPath=new ArrayList<String>();

		Dijkstra dijkstra = new Dijkstra();//number of edge
		
		boolean addNode = true;
		Graph g = new SingleGraph("graph_temp");
		for (ArrayList<String> nodes : this.listeDesNoeuds.values()) {
			for (String n : nodes) {
				addNode = true;
				for (Object ng : g.nodes().toArray()) {
					if (((Node)ng).getId().equals(n)) {
						addNode = false;
						break;
					}
				}
				if (addNode)
					g.addNode(n);
			}
		}
		for (Couple<String, String> edge : this.listeDesArcs)
			g.addEdge("(" + edge.getLeft() + ", " + edge.getRight() + ")", edge.getLeft(), edge.getRight());

		dijkstra.init(g);
		dijkstra.setSource(g.getNode(idFrom));
		dijkstra.compute();  // compute the distance to all nodes from idFrom
		List<Node> path=dijkstra.getPath(g.getNode(idTo)).getNodePath(); //the shortest path from idFrom to idTo
		Iterator<Node> iter=path.iterator();
		while (iter.hasNext()){
			shortestPath.add(iter.next().getId());
		}
		dijkstra.clear();
		shortestPath.remove(0);//remove the current position
		return shortestPath;
	}
	
	public String getCenter() {
		return "5";
	}
	
	public List<String> getNodeNeighbours(String idNode) {
		List<String> neighbours = new ArrayList<String>();
		for (Couple<String, String> edge : this.listeDesArcs) {
			if (edge.getLeft().equals(idNode))
				neighbours.add(edge.getRight());
			if (edge.getRight().equals(idNode))
				neighbours.add(edge.getLeft());
		}
		neighbours.sort(new StringCustomComparator());
		return neighbours;
	}
	
	
	public class StringCustomComparator implements Comparator<String> {
		
		public StringCustomComparator() {}

		@Override
		public int compare(String o1, String o2) {
			// TODO Auto-generated method stub
			return o1.compareTo(o2);
		}

	}
	
}
