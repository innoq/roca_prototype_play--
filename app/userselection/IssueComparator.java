package userselection;

import models.Issue;
import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.NullComparator;
import org.apache.commons.collections.comparators.ReverseComparator;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import userselection.PartialSorting.SortDirection;
import userselection.PartialSorting.SortableAttribute;

import java.util.Comparator;

/**
 * Comparator for Issues. Works with the bean notation (reflection) to reduce the boiler plate. IssueComparator are among each other comparable by their priority.
 */
final class IssueComparator extends BeanComparator implements Comparable<IssueComparator> {

    private static final long serialVersionUID = 1L;
    private final int priority;
    private final SortableAttribute attribute;
    private final SortDirection ordering;

    public IssueComparator(int priority, SortableAttribute attribute, SortDirection ordering) {
        super(attribute.getIssuePropertyName(), new NullComparator(true));
        this.priority = priority;
        this.attribute = attribute;
        this.ordering = ordering;
    }

    @Override
    public int compareTo(IssueComparator o) {
        return o.priority - this.priority;
    }

    @Override
    public int compare(Object arg0, Object arg1) {
        int x = (ordering.equals(SortDirection.DESCENDING)) ? -1 : 1;
        return x * super.compare(arg0, arg1);
    }

    @SuppressWarnings("unchecked")
    @Override
    public Comparator<Issue> getComparator() {
        return (ordering.equals(SortDirection.DESCENDING)) ? new ReverseComparator(super.getComparator()) : super.getComparator();
    }

    public int getPriority() {
        return priority;
    }

    public SortableAttribute getAttribute() {
        return attribute;
    }

    public SortDirection getOrdering() {
        return ordering;
    }

    public String unbind(String key) {
        return StringUtils.join(new String[]{key + "=" + getPriority() + "", getAttribute().name(), getOrdering().name()}, ",");
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((attribute == null) ? 0 : attribute.hashCode());
        result = prime * result + ((ordering == null) ? 0 : ordering.hashCode());
        result = prime * result + priority;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!super.equals(obj))
            return false;
        if (getClass() != obj.getClass())
            return false;
        IssueComparator other = (IssueComparator) obj;
        if (attribute != other.attribute)
            return false;
        if (ordering != other.ordering)
            return false;
        if (priority != other.priority)
            return false;
        return true;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SIMPLE_STYLE);
    }
}