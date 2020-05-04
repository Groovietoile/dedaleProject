package eu.su.mas.dedaleEtu.mas.behaviours;

import java.util.ArrayList;

import eu.su.mas.dedale.mas.AbstractDedaleAgent;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.SimpleBehaviour;
import jade.lang.acl.ACLMessage;

/**
 * agent qui envoie un message à ses voisins lorsqu'il détecte une odeur/un golem
 */
public class AskForHelp extends SimpleBehaviour{ //OneShotBehavior ?
	
	private boolean finished;
	
	public AskForHelp(final AbstractDedaleAgent myAgent) {
		super(myAgent);
		this.finished = false;
	}

	@Override
	public void action() {
		String myPosition=((AbstractDedaleAgent)this.myAgent).getCurrentPosition();
		
		ACLMessage msg=new ACLMessage(ACLMessage.PROPAGATE);//INFORM ?
		msg.setSender(this.myAgent.getAID());
		msg.setProtocol("Stench/Golem detected");
		
		ArrayList<AbstractDedaleAgent> liste;//récup liste des agents autour
		
		//msg.setContent(myPosition, instantT);
		
		//for each agent in liste:
		msg.addReceiver(new AID("Collect1",AID.ISLOCALNAME));
		
		((AbstractDedaleAgent)this.myAgent).sendMessage(msg);
	}

	@Override
	public boolean done() {
		return finished;
	}

}
