package integration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

import java.util.List;

import models.Issue;

import org.junit.Test;

import repository.RepositoryFactory;

import com.avaje.ebean.Ebean;

public class IssueModelIntTest extends PlayBaseTest {

	@Test
	public void issuesSupportEquals() {

		Issue dummyIssue1 = TestUtil.createDummyIssue(1L);
		Issue dummyIssue2 = TestUtil.createDummyIssue(1L);
		assertEquals(dummyIssue2, dummyIssue1);
	}

	@Test
	public void errorIssueCouldBePersisted() {

		Issue errorIssue = TestUtil.createDummyIssue(2L);
		Ebean.save(errorIssue);

		List<Issue> allIssues = RepositoryFactory.getRepository().getAll();
		assertEquals(allIssues.get(0), errorIssue);
	}

	@Test
	public void errorIssueAttributesArePersisted() {

		Issue issue = TestUtil.createDummyIssue(3L);
		Ebean.save(issue);

		List<Issue> allIssues = RepositoryFactory.getRepository().getAll();

		assertSame(allIssues.size(), 1);
		Issue dbIssue = allIssues.get(0);

		assertEquals(issue.getId(), dbIssue.getId());
		assertEquals(issue.getProjectName(), dbIssue.getProjectName());
		assertEquals(issue.getPriority(), dbIssue.getPriority());
		assertEquals(issue.getIssueType(), dbIssue.getIssueType());
		assertEquals(issue.getSummary(), dbIssue.getSummary());
		assertEquals(issue.getExceptionStackTrace(), dbIssue.getExceptionStackTrace());
		assertEquals(issue.getDescription(), dbIssue.getDescription());
		assertEquals(issue.getReporter(), dbIssue.getReporter());
		assertEquals(issue.getComponentName(), dbIssue.getComponentName());
		assertEquals(issue.getComponentVersion(), dbIssue.getComponentVersion());
		assertEquals(issue.getProcessingState(), dbIssue.getProcessingState());
		assertEquals(issue.getOpenDate(), dbIssue.getOpenDate());
		assertEquals(issue.getCloseDate(), dbIssue.getCloseDate());
		assertEquals(issue.getCloseAction(), dbIssue.getCloseAction());
		assertEquals(issue.getAssignedUser(), dbIssue.getAssignedUser());
		assertEquals(issue.getArguments(), dbIssue.getArguments());
		assertEquals(issue.getComment(), dbIssue.getComment());
	}
}
