import java.util.ArrayList;
import java.util.List;

public class UnitTestResult {
    private final String className;
    private int testsrun, testsfinished;
    private final List<String> errors;

    public UnitTestResult(String className) {
        this.className = className;
        testsrun = 0;
        testsfinished = 0;
        this.errors = new ArrayList<>();
    }

    public UnitTestResult(String className, int testsRun, int testsFinished) {
        this.className = className;
        this.testsrun = testsRun;
        this.testsfinished = testsFinished;
        this.errors = new ArrayList<>();
    }

    public int getTestsRun() { return testsrun; }
    public int getTestsFinished() { return testsfinished; }
    public String getErrors() { return String.join("\n", errors); }

    public void factorResults(UnitTestResult other) {
        this.testsrun += other.testsrun;
        this.testsfinished += other.testsfinished;
        this.errors.addAll(other.errors);
    }

    public void recordNewTask(boolean success) {
        testsrun++;
        if (success) {
            testsfinished++;
        } else {
            String errorMessage = className + ": Test number " + testsrun + " has failed.";
            errors.add(errorMessage);
        }
    }
}
