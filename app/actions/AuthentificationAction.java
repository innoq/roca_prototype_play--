package actions;

import models.User;
import play.mvc.Controller;
import play.mvc.Http.Context;
import play.mvc.Result;
import repository.Repository;

/**
 * Regelt die User Autentifizierung. Da diese praktisch diabled ist wird immer
 * ein beliebiger User zurueckgegeben
 * 
 */
public class AuthentificationAction extends play.mvc.Action<AuthentificationAction> {

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
