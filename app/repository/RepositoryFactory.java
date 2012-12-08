package repository;

import repository.CacheRepository;
import repository.DatabaseRepository;
import repository.Repository;

/**
 * Factory um den switch zwischen den beiden Repositoryarten zu ermoeglichen.
 * Alle unterstuetzten Modi sind in {@link Mode} hinterlegt und ein switch kann
 * ueber notmyissue.propertie erfolgen.
 * 
 */
public class RepositoryFactory {

	public enum Mode {
		CACHE, DB
	}

	private static Repository repository = new DatabaseRepository();

	public static void setMode(Mode mode) {

		switch (mode) {
		case CACHE:
			repository = new CacheRepository();
			return;
		case DB:
			repository = new DatabaseRepository();
			return;
		default:
			throw new IllegalStateException("unknown state!");
		}
	}

	public static Repository getRepository() {
		return repository;
	}
}
