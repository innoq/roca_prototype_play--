package models;

public enum ExecutionAction {

	/**
	 * Die Execution soll wieder neu eingestellt werden
	 */
	RETRY("retry execution"),

	/**
	 * Die Execution soll abgebrochen werden
	 */
	ABORT("abort execution"),
	/**
	 * Die Execution soll beendet werden
	 */
	COMPLETE("complete execution");

	private ExecutionAction(String description) {
	}


}
