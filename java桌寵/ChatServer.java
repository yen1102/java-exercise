import java.io.*;
import java.net.*;
import java.util.*;

public class ChatServer {
      private static Set<String> clientNames = new HashSet<>();
      private static Set<PrintWriter> clientWriters = new HashSet<>();

      public static void main(String[] args) {
            System.out.println("Chat server started...");
            try (ServerSocket listener = new ServerSocket(8888)) {
                  while (true) {
                        new Handler(listener.accept()).start();
                  }
            } catch (IOException e) {
                  e.printStackTrace();
            }
      }

      public static class Handler extends Thread {
            private String name;
            private Socket socket;
            private BufferedReader in;
            private PrintWriter out;

            public Handler(Socket socket) {
                  this.socket = socket;
            }

            public void run() {
                  try {
                        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                        out = new PrintWriter(socket.getOutputStream(), true);

                        while (true) {
                              out.println("SUBMITNAME");
                              name = in.readLine();
                              if (name == null) {
                                    return;
                              }
                              synchronized (clientNames) {// 同步處理
                                    if (!name.isBlank() && !clientNames.contains(name)) {
                                          clientNames.add(name);
                                          break;
                                    }
                              }
                        }

                        out.println("NAMEACCEPTED " + name);
                        for (PrintWriter writer : clientWriters) {
                              writer.println(name + "has joined!");
                        }
                        clientWriters.add(out);

                        while (true) {
                              String input = in.readLine();
                              if (input == null) {
                                    return;
                              }

                              for (PrintWriter writer : clientWriters) {
                                    writer.println("MESSAGE " + name + ": " + input);
                              }
                        }
                  } catch (IOException e) {
                        System.out.println(e);
                  } finally {
                        if (name != null) {
                              clientNames.remove(name);
                        }
                        if (out != null) {
                              clientWriters.remove(out);
                        }
                        try {
                              socket.close();
                        } catch (IOException e) {
                              e.printStackTrace();
                        }
                        for (PrintWriter writer : clientWriters) {
                              writer.println(name + " has left!");
                        }
                  }
            }
      }

}
