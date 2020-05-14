package eu.su.mas.dedaleEtu.mas.agents.dummies;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import dataStructures.tuple.Couple;
import eu.su.mas.dedale.env.Observation;
import eu.su.mas.dedale.mas.AbstractDedaleAgent;
import eu.su.mas.dedale.mas.agent.behaviours.startMyBehaviours;
import eu.su.mas.dedaleEtu.mas.behaviours.BlockGolemBehaviour;
import eu.su.mas.dedaleEtu.mas.behaviours.CheckBlockBehaviour;
import eu.su.mas.dedaleEtu.mas.behaviours.ExploMultiBehaviour;
import eu.su.mas.dedaleEtu.mas.behaviours.ExploSoloBehaviour;
import eu.su.mas.dedaleEtu.mas.behaviours.FollowGolemBehaviour;
import eu.su.mas.dedaleEtu.mas.behaviours.PatrollingBehaviour;
import eu.su.mas.dedaleEtu.mas.behaviours.RandomWalkBehaviour;
import eu.su.mas.dedaleEtu.mas.behaviours.ReceiveInfoBehaviour;
import eu.su.mas.dedaleEtu.mas.behaviours.ReceiveInfoHunterBehaviour;
import eu.su.mas.dedaleEtu.mas.behaviours.ReturnBehaviour;
import eu.su.mas.dedaleEtu.mas.behaviours.SayHello;
import eu.su.mas.dedaleEtu.mas.behaviours.SendInfoBehaviour;
import eu.su.mas.dedaleEtu.mas.behaviours.TestOdeurBehaviour;
import eu.su.mas.dedaleEtu.mas.behaviours.WaitingBehaviour;
import eu.su.mas.dedaleEtu.mas.knowledge.MapMessage;
import eu.su.mas.dedaleEtu.mas.knowledge.MapRepresentation.MapAttribute;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;

public class HunterAgent extends AbstractExploreMultiAgent {

