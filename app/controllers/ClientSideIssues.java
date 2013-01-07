package controllers;

import models.Issue;
import org.apache.commons.lang3.ArrayUtils;
import play.mvc.Controller;
import play.mvc.Result;
import repository.Repository;
import views.html.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;


/**
 * ClientSideIssues is the main controller for the app if the logic reside on the clientside by JS-components.
 *
 * TODO: actually the clientside and serverside implementation results in two different controllers on different uris. A perhaps better implementation is to combine the controllers and endpoint. The clientside implementation could be reached by providing a different unobstrusive js component.
 *
 */
public class ClientSideIssues extends Controller {

    public static Result getClientSideOverview(IssueOverviewStateBinder binder) {

        List<Issue> requestedIssues = new ArrayList<Issue>(Repository.getInstance().getAllIssues());
        IssuesOverviewState state = binder.getState();

        Issues.filterIssuesForState(requestedIssues, state);

        ClientSideLogicUris context = new ClientSideLogicUris();
        return ok(main.render(context,
                issuesOverview.render(requestedIssues, context,binder.getState()),
                clientSideLogicScripts.render(),binder.getState()));
    }

    public static Result getClientSideOverviewPage() {
        return ok();
    }

    public static Result closeIssues() {
        Issues.doCloseIssues();
        return redirect(routes.ClientSideIssues
                .getClientSideOverview(IssueOverviewStateBinder.ASSIGNED_CURRENT_USER));
    }

    public static Result issuesClosing() {

        Map<String, String[]> queryString = request().queryString();
        List<String> ids = Arrays.asList(ArrayUtils.nullToEmpty(queryString
                .get("issueId")));

        Uris uris = new ClientSideLogicUris();
        return (ids.size() == 0) ? ok(main.render(uris,
                issuesClosingError.render(uris),
                clientSideLogicScripts.render(), IssuesOverviewState.ASSIGNED_CURRENT_USER)) : ok(main.render(uris,
                issuesClosing.render(ids, uris),
                clientSideLogicScripts.render(), IssuesOverviewState.ASSIGNED_CURRENT_USER));
    }

    public static Result unassignIssue() {

        Issues.doRenameIssueAssignment();
        return redirect(routes.ClientSideIssues
                .getClientSideOverview(IssueOverviewStateBinder.ASSIGNED_CURRENT_USER));
    }

    public static Result assignIssueToUser(String userName) {

        Issues.doAssignIssueToUser(userName);
        return redirect(routes.ClientSideIssues
                .getClientSideOverview(IssueOverviewStateBinder.OPEN));
    }

    public static Result getIssue(int id) {
        Issue currentIssue = Repository.getInstance()
                .findIssueById(id);

        ClientSideLogicUris context = new ClientSideLogicUris();
        return ok(main.render(context,
                issueDetail.render(currentIssue, context),
                clientSideLogicScripts.render(),IssuesOverviewState.getByIssue(currentIssue)));
    }

    public static Result updateIssue(int id) {

        Issues.doUpdateIssue(id);
        return redirect(routes.ClientSideIssues
                .getClientSideOverview(IssueOverviewStateBinder.ASSIGNED_CURRENT_USER));
    }
}
