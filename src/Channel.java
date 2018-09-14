/**
 * Created by Carlos on 9/12/2018.
 */
import java.util.*;

public class Channel {

    public static final int CLEAR_CHANNEL = -1;
    public static final int COLLISION = -2;

    private Queue<Integer> queue;
    private List<Node> nodeList;
    private int timeCounter;
    public  int nNodes;

    public Channel(int numberOfNodes, int delay) {
        nNodes = numberOfNodes;
        nodeList = new ArrayList<>();
        for (int i = 0; i < numberOfNodes; i++) {
            nodeList.add(new Node(this, .4 ));
        }

        // Create queue with clear channel size equal to delay
        queue = new ArrayDeque<>(delay);
        for (int i = 0; i < delay; i++) {
            queue.add(CLEAR_CHANNEL);
        }
        timeCounter = 0;
    }

    public boolean isClear() {
        return queue.peek() == CLEAR_CHANNEL;
    }

    public boolean isSeizedByNode(Node n) {
        int nodeIndex = nodeList.indexOf(n);
        return queue.peek() == nodeIndex;
    }

    public ArrayList<Integer> advanceTime() {
        int queueCode = CLEAR_CHANNEL;
        ArrayList<Integer> transmissions = new ArrayList<Integer>();
        transmissions.add(queueCode);

        for (int i = 0; i < nNodes; i++){ //Populate max size of nodes
            transmissions.add(0, 0);
        }

        for (int nodeIndex = 0; nodeIndex < nodeList.size(); nodeIndex++) {
            if (nodeList.get(nodeIndex).postsFrameToChannel()) {
                System.out.println("Node " + (nodeIndex + 1) + " begins transmission.");
                transmissions.set(nodeIndex, 1);
                if (queueCode != CLEAR_CHANNEL) {
                    queueCode = COLLISION;
                    break;
                } else {
                    queueCode = nodeIndex;
                }
            }
        }
        queue.add(queueCode);
        queue.poll(); // remove element to keep queue size the same
        transmissions.set(transmissions.size()-1, queueCode);
        timeCounter++;
        return transmissions;

    }

    public int getTime() {
        return timeCounter;
    }

    public List<Node> getNodeList() {
        return nodeList;
    }

    public String printChannel() {
        return queue.toString();
    }
}
