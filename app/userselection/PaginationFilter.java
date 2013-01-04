package userselection;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import java.util.Map;
import models.Issue;
import org.apache.commons.collections.CollectionUtils;

import org.apache.commons.lang3.tuple.Pair;

import play.libs.F.Option;
import play.mvc.QueryStringBindable;

/**
 * Provides the selection of the subset of issues which should be displayed on a specific page.
 * Also stores the overall information of visible pages.
 */
public class PaginationFilter implements QueryStringBindable<PaginationFilter> {

    private static final int PAGE_SIZE = 10;

    private int pageSize = 10;

    private int pageNumber = 1;

    private int maxPage;

    public PaginationFilter() {
        super();
    }

    public PaginationFilter(int pageNumber) {
        super();
        this.pageNumber = pageNumber;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    /**
     * Returns the quantity of for the user reachable pages by pairs of display names and site numbers.
     *
     * @return the quantity of reachable sites for the user.
     */
    public List<Pair<String, Integer>> getVisiblePages() {

        Pair<String, Integer> endPair = Pair.of("Last", maxPage);
        Pair<String, Integer> startPair = Pair.of("First", 1);
        List<Pair<String, Integer>> visiblePages = new ArrayList<Pair<String, Integer>>();

        if (pageNumber == 1) {
            addPagesFromUntilTo(1, Math.min(maxPage, 1 + 4), visiblePages);
            visiblePages.add(endPair);
            return visiblePages;
        } else if (pageNumber == maxPage) {
            visiblePages.add(startPair);
            addPagesFromUntilTo(Math.max(1, maxPage - 4), maxPage, visiblePages);
            return visiblePages;
        } else {
            visiblePages.add(startPair);
            addPagesFromUntilTo(Math.max(1, pageNumber - 2), Math.min(maxPage, pageNumber + 2), visiblePages);
            visiblePages.add(endPair);
            return visiblePages;
        }
    }

    private void addPagesFromUntilTo(int from, int until, List<Pair<String, Integer>> visiblePages) {
        while (from <= until) {
            visiblePages.add(Pair.of(String.valueOf(from), from));
            from++;
        }
    }

    @Override
    public String toString() {
        return "PaginationFilter [pageSize=" + pageSize + ", pageNumber=" + pageNumber + "]";
    }

    @Override
    public Option<PaginationFilter> bind(String key, Map<String, String[]> data) {
        String[] params = data.get(key);

        if (params == null || params.length == 0 || params.length > 1) {
            return Option.Some(createFilter(1));
        }

        return Option.Some(createFilter(Integer.valueOf(params[0])));
    }

    private static PaginationFilter createFilter(int pageNumber) {
        PaginationFilter filter = new PaginationFilter();
        filter.setPageNumber(pageNumber);
        filter.setPageSize(PAGE_SIZE);
        return filter;
    }

    @Override
    public String unbind(String key) {
        return key + "=" + pageNumber;
    }

    @Override
    public String javascriptUnbind() {
        throw new UnsupportedOperationException("JavaScript unbind is not supported!");
    }

    public void filterIssues(List<Issue> issues) {

        maxPage = (int) Math.ceil(issues.size() / pageSize);

        int lowerBound = Math.min((pageNumber - 1) * pageSize, issues.size());
        int upperBound = Math.min(lowerBound + 10, issues.size());

        List<Issue> saveList = issues.subList(lowerBound, upperBound);
        Collection<?> disjunction = CollectionUtils.disjunction(issues, saveList);
        issues.removeAll(disjunction);
    }

}
