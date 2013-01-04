package models;

public enum IssueProcessingState {

	/**
	 * Der Issue ist noch keinem Bearbeiter zugeordnet.
	 */
	OPEN("open"),

	/**
	 * Der Issue ist einem Berabeiter zugeordnet.
	 */
	CLAIMED("claimed"),

	/**
	 * Die Bearbeitung des Issue wurde abgeschlossen.
	 */
	CLOSED("closed");

	private final String description;

	private IssueProcessingState(String description) {
		this.description = description;
	}

	public String getDescription() {
		return description;
	}



}
