
import javax.swing.*;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.firebase.client.Firebase;


import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
public class IdWindow {
    public static String storedID;
    public static JFrame frame;
    public IdWindow() {

        frame = new JFrame("ID Input Window");
        frame.setSize(400, 200);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);  

        JPanel panel = new JPanel(new GridBagLayout());
        frame.add(panel);
        placeComponents(panel);


        frame.setVisible(true);
    }

    private static void placeComponents(JPanel panel) {
        
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.insets = new Insets(10, 10, 10, 10); 

        
        JLabel userLabel = new JLabel("請自訂輸入id碼(最多八碼):", JLabel.CENTER);
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.gridwidth = 2;
        panel.add(userLabel, constraints);

        
        JTextField userText = new JTextField(8);
        constraints.gridy = 1;
        constraints.gridwidth = 2;
        panel.add(userText, constraints);

        
        JButton startButton = new JButton("GET START");
        constraints.gridy = 2;
        constraints.gridwidth = 2;
        panel.add(startButton, constraints);

        
        startButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String inputID = userText.getText();
                if (inputID.length() > 8) {
                    JOptionPane.showMessageDialog(panel, "ID碼不能超過八碼。", "錯誤", JOptionPane.ERROR_MESSAGE);
                } else {
                    storedID = inputID;
                    Firebase myFirebaseRef =new Firebase ( "https://keepetogether-default-rtdb.asia-southeast1.firebasedatabase.app/> " );
                    myFirebaseRef.child(storedID).child("use").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                        
                            String data = dataSnapshot.getValue(String.class);
                            String comp="1";
                            if(data!=null){
                                if(data.equals( comp )){
                                    frame.setVisible(false);
                                    new Desktoppet(0).setVisible(true);
                                }else{
                                    myFirebaseRef.child(storedID).child("use").setValue("1");
                                    frame.setVisible(false);
                                    SwingUtilities.invokeLater(new Runnable() {
                                    @Override
                                    public void run() {
                                        new Desktoppet(1).setVisible(true);
                                    }
                                    
                                });
                                }
                            }else{
                                myFirebaseRef.child(storedID).child("use").setValue("1");
                                frame.setVisible(false);
                                SwingUtilities.invokeLater(new Runnable() {
                                @Override
                                public void run() {
                                    new Desktoppet(1).setVisible(true);
                                }
                                
                            });
                            }
                        }
            
                        @Override
                        public void onCancelled(FirebaseError databaseError) {
                            System.err.println("Error: " + databaseError.getMessage());
                        }
                    });
                    
                }
            }
        });
    }
}