package models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import play.db.ebean.Model;

@Entity
public class User  {

    private static final long serialVersionUID = 1L;
    public String name;
    public final List<Issue> issues = new ArrayList<Issue>();


    public User(String name) {
        super();
        this.name = name;
    }

    @Override
    public String toString() {
        return String.valueOf(name);
    }

    public void addIssue(Issue issue) {
        issues.add(issue);
        Collections.sort(issues);
    }

    public List<Issue> getIssues() {
        return Collections.unmodifiableList(issues);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        if (!name.equals(user.name)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }
}
