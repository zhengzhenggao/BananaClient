

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Created by Gao Liangjie on 5/1/2015.
 */
public class Client extends JFrame {
    private JLabel labelName = new JLabel("Name");
    private JTextField typeName = new JTextField();
    private String name;

    private JLabel labelPassword = new JLabel("Password");
    private JTextField typePassword = new JTextField();
    private String password;

    private JLabel labelIP = new JLabel("IP Address");
    private JTextField typeIP = new JTextField();
    private String IP;

    private JLabel labelPort = new JLabel("Port");
    private JTextField typePort = new JTextField();
    private int port;

    private JButton register = new JButton("Register");
    private JButton login = new JButton("Login");

    private JPanel panel = new JPanel(new GridLayout(5, 2));

    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;

    private String line = null;

    private BananaChat chat;

    
    public Client() {
        panelAdd();
        add(panel);
        setVisible(true);

        setSize(500, 1000);
    }

    private void panelAdd() {
        panel.add(labelName);
        panel.add(typeName);
        panel.add(labelPassword);
        panel.add(typePassword);
        panel.add(labelIP);
        panel.add(typeIP);
        panel.add(labelPort);
        panel.add(typePort);
        panel.add(register);
        panel.add(login);

        register.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                extraInfo();
                sendMessage("register");
                sendMessage(name);
                sendMessage(password);

                receiveStatus();
            }
        });

        login.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                extraInfo();
                sendMessage("login");
                sendMessage(name);
                sendMessage(password);

                receiveStatus();
            }
        });
    }

    private void extraInfo() {
        name = typeName.getText();
        password = typePassword.getText();
        IP = typeIP.getText();
        port = Integer.parseInt(typePort.getText());

        try {
            socket = new Socket(IP, port);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);
        } catch (UnknownHostException e) {
            showInfo("this port is unknown");
        } catch (IOException ioException) {
            showInfo("can't send or receive the message");
        }
    }

    public InfoFrame showInfo(String message) {
        InfoFrame info = new InfoFrame(message);
        info.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        return info;
    }

    private void sendMessage(String message) {
        out.println(message);
    }

    private void receiveStatus() {
        try {
            line = in.readLine();
            InfoFrame info = showInfo(line);
            if (line.equals("log in successfully")) {
                chat = new BananaChat(name, IP, port);
                //chat.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                //chat.chatting();
            }

        } catch (Exception e) {
            new RuntimeException("wo");
        }
    }



    class InfoFrame extends JFrame {
        JLabel info;

        JButton confirm;

        public InfoFrame(String message) {
            info = new JLabel(message);
            add(info, BorderLayout.CENTER);

            confirm = new JButton("confirm");
            add(confirm, BorderLayout.SOUTH);

            confirm.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    dispose();
                }
            });

            setVisible(true);
            setSize(500, 500);

        }
    }


}
