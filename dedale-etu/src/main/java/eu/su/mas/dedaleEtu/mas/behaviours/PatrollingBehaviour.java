package eu.su.mas.dedaleEtu.mas.behaviours;

import java.util.List;

import eu.su.mas.dedale.mas.AbstractDedaleAgent;
import eu.su.mas.dedaleEtu.mas.agents.dummies.AbstractExploreMultiAgent;
import eu.su.mas.dedaleEtu.mas.agents.dummies.HunterAgent;
import eu.su.mas.dedaleEtu.mas.knowledge.MapMessage;
import jade.core.AID;
import jade.core.behaviours.SimpleBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class PatrollingBehaviour extends SimpleBehaviour {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2675105124699433260L;
	private boolean finished;
	private String center;
	private List<String> centerNeighbours;
	private MapMessage mapToPatroll;
	private boolean recopyMap;
	private String nextNode;
	private boolean startReached;
	private String nodeToStart;
	public static final String giveMeWay = "GIVE ME WAY PLS !";
	public static final String delim = ",";
	
	public PatrollingBehaviour(AbstractDedaleAgent myAgent) {
		super(myAgent);
		this.finished = false;
		this.center = null;
		this.centerNeighbours = null;
		this.mapToPatroll = null;
		this.recopyMap = true;
		this.nextNode = null;
		this.startReached = false;
		this.nodeToStart = null;
	}

	@Override
	public void action() {
		try {
			if (!((HunterAgent)this.myAgent).isPatrolling()) { return; }
		}
		catch(Exception e) {
			if (!e.getClass().getName().equals("java.lang.ClassCastException"))
				System.out.println(e.getMessage());
			else 
				e.printStackTrace();
		}
		if (this.center == null) {
			this.center = ((AbstractExploreMultiAgent)this.myAgent).getMyMap().getCenter();
			this.updateCenterNeighbours();
		}
		if (this.mapToPatroll == null) {
			this.mapToPatroll = new MapMessage();
			this.mapToPatroll.mergeWith(((AbstractExploreMultiAgent)this.myAgent).getMyMap());
		}
		if (recopyMap) {
			while (this.mapToPatroll.getListeDesNoeuds().get("closed").size() != 0) {
				String idNode = this.mapToPatroll.getListeDesNoeuds().get("closed").get(0);
				this.mapToPatroll.getListeDesNoeuds().get("closed").remove(0);
				this.mapToPatroll.getListeDesNoeuds().get("open").add(idNode);
			}
			recopyMap = false;
		}
		List<Integer> indicesPatrolling = ((HunterAgent)this.myAgent).getIndicesPatrolling();
		if (this.nodeToStart == null) {
			System.out.println(indicesPatrolling);
			System.out.println(this.centerNeighbours);
			this.nodeToStart = this.centerNeighbours.get(indicesPatrolling.get(0));
			((HunterAgent)this.myAgent).getIndicesPatrolling().remove(0);
		}
		if (!this.startReached) {
			// On traite le cas dans lequel on ne se situe pas encore dans un noeud de départ
//			System.out.println(this.myAgent.getLocalName());
//			System.out.println("Ma position actuelle est " + ((AbstractDedaleAgent)this.myAgent).getCurrentPosition() +
//					" mon point de départ est "  + this.nodeToStart);
			if (this.nodeToStart.equals(((AbstractDedaleAgent)this.myAgent).getCurrentPosition())) {
				// Si on est déjà dans un point de départ alors on a atteint évidemment ce point de départ
				this.startReached = true;
				this.nextNode = null;
				((AbstractExploreMultiAgent)this.myAgent).setNextNode(((AbstractDedaleAgent)this.myAgent).getCurrentPosition());
			}
			else {
				// Sinon on doit trouver un chemin vers le point de départ et aller au premier point de ce chemin
				this.nextNode = ((AbstractExploreMultiAgent)this.myAgent).getMyMap().getShortestPath(
						((AbstractDedaleAgent)this.myAgent).getCurrentPosition(), this.nodeToStart).get(0);
				((AbstractExploreMultiAgent)this.myAgent).setNextNode(this.nextNode);
				// System.out.println("Je voudrais accéder alors vers un noeud " + this.nextNode);
				
				ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
				msg.setSender(this.myAgent.getAID());
				msg.setProtocol("UselessProtocol");
				msg.setContent(giveMeWay + delim + ((AbstractDedaleAgent)this.myAgent).getCurrentPosition() +
 						delim + this.nextNode + delim + this.nodeToStart);
				for (String receiver : ((HunterAgent)this.myAgent).getListeAmis())
					msg.addReceiver(new AID(receiver, AID.ISLOCALNAME));
				((AbstractDedaleAgent)this.myAgent).sendMessage(msg);
				this.myAgent.doWait(500);
				((AbstractExploreMultiAgent)this.myAgent).moveTo(this.nextNode);
			}
		}
		else {
			finished = true;
		}
		
	}

	public String getCenter() {
		return center;
	}

	public void setCenter(String center) {
		this.center = center;
	}
	
	public void updateCenterNeighbours() {
		if (this.centerNeighbours == null)
			this.centerNeighbours = ((AbstractExploreMultiAgent)this.myAgent).getMyMap().getNodeNeighbours(this.center);
	}
	

	@Override
	public boolean done() {
		// TODO Auto-generated method stub
		return finished;
	}

}
