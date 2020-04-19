package eu.su.mas.dedaleEtu.mas.behaviours;

import java.util.List;

import eu.su.mas.dedale.mas.AbstractDedaleAgent;
import eu.su.mas.dedaleEtu.mas.agents.dummies.AbstractExploreMultiAgent;
import eu.su.mas.dedaleEtu.mas.agents.dummies.HunterAgent;
import eu.su.mas.dedaleEtu.mas.knowledge.MapMessage;
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
	
	public PatrollingBehaviour(AbstractDedaleAgent myAgent) {
		super(myAgent);
		this.finished = false;
		this.center = null;
		this.centerNeighbours = null;
		this.mapToPatroll = null;
		this.recopyMap = true;
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
		System.out.println("CLOSED");
		System.out.println(this.mapToPatroll.getListeDesNoeuds().get("closed"));
		System.out.println("OPEN");
		System.out.println(this.mapToPatroll.getListeDesNoeuds().get("open"));
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
