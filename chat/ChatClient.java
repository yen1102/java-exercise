package chatroom;

import java.awt.*;
import java.awt.event.*;
import java.net.*;
import java.io.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.UIManager;

public class ChatClient implements ActionListener, KeyListener {
      public static void main(String[] args) throws Exception {
            UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
            new ChatClient(new ChatFrame("聊天客户端"));
      }

      private BufferedWriter bh = null;
      private JTextField jtf;
      private JTextArea jta;

      public ChatClient(ChatFrame chatFrame) {
            this.jtf = chatFrame.jtf;
            this.jta = chatFrame.jta;
            chatFrame.jb.addActionListener(this);
            jtf.addKeyListener(this);
            try {
                  Socket Socket = new Socket("127.0.0.1", 8888);
                  BufferedReader br = new BufferedReader(new InputStreamReader(Socket.getInputStream()));
                  bh = new BufferedWriter(new OutputStreamWriter(Socket.getOutputStream()));
                  String line = null;

                  Date sb = new Date();
                  SimpleDateFormat sdf = new SimpleDateFormat();
                  sdf.format(sb);
                  jta.append("\t" + sdf.format(sb) + "\n");

                  while ((line = br.readLine()) != null) {
                        jta.append(line + "\n");
                  }
                  Socket.close();
            } catch (Exception e) {
                  e.printStackTrace();
            }
      }

      @Override
      public void actionPerformed(ActionEvent e) {
            wdw();
      }

      private void wdw() {
            String text = jtf.getText();
            text = "Client：" + text;
            jta.append(text + "\n");
            try {
                  bh.write(text);
                  bh.newLine();
                  bh.flush();
                  jtf.setText("");
            } catch (IOException e1) {
                  e1.printStackTrace();
            }
      }

      @Override
      public void keyPressed(KeyEvent e) {
            if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                  wdw();
            }
      }

      @Override
      public void keyTyped(KeyEvent e) {
      }

      @Override
      public void keyReleased(KeyEvent e) {
      }
}