package eu.su.mas.dedaleEtu.mas.knowledge;

import java.io.Serializable;

import eu.su.mas.dedaleEtu.mas.agents.dummies.HunterAgent.AgentRole;
import eu.su.mas.dedaleEtu.mas.knowledge.MapRepresentation;

public class ExploMultiAgentMessageContent implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 396789482224524152L;

	private String agentName;
	private String currentPosition;
	private String nextPosition;
	private MapMessage map;
	private boolean giveMeWay;
	private AgentRole role;

	public ExploMultiAgentMessageContent(String agentName, String currentPosition, String nextPosition, MapMessage map, boolean giveMeWay) {
		this.agentName = agentName;
		this.currentPosition = currentPosition;
		this.nextPosition = nextPosition;
		this.map = map;
		this.giveMeWay = giveMeWay;
		this.role = null;
	}
	
	public ExploMultiAgentMessageContent(String agentName, String currentPosition, String nextPosition, MapMessage map, boolean giveMeWay, AgentRole role) {
		this.agentName = agentName;
		this.currentPosition = currentPosition;
		this.nextPosition = nextPosition;
		this.map = map;
		this.giveMeWay = giveMeWay;
		this.role = role;
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

	public MapMessage getMap() {
		return map;
	}

	public void setMap(MapMessage map) {
		this.map = map;
	}
	
	public boolean isGiveMeWay() {
		return giveMeWay;
	}

	public void setGiveMeWay(boolean giveMeWay) {
		this.giveMeWay = giveMeWay;
	}

	public AgentRole getRole() {
		return role;
	}

	public void setRole(AgentRole role) {
		this.role = role;
	}
}
