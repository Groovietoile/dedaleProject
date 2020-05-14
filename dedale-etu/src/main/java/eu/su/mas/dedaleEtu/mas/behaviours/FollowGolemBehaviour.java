package eu.su.mas.dedaleEtu.mas.behaviours;

import java.util.ArrayList;
import java.util.List;

import dataStructures.tuple.Couple;
import eu.su.mas.dedale.env.Observation;
import eu.su.mas.dedale.mas.AbstractDedaleAgent;
import eu.su.mas.dedaleEtu.mas.agents.dummies.AbstractExploreMultiAgent;
import eu.su.mas.dedaleEtu.mas.agents.dummies.HunterAgent;
import eu.su.mas.dedaleEtu.mas.agents.dummies.HunterAgent.AgentRole;
import jade.core.behaviours.SimpleBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

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
		if (((HunterAgent)this.myAgent).isBlocking())
			return;
		
		final MessageTemplate msgTemplate = MessageTemplate.MatchPerformative(ACLMessage.REFUSE);
		final ACLMessage blockingMsg = this.myAgent.receive(msgTemplate);
		if (blockingMsg != null) {
			((HunterAgent)this.myAgent).getBlockingPositions().add(blockingMsg.getContent());
		}
		boolean stenchExistance = false;
		String posToMove = "";
		List<Couple<String, List<Couple<Observation, Integer>>>> obsRes = ((AbstractDedaleAgent)this.myAgent).observe();
		List<String> tmpPath = new ArrayList<String>();
		for (Couple<String, List<Couple<Observation, Integer>>> obsInPos : obsRes) {
			for (Couple<Observation, Integer> eachObsInPos : obsInPos.getRight()) {
				if (eachObsInPos.getLeft() == Observation.STENCH && !((HunterAgent)this.myAgent).getBlockingPositions().contains(obsInPos.getLeft())) {
					stenchExistance = true;
					// System.out.println(((AbstractDedaleAgent)this.myAgent).getCurrentPosition());
					// tmpPath = ((AbstractExploreMultiAgent)this.myAgent).getMyMap().getShortestPath(((AbstractDedaleAgent)this.myAgent).getCurrentPosition(), obsInPos.getLeft());
					// posToMove = tmpPath.size() == 0 ? ((AbstractDedaleAgent)this.myAgent).getCurrentPosition() : tmpPath.get(0);
					posToMove = obsInPos.getLeft();
					break;
				}
			}
			if (stenchExistance) { break; }
		}
		if (((HunterAgent)this.myAgent).getRole() != AgentRole.following)
			this.roleBefore = ((HunterAgent)this.myAgent).getRole();
		if (stenchExistance) {
//			System.out.println("AGENT " + this.myAgent.getLocalName() + obsRes);
//			System.out.println(posToMove);
			((HunterAgent)this.myAgent).setRole(AgentRole.following);
			((HunterAgent)this.myAgent).moveTo(posToMove);
			((HunterAgent)this.myAgent).setMeasureTime(true);
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
