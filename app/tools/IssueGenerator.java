package tools;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

import models.ExecutionAction;
import models.Issue;
import models.IssueProcessingState;
import models.User;

/**
 * Generator zum Erstellen von Issues, im Wesentlichen uebernommen vom jersey
 * Prototypen.
 *
 *
 */
public final class IssueGenerator {


    private  final static IssueGenerator INSTANCE = new IssueGenerator();
    public final AtomicInteger idCounter = new AtomicInteger();


    final Date now = new Date();
    // vor hundert Tagen
    final Date onceUponATime = new Date(now.getTime() - 100L * 24L * 60L * 60L * 1000L);
    final Random random = new Random();
    private static final List<Character> CHAR_TABLE = createCharTable();
    private static String EXCEPTION_STACK_TRACE = createExceptionStackTrace();
    private static String[] PERSONS = {"dev5878", "dev5879", "dev13425", "dev501", "dev60001", "dev621", "dev53"};
    private static String[] SUMMARIES = {"Attribute name is wrong", "Current Performance is unacceptable ", "New Attribute required",
        "Strange Concurrency behaviour", "Inconsistent Data in Table DATA_ALL", "Application server does not start"};
    private static String[] TYPES = {"Error", "New Feature", "Task", "Code Quality", "Architecture"};
    private static String[] COMPONENTS = {"Order Managment", "Customer Management", "Data Export", "Staff Management", "Product Management",
        "Data Export", "Billing", "Reports", "Asset Management"};
    private static String[] VERSIONS = {"1.0", "1.2", "1.1", "2.0.1", "2.0.12232", "3.1", "3.2.1", "1.6.8"};
    private static String[] PROJECT_NAMES = {"WAHN III", "ROCKET", "IMPROVE", "DISCOVER", "MAGENTA", "SENATOR", "ROCA"};
    private static String[] PRIORITIES = {"HIGH", "URGENT", "CRASH", "LOW", "MEDIUM", "VERY VERY LOW", "MEDIUM HIGH", "MEDIUM LOW"};
    private static final String[][] argumentNameLists = {{"oneArgument", "anOtherArgument"},
        {"firstArgument", "secondArgument", "thirdArgument"}, {}, {"oneLonelyArgument"}};

    private IssueGenerator() {
        super();
    }


    public static IssueGenerator getInstance() {
        return INSTANCE;
    }

    public void reset(){
        idCounter.set(0);
    }

    public List<User> createUsers() {

        List<User> users = new ArrayList<User>();
        for (String username : PERSONS) {
            User user = new User(username);
            users.add(user);
        }
        return users;
    }

    public List<Issue> createRandomIssues(final int number, Collection<User> users) {
        if (number < 0) {
            throw new IllegalArgumentException("number must not be negative");
        }

        final List<Issue> randomIssues = new ArrayList<Issue>();

        for (int i = 0; i < number; ++i) {

            final IssueProcessingState randomProcessingState = selectRandomly(IssueProcessingState.values());
            final Date randomOpenDate = createRandomDate(onceUponATime, now);
            final Date randomCloseDate;
            final ExecutionAction randomCloseAction;

            final User randomAssignedUser;
            if (randomProcessingState == IssueProcessingState.CLAIMED) {
                randomAssignedUser = selectRandomly(users.toArray(new User[]{}));
                randomCloseDate = null;
                randomCloseAction = selectRandomly(ExecutionAction.values());
            } else if (randomProcessingState == IssueProcessingState.CLOSED) {
                randomAssignedUser = selectRandomly(users.toArray(new User[]{}));
                randomCloseDate = createRandomDate(randomOpenDate, now);
                randomCloseAction = selectRandomly(ExecutionAction.values());
            } else {
                randomAssignedUser = null;
                randomCloseDate = null;
                randomCloseAction = ExecutionAction.RETRY;
            }

            final Issue issue = new Issue(createNextId());
            issue.projectName = selectRandomly(PROJECT_NAMES);
            issue.priority = selectRandomly(PRIORITIES);
            issue.summary = selectRandomly(SUMMARIES);
            issue.issueType = selectRandomly(TYPES);
            issue.exceptionStackTrace = createRandomExceptionStackTrace(60);
            issue.reporter = selectRandomly(PERSONS);
            issue.componentName = selectRandomly(COMPONENTS);
            issue.componentVersion = selectRandomly(VERSIONS);
            issue.processingState = randomProcessingState;
            issue.openDate = randomOpenDate;
            issue.closeDate = randomCloseDate;
            issue.closeAction = randomCloseAction;
            issue.assignedUser = randomAssignedUser;
            issue.setArguments(createRandomArguments());
            issue.description = createRandomString(20, 5, 80, 20);
            issue.comment = createRandomString(0, 1000);

            randomIssues.add(issue);
        }

        return randomIssues;
    }

