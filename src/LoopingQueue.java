public class LoopingQueue<T> extends Queue<T> {
    private boolean isForward = true;
    private int dequeueCount = 0;

    @Override
    public T dequeue() {
        if (head == null) return null;

        T value;
        if (isForward) {
            // Dequeue from head and append to the end
            QueueRecord returnRecord = head;
            head = head.nextrecord;

            if (head == null) current = returnRecord;
            else {
                current.nextrecord = returnRecord;
                current = returnRecord;
                current.nextrecord = null;
            }
            value = returnRecord.value;
        } else {
            // Dequeue from the tail (current) and prepend to the head
            QueueRecord returnRecord = current;

            if (head == current) { head = null; current = null; }
            else {
                // Find previous of current
                QueueRecord prev = head;
                while (prev.nextrecord != current) { prev = prev.nextrecord; }
                prev.nextrecord = null;
                current = prev;

                returnRecord.nextrecord = head;
                head = returnRecord;
            }
            value = returnRecord.value;
        }

        dequeueCount++;
        if (dequeueCount == getLength()) {
            isForward = !isForward;
            dequeueCount = 0;

            // Move to the next element to avoid repeating the last one
            if (isForward && head != null) { head = head.nextrecord; }
            else if (!isForward && current != null) {
                QueueRecord prev = head;
                if (prev != null) {
                    while (prev.nextrecord != current) {
                        prev = prev.nextrecord;
                    }
                    current = prev;
                }
            }
        }
        return value;
    }

    public static UnitTestResult UnitTest() {
        UnitTestResult result = new UnitTestResult("LoopingQueue");

        LoopingQueue<Job> lq1 = new LoopingQueue<Job>();
        lq1.enqueue(new Job(0, "frederick"));
        lq1.enqueue(new Job(0, "westminster"));
        lq1.enqueue(new Job(0, "dc"));
        lq1.enqueue(new Job(0, "frostburg"));

        result.recordNewTask(lq1.dequeue().getOnboardingStation().equals("frederick"));
        result.recordNewTask(lq1.dequeue().getOnboardingStation().equals("westminster"));
        result.recordNewTask(lq1.dequeue().getOnboardingStation().equals("dc"));
        result.recordNewTask(lq1.dequeue().getOnboardingStation().equals("frostburg"));
        result.recordNewTask(lq1.dequeue().getOnboardingStation().equals("dc"));

        return result;
    }
}
