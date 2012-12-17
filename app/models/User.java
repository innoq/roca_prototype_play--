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
    public String name;
    @OneToMany(cascade = CascadeType.PERSIST)
    public final List<Issue> issues = new ArrayList<Issue>();
    public static Finder<Long, User> find = new Finder<Long, User>(Long.class, User.class);

    public User(Long id) {
        this(id, null);
    }

    public User(String name) {
        this(null, name);
    }

    public User(Long id, String name) {
        super();
        this.id = id;
        this.name = name;
    }

    @Override
    public String toString() {
        return id + "";
    }

    public void addIssue(Issue issue) {
        issues.add(issue);
        Collections.sort(issues);
    }

    public List<Issue> getIssues() {
        return Collections.unmodifiableList(issues);
    }
}
