package repository;

import java.util.Collection;
import java.util.List;

import models.Issue;
import models.User;
import play.db.ebean.Model;

/**
 * Repository zum Zugriff auf den unterliegenden Datastore.
 * 
 */
public interface Repository {

	void save(Model model);

	void save(Collection<? extends Model> models);

	Issue findIssueById(Long id);

	User findUserByName(String name);

	List<Issue> getAll();

	User getRandomUser();

	void init();

	void delete(Model model);
}
