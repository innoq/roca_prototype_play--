package controllers;

import play.mvc.Call;


/**
 * Context for the templates. Dynamic parts of the templates could be provided by different implementations.
 *
 * TODO: the state methods should belong to the state class.
 * TODO: could be replaced by a simple map like in rails. Smaller footprint but not really ideomatic java.
 */
public interface Context {
	
	Call getCloseIssuesUri();

	Call getClosingProcessUri();
	
	Call getUriForIssue(int id);

	Call getAssignIssueUri();

	Call getUnassignIssueUri();

	Call getOverviewUriForState(IssuesOverviewState state);
	
	Call getUpdateIssueUri(int id);
	
	boolean isStateAssignedOtherUser();

	boolean isStateAssignedCurrentUser();

	boolean isStateClosed();

	boolean isStateOpen();
	
	boolean isFilterable();

}
