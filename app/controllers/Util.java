package controllers;

/**
 * Util, always bad practice, always needed.
 *
 */
public final class Util {

    private Util(){
        super();
    }


    /**
     * Count the minimal number of lines a textarea should provide to display the given string.
     *
     * @param string the string to display
     * @return the number of lines to display
     */
    public static int countLines(final String string) {
        return string == null ? 5 : Math.max(5, string.split(System.getProperty("line.separator")).length);
    }
}
