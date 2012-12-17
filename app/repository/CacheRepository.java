package repository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import models.Issue;
import models.ServiceArguments;
import models.User;
import play.db.ebean.Model;
import tools.IssueGenerator;

/**
 * Ein {@link CacheRepository} haelt alle Model Objekte in einer in memory
 * Datenstruktur.
 *
 */
public class CacheRepository implements Repository {

    private final Map<Long, Issue> issueCache = new ConcurrentHashMap<Long, Issue>();
    private final Map<String, User> userCache = new ConcurrentHashMap<String, User>();

    @Override
    public Issue findIssueById(Long id) {
        return issueCache.get(id);
    }

    @Override
    public List<Issue> getAll() {
        return new ArrayList<Issue>(issueCache.values());
    }

    @Override
    public User getRandomUser() {
        return userCache.entrySet().iterator().next().getValue();
    }

    @Override
    public void save(Model model) {

        if (model instanceof User) {
            User user = (User) model;
            userCache.put(user.name, user);
        } else if (model instanceof Issue) {
            Issue issue = (Issue) model;
            issueCache.put(issue.id, issue);
        } else {
            throw new IllegalArgumentException("only users and issues are supported by the cache repository");
        }

    }

    @Override
    public void save(Collection<? extends Model> models) {
        for (Model model : models) {
            save(model);
        }
    }

    @Override
    public void init() {
        // fill
        IssueGenerator issueGenerator = new IssueGenerator(new Random());
        List<User> users = issueGenerator.createUsers();
        save(users);
        List<Issue> issues = issueGenerator.createRandomIssues(new Random().nextInt(1000 - 100 + 1) + 100, users);
        save(issues);
    }

    @Override
    public User findUserByName(String name) {
        return userCache.get(name);
    }

    @Override
    public void delete(Model model) {

        if (model instanceof User) {
            User user = (User) model;
            userCache.remove(user.name);
        } else if (model instanceof Issue) {
            Issue issue = (Issue) model;
            issueCache.remove(issue.id);
        } else if (model instanceof ServiceArguments) {
            // nothing should happen
            // TODO better solution is desirable
        } else {
            throw new IllegalArgumentException("only users and issues are supported by the cache repository");
        }
    }
}
