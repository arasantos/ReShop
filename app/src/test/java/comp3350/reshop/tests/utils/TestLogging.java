package comp3350.reshop.tests.utils;

import org.junit.rules.TestWatcher;
import org.junit.runner.Description;

public class TestLogging {
    /*
     * Create a test watcher to format output for test suites
     */
    public static TestWatcher getTestLogger() {
        return new TestWatcher() {
            protected void starting(Description description) {
                System.out.println("Starting test: " + description.getMethodName() + "...");
            }

            @Override
            protected void succeeded(Description description) {
                System.out.println(Colors.GREEN + "    Success!" + Colors.RESET);
            }

            @Override
            protected void failed(Throwable e, Description description) {
                System.out.print(Colors.RED + "    Failure!" + Colors.RESET);
            }
        };
    }
}
