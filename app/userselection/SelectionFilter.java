package userselection;

import models.Issue;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.SetUtils;
import org.apache.commons.collections.functors.NotNullPredicate;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import play.libs.F.Option;
import play.mvc.QueryStringBindable;
import repository.Repository;

import java.util.*;

/**
 * Provides a filter function for a quantity of issues by specific user selections. Also stores all possible selection criteria.
 * </p>
 * A SelectionFilter is its own bindable, because play 2 only supports self recursive types as bindables
 */
public class SelectionFilter implements QueryStringBindable<SelectionFilter> {

    private Set<String> allIds = new TreeSet<String>();
    private Set<String> allReporters = new TreeSet<String>();
    private Set<String> allAssignedUsers = new TreeSet<String>();
    private Set<String> allProjects = new TreeSet<String>();
    private Set<String> allComponents = new TreeSet<String>();
    private Set<String> allIssueTypes = new TreeSet<String>();
    private Set<String> selectedReporters = new HashSet<String>();
    private Set<String> selectedAssignedUsers = new HashSet<String>();
    private Set<String> selectedProjects = new HashSet<String>();
    private Set<String> selectedComponents = new HashSet<String>();
    private Set<String> selectedIssueTypes = new HashSet<String>();

    @SuppressWarnings("unchecked")
    private final static Set<String> createNotNullSet() {
        return SetUtils.predicatedSet(new HashSet<String>(), NotNullPredicate.getInstance());
    }

    public static SelectionFilter create(List<Issue> issues, Map<String, String[]> queryString) {

        SelectionFilter selectionFilter = new SelectionFilter();

        selectionFilter.setSelectedAssignedUsers(getFilterableAttributesInQueryString(queryString, FilterableAttribute.ASSIGENED_USER));
        selectionFilter.setSelectedComponents(getFilterableAttributesInQueryString(queryString, FilterableAttribute.COMPONENT));
        selectionFilter.setSelectedProjects(getFilterableAttributesInQueryString(queryString, FilterableAttribute.PROJECT));
        selectionFilter.setSelectedReporters(getFilterableAttributesInQueryString(queryString, FilterableAttribute.REPORTER));
        selectionFilter.setSelectedIssueTypes(getFilterableAttributesInQueryString(queryString, FilterableAttribute.ISSUE_TYPE));

        return selectionFilter;
    }

    private static List<String> getFilterableAttributesInQueryString(Map<String, String[]> queryString, FilterableAttribute attributes) {
        return (queryString.get(attributes.getQueryParam()) == null) ? Collections
                .<String>emptyList() : Arrays.<String>asList(queryString.get(attributes.getQueryParam()));
    }

    public void setSelectedReporters(Collection<String> selectedReporters) {
        this.selectedReporters = new HashSet<String>(selectedReporters);
    }

    public void setSelectedAssignedUsers(Collection<String> selectedAssignedUsers) {
        this.selectedAssignedUsers = new HashSet<String>(selectedAssignedUsers);
    }

    public void setSelectedProjects(Collection<String> selectedProjects) {
        this.selectedProjects = new HashSet<String>(selectedProjects);
    }

    public void setSelectedComponents(Collection<String> selectedComponents) {
        this.selectedComponents = new HashSet<String>(selectedComponents);
    }

    public void setSelectedIssueTypes(Collection<String> selectedIssueTypes) {
        this.selectedIssueTypes = new HashSet<String>(selectedIssueTypes);
    }

    public List<String> getAllIds() {
        return new ArrayList<String>(allIds);
    }

    public void setAllIds(Set<String> allIds) {
        this.allIds = new TreeSet<String>(allIds);
    }

    public List<String> getAllReporters() {
        return new ArrayList<String>(allReporters);
    }

    public void setAllReporters(Collection<String> allReporters) {
        this.allReporters = new TreeSet<String>(allReporters);
    }

    public List<String> getAllAssignedUsers() {
        return new ArrayList<String>(allAssignedUsers);
    }

    public void setAllAssignedUsers(Collection<String> allAssignedUsers) {
        this.allAssignedUsers = new TreeSet<String>(allAssignedUsers);
    }

    public List<String> getAllProjects() {
        return new ArrayList<String>(allProjects);
    }

    public void setAllProjects(Collection<String> allProjects) {
        this.allProjects = new TreeSet<String>(allProjects);
    }

    public List<String> getAllComponents() {
        return new ArrayList<String>(allComponents);
    }

    public void setAllComponents(Collection<String> allComponents) {
        this.allComponents = new TreeSet<String>(allComponents);
    }

    public List<String> getAllIssueTypes() {
        return new ArrayList<String>(allIssueTypes);
    }

