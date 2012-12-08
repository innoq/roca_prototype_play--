package integration;

import models.Issue;
import models.User;

import org.junit.Test;
import static org.junit.Assert.*;

import com.avaje.ebean.Ebean;

public class UserModelIntTest extends PlayBaseTest {

	@Test
	public void findSavedUser() {
		User user = new User(1L);
		Ebean.save(user);

		User dbUser = User.find.byId(1L);
		assertEquals(user, dbUser);
	}

	@Test
	public void everyUserCouldHave2ErrorIssues() {

		User user = new User(1L);
		Issue issue1 = new Issue(1L);
		Issue issue2 = new Issue(2L);

		user.addIssue(issue1);
		user.addIssue(issue2);

		Ebean.save(user);

		User dbUser = User.find.byId(1L);
		assertEquals(user, dbUser);
		assertEquals(issue1, dbUser.getIssues().get(0));
		assertEquals(issue2, dbUser.getIssues().get(1));
	}
}
