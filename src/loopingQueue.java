public class loopingQueue<T> extends Queue {
    public T dequeue() {
        if (head == null) { return null; }

        QueueRecord returnrecord = head;
        head = head.nextrecord;
        if (head == null) { head = returnrecord; } // If only one element, it remains
        else {
            current.nextrecord = returnrecord;
            current = returnrecord;
            current.nextrecord = null;
        }
        return (T)returnrecord.value;
    }

    public static UnitTestResult UnitTest() {
        UnitTestResult result = new UnitTestResult();

        loopingQueue<Job> lq1 = new loopingQueue<Job>();
        lq1.enqueue(new Job(0, "frederick"));
        lq1.enqueue(new Job(0, "westminster"));
        lq1.enqueue(new Job(0, "dc"));
        lq1.enqueue(new Job(0, "frostburg"));

        result.recordNewTask(lq1.dequeue().getOnboardingStation().equals("frederick"));
        result.recordNewTask(lq1.dequeue().getOnboardingStation().equals("westminster"));
        result.recordNewTask(lq1.dequeue().getOnboardingStation().equals("dc"));
        result.recordNewTask(lq1.dequeue().getOnboardingStation().equals("frostburg"));
        result.recordNewTask(lq1.dequeue().getOnboardingStation().equals("frederick"));
        result.recordNewTask(!(lq1.dequeue().getOnboardingStation().equals("frederick")));

        return result;
    }
}
