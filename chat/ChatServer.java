package chatroom;

import java.awt.*;
import java.awt.event.*;
import java.net.*;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.UIManager;

public class ChatServer implements ActionListener, KeyListener {
      public static void main(String[] args) throws Exception {
            UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
            new ChatServer(new ChatFrame("聊天服務端"));// 設置window樣式外觀
      }// 創建ChatFrame對象並傳遞給ChatServer

      private JTextArea jta;// 文本區域
      private JTextField jtf;// 文本字段
      private BufferedWriter bh = null;// 用於寫入數據到客戶端的緩衝字符輸出流

      public ChatServer(ChatFrame chatFrame) {
            this.jtf = chatFrame.jtf;
            this.jta = chatFrame.jta;
            chatFrame.jb.addActionListener(this);
            jtf.addKeyListener(this);
            try {
                  ServerSocket ServerSocket = new ServerSocket(8888);// 創建ServerSocket 綁定port
                  Socket Socket = ServerSocket.accept();// 調用accept方式等待客戶端連接
                  BufferedReader br = new BufferedReader(new InputStreamReader(Socket.getInputStream()));// 讀取客戶端數據
                  bh = new BufferedWriter(new OutputStreamWriter(Socket.getOutputStream()));// 寫入數據到客戶端
                  String line = null;

                  Date sb = new Date();
                  SimpleDateFormat sdf = new SimpleDateFormat();// 格式化
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

      private void wdw() {// 按鈕被點擊
            String text = jtf.getText();
            text = "Server：" + text;
            jta.append(text + "\n");
            try {
                  bh.write(text);
                  bh.newLine();
                  bh.flush();// 把緩衝區內容強制寫出
                  jtf.setText("");
            } catch (IOException e1) {
                  e1.printStackTrace();
            }
      }

      @Override // 實現接口KeyListener的所有方法
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