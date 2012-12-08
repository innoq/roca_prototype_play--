package models;

public enum ExecutionAction {

	/**
	 * Die Execution soll wieder neu eingestellt werden
	 */
	RETRY("Ausfuehrung wiederholen"),

	/**
	 * Die Execution soll abgebrochen werden
	 */
	ABORT("Ausfuehrung abbrechen"),
	/**
	 * Die Execution soll beendet werden
	 */
	COMPLETE("Ausfuehrung beenden");

	private final String description;

	private ExecutionAction(String description) {
		this.description = description;
	}


}