package eu.su.mas.dedaleEtu.mas.behaviours;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import dataStructures.tuple.Couple;
import eu.su.mas.dedale.mas.AbstractDedaleAgent;
import eu.su.mas.dedaleEtu.mas.agents.dummies.AbstractExploreMultiAgent;
import eu.su.mas.dedaleEtu.mas.agents.dummies.HunterAgent;
import eu.su.mas.dedaleEtu.mas.agents.dummies.HunterAgent.AgentRole;
import eu.su.mas.dedaleEtu.mas.knowledge.ExploMultiAgentMessageContent;
import eu.su.mas.dedaleEtu.mas.knowledge.MapMessage;
import jade.core.Agent;
import jade.core.behaviours.SimpleBehaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;

// TODO REFACTOR THIS
public class ReceiveInfoHunterBehaviour extends SimpleBehaviour {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2401517043351791117L;
	private boolean finished = false;

	public ReceiveInfoHunterBehaviour(Agent myAgent) {
		super(myAgent);
	}

	@Override
	public void action() {
		
		try {
			if (((HunterAgent)this.myAgent).isBlocking() || ((HunterAgent)this.myAgent).isFollowing()) {
				this.finished = true;
				return;
			}
		}
		catch(Exception e) {
			if (!e.getClass().getName().equals("java.lang.ClassCastException")) {
				System.out.println(e.getMessage());
			}
		}
		
		final MessageTemplate msgTemplate = MessageTemplate.MatchPerformative(ACLMessage.INFORM_REF);
		final String giveMeWayString = "Laisse-moi passer stp !";

		final ACLMessage msg = this.myAgent.receive(msgTemplate);
		ExploMultiAgentMessageContent msgContent;
		if (msg != null) {
			try {
				msgContent = (ExploMultiAgentMessageContent)msg.getContentObject();
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				// e1.printStackTrace();
				return;
			}
		}
		else { return; }
		MapMessage senderMapMessage = msgContent.getMap();
		((AbstractExploreMultiAgent)this.myAgent).mergeMap(senderMapMessage);
		boolean noNeighbours = false;
		AgentRole myRole = ((HunterAgent)this.myAgent).getRole(), senderRole = msgContent.getRole();
		if (myRole == AgentRole.waiting) {
			System.out.println("INFO DE SENDER :");
			System.out.println(msgContent.getAgentName());
			System.out.println(msgContent.getCurrentPosition());
			System.out.println(msgContent.getNextPosition());
			System.out.println(msgContent.getRole());
			
			System.out.println("INFO DE MOI :");
			System.out.println(this.myAgent.getLocalName());
			System.out.println(((AbstractDedaleAgent)this.myAgent).getCurrentPosition());
			System.out.println(((AbstractExploreMultiAgent)this.myAgent).getNextNode());
			System.out.println(myRole);
			
		}
		if (!msgContent.isGiveMeWay() && HunterAgent.AgentsRolesPriority(myRole, senderRole) >= 0) {
			String senderCurrentNode = msgContent.getCurrentPosition(), senderNextNode = msgContent.getNextPosition(),
				   myCurrentNode = ((AbstractDedaleAgent)this.myAgent).getCurrentPosition(),
				   myNextNode = ((AbstractExploreMultiAgent)this.myAgent).getNextNode();
			// System.out.println(senderCurrentNode + ',' + myNextNode + ',' + senderNextNode + ',' + myCurrentNode);
			// System.out.println(senderCurrentNode.equals(myNextNode) && senderNextNode.equals(myCurrentNode));
//			System.out.println("INFO DE SENDER :");
//			System.out.println(msgContent.getAgentName());
//			System.out.println(msgContent.getCurrentPosition());
//			System.out.println(msgContent.getNextPosition());
//			System.out.println(msgContent.getRole());
//			
//			System.out.println("INFO DE MOI :");
//			System.out.println(this.myAgent.getLocalName());
//			System.out.println(((AbstractDedaleAgent)this.myAgent).getCurrentPosition());
//			System.out.println(((AbstractExploreMultiAgent)this.myAgent).getNextNode());
//			System.out.println(myRole);
//			
			if ((senderCurrentNode.equals(myNextNode) || myNextNode.equals(myCurrentNode)) && senderNextNode.equals(myCurrentNode)) {
				noNeighbours = !this.giveWay(myCurrentNode, senderCurrentNode);
			}
			if (noNeighbours) {
				System.out.println("Agent " + this.myAgent.getLocalName() + " : " + giveMeWayString);
				ACLMessage msgGiveMeWay = new ACLMessage(ACLMessage.INFORM_REF);
				msgGiveMeWay.setSender(this.myAgent.getAID());
				msgGiveMeWay.setProtocol("UselessProtocol");

				MapMessage myMap = ((AbstractExploreMultiAgent)this.myAgent).getMyMap();
				try {
					msgGiveMeWay.setContentObject(
							new ExploMultiAgentMessageContent(myAgent.getLocalName(), myCurrentNode, myNextNode, myMap, true, ((HunterAgent)this.myAgent).getRole()));
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				msgGiveMeWay.addReceiver(msg.getSender());

				((AbstractDedaleAgent)this.myAgent).sendMessage(msgGiveMeWay);
			}
		}
		else if (msgContent.isGiveMeWay()) {
			String senderCurrentNode = msgContent.getCurrentPosition();
			String myCurrentNode = ((AbstractDedaleAgent)this.myAgent).getCurrentPosition();
			boolean done = this.giveWay(myCurrentNode, senderCurrentNode);
			if (done) {
				System.out.println("Vas-y !");
			}
			else {
				System.out.println("J'arrive pas :(");
			}
			((AbstractExploreMultiAgent)this.myAgent).setNextNode(((AbstractDedaleAgent)this.myAgent).getCurrentPosition());
		}
		
	}
	
	private boolean giveWay(String myCurrentNode, String senderCurrentNode) {
		boolean success = true;
		MapMessage myMap = ((AbstractExploreMultiAgent)this.myAgent).getMyMap();
		ArrayList<String> currentNodeOpenNeighbours = new ArrayList<String>(),
				currentNodeClosedNeighbours = new ArrayList<String>();
		for (Couple<String, String> edge : myMap.getListeDesArcs()) {
			if (edge.getLeft().equals(myCurrentNode) && myMap.getListeDesNoeuds().get("open").contains(edge.getRight())) {
				currentNodeOpenNeighbours.add(edge.getRight());
			}
			else if (edge.getLeft().equals(myCurrentNode) && myMap.getListeDesNoeuds().get("closed").contains(edge.getRight())) {
				currentNodeClosedNeighbours.add(edge.getRight());
			}
			else if (edge.getRight().equals(myCurrentNode) && myMap.getListeDesNoeuds().get("open").contains(edge.getLeft())) {
				currentNodeOpenNeighbours.add(edge.getLeft());
			}
			else if (edge.getRight().equals(myCurrentNode) && myMap.getListeDesNoeuds().get("closed").contains(edge.getLeft())) {
				currentNodeClosedNeighbours.add(edge.getLeft());
			}
		}
		
		String neighbourRandom = "", neighbourTemp = "";
		Integer neighbourRandomIndex = -1, indexTemp = -1;

		while (currentNodeOpenNeighbours.size() > 0) {
			indexTemp = new Random().nextInt(currentNodeOpenNeighbours.size());
			neighbourTemp = currentNodeOpenNeighbours.get(indexTemp);
			if (!neighbourTemp.equals(senderCurrentNode)) {
				neighbourRandomIndex = indexTemp;
				neighbourRandom = neighbourTemp;
				break;
			}
			if (currentNodeOpenNeighbours.size() == 1) { break; }
			
		}
		if (neighbourRandomIndex == -1) {
			while (currentNodeClosedNeighbours.size() > 0) {
				indexTemp = new Random().nextInt(currentNodeClosedNeighbours.size());
				neighbourTemp = currentNodeClosedNeighbours.get(indexTemp);
				if (!neighbourTemp.equals(senderCurrentNode)) {
					neighbourRandomIndex = indexTemp;
					neighbourRandom = neighbourTemp;
					break;
				}
				if (currentNodeClosedNeighbours.size() == 1) { break; }
			}
		}
		if (neighbourRandomIndex != -1) {
			String nextNodeCopy = ((HunterAgent)this.myAgent).getNextNode();
			((HunterAgent)this.myAgent).setNextNode(neighbourRandom);
			((HunterAgent)this.myAgent).moveTo(neighbourRandom);
			this.myAgent.doWait(500);
			// ((HunterAgent)this.myAgent).setNextNode(nextNodeCopy);
		}
		else { success = false; }
		return success;
	}

	@Override
	public boolean done() {
		return this.finished;
	}
}
