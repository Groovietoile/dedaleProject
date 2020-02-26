package eu.su.mas.dedaleEtu.mas.behaviours;

import java.io.IOException;

import eu.su.mas.dedale.mas.AbstractDedaleAgent;
import eu.su.mas.dedaleEtu.mas.agents.dummies.ExploreMultiAgent;
import eu.su.mas.dedaleEtu.mas.knowledge.ExploMultiAgentMessageContent;
import eu.su.mas.dedaleEtu.mas.knowledge.MapRepresentation;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;

public class ReceiveInfoBehaviour extends TickerBehaviour {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2401517043351791117L;

	public ReceiveInfoBehaviour(Agent myAgent) {
		super(myAgent, 3000);
	}

	@Override
	public void onTick() {
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
		if (!msgContent.isGiveMeWay()) {
			String senderCurrentNode = msgContent.getCurrentPosition(), senderNextNode = msgContent.getNextPosition(),
				   myCurrentNode = ((AbstractDedaleAgent)this.myAgent).getCurrentPosition(),
				   myNextNode = ((ExploreMultiAgent)this.myAgent).getNextNode();
			MapRepresentation senderMap = msgContent.getMap();
			// myAgent.mergeMap(senderMap);
			// System.out.println(senderCurrentNode + ',' + myNextNode + ',' + senderNextNode + ',' + myCurrentNode);
			// System.out.println(senderCurrentNode.equals(myNextNode) && senderNextNode.equals(myCurrentNode));
			if (senderCurrentNode.equals(myNextNode) && senderNextNode.equals(myCurrentNode) && !myCurrentNode.equals(myNextNode)) {
				String[] senderCurrentCoordsStr = senderCurrentNode.split("_"),
						 senderNextCoordsStr = senderNextNode.split("_"),
						 myCurrentCoordsStr = myCurrentNode.split("_"),
						 myNextCoordsStr = myNextNode.split("_");
				int [] senderCurrentCoords = {Integer.parseInt(senderCurrentCoordsStr[0]), Integer.parseInt(senderCurrentCoordsStr[1])},
					   senderNextCoords = {Integer.parseInt(senderNextCoordsStr[0]), Integer.parseInt(senderNextCoordsStr[1])},
					   myCurrentCoords = {Integer.parseInt(myCurrentCoordsStr[0]), Integer.parseInt(myCurrentCoordsStr[1])},
					   myNextCoords = {Integer.parseInt(myNextCoordsStr[0]), Integer.parseInt(myNextCoordsStr[1])};
				if (myCurrentCoords[1] == senderCurrentCoords[1] && myCurrentCoords[0] == senderCurrentCoords[0]+1) {
					try {
						this.myAgent.doWait(500); 
						((AbstractDedaleAgent)this.myAgent).moveTo(Integer.toString(myCurrentCoords[0]) + "_" + Integer.toString(myCurrentCoords[1]+1));
						this.myAgent.doWait(500);
						((AbstractDedaleAgent)this.myAgent).moveTo(Integer.toString(myCurrentCoords[0]+1) + "_" + Integer.toString(myCurrentCoords[1]+1));
						this.myAgent.doWait(500);
						System.out.println("Agent " + this.myAgent.getLocalName() + " : j'ai réussi à trouver un autre chemin !");
					}
					catch(RuntimeException e) {
						System.out.println("Agent " + this.myAgent.getLocalName() + " : OK, j'attends !");
					}
				}
				else if (myCurrentCoords[1] == senderCurrentCoords[1] && myCurrentCoords[0] == senderCurrentCoords[0]-1) {
					try {
						this.myAgent.doWait(500); 
						((AbstractDedaleAgent)this.myAgent).moveTo(Integer.toString(myCurrentCoords[0]) + "_" + Integer.toString(myCurrentCoords[1]-1));
						this.myAgent.doWait(500);
						((AbstractDedaleAgent)this.myAgent).moveTo(Integer.toString(myCurrentCoords[0]-1) + "_" + Integer.toString(myCurrentCoords[1]-1));
						this.myAgent.doWait(500);
						System.out.println("Agent " + this.myAgent.getLocalName() + " : j'ai réussi à trouver un autre chemin !");
					}
					catch(RuntimeException e) {
						System.out.println("Agent " + this.myAgent.getLocalName() + " : OK, j'attends!");
					}
				}
				else if (myCurrentCoords[0] == senderCurrentCoords[0] && myCurrentCoords[1] == senderCurrentCoords[1]-1) {
					try {
						this.myAgent.doWait(500); 
						((AbstractDedaleAgent)this.myAgent).moveTo(Integer.toString(myCurrentCoords[0]+1) + "_" + Integer.toString(myCurrentCoords[1]));
						this.myAgent.doWait(500);
						((AbstractDedaleAgent)this.myAgent).moveTo(Integer.toString(myCurrentCoords[0]+1) + "_" + Integer.toString(myCurrentCoords[1]-1));
						this.myAgent.doWait(500);
						System.out.println("Agent " + this.myAgent.getLocalName() + " : j'ai réussi à trouver un autre chemin !");
					}
					catch(RuntimeException e) {
						System.out.println("Agent " + this.myAgent.getLocalName() + " : OK, j'attends!");
					}
				}
				else if (myCurrentCoords[0] == senderCurrentCoords[0] && myCurrentCoords[1] == senderCurrentCoords[1]+1) {
					try {
						this.myAgent.doWait(500); 
						((AbstractDedaleAgent)this.myAgent).moveTo(Integer.toString(myCurrentCoords[0]-1) + "_" + Integer.toString(myCurrentCoords[1]));
						this.myAgent.doWait(500); 
						((AbstractDedaleAgent)this.myAgent).moveTo(Integer.toString(myCurrentCoords[0]-1) + "_" + Integer.toString(myCurrentCoords[1]+1));
						this.myAgent.doWait(500);
						System.out.println("Agent " + this.myAgent.getLocalName() + " : j'ai réussi à trouver un autre chemin !");
					}
					catch(RuntimeException e) {
						System.out.println("Agent " + this.myAgent.getLocalName() + " : OK, j'attends!");
					}
				}
			}
			if (senderCurrentNode.equals(senderNextNode) && senderCurrentNode.equals(myNextNode) && !myCurrentNode.equals(myNextNode)) {
				System.out.println("Agent " + this.myAgent.getLocalName() + " : " + giveMeWayString);
				ACLMessage msgGiveMeWay = new ACLMessage(ACLMessage.INFORM);
				msgGiveMeWay.setSender(this.myAgent.getAID());
				msgGiveMeWay.setProtocol("UselessProtocol");

				MapRepresentation myMap = ((ExploreMultiAgent)this.myAgent).getMyMap();
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
			String[] senderCurrentCoordsStr = senderCurrentNode.split("_"),
					 myCurrentCoordsStr = ((AbstractDedaleAgent)this.myAgent).getCurrentPosition().split("_");
			int [] senderCurrentCoords = {Integer.parseInt(senderCurrentCoordsStr[0]), Integer.parseInt(senderCurrentCoordsStr[1])},
				   myCurrentCoords = {Integer.parseInt(myCurrentCoordsStr[0]), Integer.parseInt(myCurrentCoordsStr[1])};
			boolean done = false;
			this.myAgent.doWait(500);
			if (myCurrentCoords[1]+1 != senderCurrentCoords[1] && !done) {
				try {
					((AbstractDedaleAgent)this.myAgent).moveTo(Integer.toString(myCurrentCoords[0]) + "_" + Integer.toString(myCurrentCoords[1]+1));
					done = true;
				}
				catch(RuntimeException e) {}
			}
			if (myCurrentCoords[0]+1 != senderCurrentCoords[0] && !done) {
				try {
					((AbstractDedaleAgent)this.myAgent).moveTo(Integer.toString(myCurrentCoords[0]+1) + "_" + Integer.toString(myCurrentCoords[1]));
					done = true;
				}
				catch(RuntimeException e) {}
			}
			if (myCurrentCoords[1]-1 != senderCurrentCoords[1] && !done) {
				try {
					((AbstractDedaleAgent)this.myAgent).moveTo(Integer.toString(myCurrentCoords[0]) + "_" + Integer.toString(myCurrentCoords[1]-1));
					done = true;
				}
				catch(RuntimeException e) {}
			}
			if (myCurrentCoords[0]-1 != senderCurrentCoords[0] && !done) {
				try {
					((AbstractDedaleAgent)this.myAgent).moveTo(Integer.toString(myCurrentCoords[0]-1) + "_" + Integer.toString(myCurrentCoords[1]));
					done = true;
				}
				catch(RuntimeException e) {}
			}
			if (done) {
				System.out.println("Vas-y !");
			}
			else {
				System.out.println("J'arrive pas :(");
			}
			((ExploreMultiAgent)this.myAgent).setNextNode(((AbstractDedaleAgent)this.myAgent).getCurrentPosition());
		}
		
	}

}
