package eu.su.mas.dedaleEtu.mas.agents.dummies;

import java.util.ArrayList;
import java.util.List;

import eu.su.mas.dedale.mas.AbstractDedaleAgent;
import eu.su.mas.dedale.mas.agent.behaviours.startMyBehaviours;
import eu.su.mas.dedaleEtu.mas.behaviours.ExploMultiBehaviour;
import eu.su.mas.dedaleEtu.mas.behaviours.ExploSoloBehaviour;
import eu.su.mas.dedaleEtu.mas.behaviours.RandomWalkBehaviour;
import eu.su.mas.dedaleEtu.mas.behaviours.ReceiveInfoBehaviour;
import eu.su.mas.dedaleEtu.mas.behaviours.SayHello;
import eu.su.mas.dedaleEtu.mas.behaviours.SendInfoBehaviour;
import eu.su.mas.dedaleEtu.mas.behaviours.TestOdeurBehaviour;
import eu.su.mas.dedaleEtu.mas.knowledge.MapMessage;
import jade.core.behaviours.Behaviour;

public class HunterAgent extends AbstractDedaleAgent {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5053345286495358254L;
	private MapMessage myMapMessage;
	private List<String> listeAmis; 
	
	protected void setup() {

		super.setup();
		
		List<Behaviour> lb=new ArrayList<Behaviour>();
		this.setMyMapMessage(new MapMessage());
		
		/************************************************
		 * 
		 * ADD the behaviours of the Dummy Moving Agent
		 * 
		 ************************************************/
		
		this.listeAmis = new ArrayList<String>();
		String[] listeAmisPrevisionnelle = {"Explo1", "Explo2"};
		for (int i = 0; i < listeAmisPrevisionnelle.length; i++) {
			if (!listeAmisPrevisionnelle[i].equals(this.getLocalName()))
				this.listeAmis.add(listeAmisPrevisionnelle[i]);
		}
		
		lb.add(new TestOdeurBehaviour(this));
		
		/***
		 * MANDATORY TO ALLOW YOUR AGENT TO BE DEPLOYED CORRECTLY
		 */
		
		
		addBehaviour(new startMyBehaviours(this,lb));
		
		System.out.println("the  agent "+this.getLocalName()+ " is started");
	}

	public MapMessage getMyMapMessage() {
		return myMapMessage;
	}

	public void setMyMapMessage(MapMessage myMapMessage) {
		this.myMapMessage = myMapMessage;
	}
	
}
