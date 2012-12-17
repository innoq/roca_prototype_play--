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

		assertEquals(issue.id, dbIssue.id);
		assertEquals(issue.projectName, dbIssue.projectName);
		assertEquals(issue.priority, dbIssue.priority);
		assertEquals(issue.issueType, dbIssue.issueType);
		assertEquals(issue.summary, dbIssue.summary);
		assertEquals(issue.exceptionStackTrace, dbIssue.exceptionStackTrace);
		assertEquals(issue.description, dbIssue.description);
		assertEquals(issue.reporter, dbIssue.reporter);
		assertEquals(issue.componentName, dbIssue.componentName);
		assertEquals(issue.componentVersion, dbIssue.componentVersion);
		assertEquals(issue.processingState, dbIssue.processingState);
		assertEquals(issue.openDate, dbIssue.openDate);
		assertEquals(issue.closeDate, dbIssue.closeDate);
		assertEquals(issue.closeAction, dbIssue.closeAction);
		assertEquals(issue.assignedUser, dbIssue.assignedUser);
		assertEquals(issue.arguments, dbIssue.arguments);
		assertEquals(issue.comment, dbIssue.comment);
	}
}
