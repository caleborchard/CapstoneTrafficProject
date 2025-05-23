public class UnitTest {
    private static UnitTestResult totaltests = new UnitTestResult("totalTests");

    public static void main(String[] args) {
        System.out.println("Running all unit tests");
        doTests();
        System.out.println(totaltests.getTestsFinished() + " of " + totaltests.getTestsRun() + " passed.");
        if(totaltests.getTestsFinished() == totaltests.getTestsRun()) {
            System.out.println("SUCCESS");
        }
        else {
            System.out.println("FAIL");
            System.out.println(totaltests.getErrors());
        }
    }

    private static void doTests() {
        //PUT ALL UNIT TESTS HERE. Run this to perform Unit Tests. Below is example structure for adding Unit Tests of a class.
        //totalTests.factorResults(ExampleClass.UnitTest());
        totaltests.factorResults(Queue.UnitTest());
        totaltests.factorResults(LoopingQueue.UnitTest());
        totaltests.factorResults(BatchServerQueue.UnitTest());
        totaltests.factorResults(Station.UnitTest());
        totaltests.factorResults(Config.UnitTest());
    }
}
