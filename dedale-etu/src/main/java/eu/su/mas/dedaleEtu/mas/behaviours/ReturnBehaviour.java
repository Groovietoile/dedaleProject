package eu.su.mas.dedaleEtu.mas.behaviours;

import java.io.Serializable;
import java.util.List;
import java.util.ArrayList;

import eu.su.mas.dedale.mas.AbstractDedaleAgent;
import eu.su.mas.dedaleEtu.mas.agents.dummies.AbstractExploreMultiAgent;
import eu.su.mas.dedaleEtu.mas.agents.dummies.HunterAgent;
import eu.su.mas.dedaleEtu.mas.agents.dummies.HunterAgent.AgentRole;
import eu.su.mas.dedaleEtu.mas.knowledge.ExploMultiAgentMessageContent;
import jade.core.Agent;
import jade.core.behaviours.SimpleBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;

public class ReturnBehaviour extends SimpleBehaviour {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1392591852144164893L;
	private boolean finished;
	private String center;
	private List<String> path;
	private List<String> centerNeighbours;
	public static final String demandSubTree = "OK DONNE-MOI SOUS PARTIE ALORS !";

	public ReturnBehaviour(final AbstractDedaleAgent myAgent) {
		super(myAgent);
		this.finished = false;
		this.center = null;
		this.path = null;
	}

	@Override
	public void action() {
		try {
			if (!((HunterAgent)this.myAgent).isReturning()) { return; }
		}
		catch(Exception e) {
			if (!e.getClass().getName().equals("java.lang.ClassCastException"))
				System.out.println(e.getMessage());
		}

		if (this.center == null)
			this.center = ((AbstractExploreMultiAgent)this.myAgent).getMyMap().getCenter();

		final MessageTemplate msgTemplate = MessageTemplate.MatchPerformative(ACLMessage.INFORM);
		final ACLMessage msg = this.myAgent.receive(msgTemplate);
		if (msg != null) {
			try {
				Serializable msgContent = msg.getContentObject();
				boolean isContentList = false;
				List<Integer> contentList = new ArrayList<Integer>();
				try {
					contentList = (ArrayList<Integer>) msgContent;
					isContentList = true;
				}
				catch (Exception e) {}
				if (!isContentList  && msgContent.equals(WaitingBehaviour.iAmWaiting)) {
					ACLMessage msgDemandSubTree = new ACLMessage(ACLMessage.REQUEST);
					msgDemandSubTree.setSender(this.myAgent.getAID());
					msgDemandSubTree.setProtocol("UselessProtocol");
					msgDemandSubTree.setContent(demandSubTree);
					msgDemandSubTree.addReceiver(msg.getSender());
					((AbstractDedaleAgent)this.myAgent).sendMessage(msgDemandSubTree);
				}
				else if (isContentList) {
					List<Integer> indicesPatrolling = contentList;
					((HunterAgent)this.myAgent).setIndicesPatrolling(indicesPatrolling);
					((HunterAgent)this.myAgent).setRole(AgentRole.patrolling);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		String currentPosition = ((AbstractDedaleAgent)this.myAgent).getCurrentPosition();
		if (currentPosition.equals(this.center)) {
			finished = true;
			((HunterAgent)this.myAgent).setRole(AgentRole.waiting);
			return;
		}
		if (this.path == null) {
			this.path = ((AbstractExploreMultiAgent)this.myAgent).getMyMap().getShortestPath(currentPosition, this.center);
			((AbstractExploreMultiAgent)this.myAgent).setNextNode(this.path.size() != 0 ? this.path.get(0) : currentPosition);
		}
		if (this.path.size() > 0) {
			try {
				this.myAgent.doWait(500);
				((AbstractDedaleAgent)this.myAgent).moveTo(this.path.get(0));
				this.path.remove(0);
				((AbstractExploreMultiAgent)this.myAgent).setNextNode(this.path.size() != 0 ? this.path.get(0) : currentPosition);
			}
			catch (Exception e) {
				this.path = ((AbstractExploreMultiAgent)this.myAgent).getMyMap().getShortestPath(currentPosition, this.center);
				((AbstractExploreMultiAgent)this.myAgent).setNextNode(this.path.size() != 0 ? this.path.get(0) : currentPosition);
				System.out.println(currentPosition);
				System.out.println(path);
				e.printStackTrace();
			}
		}
		
	}
	

	@Override
	public boolean done() {
		// TODO Auto-generated method stub
		return finished;
	}

}
