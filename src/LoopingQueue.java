import java.util.ArrayList;
import java.util.List; //List only used for Unit Test

public class LoopingQueue<T> extends Queue<T> {
    protected class DoublyQueueRecord extends QueueRecord {
        public DoublyQueueRecord prevrecord;
        public DoublyQueueRecord nextrecord;

        public DoublyQueueRecord(T value) {
            super(value);
            this.prevrecord = null;
        }
    }

    protected DoublyQueueRecord current;
    private boolean forward = true;
    private final List<CityInfoHolder> stationInfo = new ArrayList<>();
    public CityInfoHolder[] getStationNames() { return stationInfo.toArray(new CityInfoHolder[0]); }

    public void enqueue(T value) {
        DoublyQueueRecord newRecord = new DoublyQueueRecord(value);
        if(newRecord.value instanceof Station s) { stationInfo.add(new CityInfoHolder(s.getName(), s.getNumWorkers())); }

        if(current == null) { current = newRecord; }
        else {
            DoublyQueueRecord lastValid = current;
            while(lastValid.nextrecord != null) {
                lastValid = lastValid.nextrecord;
            }
            lastValid.nextrecord = newRecord;
            newRecord.prevrecord = lastValid;
        }
        length++;
    }

    public T dequeue() {
        if ((current.nextrecord == null && forward) ^ (current.prevrecord == null && !forward)) forward = !forward;
        DoublyQueueRecord returnRecord = current;
        if (forward) {
            if(current.nextrecord != null) current = current.nextrecord;
        } else {
            if(current.prevrecord != null) current = current.prevrecord;
        }
        return returnRecord.value;
    }

    public static UnitTestResult UnitTest() {
        UnitTestResult result = new UnitTestResult("LoopingQueue");

        LoopingQueue<Job> lq1 = new LoopingQueue<>();
        lq1.enqueue(new Job(0, "frederick", ""));
        lq1.enqueue(new Job(0, "westminster", ""));
        lq1.enqueue(new Job(0, "dc", ""));
        lq1.enqueue(new Job(0, "frostburg", ""));

        List<Boolean> boolList = new ArrayList<>();
        for(int i = 0; i < 5; i++) {
            boolList.add(lq1.dequeue().getOnboardingStation().equals("frederick"));
            boolList.add(lq1.dequeue().getOnboardingStation().equals("westminster"));
            boolList.add(lq1.dequeue().getOnboardingStation().equals("dc"));
            boolList.add(lq1.dequeue().getOnboardingStation().equals("frostburg"));
            boolList.add(lq1.dequeue().getOnboardingStation().equals("dc"));
            boolList.add(lq1.dequeue().getOnboardingStation().equals("westminster"));
        }
        boolean testResult = true;
        for(boolean b : boolList) {
            if (!b) {
                testResult = false;
                break;
            }
        }
        result.recordNewTask(testResult);
        return result;
    }
}