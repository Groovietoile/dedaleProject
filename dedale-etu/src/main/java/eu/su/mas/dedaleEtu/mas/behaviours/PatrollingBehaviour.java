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
			if (this.mapToPatroll.getListeDesNoeuds().get("open").contains(this.center))
				this.mapToPatroll.getListeDesNoeuds().get("open").remove(this.center);
			if (!this.mapToPatroll.getListeDesNoeuds().get("closed").contains(this.center))
				this.mapToPatroll.getListeDesNoeuds().get("closed").add(this.center);
			recopyMap = false;
		}
		List<Integer> indicesPatrolling = ((HunterAgent)this.myAgent).getIndicesPatrolling();
		if (this.nodeToStart == null) {
			System.out.println(indicesPatrolling);
			System.out.println(this.centerNeighbours);
			try {
				// le cas où on a encore une sous-partie à patrouiller
				this.nodeToStart = this.centerNeighbours.get(indicesPatrolling.get(0));
				((HunterAgent)this.myAgent).getIndicesPatrolling().remove(0);
			} catch (Exception e) {
				// le cas où on a déjà patrouillé toutes les sous-parties demandées
				e.printStackTrace();
				((HunterAgent)this.myAgent).setRole(HunterAgent.AgentRole.returning);
			}
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
				// ((AbstractExploreMultiAgent)this.myAgent).setNextNode(((AbstractDedaleAgent)this.myAgent).getCurrentPosition());
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
			// On traite le cas où on a déjà atteint un point de départ et où on patrouille une sous-partie actuelle
			((AbstractDedaleAgent)this.myAgent).getCurrentPosition();
			if (this.mapToPatroll.getListeDesNoeuds().get("open").contains(((AbstractDedaleAgent)this.myAgent).getCurrentPosition()))
				this.mapToPatroll.getListeDesNoeuds().get("open").remove((Object)((AbstractDedaleAgent)this.myAgent).getCurrentPosition());
			if (!this.mapToPatroll.getListeDesNoeuds().get("closed").contains(((AbstractDedaleAgent)this.myAgent).getCurrentPosition()))
				this.mapToPatroll.getListeDesNoeuds().get("closed").add(((AbstractDedaleAgent)this.myAgent).getCurrentPosition());
			System.out.println(this.mapToPatroll.getListeDesNoeuds().get("open"));
			// System.out.println(this.mapToPatroll.getListeDesNoeuds().get("closed"));
			
			List<String> currentNeighbours = this.mapToPatroll.getNodeNeighbours(((AbstractDedaleAgent)this.myAgent).getCurrentPosition());
			this.nextNode = null;
			for (String neighbour: currentNeighbours) {
				// On essaie de trouver un voisin ouvert
				if (this.mapToPatroll.getListeDesNoeuds().get("open").contains(neighbour)) {
					this.nextNode = neighbour;
				}
			}
			if (this.nextNode == null) {
				// Si on n'a pas réussi à trouver un voisin ouvert on prend pour notre noeud suivant un sommet le plus proche parmi ceux ouverts
				Integer shortestPathLength = Integer.MAX_VALUE;
				for (String node: this.mapToPatroll.getListeDesNoeuds().get("open")) {
					List<String> path = this.mapToPatroll.getShortestPath(((AbstractDedaleAgent)this.myAgent).getCurrentPosition(), node);
					if (path.size() < shortestPathLength) {
						shortestPathLength = path.size();
						nextNode = path.get(0);
					}
				}
			}
			if (this.nextNode != null) {
				// On n'a pas encore fermé tous les noeuds alors on bouge vers un noeud ouvert suivant
				((AbstractExploreMultiAgent)this.myAgent).setNextNode(nextNode);
				this.myAgent.doWait(500);
				((AbstractDedaleAgent)this.myAgent).moveTo(nextNode);
			}
			else {
				// On a déjà fermé tous les noeuds ouverts et on recommence par une autre sous-partie
				this.nodeToStart = null;
				this.startReached = false;
				this.recopyMap = true;
			}
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
