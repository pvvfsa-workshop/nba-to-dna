package dataobjects;

public abstract class State {

	
	public State(String name, boolean isStarting, boolean isAccepting) {
		super();
		this.name = name;
		this.isStarting = isStarting;
		this.isAccepting = isAccepting;
	}
	
	private String name;
	private boolean isStarting;
	private boolean isAccepting;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public boolean isStarting() {
		return isStarting;
	}
	public void setStarting(boolean isStarting) {
		this.isStarting = isStarting;
	}
	public boolean isAccepting() {
		return isAccepting;
	}
	public void setAccepting(boolean isAccepting) {
		this.isAccepting = isAccepting;
	}
}
