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
		return routes.ClientSideIssuesController.issuesClosing();
	}

	@Override
	public Call getAssignIssueUri() {
		return routes.ClientSideIssuesController.assignIssueToUser(AuthentificationAction.getCurrentUserName());
	}

	@Override
	public Call getUnassignIssueUri() {
		return routes.ClientSideIssuesController.unassignIssue();
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
		return routes.ClientSideIssuesController.getClientSideOverview(IssueOverviewStateBinder.create(state));
	}

	@Override
	public Call getUriForIssue(Long id) {
		return routes.ClientSideIssuesController.getIssue(id);
	}

	@Override
	public Call getCloseIssuesUri() {
		return routes.ClientSideIssuesController.closeIssues();
	}

	@Override
	public Call getUpdateIssueUri(Long id) {
		return routes.ClientSideIssuesController.updateIssue(id);
	}
	
}
