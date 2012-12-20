package models;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import play.db.ebean.Model;
import repository.Repository;

@Entity
public class Issue extends Model implements Comparable<Issue> {

    private static final long serialVersionUID = 1L;

    @Id
    public Long id;
    public String projectName;
    public String priority;
    public String issueType;
    public String summary;
    @Lob
    public String exceptionStackTrace;
    @Column(length = 1000)
    public String description;
    public String reporter;
    public String componentName;
    public String componentVersion;
    @Enumerated(EnumType.STRING)
    public IssueProcessingState processingState;
    public Date openDate;
    public Date closeDate;
    @Enumerated(EnumType.STRING)
    public ExecutionAction closeAction;
    @ManyToOne()
    @JoinColumn(name = "USER_NAME")
    public User assignedUser;
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    public List<ServiceArguments> arguments;
    @Lob
    public String comment;

    public Issue(Long id) {
        this.id = id;
    }

    public Issue() {
        super();
    }

    public Long getId() {
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

    public String getComment() {
        return comment;
    }

    public void setArguments(Map<String, String> arguments) {

        for (ServiceArguments argument : this.arguments) {
            Repository.getInstance().delete(argument);
        }
        this.arguments.clear();

        for (Entry<String, String> item : arguments.entrySet()) {
            this.arguments.add(new ServiceArguments(item.getKey(), item.getValue()));
        }
    }

    @Override
    public int compareTo(Issue o) {
        return (int) (this.id - o.id);
    }

    public boolean isClosed() {
        return processingState.equals(IssueProcessingState.CLOSED);
    }

    public boolean isOpen() {
        return processingState.equals(IssueProcessingState.OPEN);
    }

    public boolean isAssignedToCurrentUser(String currentUserName) {
        return (processingState.equals(IssueProcessingState.CLAIMED) && assignedUser != null) ? currentUserName.equals(
                assignedUser.name) : false;
    }

    public boolean isAssignedToOtherUser(String currentUserName) {
        return (processingState.equals(IssueProcessingState.CLAIMED) && assignedUser != null) ? !currentUserName.equals(
                assignedUser.name) : false;
    }

    public void closeAction() {
        processingState = IssueProcessingState.CLOSED;
        closeDate = new Date();
    }
}
