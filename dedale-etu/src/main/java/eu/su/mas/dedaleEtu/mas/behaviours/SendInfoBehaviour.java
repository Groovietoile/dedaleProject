package eu.su.mas.dedaleEtu.mas.behaviours;

import java.io.IOException;
import java.util.List;

import eu.su.mas.dedale.mas.AbstractDedaleAgent;
import eu.su.mas.dedaleEtu.mas.agents.dummies.AbstractExploreMultiAgent;
import eu.su.mas.dedaleEtu.mas.agents.dummies.HunterAgent;
import eu.su.mas.dedaleEtu.mas.knowledge.ExploMultiAgentMessageContent;
import eu.su.mas.dedaleEtu.mas.knowledge.MapMessage;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;

/**
 * This example behaviour try to send a hello message (every 3s maximum) to agents Collect2 Collect1
 * @author hc
 *
 */
public class SendInfoBehaviour extends TickerBehaviour{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2058134622078521998L;
	private List<String> receivers;

	/**
	 * An agent tries to contact its friend and to give him its current position
	 * @param myagent the agent who posses the behaviour
	 *  
	 */
	public SendInfoBehaviour (final Agent myagent, List<String> receivers) {
		super(myagent, 3000);
		this.receivers = receivers;
		//super(myagent);
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

		String myPosition=((AbstractDedaleAgent)this.myAgent).getCurrentPosition();
		String myNextNode = ((AbstractExploreMultiAgent)this.myAgent).getNextNode();
		MapMessage mapMsg = ((AbstractExploreMultiAgent)this.myAgent).getMyMap();
		
		//A message is defined by : a performative, a sender, a set of receivers, (a protocol),(a content (and/or contentOBject))
		ACLMessage msg=new ACLMessage(ACLMessage.INFORM);
		msg.setSender(this.myAgent.getAID());
		msg.setProtocol("UselessProtocol");
		
//		if (myMap == null) {
//			System.out.println("myMap == null !");
//		}

		if (myPosition!="") {
			
//			myMap.prepareMigration();
			ExploMultiAgentMessageContent msgContent = new ExploMultiAgentMessageContent(myAgent.getLocalName(), myPosition, myNextNode, mapMsg, false);
			//System.out.println("Agent "+this.myAgent.getLocalName()+ " is trying to reach its friends");
			try {
				msg.setContentObject(msgContent);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			for (String receiver : this.receivers) {
				msg.addReceiver(new AID(receiver, AID.ISLOCALNAME));
			}

			//Mandatory to use this method (it takes into account the environment to decide if someone is reachable or not)
			((AbstractDedaleAgent)this.myAgent).sendMessage(msg);
			// System.out.println("Current : " + myPosition + " Next : " + myNextNode);
		}
	}
}