package app;

import controllers.GlobalConfiguration;
import models.Issue;
import models.User;
import play.Application;
import play.GlobalSettings;
import repository.Repository;
import tools.IssueGenerator;

import java.util.List;


/**
 * Common Play Global object for bootstrapping the application.
 * Initializes the repository and the GlobalConfiguration. Could provide the common error handling in a future version.
 */
public class Global extends GlobalSettings {

    @Override
    public void onStart(Application app) {
        super.onStart(app);

        GlobalConfiguration config = GlobalConfiguration.getInstance();
        config.init();

        IssueGenerator issueGenerator = IssueGenerator.getInstance();
        List<User> users = issueGenerator.createUsers();
        Repository.getInstance().saveAllUser(users);

        List<Issue> randomIssues = issueGenerator.createRandomIssues(config.getDefaultNumberOfIssues(), users);
        Repository.getInstance().saveAllIssues(randomIssues);
    }


}
