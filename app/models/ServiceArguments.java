package models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import play.db.ebean.Model;

@Entity
public class ServiceArguments extends Model {

    private static final long serialVersionUID = 1L;
    @Id
    public Long id;
    public final String argumentId;
    @Column(length = 1000)
    public final String arguments;

    public ServiceArguments(String key, String value) {
        argumentId = key;
        arguments = value;
    }
}
