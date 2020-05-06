package eu.su.mas.dedaleEtu.mas.behaviours;

import java.util.List;

import dataStructures.tuple.Couple;
import eu.su.mas.dedale.env.Observation;
import eu.su.mas.dedale.mas.AbstractDedaleAgent;
import eu.su.mas.dedaleEtu.mas.agents.dummies.HunterAgent;
import jade.core.Agent;
import jade.core.behaviours.SimpleBehaviour;

public class CheckBlockBehaviour extends SimpleBehaviour {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7262549181358315079L;
	private boolean finished;
	private final long timeLimit = 5000;

	public CheckBlockBehaviour(final AbstractDedaleAgent myagent) {
		super(myagent);
		this.finished = false;
	}

	@Override
	public void action() {
		if (!((HunterAgent)this.myAgent).isFollowing() || !((HunterAgent)this.myAgent).isMeasureTime())
			return;
		// System.out.println("AGENT : " + this.myAgent.getLocalName() + " ON EXECUTE?");
		long start = System.nanoTime(), end = System.nanoTime();
		String startPos = ((AbstractDedaleAgent)this.myAgent).getCurrentPosition(), curPos = "";
		List<Couple<String, List<Couple<Observation, Integer>>>> obsRes = ((AbstractDedaleAgent)this.myAgent).observe();
		boolean stenchExistance = false;
		String posToMove = "";

		while ((end - start) / 1000000 < timeLimit) {
			stenchExistance = false;
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
			// System.out.println((end - start) / 1000000);
			curPos = ((AbstractDedaleAgent)this.myAgent).getCurrentPosition();
			if (!stenchExistance || !curPos.equals(startPos) || !curPos.equals(posToMove)) {
				((HunterAgent)this.myAgent).setMeasureTime(false);
				return;
			}
//			try {
//				Thread.sleep(500);
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
			end = System.nanoTime();
		}
//		System.out.println("AGENT : " + this.myAgent.getLocalName() + " ; JE LE BLOQUE !");
		System.out.println("AGENT : " + this.myAgent.getLocalName() + "\n Current position : " + curPos + "\n Start position : " + startPos + "\n Next position : " + posToMove);
		((HunterAgent)this.myAgent).setRole(HunterAgent.AgentRole.blocking);
		((HunterAgent)this.myAgent).setMeasureTime(false);
	}

	@Override
	public boolean done() {
		return finished;
	}

}
