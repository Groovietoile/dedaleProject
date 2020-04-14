package eu.su.mas.dedaleEtu.mas.behaviours;

import java.util.List;

import dataStructures.tuple.Couple;
import eu.su.mas.dedale.env.Observation;
import eu.su.mas.dedale.mas.AbstractDedaleAgent;
import eu.su.mas.dedaleEtu.mas.agents.dummies.HunterAgent;
import eu.su.mas.dedaleEtu.mas.agents.dummies.HunterAgent.AgentRole;
import jade.core.behaviours.SimpleBehaviour;

public class FollowGolemBehaviour extends SimpleBehaviour {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1113727399831216248L;
	private boolean finished;
	private AgentRole roleBefore;
	
	public FollowGolemBehaviour(final AbstractDedaleAgent myagent) {
		super(myagent);
		this.finished = false;
	}

	@Override
	public void action() {
		// TODO Auto-generated method stub
		boolean stenchExistance = false;
		String posToMove = "";
		List<Couple<String, List<Couple<Observation, Integer>>>> obsRes = ((AbstractDedaleAgent)this.myAgent).observe();
		for (Couple<String, List<Couple<Observation, Integer>>> obsInPos : obsRes) {
			for (Couple<Observation, Integer> eachObsInPos : obsInPos.getRight()) {
				if (eachObsInPos.getLeft() == Observation.STENCH) {
					stenchExistance = true;
					posToMove = obsInPos.getLeft();
					break;
				}
			}
			if (stenchExistance) { break; }
		}
		if (((HunterAgent)this.myAgent).getRole() != AgentRole.following)
			roleBefore = ((HunterAgent)this.myAgent).getRole();
		if (stenchExistance) {
//			System.out.println("AGENT " + this.myAgent.getLocalName() + obsRes);
//			System.out.println(posToMove);
			((HunterAgent)this.myAgent).setRole(AgentRole.following);
			((AbstractDedaleAgent) this.myAgent).moveTo(posToMove);
		}
		else {
			((HunterAgent) this.myAgent).setRole(roleBefore);
		}
		
	}

	@Override
	public boolean done() {
		// TODO Auto-generated method stub
		return finished;
	}

}