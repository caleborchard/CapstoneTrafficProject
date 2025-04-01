public class Queue<T> {
    protected class QueueRecord {
        public T value;
        public QueueRecord nextrecord;

        public QueueRecord(T value) {
            this.value = value; this.nextrecord = null;
        }
    }

    protected int length;
    protected QueueRecord head, current;

    public int getLength() { return length; }
    public boolean isQueueEmpty() { return (length == 0); }

    public Queue() {
        length = 0; head = null; current = null;
    }

    public void enqueue(T value) {
        QueueRecord newrecord = new QueueRecord(value);

        if(head == null) { head = newrecord; current = newrecord; }
        else { current.nextrecord = newrecord; current = newrecord; }
        length++;
    }

    public T dequeue() {
        QueueRecord returnrecord = head;
        if(head == null) { return null; }
        else if(current == head) { head = null; current = null; }
        else { head = head.nextrecord; }
        length--;
        return returnrecord.value;
    }

    public static UnitTestResult UnitTest() {
        UnitTestResult result = new UnitTestResult();

        Queue<Integer> queue = new Queue<>();
        result.recordNewTask(queue.isQueueEmpty()); // Test empty queue

        queue.enqueue(1);
        result.recordNewTask(queue.getLength() == 1); // Test enqueue increases length
        result.recordNewTask(!queue.isQueueEmpty()); // Test queue is not empty after enqueue

        queue.enqueue(2);
        result.recordNewTask(queue.getLength() == 2); // Test enqueue multiple elements

        result.recordNewTask(queue.dequeue() == 1); // Test dequeue order (FIFO)
        result.recordNewTask(queue.getLength() == 1); // Test length after dequeue

        result.recordNewTask(queue.dequeue() == 2); // Test dequeue second element
        result.recordNewTask(queue.isQueueEmpty()); // Test queue is empty after all dequeues
        result.recordNewTask(queue.getLength() == 0); // Test length after all dequeues

        result.recordNewTask(queue.dequeue() == null); // Test dequeue on empty queue

        return result;
    }
}