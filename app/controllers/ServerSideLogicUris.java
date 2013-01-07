package controllers;

import actions.AuthenticationAction;
import userselection.PaginationFilter;
import userselection.PartialSorting;
import userselection.PartialSorting.SortDirection;
import userselection.PartialSorting.SortableAttribute;
import userselection.SelectionFilter;
import play.mvc.Call;


/**
 *  Template context for the serverside logic implementation.
 */
public class ServerSideLogicUris implements Uris {

	private final PaginationFilter paginationFilter;

	private final SelectionFilter selectionFilter;

	private final PartialSorting partialSorting;

	public PaginationFilter getPaginationFilter() {
		return paginationFilter;
	}

	public SelectionFilter getSelectionFilter() {
		return selectionFilter;
	}

	public PartialSorting getPartialSorting() {
		return partialSorting;
	}

	public ServerSideLogicUris(PaginationFilter paginationFilter,
                               SelectionFilter selectionFilter, PartialSorting partialSorting) {
		super();
		this.paginationFilter = paginationFilter;
		this.selectionFilter = selectionFilter;
		this.partialSorting = partialSorting;
	}


	public Call getUnassignIssueUri() {
		return routes.ApplicationController.removeIssueAssignment(partialSorting,
				paginationFilter, selectionFilter);
	}

	@Override
	public Call getAssignIssueUri() {
		return routes.ApplicationController.assignIssueToUser(
				AuthenticationAction.getCurrentUserName(), partialSorting,
				paginationFilter, selectionFilter);
	}

	@Override
	public Call getClosingProcessUri() {
		return routes.ApplicationController.getIssuesClosingProcess();
	}

	public String getAllIssuesWithNewSortingUri(SortableAttribute attribute,
			SortDirection direction,IssuesOverviewState state) {
		return routes.ApplicationController.getIssueOverview(partialSorting,
				paginationFilter, selectionFilter,
				IssueOverviewStateBinder.create(state))
				+ "&"
				+ partialSorting.getUrlForTopSorting(attribute, direction);
	}

	public Call getIssuesOverviewUriForPage(int pageNumber,IssuesOverviewState state) {
		return routes.ApplicationController.getIssueOverview(partialSorting,
				new userselection.PaginationFilter(pageNumber), selectionFilter,
				IssueOverviewStateBinder.create(state));
	}

	@Override
	public boolean isFilterable() {
		return true;
	}

	@Override
	public Call getOverviewUriForState(IssuesOverviewState state) {
		return routes.ApplicationController.getIssueOverview(new PartialSorting(), new PaginationFilter(), new SelectionFilter(), IssueOverviewStateBinder.create(state));
	}

	@Override
	public Call getUriForIssue(int id) {
		return routes.ApplicationController.getIssueDetails(id);
	}

	@Override
	public Call getCloseIssuesUri() {
		return routes.ApplicationController.closeIssues();
	}

	@Override
	public Call getUpdateIssueUri(int id) {
		return routes.ApplicationController.updateIssue(id);
	}
	
}
