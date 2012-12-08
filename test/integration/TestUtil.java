package integration;

import java.util.Collections;
import java.util.Date;

import models.ExecutionAction;
import models.Issue;
import models.IssueProcessingState;
import models.User;

public class TestUtil {

	static Issue createDummyIssue(Long id) {
		Issue issue = new Issue(id);
		issue.setProjectName("test");
		issue.setPriority("test");
		issue.setSummary("test");
		issue.setIssueType("test");
		issue.setExceptionStackTrace("test");
		issue.setReporter("test");
		issue.setComponentName("test");
		issue.setComponentVersion("test");
		issue.setProcessingState(IssueProcessingState.CLAIMED);
		issue.setOpenDate(new Date());
		issue.setCloseDate(new Date());
		issue.setCloseAction(ExecutionAction.ABORT);
		issue.setAssignedUser(User.find.byId(TEST_USER.getId()));
		issue.setArguments(Collections.<String, String> emptyMap());
		issue.setDescription("test");

		return issue;
	}

	static final User TEST_USER = new User(1L, "test");

}
