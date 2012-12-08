package models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import play.db.ebean.Model;

@Entity
public class User extends Model {

	private static final long serialVersionUID = 1L;

	@Id
	public Long id;

	private String name;

	@OneToMany(cascade = CascadeType.PERSIST)
	private final List<Issue> issues = new ArrayList<Issue>();

	public User(Long id) {
		super();
		this.id = id;
	}

	public User(String name) {
		this.name = name;
	}

	public User(Long id, String name) {
		this.id = id;
		this.name = name;
	}

	public static Finder<Long, User> find = new Finder<Long, User>(Long.class, User.class);

	@Override
	public String toString() {
		return id + "";
	}

	public Long getId() {
		return id;
	}

	public void addIssue(Issue issue) {
		issues.add(issue);
		Collections.sort(issues);
	}

	public List<Issue> getIssues() {
		return Collections.unmodifiableList(issues);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
