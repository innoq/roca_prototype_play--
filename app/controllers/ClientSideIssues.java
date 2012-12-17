package controllers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import models.Issue;
import org.apache.commons.lang3.ArrayUtils;
import play.mvc.Controller;
import play.mvc.Result;
import repository.RepositoryFactory;
import views.html.clientSideLogicScripts;
import views.html.issueDetail;
import views.html.issuesClosing;
import views.html.issuesClosingError;
import views.html.issuesOverview;
import views.html.main;

public class ClientSideIssues extends Controller {

	public static Result getClientSideOverview(IssueOverviewStateBinder binder) {

		List<Issue> requestedIssues = new ArrayList<Issue>(RepositoryFactory
				.getRepository().getAll());
		IssuesOverviewState state = binder.getState();

		Issues.filterIssuesForState(requestedIssues, state);

		ClientSideLogicContext context = new ClientSideLogicContext(
				binder.getState());
		return ok(main.render(context,
				issuesOverview.render(requestedIssues, context),
				clientSideLogicScripts.render()));
	}

	public static Result getClientSideOverviewPage() {
		return ok();
	}

	public static Result closeIssues() {
		Issues.doCloseIssues();
		return redirect(routes.ClientSideIssuesController
				.getClientSideOverview(IssueOverviewStateBinder.ASSIGNED_CURRENT_USER));
	}

	public static Result issuesClosing() {

		Map<String, String[]> queryString = request().queryString();
		List<String> ids = Arrays.asList(ArrayUtils.nullToEmpty(queryString
				.get("issueId")));

		Context context = new ClientSideLogicContext(
				IssuesOverviewState.ASSIGNED_CURRENT_USER);
		return (ids.size() == 0) ? ok(main.render(context,
				issuesClosingError.render(context),
				clientSideLogicScripts.render())) : ok(main.render(context,
				issuesClosing.render(ids, context),
				clientSideLogicScripts.render()));
	}

	public static Result unassignIssue() {

		Issues.doUnassignIssue();
		return redirect(routes.ClientSideIssuesController
				.getClientSideOverview(IssueOverviewStateBinder.ASSIGNED_CURRENT_USER));
	}

	public static Result assignIssueToUser(String userName) {

		Issues.doAssignIssueToUser(userName);
		return redirect(routes.ClientSideIssuesController
				.getClientSideOverview(IssueOverviewStateBinder.OPEN));
	}

	public static Result getIssue(Long id) {
		Issue currentIssue = RepositoryFactory.getRepository()
				.findIssueById(id);

		ClientSideLogicContext context = new ClientSideLogicContext(
				IssuesOverviewState.getByIssue(currentIssue));
		return ok(main.render(context,
				issueDetail.render(currentIssue, context),
				clientSideLogicScripts.render()));
	}

	public static Result updateIssue(Long id) {

		Issues.doUpdateIssue(id);
		return redirect(routes.ClientSideIssuesController
				.getClientSideOverview(IssueOverviewStateBinder.ASSIGNED_CURRENT_USER));
	}
}
