package eu.su.mas.dedaleEtu.mas.behaviours;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import dataStructures.tuple.Couple;
import eu.su.mas.dedale.env.Observation;
import eu.su.mas.dedale.mas.AbstractDedaleAgent;
import eu.su.mas.dedaleEtu.mas.agents.dummies.AbstractExploreMultiAgent;
import eu.su.mas.dedaleEtu.mas.agents.dummies.ExploreSoloAgent;
import eu.su.mas.dedaleEtu.mas.agents.dummies.HunterAgent;
import eu.su.mas.dedaleEtu.mas.agents.dummies.HunterAgent.AgentRole;
import eu.su.mas.dedaleEtu.mas.knowledge.MapRepresentation;
import eu.su.mas.dedaleEtu.mas.knowledge.MapRepresentation.MapAttribute;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.SimpleBehaviour;


public class ExploMultiBehaviour extends SimpleBehaviour {

	private static final long serialVersionUID = 8567689731496787661L;

	private boolean finished = false;

	/**
	 * Current knowledge of the agent regarding the environment
	 */
	private MapRepresentation myMap;

	/**
	 * Nodes known but not yet visited
	 */
	private List<String> openNodes;
	/**
	 * Visited nodes
	 */
	private Set<String> closedNodes;


	public ExploMultiBehaviour(final AbstractDedaleAgent myagent, MapRepresentation myMap) {
		super(myagent);
		this.myMap=myMap;
		this.openNodes=new ArrayList<String>();
		this.closedNodes=new HashSet<String>();
	}

