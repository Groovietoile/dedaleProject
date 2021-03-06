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
	
	private ArrayList<Tuple<String, Integer>> allnodes;
	private HashMap<String, ArrayList<Tuple<String, Integer>>> voisins;
	private boolean cycle;
	
	public MapMessage() {
		listeDesNoeuds = new HashMap<String, ArrayList<String>>();
		listeDesNoeuds.put("open", new ArrayList<String>());
		listeDesNoeuds.put("closed", new ArrayList<String>());
		listeDesArcs = new ArrayList<Couple<String, String>>();
		allnodes = new ArrayList<>();
		voisins = new HashMap<>();
		cycle = false;
	}


	public HashMap<String, ArrayList<String>> getListeDesNoeuds() {
		return listeDesNoeuds;
	}
	
	public ArrayList<String> getListeDesNoeudsMerged() {
		ArrayList<String> res = new ArrayList<>();
		res.addAll(this.listeDesNoeuds.get("open"));
		res.addAll(this.listeDesNoeuds.get("closed"));
		return res;
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
		if (shortestPath.size() == 0)
			return null;
		shortestPath.remove(0);//remove the current position
		return shortestPath;
	}
	
	// Améliorer la logique pour le calcul du centre
	public String getCenter() {
		return "2";
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
	
	public class Tuple<X, Y> { 
		private X x; 
		private Y y;
		
		public Tuple(X x, Y y) {
			this.x = x; 
		    this.y = y; 
		}
		
		public X getLeft() {
			return x;
		}
		
		public Y getRight() {
			return y;
		}

		public void setLeft(X x) {
			this.x = x;
		}

		public void setRight(Y y) {
			this.y = y;
		} 
		  
	} 
	
	// Pour Maria
	// Une fonction qui indique si une carte courante est un arbre ou pas
	public boolean isTree() {
//		private HashMap<String, ArrayList<String>> listeDesNoeuds;
//		private ArrayList<Couple<String, String>> listeDesArcs;
		
//		ArrayList<String> opennodes = this.listeDesNoeuds.get("open");
//		ArrayList<String> closednodes = this.listeDesNoeuds.get("closed");
//		
//		for (Couple<String, String> arc : this.listeDesArcs) {
//			mapRep.addEdge(arc.getLeft(), arc.getRight());
//		}
		
		
//		private ArrayList<Couple<String, Integer>> allnodes;
//		private HashMap<String, ArrayList<Couple<String, Integer>>> voisins;
		
		//récupération de tous les noeuds
		//ArrayList<String> allnodes = new ArrayList<>();
		for (String key : this.listeDesNoeuds.keySet()) {
			ArrayList<String> nodelist = this.listeDesNoeuds.get(key);
			for (String node : nodelist) {
				allnodes.add(new Tuple(node, 0));
			}		
		}
		
		//récupération de tous les voisins par noeud
		//HashMap<String, ArrayList<String>> voisins = new HashMap<>();
		for (Couple<String, String> arc : listeDesArcs) {
			if (!voisins.containsKey(arc.getLeft())){
				ArrayList<Tuple<String, Integer>> v = new ArrayList<>();
				v.add(new Tuple(arc.getRight(),0));
				voisins.put(arc.getLeft(), v);
			}
			else {
				voisins.get(arc.getLeft()).add(new Tuple(arc.getRight(), 0));
			}
		}
		
		//DFS
		for (Tuple<String, Integer> node : allnodes) {
			//si node est pas marqué
			if (node.getRight() == 0) {
				exploDFS(node);
			}	
		}
		
		if (cycle) return false;
		
		//si jamais graphe pas connexe, vérifier :
		//si un arc pas visité -> return false;
		
		return true;
	}
	

	public MapMessage shallowCopy() {
		MapMessage copy = new MapMessage();
		for (String on: this.listeDesNoeuds.get("open"))
			copy.listeDesNoeuds.get("open").add(on);
		for (String cn: this.listeDesNoeuds.get("closed"))
			copy.listeDesNoeuds.get("closed").add(cn);
		for (Couple<String, String> edge: this.listeDesArcs)
			copy.listeDesArcs.add(new Couple<String, String>(edge.getLeft(), edge.getRight()));
		return copy;
	}
	
	public MapMessage deleteNode(String idNode) {
		MapMessage res = this.shallowCopy();
		res.listeDesNoeuds.get("open").remove(idNode);
		res.listeDesNoeuds.get("closed").remove(idNode);
		Couple<String, String> tempEdge = null;
		for (int i = 0; i < res.listeDesArcs.size(); i++) {
			tempEdge = res.getListeDesArcs().get(i);
			if (tempEdge.getLeft().equals(idNode) || tempEdge.getRight().equals(idNode)) {
				res.listeDesArcs.remove(i);
				i--;
			}
		}
		return res;
	}
	
	public MapMessage deleteNode(List<String> idsNodes) {
		MapMessage res = this.shallowCopy();
		for (String idNode: idsNodes)
			res = res.deleteNode(idNode);
		return res;
	}
	
	public ArrayList<String> getAccessibleNodes(String idNode) {
		ArrayList<String> res = new ArrayList<>();
		res.add(idNode);
		ArrayList<String> nodesToProcess = new ArrayList<>();
		nodesToProcess.addAll(this.getNodeNeighbours(idNode));

		while (nodesToProcess.size() > 0) {
			String np = nodesToProcess.get(0);
			nodesToProcess.remove(0);
			for (String npNeigbour: this.getNodeNeighbours(np)) {
				if (!nodesToProcess.contains(npNeigbour) && !res.contains(npNeigbour))
					nodesToProcess.add(npNeigbour);
			}
			res.add(np);
		}

		return res;
	}
	
	public MapMessage getAccessibleMap(String idNode) {
		ArrayList<String> accessibleNodes = this.getAccessibleNodes(idNode);
		ArrayList<String> nodesToDelete = new ArrayList<String>();
		for (String node: this.getListeDesNoeudsMerged())
			if (!accessibleNodes.contains(node))
				nodesToDelete.add(node);
		return this.deleteNode(nodesToDelete);
	}
	
	public HashMap<String, Integer> degrees() {
		HashMap<String, Integer> degrees = new HashMap<String, Integer>();
		for (String node: this.getListeDesNoeudsMerged())
			degrees.put(node, 0);
		for (Couple<String, String> edge: this.getListeDesArcs()) {
			degrees.put(edge.getLeft(), degrees.get(edge.getLeft()) + 1);
			degrees.put(edge.getRight(), degrees.get(edge.getRight()) + 1);
		}
		return degrees;
	}
	
	public Couple<String, Integer> degreeMax() {
		HashMap<String, Integer> degrees = this.degrees();
		Couple<String, Integer> res = new Couple<String, Integer>("", -1);
		for (String key: degrees.keySet())
			if (degrees.get(key) > res.getRight())
				res = new Couple<String, Integer>(key, degrees.get(key));
		return res;
	}
	
	public void exploDFS(Tuple<String, Integer> node){
		//marquer node
		node.setRight(1);
		
		System.err.println("DFS : noeud marqué : "+node);
		
		//récupération des voisins
		ArrayList<Tuple<String, Integer>> v = voisins.get(node);
		
		for (Tuple fils : v) {
			//si le fils est pas marqué
			if ((int)fils.getRight() == 0) { 
				exploDFS(fils);
			}	
			//si fils déjà visité
			else {
				cycle = true;
				System.err.println("DFS : cycle détecté");
			}
		
		}
	}
}
