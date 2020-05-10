package eu.su.mas.dedaleEtu.mas.behaviours;

import java.util.ArrayList;
import java.util.List;

import dataStructures.tuple.Couple;
import eu.su.mas.dedale.env.Observation;
import eu.su.mas.dedale.mas.AbstractDedaleAgent;
import eu.su.mas.dedaleEtu.mas.agents.dummies.HunterAgent;
import jade.core.Agent;
import jade.core.behaviours.SimpleBehaviour;

public class BlockGolemBehaviour extends SimpleBehaviour {


	/**
	 * 
	 */
	private static final long serialVersionUID = 4518807690708981686L;
	private boolean finished = false;
	private List<String> blockedGolemsPositions;
	private final long timeLimit = 5000;

	public BlockGolemBehaviour(AbstractDedaleAgent myagent) {
		super(myagent);
		this.blockedGolemsPositions = null;
	}

	@Override
	public void action() {
		if (!((HunterAgent)this.myAgent).isBlocking())
			return;
		if (this.blockedGolemsPositions == null) {
			this.blockedGolemsPositions = new ArrayList<String>();
			List<String> candidats = new ArrayList<String>(), candidatsOld = new ArrayList<String>(), candidatsBad = new ArrayList<String>();
			long start = System.nanoTime(), end = System.nanoTime();
			while ((end - start) / 1000000 < this.timeLimit) {
				candidatsOld.clear();
				for (String c: candidats)
					candidatsOld.add(c);
				candidats.clear();
				List<Couple<String, List<Couple<Observation, Integer>>>> obsRes = ((AbstractDedaleAgent)this.myAgent).observe();
				for (Couple<String, List<Couple<Observation, Integer>>> obsInPos : obsRes)
					for (Couple<Observation, Integer> eachObsInPos : obsInPos.getRight())
						if (eachObsInPos.getLeft() == Observation.STENCH)
							candidats.add(obsInPos.getLeft());
				String currentPosition = ((AbstractDedaleAgent)this.myAgent).getCurrentPosition();
				candidats.remove(currentPosition);
				for (String co: candidatsOld)
					if (!candidats.contains(co))
						candidatsBad.add(co);
				end = System.nanoTime();
			}
			for (String c: candidats)
				if (!candidatsBad.contains(c))
					this.blockedGolemsPositions.add(c);
		}
		System.out.println("AGENT : " + this.myAgent.getLocalName() + " ; JE LE BLOQUE ! DANS DES POSITIONS :");
		System.out.println(this.blockedGolemsPositions);
	}

	@Override
	public boolean done() {
		// TODO Auto-generated method stub
		return finished;
	}

}
