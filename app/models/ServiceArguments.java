package models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import play.db.ebean.Model;

@Entity
public class ServiceArguments extends Model {

	private static final long serialVersionUID = 1L;

	@Id
	private Long id;

	private final String argumentId;

	@Column(length = 1000)
	private final String arguments;

	public ServiceArguments(String key, String value) {
		argumentId = key;
		arguments = value;
	}

	public Long getId() {
		return id;
	}

	public String getArgumentId() {
		return argumentId;
	}

	public String getArguments() {
		return arguments;
	}

}
