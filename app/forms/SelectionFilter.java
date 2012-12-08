package forms;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import models.Issue;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.SetUtils;
import org.apache.commons.collections.functors.NotNullPredicate;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;

import play.libs.F.Option;
import play.mvc.QueryStringBindable;
import repository.RepositoryFactory;

/**
 * Ermoeglicht die Filterung einer Menge von Issues nach einer Kombination von
 * Attribut Werten.
 * 
 * Ein {@link SelectionFilter} ist sein eigenes Bindable, da Play 2 nur self
 * recursive types als bindables unterstuetzt.
 * 
 */
public class SelectionFilter implements QueryStringBindable<SelectionFilter> {

	private final class MatchesRelevantAttributesPredicate implements Predicate {
		@Override
		public boolean evaluate(Object arg0) {
			Issue issue = (Issue) arg0;
			boolean matches = true;

			String project = issue.getProjectName();
			String component = issue.getComponentName();
			String issueType = issue.getIssueType();
			String reporter = issue.getReporter();
			String assignedUser = (issue.getAssignedUser() == null) ? null : issue.getAssignedUser().getName();

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

	public static enum FilterableAttributes {

		ID("id"), ASSIGENED_USER("assignedUser"), COMPONENT("component"), REPORTER("reporter"), PROJECT("project"), ISSUE_TYPE("issueType");

		private final String queryParam;

		private FilterableAttributes(String queryParam) {
			this.queryParam = queryParam;
		}

		public String getQueryParam() {
			return queryParam;
		}

	}

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

	public void setAllIds(Set<String> allIds) {
		this.allIds = new TreeSet<String>(allIds);
	}

	public void setAllReporters(Collection<String> allReporters) {
		this.allReporters = new TreeSet<String>(allReporters);
	}

	public void setAllAssignedUsers(Collection<String> allAssignedUsers) {
		this.allAssignedUsers = new TreeSet<String>(allAssignedUsers);
	}

	public void setAllProjects(Collection<String> allProjects) {
		this.allProjects = new TreeSet<String>(allProjects);
	}

	public void setAllComponents(Collection<String> allComponents) {
		this.allComponents = new TreeSet<String>(allComponents);
	}

	public void setAllIssueTypes(Collection<String> allIssueTypes) {
		this.allIssueTypes = new TreeSet<String>(allIssueTypes);
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

	public List<String> getAllReporters() {
		return new ArrayList<String>(allReporters);
	}

	public List<String> getAllAssignedUsers() {
		return new ArrayList<String>(allAssignedUsers);
	}

	public List<String> getAllProjects() {
		return new ArrayList<String>(allProjects);
	}

	public List<String> getAllComponents() {
		return new ArrayList<String>(allComponents);
	}

	public List<String> getAllIssueTypes() {
		return new ArrayList<String>(allIssueTypes);
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

	@SuppressWarnings("unchecked")
	private final static Set<String> createNotNullSet() {
		return SetUtils.predicatedSet(new HashSet<String>(), NotNullPredicate.getInstance());
	}

	public void filterIssues(Collection<Issue> issues) {

		// das hier ist unschoen da es eigentlich lieber beim binding (also der
		// factory Methode) passieren sollte.
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
			allIds.add(issue.getId() + "");
			allReporters.add(issue.getReporter());
			if (issue.getAssignedUser() != null) {
				allAssignedUsers.add(issue.getAssignedUser().getName());
			}
			allProjects.add(issue.getProjectName());

			allComponents.add(issue.getComponentName());
			allIssueTypes.add(issue.getIssueType());
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
		List<Issue> issues = RepositoryFactory.getRepository().getAll();
		return Option.Some(SelectionFilter.create(issues, queryString));
	}

	@Override
	public String unbind(String key) {

		List<Pair<FilterableAttributes, ? extends Collection<String>>> list = new ArrayList<Pair<FilterableAttributes, ? extends Collection<String>>>();
		list.add(Pair.of(FilterableAttributes.ASSIGENED_USER, selectedAssignedUsers));
		list.add(Pair.of(FilterableAttributes.COMPONENT, selectedComponents));
		list.add(Pair.of(FilterableAttributes.PROJECT, selectedProjects));
		list.add(Pair.of(FilterableAttributes.REPORTER, selectedReporters));
		list.add(Pair.of(FilterableAttributes.ISSUE_TYPE, selectedIssueTypes));

		List<String> params = new ArrayList<String>();
		for (Pair<FilterableAttributes, ? extends Collection<String>> pair : list) {
			for (String selected : pair.getRight()) {
				params.add(pair.getLeft().queryParam + "=" + selected);
			}
		}

		return StringUtils.join(params, "&");
	}

	@Override
	public String javascriptUnbind() {
		throw new UnsupportedOperationException("JavaScript unbind wird nicht unterstuetzt!");
	}

	public static SelectionFilter create(List<Issue> issues, Map<String, String[]> queryString) {

		final List<String> selectedAssignedUsers = (queryString.get(FilterableAttributes.ASSIGENED_USER.getQueryParam()) == null) ? Collections
				.<String> emptyList() : Arrays.<String> asList(queryString.get(FilterableAttributes.ASSIGENED_USER.getQueryParam()));
		final List<String> selectedComponents = (queryString.get(FilterableAttributes.COMPONENT.getQueryParam()) == null) ? Collections
				.<String> emptyList() : Arrays.<String> asList(queryString.get(FilterableAttributes.COMPONENT.getQueryParam()));
		final List<String> selectedReporters = (queryString.get(FilterableAttributes.REPORTER.getQueryParam()) == null) ? Collections
				.<String> emptyList() : Arrays.<String> asList(queryString.get(FilterableAttributes.REPORTER.getQueryParam()));
		final List<String> selectedProjects = (queryString.get(FilterableAttributes.PROJECT.getQueryParam()) == null) ? Collections
				.<String> emptyList() : Arrays.<String> asList(queryString.get(FilterableAttributes.PROJECT.getQueryParam()));
		final List<String> selectedIssueTypes = (queryString.get(FilterableAttributes.ISSUE_TYPE.getQueryParam()) == null) ? Collections
				.<String> emptyList() : Arrays.<String> asList(queryString.get(FilterableAttributes.ISSUE_TYPE.getQueryParam()));

		SelectionFilter selectionFilter = new SelectionFilter();
		selectionFilter.setSelectedAssignedUsers(selectedAssignedUsers);
		selectionFilter.setSelectedComponents(selectedComponents);
		selectionFilter.setSelectedProjects(selectedProjects);
		selectionFilter.setSelectedReporters(selectedReporters);
		selectionFilter.setSelectedIssueTypes(selectedIssueTypes);

		return selectionFilter;
	}
}
