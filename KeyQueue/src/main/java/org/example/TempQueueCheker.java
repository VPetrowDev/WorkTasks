package org.example;

public class TempQueueCheker implements Runnable{
    private final KeyEvent keyEvent;

    public TempQueueCheker(KeyEvent keyEvent) {
        this.keyEvent = keyEvent;
    }

    @Override
    public void run() {
        try {
            while (true) {

                if (keyEvent.mySet.size() == 1 && keyEvent.mySet.contains(Message.POISON_PILL)) {
                    break;
                }

                keyEvent.checkIfEventHasBeenProcessed();
            }
        } catch (InterruptedException e) {

        }
    }
}
