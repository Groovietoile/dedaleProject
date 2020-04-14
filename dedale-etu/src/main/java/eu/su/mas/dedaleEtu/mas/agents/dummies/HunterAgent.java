package eu.su.mas.dedaleEtu.mas.agents.dummies;

import java.util.ArrayList;
import java.util.List;

import eu.su.mas.dedale.mas.AbstractDedaleAgent;
import eu.su.mas.dedale.mas.agent.behaviours.startMyBehaviours;
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
import jade.core.behaviours.Behaviour;

public class HunterAgent extends AbstractExploreMultiAgent {

	/**
	 * 
	 */
	public static enum AgentRole {
		exploring,
		following,
		blocking,
		waiting,
		returning,
		patrolling;
	}
	private static final long serialVersionUID = 5053345286495358254L;
	private List<String> listeAmis;
	private boolean followingGolem;
	private AgentRole role;
	private Integer indexPatrolling;
	
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
		this.followingGolem = false;
		this.role = AgentRole.exploring;
		this.indexPatrolling = -1;
		
		/************************************************
		 * 
		 * ADD the behaviours of the Dummy Moving Agent
		 * 
		 ************************************************/
		
		this.listeAmis = new ArrayList<String>();
		String[] listeAmisPrevisionnelle = {"Explo1", "Explo2"};
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
	
	public boolean isFollowingGolem() {
		return this.role == AgentRole.following;
	}
	
	public boolean isExploring() {
		return this.role == AgentRole.exploring;
	}
	
	public boolean isReturning() {
		return this.role == AgentRole.returning;
	}
	
	public boolean isWaiting() {
		return this.role == AgentRole.waiting;
	}
	
	public boolean isPatrolling() {
		return this.role == AgentRole.patrolling;
	}

	public Integer getIndexPatrolling() {
		return indexPatrolling;
	}

	public void setIndexPatrolling(Integer indexPatrolling) {
		this.indexPatrolling = indexPatrolling;
	}

//	public void setFollowingGolem(boolean followingGolem) {
//		this.followingGolem = followingGolem;
//	}
	
}
