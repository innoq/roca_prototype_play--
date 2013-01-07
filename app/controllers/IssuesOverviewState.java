package controllers;

import actions.AuthenticationAction;
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
			return "issues/claimed/" + AuthenticationAction.getCurrentUserName() + "/assigned";
		}

	}

	,
	ASSIGNED_OTHERS("issues/claimed/.*/notassigned") {

		@Override
		public String unbind() {
			return "issues/claimed/" + AuthenticationAction.getCurrentUserName() + "/notassigned";
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
		} else if (issue.isAssignedToCurrentUser(AuthenticationAction.getCurrentUserName())) {
			return ASSIGNED_CURRENT_USER;
		} else if (issue.isAssignedToOtherUser(AuthenticationAction.getCurrentUserName())) {
			return ASSIGNED_OTHERS;
		}

		throw new IllegalStateException("unknown issue state");
	}

    public boolean isAssignedOtherUser() {
        return ASSIGNED_OTHERS.equals(this);
    }

    public boolean isAssignedCurrentUser() {
        return IssuesOverviewState.ASSIGNED_CURRENT_USER.equals(this);
    }


    public boolean isOpen() {
        return IssuesOverviewState.OPEN.equals(this);
    }

    public boolean isClosed() {
        return IssuesOverviewState.CLOSED.equals(this);
    }
}