    public void setAllIssueTypes(Collection<String> allIssueTypes) {
        this.allIssueTypes = new TreeSet<String>(allIssueTypes);
    }

    public boolean isSelectedProject(String project) {
        return selectedProjects.contains(project);
    }

    public boolean isSelectedComponent(String component) {
        return selectedComponents.contains(component);
    }

    public boolean isSelectedType(String type) {
        return selectedIssueTypes.contains(type);
    }

    public boolean isSelectedReporter(String reporter) {
        return selectedReporters.contains(reporter);
    }

    public boolean isSelectedUser(String userName) {
        return selectedAssignedUsers.contains(userName);
    }

    public void filterIssues(Collection<Issue> issues) {
        fillFilterableAttributes(issues);
        CollectionUtils.filter(issues, new MatchesRelevantAttributesPredicate());
    }

    private void fillFilterableAttributes(Collection<Issue> issues) {

        final Set<String> allIds = createNotNullSet();
        final Set<String> allReporters = createNotNullSet();
        final Set<String> allAssignedUsers = createNotNullSet();
        final Set<String> allProjects = createNotNullSet();
        final Set<String> allComponents = createNotNullSet();
        final Set<String> allIssueTypes = createNotNullSet();

        for (Issue issue : issues) {
            allIds.add(issue.id + "");
            allReporters.add(issue.reporter);
            if (issue.assignedUser != null) {
                allAssignedUsers.add(issue.assignedUser.name);
            }
            allProjects.add(issue.projectName);

            allComponents.add(issue.componentName);
            allIssueTypes.add(issue.issueType);
        }

        this.setAllAssignedUsers(allAssignedUsers);
        this.setAllComponents(allComponents);
        this.setAllIds(allIds);
        this.setAllIssueTypes(allIssueTypes);
        this.setAllProjects(allProjects);
        this.setAllReporters(allReporters);
    }

    @Override
    public Option<SelectionFilter> bind(String key, Map<String, String[]> queryString) {
        List<Issue> issues = Repository.getInstance().getAllIssues();
        return Option.Some(SelectionFilter.create(issues, queryString));
    }

    @Override
    public String unbind(String key) {

        List<Pair<FilterableAttribute, ? extends Collection<String>>> list = new ArrayList<Pair<FilterableAttribute, ? extends Collection<String>>>();
        list.add(Pair.of(FilterableAttribute.ASSIGENED_USER, selectedAssignedUsers));
        list.add(Pair.of(FilterableAttribute.COMPONENT, selectedComponents));
        list.add(Pair.of(FilterableAttribute.PROJECT, selectedProjects));
        list.add(Pair.of(FilterableAttribute.REPORTER, selectedReporters));
        list.add(Pair.of(FilterableAttribute.ISSUE_TYPE, selectedIssueTypes));

        List<String> params = new ArrayList<String>();
        for (Pair<FilterableAttribute, ? extends Collection<String>> pair : list) {
            for (String selected : pair.getRight()) {
                params.add(pair.getLeft().queryParam + "=" + selected);
            }
        }

        return StringUtils.join(params, "&");
    }

    @Override
    public String javascriptUnbind() {
        throw new UnsupportedOperationException("JavaScript unbind is not supported!");
    }

    public static enum FilterableAttribute {

        ID("id"), ASSIGENED_USER("assignedUser"), COMPONENT("component"), REPORTER("reporter"), PROJECT("project"), ISSUE_TYPE("issueType");
        private final String queryParam;

        private FilterableAttribute(String queryParam) {
            this.queryParam = queryParam;
        }

        public String getQueryParam() {
            return queryParam;
        }
    }

    private final class MatchesRelevantAttributesPredicate implements Predicate {

        @Override
        public boolean evaluate(Object arg0) {
            Issue issue = (Issue) arg0;
            boolean matches = true;

            String project = issue.projectName;
            String component = issue.componentName;
            String issueType = issue.issueType;
            String reporter = issue.reporter;
            String assignedUser = (issue.assignedUser == null) ? null : issue.assignedUser.name;

            matches &= attributeIsRelevantAndContainsValue(selectedProjects, project);
            matches &= attributeIsRelevantAndContainsValue(selectedComponents, component);
            matches &= attributeIsRelevantAndContainsValue(selectedIssueTypes, issueType);
            matches &= attributeIsRelevantAndContainsValue(selectedReporters, reporter);
            matches &= attributeIsRelevantAndContainsValue(selectedAssignedUsers, assignedUser);

            return matches;
        }

        private final boolean attributeIsRelevantAndContainsValue(Collection<String> selectedAttributes, String value) {
            return selectedAttributes.isEmpty() || selectedAttributes.contains(value);
        }
    }
}
