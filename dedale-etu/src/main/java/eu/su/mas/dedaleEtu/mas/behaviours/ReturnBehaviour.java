package eu.su.mas.dedaleEtu.mas.behaviours;

import java.util.List;

import eu.su.mas.dedale.mas.AbstractDedaleAgent;
import eu.su.mas.dedaleEtu.mas.agents.dummies.AbstractExploreMultiAgent;
import eu.su.mas.dedaleEtu.mas.agents.dummies.HunterAgent;
import eu.su.mas.dedaleEtu.mas.agents.dummies.HunterAgent.AgentRole;
import jade.core.Agent;
import jade.core.behaviours.SimpleBehaviour;

public class ReturnBehaviour extends SimpleBehaviour {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1392591852144164893L;
	private boolean finished;
	private String center;
	private List<String> path;

	public ReturnBehaviour(final AbstractDedaleAgent myAgent) {
		super(myAgent);
		this.finished = false;
		this.center = ((AbstractExploreMultiAgent)this.myAgent).getMyMap().getCenter();
		this.path = null;
	}

	@Override
	public void action() {
		// System.out.println(((HunterAgent)this.myAgent).getRole());
		try {
			if (!((HunterAgent)this.myAgent).isReturning()) { return; }
		}
		catch(Exception e) {
			if (!e.getClass().getName().equals("java.lang.ClassCastException"))
				System.out.println(e.getMessage());
		}
		String currentPosition = ((AbstractDedaleAgent)this.myAgent).getCurrentPosition();
		if (currentPosition.equals(this.center)) {
			finished = true;
			((HunterAgent)this.myAgent).setRole(AgentRole.waiting);
			return;
		}
		if (this.path == null)
			this.path = ((AbstractExploreMultiAgent)this.myAgent).getMyMap().getShortestPath(currentPosition, this.center);
		if (this.path.size() > 0) {
			try {
				((AbstractDedaleAgent)this.myAgent).moveTo(this.path.get(0));
				this.path.remove(0);
			}
			catch (RuntimeException e) {
				System.out.println(currentPosition);
				System.out.println(path);
			}
		}
		
	}

	@Override
	public boolean done() {
		// TODO Auto-generated method stub
		return finished;
	}

}
