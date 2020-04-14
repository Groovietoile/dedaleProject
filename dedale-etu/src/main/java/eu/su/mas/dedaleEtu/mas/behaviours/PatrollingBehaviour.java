package eu.su.mas.dedaleEtu.mas.behaviours;

import java.util.List;

import eu.su.mas.dedale.mas.AbstractDedaleAgent;
import eu.su.mas.dedaleEtu.mas.agents.dummies.AbstractExploreMultiAgent;
import eu.su.mas.dedaleEtu.mas.agents.dummies.HunterAgent;
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
	
	public PatrollingBehaviour(AbstractDedaleAgent myAgent) {
		super(myAgent);
		this.finished = false;
		this.center = ((AbstractExploreMultiAgent)this.myAgent).getMyMap().getCenter();
		this.centerNeighbours = null;
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
		Integer indexPatrolling = ((HunterAgent)this.myAgent).getIndexPatrolling();
		this.updateCenterNeighbours();
		System.out.println("BRANCH TO PATROLL: " + indexPatrolling);
		System.out.println("SO NODE TO START IS: " + this.centerNeighbours.get(indexPatrolling));
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
