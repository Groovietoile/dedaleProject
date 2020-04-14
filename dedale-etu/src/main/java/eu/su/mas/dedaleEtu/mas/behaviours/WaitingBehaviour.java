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
import jade.lang.acl.MessageTemplate;

public class WaitingBehaviour extends SimpleBehaviour {

	/**
	 * 
	 */
	private static final long serialVersionUID = -582172069645201135L;
	private boolean finished;
	private String center;
	private List<String> centerNeighbours;
	private Integer indexNext;
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
		
		// On envoie un message vers l'agent
		final MessageTemplate msgTemplate = MessageTemplate.MatchPerformative(ACLMessage.INFORM);
		final ACLMessage msgDemandSubTree = this.myAgent.receive(msgTemplate);
		ACLMessage msg=new ACLMessage(ACLMessage.INFORM);
		msg.setSender(this.myAgent.getAID());
		msg.setProtocol("UselessProtocol");
		
		if (msgDemandSubTree == null) {
			for (String receiver : ((HunterAgent)this.myAgent).getListeAmis())
				msg.addReceiver(new AID(receiver, AID.ISLOCALNAME));
			msg.setContent(iAmWaiting);
			((AbstractDedaleAgent)this.myAgent).sendMessage(msg);
		}
		else {
			msg.addReceiver(msgDemandSubTree.getSender());
			msg.setContent(this.indexNext.toString());
			((AbstractDedaleAgent)this.myAgent).sendMessage(msg);
			this.indexNext = (this.indexNext + 1) % this.centerNeighbours.size();
		}
	}

	public String getCenter() {
		return center;
	}

	public void setCenter(String center) {
		this.center = center;
	}
	
	public void updateCenterNeighbours() {
		if (this.centerNeighbours == null)
			this.centerNeighbours = ((AbstractExploreMultiAgent)this.myAgent).getMyMap().getNodeNeighbours(this.center);
	}

	@Override
	public boolean done() {
		// TODO Auto-generated method stub
		return finished;
	}

}