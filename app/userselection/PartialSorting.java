package userselection;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

import models.Issue;

import org.apache.commons.collections.ComparatorUtils;
import org.apache.commons.lang.StringUtils;

import play.libs.F.Option;
import play.mvc.QueryStringBindable;

/**
 * Ermoeglicht eine partielle Sortierung einer Menge von Issues. Die Sortierung
 * kann dabei jeweils ueber Attribute von issues erfolgen die gewichtet werdne
 * koennen. Weiterhin ist fuer jedes Attribut eine eigene Sortierrichtung
 * (ASCENDING vs. DESCENDING) moeglich.
 * 
 * Ein {@link PartialSorting} ist sein eigenes Bindable, da Play 2 nur self
 * recursive types als bindables unterstuetzt.
 */
public class PartialSorting implements QueryStringBindable<PartialSorting> {

	public static enum SortableAttribute {
			ID("id"),
			PROJECT("projectName"),
			OPEN_DATE("openDate"),
			CLOSE_DATE("closeDate"),
			REPORTER("reporter"),
			ASSIGNED_USER("assignedUser"),
			ISSUE_TYPE("issueType"),
			COMPONENT("componentName"),
			SUMMARY("summary"),
			PRIORITY("priority"),
			COMPONENT_VERSION("componentVersion"),
			PROCESSING_STATE("processingState"),
			DESCRIPTION("description");

		private final String issueAttribute;

		private SortableAttribute(String issueAttribute) {
			this.issueAttribute = issueAttribute;
		}

		public String getIssuePropertyName() {
			return issueAttribute;
		}
	}

	public static enum SortDirection {
		ASCENDING, DESCENDING
	}

	private List<IssueComparator> comparators = new ArrayList<IssueComparator>();

	private Comparator<Issue> completeComparator;

	private String queryStringKey;

	public PartialSorting() {
		super();
	}

	public void sortIssues(List<Issue> issues) {
		Collections.sort(issues, completeComparator);
	}

	public String getUrlForTopSorting(SortableAttribute attribute, SortDirection ordering) {

		int highestPriority = -1;
		for (IssueComparator comparator : comparators) {
			highestPriority = (highestPriority < comparator.getPriority()) ? comparator.getPriority() : highestPriority;
		}

		return new IssueComparator(highestPriority + 1, attribute, ordering).unbind(queryStringKey);
	}

	@Override
	public Option<PartialSorting> bind(String key, Map<String, String[]> data) {
		String[] params = data.get(key);
		if (params == null || params.length == 0) {
			PartialSorting strategy = new PartialSorting();
			strategy.setComparators(Collections.singletonList(new IssueComparator(1, SortableAttribute.OPEN_DATE, SortDirection.DESCENDING)));
			strategy.queryStringKey = key;
			return Option.Some(strategy);
		}

		List<IssueComparator> comparators = new ArrayList<IssueComparator>();
		for (String param : params) {
			String[] sortingParams = param.split(",");
			comparators.add(new IssueComparator(Integer.parseInt(sortingParams[0]), SortableAttribute.valueOf(sortingParams[1].trim()), SortDirection
					.valueOf(sortingParams[2].trim())));
		}

		PartialSorting strategy = new PartialSorting();
		strategy.setComparators(comparators);
		strategy.queryStringKey = key;
		return Option.Some(strategy);
	}

	public void setComparators(Collection<IssueComparator> comparators) {
		this.comparators = new ArrayList<IssueComparator>(comparators);

		TreeSet<IssueComparator> sortedComparators = new TreeSet<IssueComparator>();
		sortedComparators.addAll(comparators);
		completeComparator = ComparatorUtils.chainedComparator(sortedComparators);
	}

	@Override
	public String unbind(String key) {

		if (comparators.isEmpty()) {
			return key + "=1," + SortableAttribute.OPEN_DATE.name() + "," + SortDirection.DESCENDING.name();
		}

		List<String> queryParams = new ArrayList<String>();
		for (IssueComparator comparator : comparators) {
			queryParams.add(comparator.unbind(key));
		}
		return StringUtils.join(queryParams, "&");
	}

	@Override
	public String javascriptUnbind() {
		throw new UnsupportedOperationException("JavaScript unbind is not supported!");
	}

}
