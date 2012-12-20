package models;

import java.util.*;
import java.util.Map.Entry;

public class Issue implements Comparable<Issue> {

    public int id;
    public String projectName;
    public String priority;
    public String issueType;
    public String summary;
    public String exceptionStackTrace;
    public String description;
    public String reporter;
    public String componentName;
    public String componentVersion;
    public IssueProcessingState processingState;
    public Date openDate;
    public Date closeDate;
    public ExecutionAction closeAction;
    public User assignedUser;
    public List<ServiceArguments> arguments;
    public String comment;


    public Issue(int id) {
        super();
        this.id = id;
        arguments = new ArrayList<ServiceArguments>();
    }

    public int getId() {
        return id;
    }

    public String getProjectName() {
        return projectName;
    }

    public String getPriority() {
        return priority;
    }

    public String getIssueType() {
        return issueType;
    }

    public String getSummary() {
        return summary;
    }

    public String getExceptionStackTrace() {
        return exceptionStackTrace;
    }

    public String getDescription() {
        return description;
    }

    public String getReporter() {
        return reporter;
    }

    public String getComponentName() {
        return componentName;
    }

    public String getComponentVersion() {
        return componentVersion;
    }

    public IssueProcessingState getProcessingState() {
        return processingState;
    }

    public Date getOpenDate() {
        return openDate;
    }

    public Date getCloseDate() {
        return closeDate;
    }

    public ExecutionAction getCloseAction() {
        return closeAction;
    }

    public User getAssignedUser() {
        return assignedUser;
    }

    public List<ServiceArguments> getArguments() {
        return arguments;
    }

    public void setArguments(Map<String, String> arguments) {

        this.arguments.clear();

        for (Entry<String, String> item : arguments.entrySet()) {
            this.arguments.add(new ServiceArguments(item.getKey(), item.getValue()));
        }
    }

    public String getComment() {
        return comment;
    }

    @Override
    public int compareTo(Issue o) {
        return this.id - o.id;
    }

    public boolean isClosed() {
        return processingState.equals(IssueProcessingState.CLOSED);
    }

    public boolean isOpen() {
        return processingState.equals(IssueProcessingState.OPEN);
    }

    public boolean isAssignedToCurrentUser(String currentUserName) {
        return (processingState.equals(IssueProcessingState.CLAIMED) && assignedUser != null) && currentUserName.equals(
                assignedUser.name);
    }

    public boolean isAssignedToOtherUser(String currentUserName) {
        return (processingState.equals(IssueProcessingState.CLAIMED) && assignedUser != null) && !currentUserName.equals(
                assignedUser.name);
    }

    public void closeAction() {
        processingState = IssueProcessingState.CLOSED;
        closeDate = new Date();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Issue issue = (Issue) o;

        if (id != issue.id) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return id;
    }
}
