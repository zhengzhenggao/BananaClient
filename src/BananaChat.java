

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Created by Gao Liangjie on 5/1/2015.
 */
public class BananaChat extends JFrame{
    private JTextArea typeArea;
    private JTextArea chatWindow;
    private JButton buttonSend;
    private JPanel panelBottom;

    private String name;
    private String IP;
    private int port;

    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;

    private String message;
    private boolean connected = false;
    public BananaChat(String name, String IP, int port) {
        this.name = name;
        this.IP = IP;
        this.port = port;

        typeArea = new JTextArea();
        chatWindow = new JTextArea();
        chatWindow.setEditable(false);

        panelBottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonSend = new JButton("send");

        buttonSend.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                message = typeArea.getText();

                if (connected) {
                    sendMessage(message);
                }
                typeArea.setText("");
            }
        });

        typeArea.setPreferredSize(new Dimension(500, 100));

        panelBottom.add(typeArea);
        panelBottom.add(buttonSend);

        add(new JScrollPane(chatWindow), BorderLayout.CENTER);
        add(panelBottom, BorderLayout.SOUTH);

        setSize(600, 1000);
        setVisible(true);
        setTitle("Banana Chat");
        
    }

    private void sendMessage(String message) {
        out.println(message);
    }

    private void showMessage(final String str) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                chatWindow.append("\n" + str);
            }
        });
    }

    public void chatting() throws Exception{
        try {
            showMessage("\nconnecting...");
            socket = new Socket(IP, port);
            showMessage("\nconnect to server");

            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);

            out.println("ready");
            out.println(name);
            out.println("haha");

            showMessage("\nready to send message");

            connected = true;

            String str = null;
            while ((str = in.readLine()) != null) {
                if (str.equals(name + ": end")) {
                    break;
                }
                showMessage(str);
            }

        } catch (IOException ioException) {
            showMessage("can't connect to server");
        }

    }
}
