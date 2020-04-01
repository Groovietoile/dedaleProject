package eu.su.mas.dedaleEtu.mas.agents.dummies;

import eu.su.mas.dedale.mas.AbstractDedaleAgent;
import eu.su.mas.dedaleEtu.mas.knowledge.MapMessage;
import eu.su.mas.dedaleEtu.mas.knowledge.MapRepresentation;

public abstract class AbstractExploreMultiAgent extends AbstractDedaleAgent {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7938253332745788873L;

	protected String nextNode;
	protected MapMessage myMapMessage;
	
	public String getNextNode() {
		return nextNode;
	}


	public void setNextNode(String nextNode) {
		this.nextNode = nextNode;
	}
	
	public MapMessage getMyMap() {
		return myMapMessage;
	}


	public void setMyMap(MapMessage myMap) {
		this.myMapMessage = myMap;
	}
	
	public void mergeMap(MapMessage otherMap) {
		this.myMapMessage.mergeWith(otherMap);
	}
	
	public void mergeMap(MapRepresentation otherMap) {
		this.myMapMessage.mergeWith(otherMap);
	}

}
