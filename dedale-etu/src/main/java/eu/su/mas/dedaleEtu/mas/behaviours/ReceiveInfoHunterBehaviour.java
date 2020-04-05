package eu.su.mas.dedaleEtu.mas.behaviours;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import dataStructures.tuple.Couple;
import eu.su.mas.dedale.mas.AbstractDedaleAgent;
import eu.su.mas.dedaleEtu.mas.agents.dummies.AbstractExploreMultiAgent;
import eu.su.mas.dedaleEtu.mas.agents.dummies.HunterAgent;
import eu.su.mas.dedaleEtu.mas.knowledge.ExploMultiAgentMessageContent;
import eu.su.mas.dedaleEtu.mas.knowledge.MapMessage;
import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;

// TODO REFACTOR THIS
public class ReceiveInfoHunterBehaviour extends TickerBehaviour {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2401517043351791117L;

	public ReceiveInfoHunterBehaviour(Agent myAgent) {
		super(myAgent, 500);
	}

	@Override
	public void onTick() {
		
		try {
			if (((HunterAgent)this.myAgent).isFollowingGolem()) { return; }
		}
		catch(Exception e) {
			if (!e.getClass().getName().equals("java.lang.ClassCastException")) {
				System.out.println(e.getMessage());
			}
		}
		
		final MessageTemplate msgTemplate = MessageTemplate.MatchPerformative(ACLMessage.INFORM);
		final String giveMeWayString = "Laisse-moi passer stp !";

		final ACLMessage msg = this.myAgent.receive(msgTemplate);
		ExploMultiAgentMessageContent msgContent;
		if (msg != null) {
			try {
				msgContent = (ExploMultiAgentMessageContent)msg.getContentObject();
			} catch (UnreadableException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				return;
			}
		}
		else { return; }
		MapMessage senderMapMessage = msgContent.getMap();
		((AbstractExploreMultiAgent)this.myAgent).mergeMap(senderMapMessage);
		boolean noNeighbours = false;
		if (!msgContent.isGiveMeWay()) {
			String senderCurrentNode = msgContent.getCurrentPosition(), senderNextNode = msgContent.getNextPosition(),
				   myCurrentNode = ((AbstractDedaleAgent)this.myAgent).getCurrentPosition(),
				   myNextNode = ((AbstractExploreMultiAgent)this.myAgent).getNextNode();
			// System.out.println(senderCurrentNode + ',' + myNextNode + ',' + senderNextNode + ',' + myCurrentNode);
			// System.out.println(senderCurrentNode.equals(myNextNode) && senderNextNode.equals(myCurrentNode));
			if (senderCurrentNode.equals(myNextNode) && senderNextNode.equals(myCurrentNode) && !myCurrentNode.equals(myNextNode)) {
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
				
				String neighbourRandom = "";
				Integer neighbourRandomIndex = 0;
				while (true) {
					neighbourRandomIndex = new Random().nextInt(currentNodeOpenNeighbours.size());
					neighbourRandom = currentNodeOpenNeighbours.get(neighbourRandomIndex);
					if (!neighbourRandom.equals(senderCurrentNode) || (currentNodeOpenNeighbours.size() + currentNodeClosedNeighbours.size()) == 1) { break; }
				}
				if (neighbourRandom.equals(senderCurrentNode)) {
					while (true) {
						neighbourRandomIndex = new Random().nextInt(currentNodeClosedNeighbours.size());
						neighbourRandom = currentNodeClosedNeighbours.get(neighbourRandomIndex);
						if (!neighbourRandom.equals(senderCurrentNode) || (currentNodeOpenNeighbours.size() + currentNodeClosedNeighbours.size()) == 1) { break; }
					}
					
				}
				if (!neighbourRandom.equals(senderCurrentNode)) {
					((AbstractDedaleAgent)this.myAgent).moveTo(neighbourRandom);
				}
				else { noNeighbours = true; }
				
			}
			if ((senderCurrentNode.equals(senderNextNode) && senderCurrentNode.equals(myNextNode) && !myCurrentNode.equals(myNextNode)) || noNeighbours) {
				System.out.println("Agent " + this.myAgent.getLocalName() + " : " + giveMeWayString);
				ACLMessage msgGiveMeWay = new ACLMessage(ACLMessage.INFORM);
				msgGiveMeWay.setSender(this.myAgent.getAID());
				msgGiveMeWay.setProtocol("UselessProtocol");

				MapMessage myMap = ((AbstractExploreMultiAgent)this.myAgent).getMyMap();
				try {
					msgGiveMeWay.setContentObject(
							new ExploMultiAgentMessageContent(myAgent.getLocalName(), myCurrentNode, myNextNode, myMap, true));
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				msgGiveMeWay.addReceiver(msg.getSender());

				((AbstractDedaleAgent)this.myAgent).sendMessage(msgGiveMeWay);
			}
		}
		else {
			String senderCurrentNode = msgContent.getCurrentPosition();
			String myCurrentNode = ((AbstractDedaleAgent)this.myAgent).getCurrentPosition();
			boolean done = false;
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
			
			String neighbourRandom = "";
			Integer neighbourRandomIndex = 0;
			while (true) {
				neighbourRandomIndex = new Random().nextInt(currentNodeOpenNeighbours.size());
				neighbourRandom = currentNodeOpenNeighbours.get(neighbourRandomIndex);
				if (!neighbourRandom.equals(senderCurrentNode) || (currentNodeOpenNeighbours.size() + currentNodeClosedNeighbours.size()) == 1) { break; }
			}
			if (neighbourRandom.equals(senderCurrentNode)) {
				while (true) {
					neighbourRandomIndex = new Random().nextInt(currentNodeClosedNeighbours.size());
					neighbourRandom = currentNodeClosedNeighbours.get(neighbourRandomIndex);
					if (!neighbourRandom.equals(senderCurrentNode) || (currentNodeOpenNeighbours.size() + currentNodeClosedNeighbours.size()) == 1) { break; }
				}
				
			}
			if (!neighbourRandom.equals(senderCurrentNode)) {
				((AbstractDedaleAgent)this.myAgent).moveTo(neighbourRandom);
				done = true;
			}
			else { noNeighbours = true; }

			if (done) {
				System.out.println("Vas-y !");
			}
			else {
				System.out.println("J'arrive pas :(");
			}
			((AbstractExploreMultiAgent)this.myAgent).setNextNode(((AbstractDedaleAgent)this.myAgent).getCurrentPosition());
		}
		
	}
}
