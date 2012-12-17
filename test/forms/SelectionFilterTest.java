package forms;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import models.Issue;

import org.junit.Test;

import forms.SelectionFilter;
import forms.SelectionFilter.FilterableAttributes;


public class SelectionFilterTest {

	private final List<Issue> issues = new ArrayList<Issue>();
	private final Map<String, String[]> queryString = new HashMap<String, String[]>();

	@Test
	public void filterIssuesSupportsAFilteringForEveryItemInFilteringValues() {

		FilterableAttributes[] filterAttributes = FilterableAttributes.values();
		for (FilterableAttributes attribute : filterAttributes) {
			queryString.put(attribute.getQueryParam(), new String[] { "xxx" });
		}

		SelectionFilter filter = SelectionFilter.create(Collections.<Issue> emptyList(), queryString);
		filter.filterIssues(issues);

		assertEquals(0, issues.size());
	}

	@Test
	public void filterIssuesFiltersWithAndConcatenationBetweenSimilarTypes() {

		Issue Issue1 = new Issue(1L);
		Issue1.componentName = "hallo";
		issues.add(Issue1);

		Issue Issue2 = new Issue(1L);
		Issue2.componentName = "xxx";
		issues.add(Issue2);

		queryString.put(FilterableAttributes.COMPONENT.getQueryParam(), new String[] { "test", "hallo", "hund" });

		SelectionFilter filter = SelectionFilter.create(Collections.<Issue> emptyList(), queryString);
		filter.filterIssues(issues);

		assertEquals(1, issues.size());
		assertEquals(Issue1, issues.get(0));
	}

	@Test
	public void filterIssuesFiltersWithOrConcatenationBetweenSimilarTypes() {

		Issue Issue1 = new Issue(1L);
		Issue1.componentName = "test";
		Issue1.reporter = "false";
		issues.add(Issue1);

		Issue Issue2 = new Issue(1L);
		Issue2.componentName = "false";
		Issue2.reporter = "test";
		issues.add(Issue2);

		Issue Issue3 = new Issue(1L);
		Issue3.componentName = "test";
		Issue3.reporter = "test";
		issues.add(Issue3);

		queryString.put(FilterableAttributes.COMPONENT.getQueryParam(), new String[] { "test", "hallo", "hund" });
		queryString.put(FilterableAttributes.REPORTER.getQueryParam(), new String[] { "test" });

		SelectionFilter filter = SelectionFilter.create(Collections.<Issue> emptyList(), queryString);
		filter.filterIssues(issues);

		assertEquals(1, issues.size());
		assertEquals(Issue3, issues.get(0));
	}
}
