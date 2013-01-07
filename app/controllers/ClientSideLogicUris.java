package controllers;

import actions.AuthenticationAction;
import play.mvc.Call;

public class ClientSideLogicUris implements Uris {


	@Override
	public Call getClosingProcessUri() {
		return routes.ClientSideIssues.issuesClosing();
	}

	@Override
	public Call getAssignIssueUri() {
		return routes.ClientSideIssues.assignIssueToUser(AuthenticationAction.getCurrentUserName());
	}

	@Override
	public Call getUnassignIssueUri() {
		return routes.ClientSideIssues.unassignIssue();
	}

	@Override
	public boolean isFilterable() {
		return false;
	}

	@Override
	public Call getOverviewUriForState(IssuesOverviewState state) {
		return routes.ClientSideIssues.getClientSideOverview(IssueOverviewStateBinder.create(state));
	}

	@Override
	public Call getUriForIssue(int id) {
		return routes.ClientSideIssues.getIssue(id);
	}

	@Override
	public Call getCloseIssuesUri() {
		return routes.ClientSideIssues.closeIssues();
	}

	@Override
	public Call getUpdateIssueUri(int id) {
		return routes.ClientSideIssues.updateIssue(id);
	}
	
}
