package eu.su.mas.dedaleEtu.mas.knowledge;

import java.io.Serializable;
import eu.su.mas.dedaleEtu.mas.knowledge.MapRepresentation;

public class ExploMultiAgentMessageContent implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 396789482224524152L;

	private String agentName;
	private String currentPosition;
	private String nextPosition;
	private MapRepresentation map;
	private boolean giveMeWay;

	public ExploMultiAgentMessageContent(String agentName, String currentPosition, String nextPosition, MapRepresentation map, boolean giveMeWay) {
		this.agentName = agentName;
		this.currentPosition = currentPosition;
		this.nextPosition = nextPosition;
		this.map = map;
		this.giveMeWay = giveMeWay;
	}
	
	public String getAgentName() {
		return agentName;
	}

	public void setAgentName(String agentName) {
		this.agentName = agentName;
	}

	public String getCurrentPosition() {
		return currentPosition;
	}

	public void setCurrentPosition(String currentPosition) {
		this.currentPosition = currentPosition;
	}

	public String getNextPosition() {
		return nextPosition;
	}

	public void setNextPosition(String nextPosition) {
		this.nextPosition = nextPosition;
	}

	public MapRepresentation getMap() {
		return map;
	}

	public void setMap(MapRepresentation map) {
		this.map = map;
	}
	
	public boolean isGiveMeWay() {
		return giveMeWay;
	}

	public void setGiveMeWay(boolean giveMeWay) {
		this.giveMeWay = giveMeWay;
	}
}
