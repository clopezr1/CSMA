/**
 * Created by Carlos on 9/12/2018.
 */
import javax.swing.*;
import java.awt.*;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.Arrays;


public class Sim extends JPanel{

    private static final int X_ORIG = 390;
    private static final int TRANSLATION_AMOUNT = 25;
    private static final int X_ADVANCED = X_ORIG + TRANSLATION_AMOUNT;
    private static final int PANEL_WIDTH = 500;
    private static final int PANEL_HEIGHT = 450;

    private Color mColor;
    private int mNumberOfNodes;
    private ArrayList<Rectangle> nodes;
    private ArrayList<Line2D> lines;
    private ArrayList<Line2D> linesToDraw;
    private String[] nodeStates;

    Rectangle chan = new Rectangle(30, 190, 80, 80);

    public Sim(int numberOfNodes) {
        setBorder(BorderFactory.createLineBorder(Color.blue));
        mColor = Color.blue;
        mNumberOfNodes = numberOfNodes;
        nodes = new ArrayList<>();
        lines = new ArrayList<>();
        linesToDraw = new ArrayList<>();
        nodeStates = new String[numberOfNodes];
        Arrays.fill(nodeStates, "");
        generateGraphics();
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(PANEL_WIDTH, PANEL_HEIGHT);
    }

    public void setColor(Color c) {
        mColor = c;
    }

    public void setNodeState(int node, Node.State nodeState) {
        String text;
        switch (nodeState) {
            case IDLE:
                text = "Idle";
                break;
            case WAITING:
                text = "Waiting to send";
                break;
            case TRANSMITTING:
                text = "Transmitting";
                break;
            default:
                text = "";
                break;
        }

        nodeStates[node] = text;
    }
    private void generateGraphics() {
        // Equally separate nodes vertically
        int separation = PANEL_HEIGHT / (mNumberOfNodes * 2 + 1);

        // lines go from right edge of node to left edge of channel

        for (int i = 0; i < mNumberOfNodes; i++) {
            // create node rectangles equally spaced
            int xPos = 390;
            int yPos = 2 * i * separation + separation;
            nodes.add(new Rectangle(xPos, yPos, separation, separation));

            // create connection lines equally spaced
            int rightEdge = xPos + separation + TRANSLATION_AMOUNT;
            int nodeVerticalCenter = yPos + (separation / 2);
            lines.add(new Line2D.Double(rightEdge, nodeVerticalCenter, chan.getCenterX(), chan.getCenterY()));
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(mColor);
        for (int i = 0; i < mNumberOfNodes; i++) {
            g2d.fill(nodes.get(i));
        }
        for (Line2D line : linesToDraw) {
            g2d.draw(line);
        }
        g2d.fill(chan);

        drawLabels(g2d);
    }
    private void drawLabels(Graphics2D g2d) {
        g2d.setColor(Color.BLACK);
        for (int i = 0; i < mNumberOfNodes; i++) {
            Rectangle node = nodes.get(i);
            g2d.drawString(nodeStates[i], (int) node.x, (int) node.getMaxY()+20);
        }
    }
    public void resizeNodes(ArrayList<Integer> transmissionStates) {
        linesToDraw.clear();
        for (int i = 0; i < mNumberOfNodes; i++) {
            Rectangle node = nodes.get(i);
            if (transmissionStates.get(i) == 1) {
                node.setLocation(X_ADVANCED, node.y);
                linesToDraw.add(lines.get(i));
            } else {
                node.setLocation(X_ORIG, node.y);
            }
        }
    }
}

