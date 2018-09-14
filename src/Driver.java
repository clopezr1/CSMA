/**
 * Created by Carlos on 9/12/2018.
 */
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Driver {
    private static int n;
    public static void main(String[] s) {

        System.out.println("Number of Nodes:");
        Scanner sc = new Scanner(System.in);
        n = sc.nextInt();

        Channel channel = new Channel(n, 2);
        List<Node> nodeList = channel.getNodeList();   // hold reference for printing state

        javax.swing.SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("CSMA Simulator");
            frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            frame.setLayout(new FlowLayout());

            JTextArea text = new JTextArea(5, 20);
            JButton startButton = new JButton("Start");
            JButton advanceButton = new JButton("Step Time");
            Sim panel = new Sim(n);

            startButton.addActionListener(e -> {
                frame.add(advanceButton);
                frame.add(panel);
                frame.add(text);
                frame.pack();
                frame.setVisible(true);
                startButton.setVisible(false);
            });

            advanceButton.addActionListener((ActionEvent e) -> {
                ArrayList<Integer> tm = channel.advanceTime();
                for (int i = 0; i < nodeList.size(); i++) {
                    panel.setNodeState(i, nodeList.get(i).getState());
                }

                System.out.println(channel.printChannel());
                text.setText("Time: " + channel.getTime() + "\n");
                if (tm.get(tm.size()-1) == Channel.COLLISION) {
                    text.append("Nodes see: Collision!");
                    panel.setColor(Color.RED);
                    panel.resizeNodes(tm);
                    panel.repaint();
                } else if (tm.get(tm.size()-1) == Channel.CLEAR_CHANNEL) {
                    text.append("Nodes see: Channel is clear");
                    panel.setColor(Color.blue);
                    panel.resizeNodes(tm);
                    panel.repaint();
                } else {
                    text.append("Nodes see: Channel used by " + (tm.get(tm.size()-1) + 1));
                    panel.setColor(Color.green);
                    panel.resizeNodes(tm);
                    panel.repaint();
                }
            });

            frame.add(startButton);
            frame.pack();
            frame.setVisible(true);
        });

    }

}