	/**
	 * 
	 */
	public static enum AgentRole {
		blocking,
		asking,
		following,
		patrolling,
		waiting,
		returning,
		exploring
	}
	// exploring <= returning <= waiting <= patrolling <= following <= asking <= blocking
	// AgentRole left, right;
	// left <= right ?
	//
	private static HashMap<Couple<AgentRole, AgentRole>, Integer> agentsRolesRelNEq = null;
	private static void initARR() {
		HunterAgent.agentsRolesRelNEq = new HashMap<Couple<AgentRole, AgentRole>, Integer>();
		

		HunterAgent.agentsRolesRelNEq.put(new Couple<AgentRole, AgentRole>(AgentRole.blocking, AgentRole.asking), -1);
		HunterAgent.agentsRolesRelNEq.put(new Couple<AgentRole, AgentRole>(AgentRole.blocking, AgentRole.following), -1);
		HunterAgent.agentsRolesRelNEq.put(new Couple<AgentRole, AgentRole>(AgentRole.blocking, AgentRole.patrolling), -1);
		HunterAgent.agentsRolesRelNEq.put(new Couple<AgentRole, AgentRole>(AgentRole.blocking, AgentRole.waiting), -1);
		HunterAgent.agentsRolesRelNEq.put(new Couple<AgentRole, AgentRole>(AgentRole.blocking, AgentRole.returning), -1);
		HunterAgent.agentsRolesRelNEq.put(new Couple<AgentRole, AgentRole>(AgentRole.blocking, AgentRole.exploring), -1);
		
		HunterAgent.agentsRolesRelNEq.put(new Couple<AgentRole, AgentRole>(AgentRole.asking, AgentRole.blocking), 1);
		HunterAgent.agentsRolesRelNEq.put(new Couple<AgentRole, AgentRole>(AgentRole.asking, AgentRole.following), -1);
		HunterAgent.agentsRolesRelNEq.put(new Couple<AgentRole, AgentRole>(AgentRole.asking, AgentRole.patrolling), -1);
		HunterAgent.agentsRolesRelNEq.put(new Couple<AgentRole, AgentRole>(AgentRole.asking, AgentRole.waiting), -1);
		HunterAgent.agentsRolesRelNEq.put(new Couple<AgentRole, AgentRole>(AgentRole.asking, AgentRole.returning), -1);
		HunterAgent.agentsRolesRelNEq.put(new Couple<AgentRole, AgentRole>(AgentRole.asking, AgentRole.exploring), -1);
		

		HunterAgent.agentsRolesRelNEq.put(new Couple<AgentRole, AgentRole>(AgentRole.following, AgentRole.asking), 1);
		HunterAgent.agentsRolesRelNEq.put(new Couple<AgentRole, AgentRole>(AgentRole.following, AgentRole.blocking), 1);
		HunterAgent.agentsRolesRelNEq.put(new Couple<AgentRole, AgentRole>(AgentRole.following, AgentRole.patrolling), -1);
		HunterAgent.agentsRolesRelNEq.put(new Couple<AgentRole, AgentRole>(AgentRole.following, AgentRole.waiting), -1);
		HunterAgent.agentsRolesRelNEq.put(new Couple<AgentRole, AgentRole>(AgentRole.following, AgentRole.returning), -1);
		HunterAgent.agentsRolesRelNEq.put(new Couple<AgentRole, AgentRole>(AgentRole.following, AgentRole.exploring), -1);
		
		HunterAgent.agentsRolesRelNEq.put(new Couple<AgentRole, AgentRole>(AgentRole.patrolling, AgentRole.blocking), 1);
		HunterAgent.agentsRolesRelNEq.put(new Couple<AgentRole, AgentRole>(AgentRole.patrolling, AgentRole.asking), 1);
		HunterAgent.agentsRolesRelNEq.put(new Couple<AgentRole, AgentRole>(AgentRole.patrolling, AgentRole.following), 1);
		HunterAgent.agentsRolesRelNEq.put(new Couple<AgentRole, AgentRole>(AgentRole.patrolling, AgentRole.waiting), -1);
		HunterAgent.agentsRolesRelNEq.put(new Couple<AgentRole, AgentRole>(AgentRole.patrolling, AgentRole.returning), -1);
		HunterAgent.agentsRolesRelNEq.put(new Couple<AgentRole, AgentRole>(AgentRole.patrolling, AgentRole.exploring), -1);
		
		HunterAgent.agentsRolesRelNEq.put(new Couple<AgentRole, AgentRole>(AgentRole.waiting, AgentRole.blocking), 1);
		HunterAgent.agentsRolesRelNEq.put(new Couple<AgentRole, AgentRole>(AgentRole.waiting, AgentRole.asking), 1);
		HunterAgent.agentsRolesRelNEq.put(new Couple<AgentRole, AgentRole>(AgentRole.waiting, AgentRole.following), 1);
		HunterAgent.agentsRolesRelNEq.put(new Couple<AgentRole, AgentRole>(AgentRole.waiting, AgentRole.patrolling), 1);
		HunterAgent.agentsRolesRelNEq.put(new Couple<AgentRole, AgentRole>(AgentRole.waiting, AgentRole.returning), -1);
		HunterAgent.agentsRolesRelNEq.put(new Couple<AgentRole, AgentRole>(AgentRole.waiting, AgentRole.exploring), -1);
		
		HunterAgent.agentsRolesRelNEq.put(new Couple<AgentRole, AgentRole>(AgentRole.returning, AgentRole.blocking), 1);
		HunterAgent.agentsRolesRelNEq.put(new Couple<AgentRole, AgentRole>(AgentRole.returning, AgentRole.asking), 1);
		HunterAgent.agentsRolesRelNEq.put(new Couple<AgentRole, AgentRole>(AgentRole.returning, AgentRole.following), 1);
		HunterAgent.agentsRolesRelNEq.put(new Couple<AgentRole, AgentRole>(AgentRole.returning, AgentRole.patrolling), 1);
		HunterAgent.agentsRolesRelNEq.put(new Couple<AgentRole, AgentRole>(AgentRole.returning, AgentRole.waiting), 1);
		HunterAgent.agentsRolesRelNEq.put(new Couple<AgentRole, AgentRole>(AgentRole.returning, AgentRole.exploring), -1);
		
		HunterAgent.agentsRolesRelNEq.put(new Couple<AgentRole, AgentRole>(AgentRole.exploring, AgentRole.blocking), 1);
		HunterAgent.agentsRolesRelNEq.put(new Couple<AgentRole, AgentRole>(AgentRole.exploring, AgentRole.asking), 1);
		HunterAgent.agentsRolesRelNEq.put(new Couple<AgentRole, AgentRole>(AgentRole.exploring, AgentRole.following), 1);
		HunterAgent.agentsRolesRelNEq.put(new Couple<AgentRole, AgentRole>(AgentRole.exploring, AgentRole.patrolling), 1);
		HunterAgent.agentsRolesRelNEq.put(new Couple<AgentRole, AgentRole>(AgentRole.exploring, AgentRole.waiting), 1);
		HunterAgent.agentsRolesRelNEq.put(new Couple<AgentRole, AgentRole>(AgentRole.exploring, AgentRole.returning), 1);
	}
	// left == right -> 0
	// left > right  -> -1
	// left < right  -> 1
	public static Integer AgentsRolesPriority(AgentRole left, AgentRole right) {
		if (left == right)
			return 0;
		if (HunterAgent.agentsRolesRelNEq == null)
			HunterAgent.initARR();
		return HunterAgent.agentsRolesRelNEq.get(new Couple<AgentRole, AgentRole>(left, right));
	}
	private static final long serialVersionUID = 5053345286495358254L;

