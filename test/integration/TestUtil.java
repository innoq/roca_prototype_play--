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
		issue.projectName = "test";
		issue.priority = "test";
		issue.summary = "test";
		issue.issueType = "test";
		issue.exceptionStackTrace = "test";
		issue.reporter = "test";
		issue.componentName = "test";
		issue.componentVersion = "test";
		issue.processingState = IssueProcessingState.CLAIMED;
		issue.openDate = new Date();
		issue.closeDate = new Date();
		issue.closeAction = ExecutionAction.ABORT;
		issue.assignedUser = User.find.byId(TEST_USER.id);
		issue.setArguments(Collections.<String, String> emptyMap());
		issue.description = "test";

		return issue;
	}

	static final User TEST_USER = new User(1L, "test");

}
