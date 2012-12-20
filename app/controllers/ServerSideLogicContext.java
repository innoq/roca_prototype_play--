package controllers;

import actions.AuthentificationAction;
import userselection.PaginationFilter;
import userselection.PartialSorting;
import userselection.PartialSorting.SortDirection;
import userselection.PartialSorting.SortableAttribute;
import userselection.SelectionFilter;
import play.mvc.Call;

public class ServerSideLogicContext implements Context{

	private final PaginationFilter paginationFilter;

	private final SelectionFilter selectionFilter;

	private final PartialSorting partialSorting;

	private final IssuesOverviewState state;

	public PaginationFilter getPaginationFilter() {
		return paginationFilter;
	}

	public SelectionFilter getSelectionFilter() {
		return selectionFilter;
	}

	public PartialSorting getPartialSorting() {
		return partialSorting;
	}

	public ServerSideLogicContext(PaginationFilter paginationFilter,
			SelectionFilter selectionFilter, PartialSorting partialSorting,
			IssuesOverviewState state) {
		super();
		this.paginationFilter = paginationFilter;
		this.selectionFilter = selectionFilter;
		this.partialSorting = partialSorting;
		this.state = state;
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

	public IssuesOverviewState getState() {
		return state;
	}

	@Override
	public Call getUnassignIssueUri() {
		return routes.Issues.unassignIssue(partialSorting,
				paginationFilter, selectionFilter);
	}

	@Override
	public Call getAssignIssueUri() {
		return routes.Issues.assignIssueToUser(
				AuthentificationAction.getCurrentUserName(), partialSorting,
				paginationFilter, selectionFilter);
	}

	@Override
	public Call getClosingProcessUri() {
		return routes.Issues.issuesClosing();
	}

	public String getAllIssuesWithNewSortingUri(SortableAttribute attribute,
			SortDirection direction) {
		return routes.Issues.getAllIssues(partialSorting,
				paginationFilter, selectionFilter,
				IssueOverviewStateBinder.create(state))
				+ "&"
				+ partialSorting.getUrlForTopSorting(attribute, direction);
	}

	public Call getIssuesOverviewUriForPage(int pageNumber) {
		return routes.Issues.getAllIssues(partialSorting,
				new userselection.PaginationFilter(pageNumber), selectionFilter,
				IssueOverviewStateBinder.create(state));
	}

	@Override
	public boolean isFilterable() {
		return true;
	}

	@Override
	public Call getOverviewUriForState(IssuesOverviewState state) {
		return routes.Issues.getAllIssues(new PartialSorting(), new PaginationFilter(), new SelectionFilter(), IssueOverviewStateBinder.create(state));
	}

	@Override
	public Call getUriForIssue(int id) {
		return routes.Issues.getIssue(id);
	}

	@Override
	public Call getCloseIssuesUri() {
		return routes.Issues.closeIssues();
	}

	@Override
	public Call getUpdateIssueUri(int id) {
		return routes.Issues.updateIssue(id);
	}
	


	
}
