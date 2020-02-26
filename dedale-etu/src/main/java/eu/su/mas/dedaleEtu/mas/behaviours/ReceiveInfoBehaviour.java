package eu.su.mas.dedaleEtu.mas.behaviours;

import eu.su.mas.dedale.mas.AbstractDedaleAgent;
import eu.su.mas.dedaleEtu.mas.agents.dummies.ExploreMultiAgent;
import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

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
		final String give_me_way_string = "Laisse-moi passer stp !";

		final ACLMessage msg = this.myAgent.receive(msgTemplate);
		if (msg != null && !msg.getContent().startsWith(give_me_way_string)) {
			String msgContent = msg.getContent();
			String[] msgContentSplitted = msgContent.split(",");
			String senderCurrentNode = msgContentSplitted[1], senderNextNode = msgContentSplitted[2],
				   myCurrentNode = ((AbstractDedaleAgent)this.myAgent).getCurrentPosition(),
				   myNextNode = ((ExploreMultiAgent)this.myAgent).getNextNode();
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
				System.out.println("Agent " + this.myAgent.getLocalName() + " : " + give_me_way_string);
				ACLMessage msg_give_me_way=new ACLMessage(ACLMessage.INFORM);
				msg_give_me_way.setSender(this.myAgent.getAID());
				msg_give_me_way.setProtocol("UselessProtocol");
				
				msg_give_me_way.setContent(give_me_way_string + "," + myCurrentNode);
				msg_give_me_way.addReceiver(msg.getSender());

				((AbstractDedaleAgent)this.myAgent).sendMessage(msg_give_me_way);
			}
		}
		else if (msg != null) {
			String senderCurrentNode = msg.getContent().split(",")[1];
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
