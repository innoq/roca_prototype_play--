package controllers;

import actions.AuthentificationAction;
import models.Issue;
import models.IssueProcessingState;
import models.User;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.lang3.ArrayUtils;
import play.api.Play;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.With;
import repository.Repository;
import scala.None$;
import scala.Option;
import tools.IssueGenerator;
import userselection.PaginationFilter;
import userselection.PartialSorting;
import userselection.SelectionFilter;
import views.html.*;

import java.util.*;

/**
 * Controller fuer die Anwendung. Das Mapping der Controller-Methoden auf die
 * HTTP Methoden und Uris erfolgt im routes file.
 * <p/>
 * Content Negotiation und Validierung werden aktuell nicht unterstuetzt.
 */
@With(AuthentificationAction.class)
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
     * Schliesst die Menge an uebergebenen Issues und updatet die fuer den User
     * aktuelle Page ueber einen redirect.
     *
     * @return redirect auf die aktuelle Page des Users.
     */
    public static Result closeIssues() {

        doCloseIssues();
        return redirect(routes.Issues.getAllIssues(new PartialSorting(), new PaginationFilter(), new SelectionFilter(),
                IssueOverviewStateBinder.ASSIGNED_CURRENT_USER));
    }

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

    public static int countLines(final String string) {
        return string == null ? 5 : Math.max(5, string.split(System.getProperty("line.separator")).length);
    }

    /**
     * Liefert eine Repraesentation aller Issues zurueck die den uebergebenen
     * Parametern entsprechen.
     *
     * @param sorting     die Sortierung fuer die Issues.
     * @param pagination  das angeforderte Subset von Issues.
     * @param filter      die Menge an Filterkriterien die ein zurueckgeliefertes
     *                    Issue erfuellen muss.
     * @param stateBinder schrenkt die Ergebnismenge aufgrund des Issue states
     *                    ein.
     * @return die Repraesentation der angeforderten Issues.
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
     * Gibt eine Repraesentation des Issues fuer die uebergebene id zurueck.
     *
     * @param id die Id des angeforderten Issues.
     * @return die Repruesentation des Issues.
     */
    public static Result getIssue(int id) {
        Issue currentIssue = Repository.getInstance().findIssueById(id);

        Context context = new ServerSideLogicContext(new PaginationFilter(), new SelectionFilter(), new PartialSorting(), IssuesOverviewState.getByIssue(currentIssue));
        return ok(main.render(context, issueDetail.render(currentIssue, context), serverSideLogicScripts.render()));
    }

    /**
     * Liefert die Root Page der Anwendung zurueck. Aktuell ein redirect auf die
     * Repraesentation der offenen Issues.
     *
     * @return die Repraesentation der Root Page.
     */
    public static Result getRoot() {
        return redirect(routes.Issues.getAllIssues(new PartialSorting(), new PaginationFilter(), new SelectionFilter(),
                IssueOverviewStateBinder.OPEN));
    }

    /**
     * Gibt eine Repraesentation des Closing Prozesses zurueck.
     */
    public static Result issuesClosing() {

        Map<String, String[]> queryString = request().queryString();
        List<String> ids = Arrays.asList(ArrayUtils.nullToEmpty(queryString.get("issueId")));

        Context context = new ServerSideLogicContext(new PaginationFilter(), new SelectionFilter(), new PartialSorting(), IssuesOverviewState.ASSIGNED_CURRENT_USER);
        return (ids.size() == 0) ? ok(main.render(context, issuesClosingError.render(context), serverSideLogicScripts.render())) : ok(main.render(context, issuesClosing.render(ids, context), serverSideLogicScripts.render()));
    }

    /**
     * Entfernt die Zuweisung der Issues zu dem User. Die Issues gelten damit
     * wieder als OPEN.
     */
    public static Result unassignIssue(PartialSorting sorting, PaginationFilter pagination, SelectionFilter filter) {

        doUnassignIssue();
        return redirect(routes.Issues.getAllIssues(sorting, pagination, filter, IssueOverviewStateBinder.ASSIGNED_CURRENT_USER));
    }

    static void doUnassignIssue() {
        Map<String, String[]> body = request().body().asFormUrlEncoded();
        String id = body.get("issueId")[0];
        Issue issue = Repository.getInstance().findIssueById(Integer.parseInt(id));
        issue.processingState = IssueProcessingState.OPEN;
        Repository.getInstance().save(issue);
    }

    /**
     * Updatet den uebergebenen Issue und updatet die aktuelle Page des Users
     * ueber einen Redirect.
     */
    public static Result updateIssue(int id) {

        doUpdateIssue(id);

        return redirect(routes.Issues.getAllIssues(new PartialSorting(), new PaginationFilter(), new SelectionFilter(),
                IssueOverviewStateBinder.ASSIGNED_CURRENT_USER));
    }

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

    public static Result getChoicePage() {
        return ok(index.render());
    }

    public static void filterIssuesForState(List<Issue> requestedIssues, IssuesOverviewState state) {

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
            return issue.isAssignedToCurrentUser(AuthentificationAction.getCurrentUserName());
        }
    }

    private final static class AssignedToOtherUserPredicate implements Predicate {

        @Override
        public boolean evaluate(Object arg0) {
            Issue issue = (Issue) arg0;
            return issue.isAssignedToOtherUser(AuthentificationAction.getCurrentUserName());
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
