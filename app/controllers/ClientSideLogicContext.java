package controllers;

import actions.AuthentificationAction;
import play.mvc.Call;

public class ClientSideLogicContext implements Context{

	private final IssuesOverviewState state;
	
	public ClientSideLogicContext(IssuesOverviewState state) {
		super();
		this.state = state;
	}

	@Override
	public Call getClosingProcessUri() {
		return routes.ClientSideIssues.issuesClosing();
	}

	@Override
	public Call getAssignIssueUri() {
		return routes.ClientSideIssues.assignIssueToUser(AuthentificationAction.getCurrentUserName());
	}

	@Override
	public Call getUnassignIssueUri() {
		return routes.ClientSideIssues.unassignIssue();
	}

	@Override
	public boolean isStateOpen() {
		return IssuesOverviewState.OPEN.equals(state);
	}

	@Override
	public boolean isStateClosed() {
		return IssuesOverviewState.CLOSED.equals(state);
	}

	@Override
	public boolean isStateAssignedCurrentUser() {
		return IssuesOverviewState.ASSIGNED_CURRENT_USER.equals(state);
	}

	@Override
	public boolean isStateAssignedOtherUser() {
		return IssuesOverviewState.ASSIGNED_OTHERS.equals(state);
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
	public Call getUriForIssue(Long id) {
		return routes.ClientSideIssues.getIssue(id);
	}

	@Override
	public Call getCloseIssuesUri() {
		return routes.ClientSideIssues.closeIssues();
	}

	@Override
	public Call getUpdateIssueUri(Long id) {
		return routes.ClientSideIssues.updateIssue(id);
	}
	
}
