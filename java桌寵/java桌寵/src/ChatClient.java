import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;

public class ChatClient {
      private String serverAddress;
      private BufferedReader in;
      private PrintWriter out;
      private JFrame frame = new JFrame("Chat Room");
      private JTextArea messageArea = new JTextArea(8, 25);
      private JTextField textField = new JTextField(25);
      private JPanel jp;
      private JButton jb;

      public ChatClient(String serverAddress) {
            this.serverAddress = serverAddress;

            Font font = new Font("Arial", Font.PLAIN, 16);

            frame.setSize(400, 400);
            messageArea.setEditable(false);
            messageArea.setFont(font);
            textField.setEditable(false);

            frame.add(new JScrollPane(messageArea), "Center");
            jp = new JPanel();
            jb = new JButton("發送");
            jb.setBackground(Color.lightGray);
            jp.add(textField);
            jp.add(jb);
            frame.add(jp, "South");
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);

            textField.addActionListener(new ActionListener() {
                  public void actionPerformed(ActionEvent e) {
                        out.println(textField.getText());
                        textField.setText("");
                  }
            });
            jb.addActionListener(new ActionListener() {
                  public void actionPerformed(ActionEvent e) {
                        out.println(textField.getText());
                        textField.setText("");
                  }

            });
      }

      private String getName() {
            return JOptionPane.showInputDialog(frame, "Enter a nickname:", "Nickname", JOptionPane.PLAIN_MESSAGE);
      }

      public void run() throws IOException {
            try (Socket socket = new Socket(serverAddress, 8888)) {
                  in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                  out = new PrintWriter(socket.getOutputStream(), true);

                  while (true) {
                        String line = in.readLine();
                        if (line.startsWith("SUBMITNAME")) {
                              out.println(getName());
                        } else if (line.startsWith("NAMEACCEPTED")) {
                              textField.setEditable(true);
                        } else if (line.startsWith("MESSAGE")) {
                              messageArea.append(line.substring(8) + "\n");
                        }
                  }
            }
      }

      public static void main(String[] args) throws Exception {
            ChatClient client = new ChatClient("127.0.0.1");
            client.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            client.run();
      }
}