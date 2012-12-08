package forms;

import static org.junit.Assert.assertEquals;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import models.Issue;

import org.junit.Before;
import org.junit.Test;

import play.libs.F.Option;
import forms.PartialSorting.SortDirection;
import forms.PartialSorting.SortableAttribute;

public class PartialSortingTest {

	private Issue Issue1;
	private Issue Issue2;
	private Issue Issue3;
	private final static SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("MM/dd/yyyy");
	private Map<String, String[]> queryString;
	private List<Issue> issues;

	@Before
	public void setUp() throws ParseException {

		Issue1 = new Issue(1L);
		Issue1.setIssueType("b");
		Issue1.setComponentName("b");
		Issue1.setOpenDate(DATE_FORMAT.parse("12/12/2012"));

		Issue2 = new Issue(2L);
		Issue2.setIssueType("b");
		Issue2.setComponentName("b");
		Issue2.setOpenDate(DATE_FORMAT.parse("12/13/2012"));

		Issue3 = new Issue(3L);
		Issue2.setIssueType("a");
		Issue3.setComponentName("c");
		Issue3.setOpenDate(DATE_FORMAT.parse("12/14/2012"));

		issues = new ArrayList<Issue>();
		issues.add(Issue1);
		issues.add(Issue2);
		issues.add(Issue3);

		queryString = new HashMap<String, String[]>();
	}

	@Test
	public void bindCreatesTheCorrectPartialSorting() {

		queryString.put("sorting", new String[] { "1", "OPEN_DATE", "DESCENDING" });
		queryString.put("sorting", new String[] { "1", "OPEN_DATE", "DESCENDING" });
		queryString.put("sorting", new String[] { "1", "OPEN_DATE", "DESCENDING" });

		Option<PartialSorting> option = new PartialSorting().bind("sorting", queryString);
		assertEquals(true, option.isDefined());

		PartialSorting sorting = option.get();

		sorting.sortIssues(issues);

		assertEquals(3, issues.size());
		assertEquals(Issue3, issues.get(0));
		assertEquals(Issue2, issues.get(1));
		assertEquals(Issue1, issues.get(2));
	}

	@Test
	public void unbindCreatesCorrectQueryString() {

		List<IssueComparator> comparators = new ArrayList<IssueComparator>();
		comparators.add(new IssueComparator(1, SortableAttribute.ASSIGNED_USER, SortDirection.ASCENDING));
		comparators.add(new IssueComparator(2, SortableAttribute.CLOSE_DATE, SortDirection.DESCENDING));
		comparators.add(new IssueComparator(3, SortableAttribute.COMPONENT_VERSION, SortDirection.DESCENDING));

		PartialSorting sorting = new PartialSorting();
		sorting.setComparators(comparators);

		String queryString = sorting.unbind("partialSorting");
		assertEquals("partialSorting=1,ASSIGNED_USER,ASCENDING&partialSorting=2,CLOSE_DATE,DESCENDING&partialSorting=3,COMPONENT_VERSION,DESCENDING",
				queryString);
	}

	@Test
	public void unbindWithANewPartialSortingShouldResultInADefaultString() {

		PartialSorting sorting = new PartialSorting();

		String queryString = sorting.unbind("partialSorting");
		assertEquals("partialSorting=1,OPEN_DATE,DESCENDING", queryString);
	}

	@Test
	public void sortIssuesAllowsPartialSortingForEverySortingValue_Descending() {

		Set<IssueComparator> comparators = createComparatorsForAllSortableAttributes(SortDirection.DESCENDING);

		PartialSorting sorting = new PartialSorting();
		sorting.setComparators(comparators);

		sorting.sortIssues(issues);

		assertEquals(3, issues.size());
	}

	private Set<IssueComparator> createComparatorsForAllSortableAttributes(SortDirection ordering) {

		Set<IssueComparator> comparators = new HashSet<IssueComparator>();
		int couner = 1;
		for (SortableAttribute attribute : SortableAttribute.values()) {
			comparators.add(new IssueComparator(couner, attribute, ordering));
		}

		return comparators;
	}

	@Test
	public void sortIssuesAllowsPartialSortingForEverySortingValue_Ascending() {

		Set<IssueComparator> comparators = createComparatorsForAllSortableAttributes(SortDirection.ASCENDING);

		PartialSorting sorting = new PartialSorting();
		sorting.setComparators(comparators);

		sorting.sortIssues(issues);

		assertEquals(3, issues.size());
		assertEquals(Issue1, issues.get(0));
		assertEquals(Issue2, issues.get(1));
		assertEquals(Issue3, issues.get(2));
	}

	@Test
	public void sortIssuesDescendingDateTypeProcessid() {

		Set<IssueComparator> comparators = new HashSet<IssueComparator>();
		comparators.add(new IssueComparator(1, SortableAttribute.ISSUE_TYPE, SortDirection.DESCENDING));
		comparators.add(new IssueComparator(2, SortableAttribute.COMPONENT, SortDirection.DESCENDING));
		comparators.add(new IssueComparator(3, SortableAttribute.OPEN_DATE, SortDirection.DESCENDING));

		PartialSorting sorting = new PartialSorting();
		sorting.setComparators(comparators);

		sorting.sortIssues(issues);

		assertEquals(3, issues.size());
		assertEquals(Issue3, issues.get(0));
		assertEquals(Issue2, issues.get(1));
		assertEquals(Issue1, issues.get(2));
	}

}
