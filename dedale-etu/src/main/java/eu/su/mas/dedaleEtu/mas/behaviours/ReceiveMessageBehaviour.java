package eu.su.mas.dedaleEtu.mas.behaviours;

import java.util.Iterator;

import org.graphstream.graph.Node;

import eu.su.mas.dedale.mas.AbstractDedaleAgent;
import eu.su.mas.dedaleEtu.mas.agents.dummies.NotDummyAgent;
import eu.su.mas.dedaleEtu.mas.knowledge.MapRepresentation;
import eu.su.mas.dedaleEtu.mas.knowledge.MessageExploMulti;
import eu.su.mas.dedaleEtu.mas.knowledge.MapRepresentation.MapAttribute;
import jade.core.Agent;
import jade.core.behaviours.SimpleBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;
import org.graphstream.graph.Graph;

public class ReceiveMessageBehaviour extends SimpleBehaviour{

	private static final long serialVersionUID = 9088209402507795289L;

	private boolean finished=false;
	private MapRepresentation map;

	/**
	 * 
	 * This behaviour is a one Shot.
	 * It receives a message tagged with an inform performative, print the content in the console and destroy itlself
	 * @param myagent
	 */
	public ReceiveMessageBehaviour(final NotDummyAgent myagent, MapRepresentation map) {
		super(myagent);
		this.map = map;

	}


	public void action() {
		//1) receive the message
		final MessageTemplate msgTemplate = MessageTemplate.MatchPerformative(ACLMessage.INFORM);			

		final ACLMessage msg = this.myAgent.receive(msgTemplate);
		
		if (msg != null) {
			//fusionner les cartes
			try {
				MessageExploMulti message = (MessageExploMulti) msg.getContentObject();
				//récupérer tt les noeuds visités = open
				Graph graphAmi = message.getMap().getG();
				Iterator<Node> iter = graphAmi.iterator();
				while(iter.hasNext()){
					Node n=iter.next();
					//if map.getG().getNode(arg0)
					//sg.addNode(n.getId(),(MapAttribute)n.getAttribute("ui.class"));
				}
					
			} catch (UnreadableException e) {
				e.printStackTrace();
			}
			//
//			System.out.println(this.myAgent.getLocalName()+"<----Result received from "+msg.getSender().getLocalName()+" ,content= "+msg.getContent());
			
		}	
//			block();// the behaviour goes to sleep until the arrival of a new message in the agent's Inbox.
		this.myAgent.addBehaviour(new ExploMultiBehaviour((AbstractDedaleAgent) this.myAgent,this.map));
		this.finished=true;
	}

	public boolean done() {
		return finished;
	}

}
