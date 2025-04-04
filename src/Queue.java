public class Queue<T> {
    protected class QueueRecord {
        public T value;
        public QueueRecord nextrecord;

        public QueueRecord(T value) {
            this.value = value; this.nextrecord = null;
        }
    }

    protected int length;
    protected QueueRecord current;

    public int getLength() { return length; }
    public boolean isQueueEmpty() { return (length == 0); }

    public Queue() {
        length = 0;
    }

    public void enqueue(T value) {
        QueueRecord newRecord = new QueueRecord(value);

        if(current == null) { current = newRecord; }
        else {
            QueueRecord lastValid = current;
            while(lastValid.nextrecord != null) {
                lastValid = lastValid.nextrecord;
            }
            lastValid.nextrecord = newRecord;
        }
        length++;
    }

    public T dequeue() {
        QueueRecord returnR = current;
        if(current == null) return null;
        else { current = current.nextrecord; }
        length--;
        return returnR.value;
    }

    public static UnitTestResult UnitTest() {
        UnitTestResult result = new UnitTestResult("Queue");

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