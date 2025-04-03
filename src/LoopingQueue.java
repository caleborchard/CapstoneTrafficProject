import java.util.ArrayList;
import java.util.List;

public class LoopingQueue<T> extends Queue<T> {
    protected class DoublyQueueRecord extends QueueRecord {
        public DoublyQueueRecord prevrecord;
        public DoublyQueueRecord nextrecord;

        public DoublyQueueRecord(T value) {
            super(value);
            this.prevrecord = null;
        }
    }

    protected DoublyQueueRecord head, current;
    private boolean forward = true;

    public void enqueue(T value) {
        DoublyQueueRecord newrecord = new DoublyQueueRecord(value);

        if(head == null) { head = newrecord; }
        else { current.nextrecord = newrecord; newrecord.prevrecord = current; }
        current = newrecord;
        length++;
    }

    public T dequeue() {
        if((head.nextrecord == null && forward) ^ (head.prevrecord == null && !forward)) forward = !forward;
        DoublyQueueRecord returnRecord = head;

        if(forward) {
            current = returnRecord.nextrecord;
        } else {
            current = returnRecord.prevrecord;
        }
        head = current;

        return returnRecord.value;
    }

    public static UnitTestResult UnitTest() {
        UnitTestResult result = new UnitTestResult("LoopingQueue");

        LoopingQueue<Job> lq1 = new LoopingQueue<>();
        lq1.enqueue(new Job(0, "frederick"));
        lq1.enqueue(new Job(0, "westminster"));
        lq1.enqueue(new Job(0, "dc"));
        lq1.enqueue(new Job(0, "frostburg"));

        List<Boolean> list = new ArrayList<>();
        for(int i = 0; i < 4; i++) {
            list.add(lq1.dequeue().getOnboardingStation().equals("frederick"));
            list.add(lq1.dequeue().getOnboardingStation().equals("westminster"));
            list.add(lq1.dequeue().getOnboardingStation().equals("dc"));
            list.add(lq1.dequeue().getOnboardingStation().equals("frostburg"));
            list.add(lq1.dequeue().getOnboardingStation().equals("dc"));
            list.add(lq1.dequeue().getOnboardingStation().equals("westminster"));
        }
        boolean finBool = true;
        for(boolean b : list) {
            if (!b) {
                finBool = false;
                break;
            }
        }
        result.recordNewTask(finBool);
        return result;
    }
}