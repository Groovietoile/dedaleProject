package eu.su.mas.dedaleEtu.mas.agents.dummies;

import java.util.ArrayList;
import java.util.List;

import eu.su.mas.dedale.mas.AbstractDedaleAgent;
import eu.su.mas.dedale.mas.agent.behaviours.startMyBehaviours;
import eu.su.mas.dedaleEtu.mas.behaviours.ReceiveMessageBehaviour;
import eu.su.mas.dedaleEtu.mas.behaviours.SayBonjour;
import eu.su.mas.dedaleEtu.mas.knowledge.MapRepresentation;
import jade.core.behaviours.Behaviour;

public class NotDummyAgent extends AbstractDedaleAgent{

	private static final long serialVersionUID = -6298347865890122682L;
	private MapRepresentation myMap;
	private List<NotDummyAgent> listeAmis;
		
	/**
	 * This method is automatically called when "agent".start() is executed.
	 * Consider that Agent is launched for the first time. 
	 * 			1) set the agent attributes 
	 *	 		2) add the behaviours       
	 */
	protected void setup(){

		super.setup();
		listeAmis = new ArrayList<NotDummyAgent>();

		List<Behaviour> lb=new ArrayList<Behaviour>();
		
		/************************************************
		 * 
		 * ADD the behaviours of the Dummy Moving Agent
		 * 
		 ************************************************/
		lb.add(new SayBonjour(this, myMap));
		lb.add(new ReceiveMessageBehaviour(this, myMap));
//			lb.add(new SayHello(this));
		// lb.add(new ExploMultiBehaviour(this,this.myMap, this.listeAmis));
//			lb.add(new ExploMultiBehaviour(this,this.myMap, listeAmis));
		
		/***
		 * MANDATORY TO ALLOW YOUR AGENT TO BE DEPLOYED CORRECTLY
		 */
		
		
		addBehaviour(new startMyBehaviours(this,lb));
		
		System.out.println("the  agent "+this.getLocalName()+ " is started");

	}

	public MapRepresentation getMyMap() {
		return myMap;
	}

	public void setMyMap(MapRepresentation myMap) {
		this.myMap = myMap;
	}

	public List<NotDummyAgent> getListeAmis() {
		return listeAmis;
	}

	public void setListeAmis(List<NotDummyAgent> listeAmis) {
		this.listeAmis = listeAmis;
	}
}
