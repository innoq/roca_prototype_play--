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
import repository.RepositoryFactory;

import com.avaje.ebean.Ebean;

import controllers.AuthentificationAction;

@Entity
public class Issue extends Model implements Comparable<Issue> {

	private static final long serialVersionUID = 1L;

	@Id
	private Long id;

	private String projectName;

	private String priority;

	private String issueType;

	private String summary;

	@Lob
	private String exceptionStackTrace;

	@Column(length = 1000)
	private String description;

	private String reporter;

	private String componentName;

	private String componentVersion;

	@Enumerated(EnumType.STRING)
	private IssueProcessingState processingState;

	private Date openDate;

	private Date closeDate;

	@Enumerated(EnumType.STRING)
	private ExecutionAction closeAction;

	@ManyToOne()
	@JoinColumn(name = "USER_NAME")
	private User assignedUser;

	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	private List<ServiceArguments> arguments;

	@Lob
	private String comment;

	public Issue(Long id) {
		this.id = id;
	}

	public Issue() {
		super();
	}

	public IssueProcessingState getProcessingState() {
		return processingState;
	}

	public void setProcessingState(IssueProcessingState processingState) {
		this.processingState = processingState;
	}

	public Date getCloseDate() {
		return closeDate;
	}

	public void setCloseDate(Date closeDate) {
		this.closeDate = closeDate;
	}

	public ExecutionAction getCloseAction() {
		return closeAction;
	}

	public void setCloseAction(ExecutionAction closeAction) {
		this.closeAction = closeAction;
	}

	public User getAssignedUser() {
		return assignedUser;
	}

	public void setAssignedUser(User assignedUser) {
		this.assignedUser = assignedUser;
	}

	public List<ServiceArguments> getArguments() {
		return arguments;
	}

	public void setArguments(Map<String, String> arguments) {
		
		for (ServiceArguments argument : this.arguments) {
			RepositoryFactory.getRepository().delete(argument);
		}
		this.arguments.clear();

		for (Entry<String, String> item : arguments.entrySet()) {
			this.arguments.add(new ServiceArguments(item.getKey(), item.getValue()));
		}
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
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

	public Date getOpenDate() {
		return openDate;
	}

	@Override
	public int compareTo(Issue o) {
		return (int) (this.id - o.id);
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public void setPriority(String priority) {
		this.priority = priority;
	}

	public void setIssueType(String issueType) {
		this.issueType = issueType;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public void setExceptionStackTrace(String exceptionStackTrace) {
		this.exceptionStackTrace = exceptionStackTrace;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setReporter(String reporter) {
		this.reporter = reporter;
	}

	public void setComponentName(String componentName) {
		this.componentName = componentName;
	}

	public void setComponentVersion(String componentVersion) {
		this.componentVersion = componentVersion;
	}

	public void setOpenDate(Date openDate) {
		this.openDate = openDate;
	}

	public boolean isClosed() {
		return getProcessingState().equals(IssueProcessingState.CLOSED);
	}

	public boolean isOpen() {
		return getProcessingState().equals(IssueProcessingState.OPEN);
	}

	public boolean isAssignedToCurrentUser() {
		return (getProcessingState().equals(IssueProcessingState.CLAIMED) && getAssignedUser() != null) ? AuthentificationAction.getCurrentUserName().equals(
				getAssignedUser().getName()) : false;
	}

	public boolean isAssignedToOtherUser() {
		return (getProcessingState().equals(IssueProcessingState.CLAIMED) && getAssignedUser() != null) ? !AuthentificationAction.getCurrentUserName().equals(
				getAssignedUser().getName()) : false;
	}

}
