package eu.su.mas.dedaleEtu.mas.behaviours;

import java.io.IOException;

import eu.su.mas.dedale.mas.AbstractDedaleAgent;
import eu.su.mas.dedaleEtu.mas.knowledge.MapRepresentation;
import eu.su.mas.dedaleEtu.mas.knowledge.MessageExploMulti;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.SimpleBehaviour;
import jade.lang.acl.ACLMessage;

public class SayBonjour extends SimpleBehaviour{


	private static final long serialVersionUID = -22704545015105895L;
	private boolean finished = false;
	private MapRepresentation myMap;

	public SayBonjour(final Agent myagent, MapRepresentation myMap) {
		super(myagent);
		this.myMap = myMap;
	}
	
	//les agents s'arrêtent
	//fusion des cartes
	@Override
	public void action() {
		String myPosition=((AbstractDedaleAgent)this.myAgent).getCurrentPosition();

		//A message is defined by : a performative, a sender, a set of receivers, (a protocol),(a content (and/or contentOBject))
		ACLMessage msg=new ACLMessage(ACLMessage.INFORM);
		msg.setSender(this.myAgent.getAID());
		msg.setProtocol("UselessProtocol");

		if (myPosition!=""){
			//System.out.println("Agent "+this.myAgent.getLocalName()+ " is trying to reach its friends");
			try {
				msg.setContentObject(new MessageExploMulti(myPosition, this.myMap));
				System.err.println("hello");

				msg.addReceiver(new AID("Explo1",AID.ISLOCALNAME));
				msg.addReceiver(new AID("Explo2",AID.ISLOCALNAME));

				((AbstractDedaleAgent)this.myAgent).sendMessage(msg);
				
			}
			catch (IOException e) {
				e.printStackTrace();
			}

		}
		finished = true;
	}

	@Override
	public boolean done() {
		return finished;
	}

}
