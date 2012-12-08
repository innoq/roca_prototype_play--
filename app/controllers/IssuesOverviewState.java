package controllers;

import models.Issue;
import models.IssueProcessingState;

/**
 * Status die in der View dargestellt werden. Diese mappen auf die 3 Status die
 * ein Issue einnehmen kann {@link IssueProcessingState}.
 */
public enum IssuesOverviewState {

	ALL("issues"), CLOSED("issues/closed"), OPEN("issues/open"), ASSIGNED_CURRENT_USER("issues/claimed/.*/assigned") {

		@Override
		public String unbind() {
			return "issues/claimed/" + AuthentificationAction.getCurrentUserName() + "/assigned";
		}

	}

	,
	ASSIGNED_OTHERS("issues/claimed/.*/notassigned") {

		@Override
		public String unbind() {
			return "issues/claimed/" + AuthentificationAction.getCurrentUserName() + "/notassigned";
		}

	};

	private final String regex;

	private IssuesOverviewState(String regex) {
		this.regex = regex;
	}

	public String getRegex() {
		return regex;
	}

	public String unbind() {
		return getRegex();
	}

	public static IssuesOverviewState getByIssue(Issue issue) {
		if (issue.isOpen()) {
			return OPEN;
		} else if (issue.isClosed()) {
			return CLOSED;
		} else if (issue.isAssignedToCurrentUser()) {
			return ASSIGNED_CURRENT_USER;
		} else if (issue.isAssignedToOtherUser()) {
			return ASSIGNED_OTHERS;
		}

		throw new IllegalStateException("unknown issue state");
	}
}
