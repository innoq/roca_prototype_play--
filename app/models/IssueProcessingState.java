package models;

public enum IssueProcessingState {

	/**
	 * Der Issue ist noch keinem Bearbeiter zugeordnet.
	 */
	OPEN("offen"),

	/**
	 * Der Issue ist einem Berabeiter zugeordnet.
	 */
	CLAIMED("zugeordnet"),

	/**
	 * Die Bearbeitung des Issue wurde abgeschlossen.
	 */
	CLOSED("abgeschlossen");

	private final String description;

	private IssueProcessingState(String description) {
		this.description = description;
	}

	public String getDescription() {
		return description;
	}

}
