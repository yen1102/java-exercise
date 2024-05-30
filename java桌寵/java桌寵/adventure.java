import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public class adventure{
    private Desktoppet deskpet;
    private picture pc;
    private info info;
    private Timer adventureTimer;
    private JDialog adventureframe;
    private JLabel cat;
    private Random random;

    private int minute;
    private int second;
    private JLabel timelabel;
    private JLabel remaintime;

    private JButton leaveadv;

    private JDialog askframe;
    private JButton noButton;
    private JButton yesButton;

    private JFrame acquirecoin;

    public adventure(Desktoppet deskpet,JFrame infoframe){
        this.deskpet=deskpet;
        random = new Random();
        deskpet.adventurestart();
        adventureframe=new JDialog((Frame) null ,"貓咪冒險啦！",false);
        adventureframe.setSize(200,250);
        adventureframe.setLayout(new FlowLayout(FlowLayout.CENTER));
        adventureframe.setUndecorated(true);
        adventureframe.setAlwaysOnTop(true);
        adventureframe.setLocationRelativeTo(deskpet);
        cat=new JLabel();
        pc=new picture();
        cat=pc.loadpicture(50, 50, "/picture/adventure/adventure1.png");
        minute=5;
        second=0;
        timelabel=new JLabel("探險剩餘時間");
        timelabel.setFont(new Font("新細明體",Font.ITALIC,20));
        remaintime=new JLabel(minute+" : "+second+"0");

        //貓險家

        leaveadv=new JButton("離開探險");

        adventureframe.add(timelabel);
        adventureframe.add(remaintime);
        adventureframe.add(cat);
        adventureframe.add(leaveadv);
        
        adventureframe.setVisible(true);

        ActionListener listener=new MyEventListner();
        leaveadv.addActionListener(listener);

        adventureTimer=new Timer(1000,new ActionListener(){
            public void actionPerformed(ActionEvent e){
                if(minute==0 && second==0){
                    adventureend(true);
                }
                if(second==0){
                    minute-=1;
                    second=59;
                }
                else{
                    second--;
                }
                if(second==0)
                    remaintime.setText(minute+" : "+second+"0");
                else if(second<10)
                    remaintime.setText(minute+" : 0"+second);
                else
                    remaintime.setText(minute+" : "+second);
            }
        });
        adventureTimer.start();
        pc.cgJLabelImg(cat, "/picture/adventure/adventure1.png", "/picture/adventure/adventure",8);

    }

    public void acquirecoin(int getcoin){
        acquirecoin=new JFrame();
        acquirecoin.setSize(150, 50);
        acquirecoin.setLayout(new FlowLayout(FlowLayout.CENTER));
        acquirecoin.setAlwaysOnTop(true);
        acquirecoin.setUndecorated(true);
        acquirecoin.setLocationRelativeTo(deskpet);
        JLabel text=new JLabel("探險結束！");
        JLabel much=new JLabel("此次獲得"+getcoin+"枚金幣!");
        text.setForeground(Color.orange);
        acquirecoin.add(text);
        acquirecoin.add(much);
        
        acquirecoin.setVisible(true);
        Timer visible=new Timer(2500, new ActionListener() {
            public void actionPerformed(ActionEvent e){
                acquirecoin.setVisible(false);
            }
        });
        visible.setRepeats(false);
        visible.start();
    }


    public void adventureend(boolean finish){
        adventureTimer.stop();
        deskpet.adventureend();
        adventureframe.setVisible(false);
        int acquire;
        if(finish){
            acquire=random.nextInt(201)+10;
            deskpet.setcoin(acquire);
            acquirecoin(acquire);
        }

    }

    public void askleave(){
        adventureTimer.stop();
        askframe = new JDialog((Frame) null, true);
        askframe.setSize(200, 100);
        askframe.setLayout(new FlowLayout(FlowLayout.CENTER));
        askframe.setAlwaysOnTop(true);
        askframe.setUndecorated(true);
        askframe.setLocationRelativeTo(adventureframe);
        JLabel text=new JLabel("是否要離開探險？");
        JLabel notice=new JLabel("離開後沒有獎勵，且飽食度跟體力值不會返還");
        notice.setForeground(Color.orange);
        notice.setFont(new Font("新細明體",Font.ITALIC,10));

        JPanel buttoPanel=new JPanel();
        noButton=new JButton("繼續探險");
        yesButton=new JButton("離開探險");
        ActionListener listener=new MyEventListner();
        noButton.addActionListener(listener);
        yesButton.addActionListener(listener);
        buttoPanel.add(noButton);
        buttoPanel.add(yesButton);

        askframe.add(text);
        askframe.add(notice);
        askframe.add(buttoPanel);

        
        askframe.setVisible(true);
    }

    private class MyEventListner implements ActionListener {
		
		public void actionPerformed(ActionEvent e){
			if(e.getSource()==leaveadv){

                askleave();
			}
            else if(e.getSource()==noButton){
                askframe.setVisible(false);
                adventureTimer.start();
            }
            else if(e.getSource()==yesButton){
                askframe.setVisible(false);
                adventureend(false);
            }

		}
	}
}