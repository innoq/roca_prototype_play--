package controllers;

import actions.AuthenticationAction;
import models.Issue;
import models.IssueProcessingState;
import models.User;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.lang3.ArrayUtils;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.With;
import repository.Repository;
import userselection.PaginationFilter;
import userselection.PartialSorting;
import userselection.SelectionFilter;
import views.html.*;

import java.util.*;

/**
 * Main controller for the serverside logic implementation.
 */
@With(AuthenticationAction.class)
public class Issues extends Controller {

    public static Result assignIssueToUser(String userName, PartialSorting sorting, PaginationFilter pagination, SelectionFilter filter) {
        doAssignIssueToUser(userName);
        return redirect(routes.Issues.getAllIssues(sorting, pagination, filter, IssueOverviewStateBinder.OPEN));
    }

    static void doAssignIssueToUser(String userName) {
        String[] issueIds = ArrayUtils.nullToEmpty(request().body().asFormUrlEncoded().get("issueId"));
        User user = Repository.getInstance().findUserByName(userName);
        for (String id : issueIds) {
            Issue issue = Repository.getInstance().findIssueById(Integer.parseInt(id));
            issue.assignedUser = user;
            issue.processingState = IssueProcessingState.CLAIMED;
            Repository.getInstance().save(issue);
        }
    }

    /**
     * Closes all delivered issues and updates the view.
     *
     * @return redirect to update the view.
     */
    public static Result closeIssues() {

        doCloseIssues();
        return redirect(routes.Issues.getAllIssues(new PartialSorting(), new PaginationFilter(), new SelectionFilter(),
                IssueOverviewStateBinder.ASSIGNED_CURRENT_USER));
    }

    /**
     * Extracted closing logic. Could be reused by the clientside logic controller.
     */
    static void doCloseIssues() {
        Map<String, String[]> body = request().body().asFormUrlEncoded();
        String[] ids = body.get("issueId");
        String comment = body.get("comment")[0];

        for (String id : ids) {
            Issue issue = Repository.getInstance().findIssueById(Integer.parseInt(id));
            issue.processingState = IssueProcessingState.CLOSED;
            issue.closeDate = new Date();
            issue.comment = comment;
            Repository.getInstance().save(issue);
        }
    }

    /**
     *  Count the minimal number of lines a textarea should provide to display the given string.
     *
     * @param string the string to display
     * @return the number of lines to display
     */
    public static int countLines(final String string) {
        return string == null ? 5 : Math.max(5, string.split(System.getProperty("line.separator")).length);
    }

    /**
     * Returns an overview page of all issues which corresponds to the given user selection criteria.
     *
     * @param sorting     specification for the sorting order.
     * @param pagination  specification for the requested page.
     * @param filter      quantity of filter criteria a specific issue must satisfy to be displayed.
     * @param stateBinder the requested state.
     *
     * @return the overview page.
     */
    public static Result getAllIssues(PartialSorting sorting, PaginationFilter pagination, SelectionFilter filter,
                                      IssueOverviewStateBinder stateBinder) {

        List<Issue> requestedIssues = new ArrayList<Issue>(Repository.getInstance().getAllIssues());
        IssuesOverviewState state = stateBinder.getState();

        filterIssuesForState(requestedIssues, state);
        filter.filterIssues(requestedIssues);
        sorting.sortIssues(requestedIssues);
        pagination.filterIssues(requestedIssues);

        Context context = new ServerSideLogicContext(pagination, filter, sorting, state);
        if (request().queryString().containsKey("ajax")) {
            return ok(issuesOverview.render(requestedIssues, context));
        }
        return ok(main.render(context, issuesOverview.render(requestedIssues, context), serverSideLogicScripts.render()));
    }

    /**
     * Returns a detailed html representation of a single issue.
     *
     * @param id the id of the requested issue.
     * @return a detailed issue representation.
     */
    public static Result getIssue(int id) {
        Issue currentIssue = Repository.getInstance().findIssueById(id);

        Context context = new ServerSideLogicContext(new PaginationFilter(), new SelectionFilter(), new PartialSorting(), IssuesOverviewState.getByIssue(currentIssue));
        return ok(main.render(context, issueDetail.render(currentIssue, context), serverSideLogicScripts.render()));
    }

    /**
     * Returns the root page of the app. Actually a redirect to the general overview page.
     * @return the root page.
     */
    public static Result getRoot() {
        return redirect(routes.Issues.getAllIssues(new PartialSorting(), new PaginationFilter(), new SelectionFilter(),
                IssueOverviewStateBinder.OPEN));
    }

