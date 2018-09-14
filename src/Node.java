/**
 * Created by Carlos on 9/12/2018.
 */

public class Node {

    private final static int MAX_BACKOFF = 16;
    private final static int FRAME_LENGTH = 10;
    private int waitCounter;
    private double transmitProb;
    private Channel channel;
    private int transmitCounter;
    private State state;

    public enum State {
        IDLE, WAITING, TRANSMITTING
    }


    public Node(Channel c, double transmitProb) {
        assert (transmitProb <= 1.0 || transmitProb >= 0);

        this.transmitProb = transmitProb;
        this.channel = c;
        state = State.IDLE;
    }

    public boolean postsFrameToChannel() {
        updateState();
        return state == State.TRANSMITTING;
    }

    private void updateState() {
        switch (state) {
            case IDLE:
                if (Math.random() < transmitProb) {
                    // We have a frame to send. Check to see if we can send it.
                    if (channel.isClear()) {
                        state = State.TRANSMITTING;
                        transmitCounter = FRAME_LENGTH;
                    } else {
                        state = State.WAITING;
                        setBackoffTimer();
                    }
                }
                break;
            case TRANSMITTING:
                // Still have to check if our transmission was stepped on
                if (channel.isClear() || channel.isSeizedByNode(this)) {
                    if (transmitCounter > 0) {
                        transmitCounter--;
                    } else {
                        state = State.IDLE;
                    }
                } else {
                    state = State.WAITING;
                    setBackoffTimer();
                }
                break;
            case WAITING:
                if (waitCounter > 0) {
                    waitCounter--;
                } else {
                    // check channel before trying again
                    if (channel.isClear()) {
                        state = State.TRANSMITTING;
                        transmitCounter = FRAME_LENGTH;
                    } else {
                        // wait again
                        setBackoffTimer();
                    }
                }
                break;
        }
    }

    private void setBackoffTimer() {
        if (waitCounter == 0) {
            waitCounter = (int) (Math.random() * MAX_BACKOFF) + 1;
        }
    }

    public State getState() {
        return state;
    }
}
