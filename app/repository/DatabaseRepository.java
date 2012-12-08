package repository;

import java.util.Collection;
import java.util.List;
import java.util.Random;

import models.Issue;
import models.IssueGenerator;
import models.User;
import play.db.ebean.Model;
import play.db.ebean.Model.Finder;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.EbeanServer;
import com.avaje.ebean.config.ServerConfig;
import com.avaje.ebean.config.dbplatform.H2Platform;
import com.avaje.ebeaninternal.api.SpiEbeanServer;
import com.avaje.ebeaninternal.server.ddl.DdlGenerator;

/**
 * Das {@link DatabaseRepository} ermoeglicht den Zugriff auf eine unterliegende
 * Datenbank. In diesem Fall h2.
 * 
 */
public class DatabaseRepository implements Repository {

	private final Finder<Long, Issue> findIssues = new Finder<Long, Issue>(Long.class, Issue.class);

	private final Finder<Long, User> findUsers = new Finder<Long, User>(Long.class, User.class);

	@Override
	public void save(Model model) {
		Ebean.save(model);
	}

	@Override
	public Issue findIssueById(Long id) {
		return findIssues.byId(id);
	}

	@Override
	public List<Issue> getAll() {
		return findIssues.all();
	}

	@Override
	public User getRandomUser() {
		return findUsers.all().iterator().next();
	}

	@Override
	public void save(Collection<? extends Model> models) {
		Ebean.save(models);
	}

	@Override
	public void init() {
		EbeanServer server = Ebean.getServer("default");

		ServerConfig config = new ServerConfig();
		config.setDebugSql(true);

		DdlGenerator ddl = new DdlGenerator((SpiEbeanServer) server, new H2Platform(), config);

		// drop
		String dropScript = ddl.generateDropDdl();
		ddl.runScript(false, dropScript);

		// create
		String createScript = ddl.generateCreateDdl();
		ddl.runScript(false, createScript);

		// fill
		IssueGenerator issueGenerator = new IssueGenerator(new Random());
		List<User> users = issueGenerator.createUsers();
		save(users);
		List<Issue> issues = issueGenerator.createRandomIssues(new Random().nextInt(1000 - 100 + 1) + 100, users);
		save(issues);
	}

	@Override
	public User findUserByName(String name) {
		return findUsers.where().eq("name", name).findUnique();
	}

	@Override
	public void delete(Model model) {
		Ebean.delete(model);
	}

}