    /**
     * Returns a representation of the closing process.
     *
     * TODO: renaming
     */
    public static Result issuesClosing() {
        Map<String, String[]> queryString = request().queryString();
        List<String> ids = Arrays.asList(ArrayUtils.nullToEmpty(queryString.get("issueId")));

        Context context = new ServerSideLogicContext(new PaginationFilter(), new SelectionFilter(), new PartialSorting(), IssuesOverviewState.ASSIGNED_CURRENT_USER);
        return (ids.size() == 0) ? ok(main.render(context, issuesClosingError.render(context), serverSideLogicScripts.render())) : ok(main.render(context, issuesClosing.render(ids, context), serverSideLogicScripts.render()));
    }

    /**
     * Unassigned an issues and changes the issue state to open. Updates the overview page by redirecting.
     *
     * @param sorting     specification for the sorting order.
     * @param pagination  specification for the requested page.
     * @param filter      quantity of filter criteria a specific issue must satisfy to be displayed.
     *
     * @return the updated overview page represantation
     *
     * TODO: rename in removeIssueAssignment
     */
    public static Result unassignIssue(PartialSorting sorting, PaginationFilter pagination, SelectionFilter filter) {

        doUnassignIssue();
        return redirect(routes.Issues.getAllIssues(sorting, pagination, filter, IssueOverviewStateBinder.ASSIGNED_CURRENT_USER));
    }

    /**
     * Extracted closing logic. Could be reused by the clientside logic controller.
     *
     * TODO: rename in doRemoveIssueAssignment
     */
    static void doUnassignIssue() {
        Map<String, String[]> body = request().body().asFormUrlEncoded();
        String id = body.get("issueId")[0];
        Issue issue = Repository.getInstance().findIssueById(Integer.parseInt(id));
        issue.processingState = IssueProcessingState.OPEN;
        Repository.getInstance().save(issue);
    }

    /**
     * Updates the specified issue and updates the view.
     *
     * @param id of the issue to update
     * @return the updated issue html represantation
     */
    public static Result updateIssue(int id) {

        doUpdateIssue(id);

        return redirect(routes.Issues.getAllIssues(new PartialSorting(), new PaginationFilter(), new SelectionFilter(),
                IssueOverviewStateBinder.ASSIGNED_CURRENT_USER));
    }


    /**
     * Extracted update logic. Could be reused by the clientside logic controller.
     *
     */
    static void doUpdateIssue(int id) {
        Issue issue = Repository.getInstance().findIssueById(id);
        Map<String, String[]> body = request().body().asFormUrlEncoded();

        String[] arguments = ArrayUtils.nullToEmpty(body.get("argument"));
        String[] argumentIds = ArrayUtils.nullToEmpty(body.get("argumentId"));
        Map<String, String> serviceArguments = new HashMap<String, String>();

        for (int i = 0; i < argumentIds.length; i++) {
            serviceArguments.put(argumentIds[i], arguments[i]);
        }

        issue.setArguments(serviceArguments);

        if (body.get("close") != null) {
            issue.closeAction();
        }
        String[] comments = body.get("comment");
        if (ArrayUtils.isNotEmpty(comments)) {
            issue.comment = comments[0];
        }
        Repository.getInstance().save(issue);
    }

    /**
     * Return a page wherer the user could decide between the serveside or clientside implementation.
     *
     * @return the page.
     */
    public static Result getChoicePage() {
        return ok(index.render());
    }

     static void filterIssuesForState(List<Issue> requestedIssues, IssuesOverviewState state) {

        switch (state) {
            case ALL:
                break;
            case OPEN:
                CollectionUtils.filter(requestedIssues, new IssueProcessingStatePredicate(IssueProcessingState.OPEN));
                break;
            case ASSIGNED_CURRENT_USER:
                CollectionUtils.filter(requestedIssues, new AssignedToCurrentUserPredicate());
                break;
            case ASSIGNED_OTHERS:
                CollectionUtils.filter(requestedIssues, new AssignedToOtherUserPredicate());
                break;
            case CLOSED:
                CollectionUtils.filter(requestedIssues, new IssueProcessingStatePredicate(IssueProcessingState.CLOSED));
                break;
            default:
                throw new IllegalStateException("unknown state!");
        }
    }

    private final static class AssignedToCurrentUserPredicate implements Predicate {

        @Override
        public boolean evaluate(Object arg0) {
            Issue issue = (Issue) arg0;
            return issue.isAssignedToCurrentUser(AuthenticationAction.getCurrentUserName());
        }
    }

    private final static class AssignedToOtherUserPredicate implements Predicate {

        @Override
        public boolean evaluate(Object arg0) {
            Issue issue = (Issue) arg0;
            return issue.isAssignedToOtherUser(AuthenticationAction.getCurrentUserName());
        }
    }

    private final static class IssueProcessingStatePredicate implements Predicate {

        private final IssueProcessingState state;

        private IssueProcessingStatePredicate(IssueProcessingState state) {
            super();
            this.state = state;
        }

        @Override
        public boolean evaluate(Object arg0) {
            Issue issue = (Issue) arg0;
            return state.equals(issue.processingState);
        }
    }
}