	private List<String> listeAmis;
	private AgentRole role;
	private List<Integer> indicesPatrolling;
	private boolean measureTime;
	private List<String> blockingPositions;
	
	public AgentRole getRole() {
		return role;
	}

	public void setRole(AgentRole role) {
		this.role = role;
	}

	protected void setup() {

		super.setup();

		List<Behaviour> lb=new ArrayList<Behaviour>();
		this.myMapMessage = new MapMessage();
		this.role = AgentRole.exploring;
		this.indicesPatrolling = new ArrayList<Integer>();
		this.measureTime = false;
		this.blockingPositions = new ArrayList<String>();
		
		/************************************************
		 * 
		 * ADD the behaviours of the Dummy Moving Agent
		 * 
		 ************************************************/
		
		this.listeAmis = new ArrayList<String>();
		String[] listeAmisPrevisionnelle = {"Explo1", "Explo2", "Explo3", "Explo4", "Explo5", "Explo6"};
		for (int i = 0; i < listeAmisPrevisionnelle.length; i++) {
			if (!listeAmisPrevisionnelle[i].equals(this.getLocalName()))
				this.listeAmis.add(listeAmisPrevisionnelle[i]);
		}
		
		lb.add(new SendInfoBehaviour(this, this.listeAmis));
		lb.add(new ReceiveInfoHunterBehaviour(this));
		lb.add(new FollowGolemBehaviour(this));
		lb.add(new ExploMultiBehaviour(this, null));
		lb.add(new ReturnBehaviour(this));
		lb.add(new WaitingBehaviour(this));
		lb.add(new PatrollingBehaviour(this));
		lb.add(new CheckBlockBehaviour(this));
		lb.add(new BlockGolemBehaviour(this));
		// lb.add(new ExploMultiBehaviour(this, null));
		/***
		 * MANDATORY TO ALLOW YOUR AGENT TO BE DEPLOYED CORRECTLY
		 */
		
		
		addBehaviour(new startMyBehaviours(this,lb));
		
		System.out.println("the  agent "+this.getLocalName()+ " is started");
	}

	public List<String> getListeAmis() {
		return listeAmis;
	}

	public void setListeAmis(List<String> listeAmis) {
		this.listeAmis = listeAmis;
	}

	public MapMessage getMyMapMessage() {
		return myMapMessage;
	}

	public void setMyMapMessage(MapMessage myMapMessage) {
		this.myMapMessage = myMapMessage;
	}
	
	public boolean isBlocking() {
		return this.role == AgentRole.blocking;
	}
	
	public boolean isAskingForHelp() {
		return this.role == AgentRole.asking;
	}
	
	public boolean isFollowing() {
		return this.role == AgentRole.following;
	}
	
	public boolean isPatrolling() {
		return this.role == AgentRole.patrolling;
	}
	
	public boolean isWaiting() {
		return this.role == AgentRole.waiting;
	}
	
	public boolean isReturning() {
		return this.role == AgentRole.returning;
	}

	public boolean isExploring() {
		return this.role == AgentRole.exploring;
	}

	public List<Integer> getIndicesPatrolling() {
		return indicesPatrolling;
	}

	public void setIndicesPatrolling(List<Integer> indexPatrolling) {
		this.indicesPatrolling = indexPatrolling;
	}
	
	public boolean isMeasureTime() {
		return measureTime;
	}
	
	public void setMeasureTime(boolean measureTime) {
		this.measureTime = measureTime;
	}
	
	public List<String> getBlockingPositions() {
		return blockingPositions;
	}

	public void setBlockingPositions(List<String> blockingPositions) {
		this.blockingPositions = blockingPositions;
	}
	
	@Override
	public boolean moveTo(String myDestination) {
		List<Couple<String, List<Couple<Observation, Integer>>>> obs = ((AbstractDedaleAgent)this).observe();
		this.exploreFromObs(obs);
		return super.moveTo(myDestination);
	}
	
	public void exploreFromObs(List<Couple<String, List<Couple<Observation, Integer>>>> obs) {
		Iterator<Couple<String, List<Couple<Observation, Integer>>>> iter=obs.iterator();
		while(iter.hasNext()){
			String nodeId=iter.next().getLeft();
			if (nodeId.equals(super.getCurrentPosition()))
				continue;
			if (!this.getMyMap().getListeDesNoeuds().get("closed").contains(nodeId) && !this.getMyMap().getListeDesNoeuds().get("open").contains(nodeId)) {
				this.getMyMap().getListeDesNoeuds().get("open").add(nodeId);	
			}
			if (!this.getMyMap().getListeDesArcs().contains(new Couple<String, String>(super.getCurrentPosition(), nodeId)) &&
				!this.getMyMap().getListeDesArcs().contains(new Couple<String, String>(nodeId, super.getCurrentPosition()))) {
				this.getMyMap().getListeDesArcs().add(new Couple<String, String>(super.getCurrentPosition(), nodeId));
			}
		}
	}
	
}
