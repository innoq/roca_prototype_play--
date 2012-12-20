package app;

import play.Application;
import play.GlobalSettings;
import repository.Repository;


/**
 * Play Global Objekt zum bootstrapping der Anwendung. Kuemmert sich aktuell vor
 * allem um das Initialisieren des Repositories.
 * 
 */
public class Global extends GlobalSettings {

	@Override
	public void onStart(Application app) {
		super.onStart(app);
        Repository.getInstance().init();
	}
}
