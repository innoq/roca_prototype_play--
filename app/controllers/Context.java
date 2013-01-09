package controllers;

import play.mvc.Call;

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
