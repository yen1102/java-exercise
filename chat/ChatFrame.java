package chatroom;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class ChatFrame extends JFrame {
      public JTextArea jta;
      public JScrollPane jsp;
      public JPanel jp;
      public JTextField jtf;
      public JButton jb;
      public String title;

      public ChatFrame(String title) {// 建立標題
            this.title = title;
            jta = new JTextArea();
            jta.setEditable(false);// 設置成不可編輯
            jsp = new JScrollPane(jta);
            jp = new JPanel();
            jtf = new JTextField(15);
            jb = new JButton("發送");
            jp.add(jtf);
            jp.add(jb);
            jp.setBackground(Color.DARK_GRAY);
            this.add(jsp, BorderLayout.CENTER);
            this.add(jp, BorderLayout.SOUTH);
            this.setTitle(title);
            this.setSize(300, 300);
            this.setLocation(500, 300);
            this.setLocationRelativeTo(null);
            this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            this.setVisible(true);
      }
}