    private Map<String, String> createRandomArguments() {
        final String[] argumentNames = selectRandomly(argumentNameLists);

        final Map<String, String> arguments = new LinkedHashMap<String, String>();

        for (final String argumentName : argumentNames) {
            arguments.put(argumentName, createRandomString(50, 10, 80, 20));
        }

        return arguments;
    }

    private <T> T selectRandomly(final T[] values) {
        return values[random.nextInt(values.length)];
    }

    private int createNextId() {
        return idCounter.incrementAndGet();
    }

    private String createRandomExceptionStackTrace(final int nullProbabilityPercent) {

        if (nullProbabilityPercent < 0 || nullProbabilityPercent > 100) {
            throw new IllegalArgumentException("nullProbabilityPercent must be in [0..100]");
        }

        if (random.nextInt(100) < nullProbabilityPercent) {
            return null;
        }

        return EXCEPTION_STACK_TRACE;
    }

    private String createRandomString(final int nullProbabilityPercent, final int maxLength) {

        if (nullProbabilityPercent < 0 || nullProbabilityPercent > 100) {
            throw new IllegalArgumentException("nullProbabilityPercent must be in [0..100]");
        }
        if (maxLength < 0) {
            throw new IllegalArgumentException("maxLength must not be negative");
        }

        if (random.nextInt(100) < nullProbabilityPercent) {
            return null;
        }

        int length = random.nextInt(maxLength + 1);
        final StringBuilder stringBuilder = new StringBuilder();

        while (--length >= 0) {
            stringBuilder.append(CHAR_TABLE.get(random.nextInt(CHAR_TABLE.size())));
        }

        return stringBuilder.toString();
    }

    private String createRandomString(final int nullProbabilityPercent, final int maxLineNum, final int maxLineLength,
            final int multiLineProbabilityPercent) {

        if (nullProbabilityPercent < 0 || nullProbabilityPercent > 100) {
            throw new IllegalArgumentException("nullProbabilityPercent must be in [0..100]");
        }
        if (maxLineNum < 0) {
            throw new IllegalArgumentException("maxLineNum must not be negative");
        }

        if (random.nextInt(100) < nullProbabilityPercent) {
            return null;
        }

        int lineNum;
        if (random.nextInt(100) < multiLineProbabilityPercent) {
            lineNum = random.nextInt(maxLineNum - 1) + 2;
        } else {
            lineNum = 1;
        }

        final StringBuilder stringBuilder = new StringBuilder();
        while (--lineNum >= 0) {
            stringBuilder.append(createRandomString(0, maxLineLength));
            if (lineNum > 0) {
                stringBuilder.append(System.getProperty("line.separator"));
            }
        }

        return stringBuilder.toString();
    }

    // Nicht perfekt, aber fuer unsere Zwecke reicht's
    private long createRandomLong(final long minValue, final long maxValue) {
        if (minValue >= maxValue) {
            throw new IllegalArgumentException("minValue must be less than maxValue");
        }

        long longValue;
        do {
            longValue = random.nextLong();
        } while (longValue == Long.MIN_VALUE);

        return Math.abs(longValue) % (maxValue - minValue) + minValue;
    }

    private static List<Character> createCharTable() {
        final List<Character> charTable = new ArrayList<Character>();

        for (char c = 'a'; c <= 'z'; ++c) {
            charTable.add(c);
        }
        for (char c = '0'; c <= '9'; ++c) {
            charTable.add(c);
        }
        charTable.add(' ');

        return charTable;
    }

    private static String createExceptionStackTrace() {
        try {
            throw new RuntimeException("A system error occurred. Please restart the universe.");
        } catch (final Exception exception) {

            final Writer stringWriter = new StringWriter();
            final PrintWriter printWriter = new PrintWriter(stringWriter);
            exception.printStackTrace(printWriter);
            return stringWriter.toString();
        }
    }

    private Date createRandomDate(final Date minDate, final Date maxDate) {
        if (minDate == null) {
            throw new IllegalArgumentException("argument minDate is mandatory");
        }
        if (maxDate == null) {
            throw new IllegalArgumentException("argument maxDate is mandatory");
        }
        if (minDate.getTime() >= maxDate.getTime()) {
            throw new IllegalArgumentException("minDate must be less than maxDate");
        }

        return new Date(createRandomLong(minDate.getTime(), maxDate.getTime()));
    }
}