package forms;

import static org.junit.Assert.assertEquals;

import integration.PlayBaseTest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import models.Issue;

import org.junit.Test;

import forms.PaginationFilter;

import play.libs.F.Option;

public class PaginationFilterTest extends PlayBaseTest {

	private static final String PAGE_QUERY_PARAM = "page";
	private final List<Issue> issues = new ArrayList<Issue>();
	private final Map<String, String[]> queryString = new HashMap<String, String[]>();

	@Test
	public void bindCreatesAnCorrectPaginationFilter() {

		queryString.put(PAGE_QUERY_PARAM, new String[] { "2" });

		Option<PaginationFilter> option = new PaginationFilter().bind(PAGE_QUERY_PARAM, queryString);

		assertEquals(true, option.isDefined());
		PaginationFilter paginationFilter = option.get();
		assertEquals(10, paginationFilter.getPageSize());
		assertEquals(2, paginationFilter.getPageNumber());
	}

	@Test
	public void bindCreatesAnCorrectDefaultPaginationFilter() {

		queryString.put(PAGE_QUERY_PARAM, null);

		Option<PaginationFilter> option = new PaginationFilter().bind(PAGE_QUERY_PARAM, queryString);

		assertEquals(true, option.isDefined());
		PaginationFilter paginationFilter = option.get();
		assertEquals(10, paginationFilter.getPageSize());
		assertEquals(1, paginationFilter.getPageNumber());
	}

	@Test
	public void unbindCreatesTheCorrectQueryString() {

		PaginationFilter filter = new PaginationFilter();
		filter.setPageNumber(50);
		filter.setPageSize(10);

		String queryString = filter.unbind(PAGE_QUERY_PARAM);
		assertEquals("page=50", queryString);
	}

	@Test
	public void unbindWithNewCreatedFilterCreatesDefaultString() {

		PaginationFilter filter = new PaginationFilter();

		String queryString = filter.unbind(PAGE_QUERY_PARAM);
		assertEquals("page=1", queryString);
	}

	@Test
	public void reduceIssuesToRequestedPage_ResultsInTheOriginalIssueListIfSizeSmaller10() {

		addNumberOfIssues(issues, 9);

		PaginationFilter paginationFilter = new PaginationFilter();
		paginationFilter.setPageNumber(1);
		paginationFilter.setPageSize(10);

		paginationFilter.filterIssues(issues);

		assertEquals(9, issues.size());
	}

	@Test
	public void reduceIssuesToRequestedPage_ResultsInTheOriginalIssues() {

		addNumberOfIssues(issues, 90);

		PaginationFilter paginationFilter = new PaginationFilter();
		paginationFilter.setPageNumber(1);
		paginationFilter.setPageSize(10);

		paginationFilter.filterIssues(issues);

		assertEquals(10, issues.size());
		assertEquals(0, issues.get(0).id.intValue());
		assertEquals(1, issues.get(1).id.intValue());
		assertEquals(2, issues.get(2).id.intValue());
		assertEquals(3, issues.get(3).id.intValue());
		assertEquals(4, issues.get(4).id.intValue());
		assertEquals(5, issues.get(5).id.intValue());
		assertEquals(6, issues.get(6).id.intValue());
		assertEquals(7, issues.get(7).id.intValue());
		assertEquals(8, issues.get(8).id.intValue());
		assertEquals(9, issues.get(9).id.intValue());
	}

	@Test
	public void reduceIssuesToRequestedPage_ResultsInTheOriginalEmptyListIfThereAreNoIssues() {

		addNumberOfIssues(issues, 0);

		PaginationFilter paginationFilter = new PaginationFilter();
		paginationFilter.setPageNumber(1);
		paginationFilter.setPageSize(10);

		paginationFilter.filterIssues(issues);

		assertEquals(0, issues.size());
	}

	@Test
	public void reduceIssuesToRequestedPage_ResultsInIssue81until90IfPageIs9() {

		addNumberOfIssues(issues, 100);

		PaginationFilter paginationFilter = new PaginationFilter();
		paginationFilter.setPageNumber(9);
		paginationFilter.setPageSize(10);

		paginationFilter.filterIssues(issues);

		assertEquals(10, issues.size());
		assertEquals(80, issues.get(0).id.intValue());
		assertEquals(81, issues.get(1).id.intValue());
		assertEquals(82, issues.get(2).id.intValue());
		assertEquals(83, issues.get(3).id.intValue());
		assertEquals(84, issues.get(4).id.intValue());
		assertEquals(85, issues.get(5).id.intValue());
		assertEquals(86, issues.get(6).id.intValue());
		assertEquals(87, issues.get(7).id.intValue());
		assertEquals(88, issues.get(8).id.intValue());
		assertEquals(89, issues.get(9).id.intValue());
	}

	private void addNumberOfIssues(List<Issue> issues, int i) {

		for (int j = 0; j < i; j++) {
			issues.add(new Issue(new Long(j)));
		}
	}
}
