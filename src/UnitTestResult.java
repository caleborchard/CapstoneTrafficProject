public class UnitTestResult {
    private int testsrun, testsfinished;

    public UnitTestResult() {
        testsrun = 0;
        testsfinished = 0;
    }

    public UnitTestResult(int testsPerformed, int testsPassed) {
        testsrun = testsPerformed;
        testsfinished = testsPassed;
    }

    public int getTestsRun() { return testsrun; }
    public int getTestsFinished() { return testsfinished; }

    public void factorResults(UnitTestResult ut) {
        testsrun += ut.getTestsRun();
        testsfinished += ut.getTestsFinished();
    }

    public void recordNewTask(boolean success) {
        testsrun++;
        if(success) {
            testsfinished++;
        }
    }
}