	@Override
	public void action() {
		
		try {
			if (!((HunterAgent)this.myAgent).isExploring()) {
//				System.out.println("AGENT " + this.myAgent.getLocalName() + " J'AI TROUVÉ UN GOLEM !");
				this.myMap.mergeWith(((AbstractExploreMultiAgent)this.myAgent).getMyMap());
				return; 
			}
		}
		catch(Exception e) {
			if (!e.getClass().getName().equals("java.lang.ClassCastException")) {
				System.out.println(e.getMessage());
			}
		}

		if(this.myMap==null) {
			this.myMap= new MapRepresentation();
			((AbstractExploreMultiAgent)this.myAgent).getMyMap().getListeDesNoeuds().get("open").add(((AbstractDedaleAgent)this.myAgent).getCurrentPosition());
		}
		else {
			this.myMap.mergeWith(((AbstractExploreMultiAgent)this.myAgent).getMyMap());
			for (String nodeClosed: ((AbstractExploreMultiAgent)this.myAgent).getMyMap().getListeDesNoeuds().get("closed")) {
				if (!this.closedNodes.contains(nodeClosed)) {
					this.closedNodes.add(nodeClosed);
				}
				if (this.openNodes.contains(nodeClosed)) {
					this.openNodes.remove(nodeClosed);
				}
			}
			for (String nodeOpen: ((AbstractExploreMultiAgent)this.myAgent).getMyMap().getListeDesNoeuds().get("open")) {
				if (!this.openNodes.contains(nodeOpen) && !this.closedNodes.contains(nodeOpen)) {
					this.openNodes.add(nodeOpen);
				}
			}
		}
		
		//0) Retrieve the current position
		String myPosition=((AbstractDedaleAgent)this.myAgent).getCurrentPosition();
	
		if (myPosition!=null){
			//List of observable from the agent's current position
			List<Couple<String,List<Couple<Observation,Integer>>>> lobs=((AbstractDedaleAgent)this.myAgent).observe();//myPosition

			/**
			 * Just added here to let you see what the agent is doing, otherwise he will be too quick
			 */
//			try {
//				this.myAgent.doWait(500);
//			} catch (Exception e) {
//				e.printStackTrace();
//			}

			//1) remove the current node from openlist and add it to closedNodes.
			this.closedNodes.add(myPosition);
			this.openNodes.remove(myPosition);

			this.myMap.addNode(myPosition,MapAttribute.closed);

			//2) get the surrounding nodes and, if not in closedNodes, add them to open nodes.
			String nextNode=null;
			Iterator<Couple<String, List<Couple<Observation, Integer>>>> iter=lobs.iterator();
			while(iter.hasNext()){
				String nodeId=iter.next().getLeft();
				if (!this.closedNodes.contains(nodeId)){
					if (!this.openNodes.contains(nodeId)){
						this.openNodes.add(nodeId);
						this.myMap.addNode(nodeId, MapAttribute.open);
						this.myMap.addEdge(myPosition, nodeId);	
					}else{
						//the node exist, but not necessarily the edge
						this.myMap.addEdge(myPosition, nodeId);
					}
					if (nextNode==null) nextNode=nodeId;
				}
			}

			//3) while openNodes is not empty, continues.
			if (this.openNodes.isEmpty()){
				//Explo finished
				finished=true;
				System.out.println("Exploration successufully done, behaviour removed.");
				try {
					((HunterAgent)this.myAgent).setRole(AgentRole.returning);
				}
				catch(Exception e) {
					if (!e.getClass().getName().equals("java.lang.ClassCastException")) {
						System.out.println(e.getMessage());
					}
				}
			}else{
				//4) select next move.
				//4.1 If there exist one open node directly reachable, go for it,
				//	 otherwise choose one from the openNode list, compute the shortestPath and go for it
				if (nextNode==null){
					//no directly accessible openNode
					//chose one, compute the path and take the first step.
					nextNode=this.myMap.getShortestPath(myPosition, this.openNodes.get(0)).get(0);
				}
				
				
				
				/***************************************************
				** 		ADDING the API CALL to illustrate their use **
				*****************************************************/

				//list of observations associated to the currentPosition
				List<Couple<Observation,Integer>> lObservations= lobs.get(0).getRight();
				// System.out.println(this.myAgent.getLocalName()+" - State of the observations : "+lobs);
				
				//example related to the use of the backpack for the treasure hunt
				Boolean b=false;
				for(Couple<Observation,Integer> o:lObservations){
					switch (o.getLeft()) {
					case DIAMOND:case GOLD:

						System.out.println(this.myAgent.getLocalName()+" - My treasure type is : "+((AbstractDedaleAgent) this.myAgent).getMyTreasureType());
						System.out.println(this.myAgent.getLocalName()+" - My current backpack capacity is:"+ ((AbstractDedaleAgent) this.myAgent).getBackPackFreeSpace());
						System.out.println(this.myAgent.getLocalName()+" - My expertise is: "+((AbstractDedaleAgent) this.myAgent).getMyExpertise());
						System.out.println(this.myAgent.getLocalName()+" - I try to open the safe: "+((AbstractDedaleAgent) this.myAgent).openLock(Observation.GOLD));
						System.out.println(this.myAgent.getLocalName()+" - Value of the treasure on the current position: "+o.getLeft() +": "+ o.getRight());
						System.out.println(this.myAgent.getLocalName()+" - The agent grabbed : "+((AbstractDedaleAgent) this.myAgent).pick());
						System.out.println(this.myAgent.getLocalName()+" - the remaining backpack capacity is: "+ ((AbstractDedaleAgent) this.myAgent).getBackPackFreeSpace());
						b=true;
						break;
					default:
						break;
					}
				}

				//If the agent picked (part of) the treasure
				if (b){
					List<Couple<String,List<Couple<Observation,Integer>>>> lobs2=((AbstractDedaleAgent)this.myAgent).observe();//myPosition
					System.out.println(this.myAgent.getLocalName()+" - State of the observations after picking "+lobs2);
					
					//Trying to store everything in the tanker
					System.out.println(this.myAgent.getLocalName()+" - My current backpack capacity is:"+ ((AbstractDedaleAgent)this.myAgent).getBackPackFreeSpace());
					System.out.println(this.myAgent.getLocalName()+" - The agent tries to transfer is load into the Silo (if reachable); succes ? : "+((AbstractDedaleAgent)this.myAgent).emptyMyBackPack("Silo"));
					System.out.println(this.myAgent.getLocalName()+" - My current backpack capacity is:"+ ((AbstractDedaleAgent)this.myAgent).getBackPackFreeSpace());
					
				}
				
				//Trying to store everything in the tanker
				//System.out.println(this.myAgent.getLocalName()+" - My current backpack capacity is:"+ ((AbstractDedaleAgent)this.myAgent).getBackPackFreeSpace());
				//System.out.println(this.myAgent.getLocalName()+" - The agent tries to transfer is load into the Silo (if reachable); succes ? : "+((AbstractDedaleAgent)this.myAgent).emptyMyBackPack("Silo"));
				//System.out.println(this.myAgent.getLocalName()+" - My current backpack capacity is:"+ ((AbstractDedaleAgent)this.myAgent).getBackPackFreeSpace());


				/************************************************
				 * 				END API CALL ILUSTRATION
				 *************************************************/
				// System.out.println("JE BOUGE");
				if (((AbstractExploreMultiAgent)this.myAgent).getMyMap().getListeDesNoeuds().get("open").size() > 0) {
					((AbstractDedaleAgent)this.myAgent).moveTo(nextNode);
					((AbstractExploreMultiAgent)this.myAgent).setNextNode(nextNode);
	//				System.out.println(myMap.toString());
					((AbstractExploreMultiAgent)this.myAgent).mergeMap(this.myMap);
				}
				else {
					this.myMap.mergeWith(((AbstractExploreMultiAgent)this.myAgent).getMyMap());
					finished = true;
					((HunterAgent)this.myAgent).setRole(AgentRole.returning);
				}

			}

		}
	}

	@Override
	public boolean done() {
		return finished;
	}

}

