package eu.su.mas.dedaleEtu.mas.agents.dummies;

import java.util.ArrayList;
import java.util.List;

import eu.su.mas.dedale.mas.AbstractDedaleAgent;
import eu.su.mas.dedale.mas.agent.behaviours.startMyBehaviours;
import eu.su.mas.dedaleEtu.mas.behaviours.ExploMultiBehaviour;
import eu.su.mas.dedaleEtu.mas.behaviours.ExploSoloBehaviour;
import eu.su.mas.dedaleEtu.mas.behaviours.ReceiveInfoBehaviour;
import eu.su.mas.dedaleEtu.mas.behaviours.SendInfoBehaviour;
import eu.su.mas.dedaleEtu.mas.knowledge.MapMessage;
import eu.su.mas.dedaleEtu.mas.knowledge.MapRepresentation;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;

/**
 * <pre>
 * ExploreSolo agent. 
 * It explore the map using a DFS algorithm.
 * It stops when all nodes have been visited.
 *  </pre>
 *  
 * @author hc
 *
 */

public class ExploreMultiAgent extends AbstractExploreMultiAgent {

	private static final long serialVersionUID = -6431752665590433727L;
	private MapRepresentation myMap;
	private List<String> listeAmis;

	/**
	 * This method is automatically called when "agent".start() is executed.
	 * Consider that Agent is launched for the first time. 
	 * 			1) set the agent attributes 
	 *	 		2) add the behaviours
	 *          
	 */
	protected void setup(){

		super.setup();
		
		this.nextNode = null;
		List<Behaviour> lb=new ArrayList<Behaviour>();
		this.myMapMessage = new MapMessage();
		
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
		
		lb.add(new SendInfoBehaviour(this, this.listeAmis));
		lb.add(new ReceiveInfoBehaviour(this));
		lb.add(new ExploMultiBehaviour(this, this.myMap));
		
		
		/***
		 * MANDATORY TO ALLOW YOUR AGENT TO BE DEPLOYED CORRECTLY
		 */
		
		
		addBehaviour(new startMyBehaviours(this,lb));
		
		System.out.println("the  agent "+this.getLocalName()+ " is started");

	}
	
	
}
