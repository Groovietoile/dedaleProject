package eu.su.mas.dedaleEtu.mas.behaviours;

import java.util.ArrayList;
import java.util.List;

import dataStructures.tuple.Couple;
import eu.su.mas.dedale.mas.AbstractDedaleAgent;
import eu.su.mas.dedaleEtu.mas.agents.dummies.AbstractExploreMultiAgent;
import eu.su.mas.dedaleEtu.mas.agents.dummies.HunterAgent;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.SimpleBehaviour;
import jade.lang.acl.ACLMessage;

public class WaitingBehaviour extends SimpleBehaviour {

	/**
	 * 
	 */
	private static final long serialVersionUID = -582172069645201135L;
	private boolean finished;
	private String center;
	private List<String> centerNeighbours;
	private int indexNext;
	public static final String iAmWaiting = "J'attends déjà !";

	public WaitingBehaviour(AbstractDedaleAgent myAgent) {
		super(myAgent);
		this.finished = false;
		this.center = ((AbstractExploreMultiAgent)this.myAgent).getMyMap().getCenter();
		this.centerNeighbours = null;
		this.indexNext = 0;
	}

	@Override
	public void action() {
		// TODO Auto-generated method stub
		try {
			if (!((HunterAgent)this.myAgent).isWaiting()) { return; }
		}
		catch(Exception e) {
			if (!e.getClass().getName().equals("java.lang.ClassCastException"))
				System.out.println(e.getMessage());
		}
		if (this.centerNeighbours == null)
			this.updateCenterNeighbours();
		// System.out.println(this.centerNeighbours);
		
		// On envoie un message vers l'agent
		ACLMessage msg=new ACLMessage(ACLMessage.INFORM);
		msg.setSender(this.myAgent.getAID());
		msg.setProtocol("UselessProtocol");
		
		for (String receiver : ((HunterAgent)this.myAgent).getListeAmis())
			msg.addReceiver(new AID(receiver, AID.ISLOCALNAME));
		msg.setContent(iAmWaiting);
		((AbstractDedaleAgent)this.myAgent).sendMessage(msg);
		
		
		this.indexNext = (this.indexNext + 1) % this.centerNeighbours.size();
	}

	public String getCenter() {
		return center;
	}

	public void setCenter(String center) {
		this.center = center;
	}
	
	public void updateCenterNeighbours() {
		if (this.centerNeighbours == null)
			this.centerNeighbours = new ArrayList<String>();
		for (Couple<String, String> edge : ((AbstractExploreMultiAgent)this.myAgent).getMyMap().getListeDesArcs()) {
			if (edge.getLeft().equals(this.center))
				this.centerNeighbours.add(edge.getRight());
			if (edge.getRight().equals(this.center))
				this.centerNeighbours.add(edge.getLeft());
		}
	}

	@Override
	public boolean done() {
		// TODO Auto-generated method stub
		return finished;
	}

}
