package models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import play.db.ebean.Model;

@Entity
public class ServiceArguments {

    public final String argumentId;
    public final String arguments;

    public ServiceArguments(String key, String value) {
        argumentId = key;
        arguments = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ServiceArguments that = (ServiceArguments) o;

        if (!argumentId.equals(that.argumentId)) return false;
        if (!arguments.equals(that.arguments)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = argumentId.hashCode();
        result = 31 * result + arguments.hashCode();
        return result;
    }
}
