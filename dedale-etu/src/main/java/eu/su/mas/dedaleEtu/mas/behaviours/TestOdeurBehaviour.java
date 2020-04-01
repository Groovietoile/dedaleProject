package eu.su.mas.dedaleEtu.mas.behaviours;

import java.util.ArrayList;
import java.util.List;

import dataStructures.tuple.Couple;
import eu.su.mas.dedale.env.Observation;
import eu.su.mas.dedale.mas.AbstractDedaleAgent;
import jade.core.Agent;
import jade.core.behaviours.SimpleBehaviour;

public class TestOdeurBehaviour extends SimpleBehaviour {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4215004301064182432L;
	
	public TestOdeurBehaviour(final AbstractDedaleAgent myagent) {
		super(myagent);
	}

	@Override
	public void action() {
		boolean stench_existance = false;
		List<Couple<String, List<Couple<Observation, Integer>>>> obsRes =  ((AbstractDedaleAgent)this.myAgent).observe();
		for (Couple<String, List<Couple<Observation, Integer>>> obsInPos : obsRes) {
			for (Couple<Observation, Integer> eachObsInPos : obsInPos.getRight()) {
				if (eachObsInPos.getLeft() == Observation.STENCH) {
					stench_existance = true;
					break;
				}
			}
			if (stench_existance) { break; }
		}
		
		if (stench_existance) {
			System.out.println("AGENT " + this.myAgent.getLocalName() + obsRes);
		}
	}

	@Override
	public boolean done() {
		return false;
	}

}
