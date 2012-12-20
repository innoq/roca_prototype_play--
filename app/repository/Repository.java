package repository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import models.Issue;
import models.ServiceArguments;
import models.User;
import play.db.ebean.Model;
import tools.IssueGenerator;

/**
 * Ein {@link Repository} haelt alle Model Objekte in einer in memory
 * Datenstruktur.
 */
public final class Repository {

    private final Map<Integer, Issue> issueCache = new ConcurrentHashMap<Integer,Issue>();
    private final Map<String, User> userCache = new ConcurrentHashMap<String, User>();

    private final static Repository INSTANCE = new Repository();

    public final static Repository getInstance() {
        return INSTANCE;
    }

    private Repository() {
        super();
    }


    public Issue findIssueById(int id) {
        return issueCache.get(id);
    }


    public List<Issue> getAllIssues() {
        return new ArrayList<Issue>(issueCache.values());
    }

    public List<User> getAllUser() {
        return new ArrayList<User>(userCache.values());
    }


    public User getRandomUser() {
        return userCache.entrySet().iterator().next().getValue();
    }


    public void save(User user) {
        userCache.put(user.name, user);
    }


    public void save(Issue issue) {
        issueCache.put(issue.id, issue);
    }

    public void saveAllIssues(Collection<Issue> issues) {
        for (Issue issue : issues) {
            save(issue);
        }
    }


    public void saveAllUser(Collection<User> users) {
        for (User user : users) {
            save(user);
        }
    }


    public User findUserByName(String name) {
        return userCache.get(name);
    }

   public void deleteUser(User user) {
       userCache.remove(user);
   }

    public void deleteAllIssues() {
        issueCache.clear();

    }

    public void deleteIssue(Issue issue) {
        issueCache.remove(issue);
    }
}
