package controllers;

import play.mvc.Call;


/**
 * Uris for the templates. Dynamic parts of the templates could be provided by different implementations.
 *
 * TODO: could be replaced by a simple map like in rails. Smaller footprint but not really ideomatic java.
 */
public interface Uris {
	
	Call getCloseIssuesUri();

	Call getClosingProcessUri();
	
	Call getUriForIssue(int id);

	Call getAssignIssueUri();

	Call getUnassignIssueUri();

	Call getOverviewUriForState(IssuesOverviewState state);
	
	Call getUpdateIssueUri(int id);

    boolean isFilterable();
}
