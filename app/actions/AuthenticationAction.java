package actions;

import models.User;
import play.mvc.Controller;
import play.mvc.Http.Context;
import play.mvc.Result;
import repository.Repository;

/**
 * Provides the "disabled" authentication for the app.
 * Security is not a relevant concern in this prototype so this class returns always the same random user.
 */
public class AuthenticationAction extends play.mvc.Action<AuthenticationAction> {

    private static final String USERNAME = "username";

    public static String getCurrentUserName() {
        return Controller.session(USERNAME);
    }

    @Override
    public Result call(Context context) throws Throwable {
        String username = USERNAME;
        if (!context.session().containsValue(username)) {
            User user = Repository.getInstance().getRandomUser();
            context.session().put(username, user.name);
        }

        return delegate.call(context);
    }

}
