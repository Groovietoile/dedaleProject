package eu.su.mas.dedaleEtu.mas.knowledge;
import java.io.Serializable;

public class MessageExploMulti implements Serializable {

	private static final long serialVersionUID = -2090891110181684799L;
	private String position;
	private MapRepresentation map;
	
	public MessageExploMulti(String position, MapRepresentation map) {
		this.position = position;
		this.map = map;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public MapRepresentation getMap() {
		return map;
	}

	public void setMap(MapRepresentation map) {
		this.map = map;
	}
	
	@Override
	public String toString() {
		return "Message : [position=" + this.position + ", map=" + this.map.toString() + "]";
	}
}
