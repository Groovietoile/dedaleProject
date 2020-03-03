package eu.su.mas.dedaleEtu.mas.knowledge;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

import dataStructures.tuple.Couple;

public class MapMessage implements Serializable {

	private static final long serialVersionUID = -1539136037779261196L;
	private HashMap<String, ArrayList<String>> listeDesNoeuds;
	private ArrayList<Couple<String, String>> listeDesArcs;
	
	
	public MapMessage() {
		listeDesNoeuds = new HashMap<String, ArrayList<String>>();
		listeDesArcs = new ArrayList<Couple<String, String>>();
	}


	public HashMap getListeDesNoeuds() {
		return listeDesNoeuds;
	}


	public void setListeDesNoeuds(HashMap listeDesNoeuds) {
		this.listeDesNoeuds = listeDesNoeuds;
	}


	public ArrayList<Couple<String, String>> getListeDesArcs() {
		return listeDesArcs;
	}


	public void setListeDesArcs(ArrayList<Couple<String, String>> listeDesArcs) {
		this.listeDesArcs = listeDesArcs;
	}
	
	public MapRepresentation toMapRepresentation () {
		MapRepresentation mapRep = new MapRepresentation();
		//TO DO
		return null;
	}
	
}
