import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;


public class info {
    private Desktoppet deskpet;
    private boolean infoVisible;
    private JProgressBar energybar;
    private JProgressBar satietybar;
    private JFrame infoFrame;
    private PhotoMoving pm;
    private adventure adv;

    private JButton feed1Button;
    private JButton feed2Button;
    private JButton feed3Button;

    private JButton adventureButton;
    private int advcount;


    private JDialog dieframe;
    private JButton leavegameButton;
    private JButton restartgameButton;

    private boolean sick;
    private JLabel text;
    private JFrame sickFrame;
    private JButton lookdoctor;
    private JButton specialmedicine;

    private JFrame successframe;


    JFrame notenough;
    JFrame cannotadv;

    public info(Desktoppet deskpet){
        this.deskpet=deskpet;
        initializeInfoFrame();
    }

    public JFrame getinfo(){
        return infoFrame;
    }

    private void initializeInfoFrame() {
        advcount=0;
        sick=false;
        infoFrame = new JFrame();
        infoFrame.setLayout(new FlowLayout(FlowLayout.LEADING));
        infoFrame.setUndecorated(true); 
        infoFrame.setSize(280, 290);
        infoFrame.setAlwaysOnTop(true);

        energybar = new JProgressBar(0, 1000);
        energybar.setValue(deskpet.getEnergy());
        energybar.setStringPainted(true);

        JLabel feedLabel=new JLabel("購買食物並餵食:");
        feedLabel.setForeground(Color.orange);

        JPanel feed1Panel=new JPanel();
        JLabel feedLabel1=new JLabel("貓貓飼料");
        JLabel feedeffect1=new JLabel(" 飽食度+50%                    ");
        feedeffect1.setForeground(Color.blue);
        feedeffect1.setFont(new Font("新細明體",Font.ITALIC,10));
        feed1Button=new JButton("購買");
        feed1Panel.add(feedLabel1);
        feed1Panel.add(feedeffect1);
        feed1Panel.add(feed1Button);

        JPanel feed2Panel=new JPanel();
        JLabel feedLabel2=new JLabel("貓貓罐頭");
        JLabel feedeffect2=new JLabel(" 體力值+25%,飽食度+25%");
        feedeffect2.setForeground(Color.blue);
        feedeffect2.setFont(new Font("新細明體",Font.ITALIC,10));
        feed2Button=new JButton("購買");
        feed2Panel.add(feedLabel2);
        feed2Panel.add(feedeffect2);
        feed2Panel.add(feed2Button);

        JPanel feed3Panel=new JPanel();
        JLabel feedLabel3=new JLabel("貓條       ");
        JLabel feedeffect3=new JLabel(" 體力值+50%                    ");
        feedeffect3.setForeground(Color.blue);
        feedeffect3.setFont(new Font("新細明體",Font.ITALIC,10));
        feed3Button=new JButton("購買");
        feed3Panel.add(feedLabel3);
        feed3Panel.add(feedeffect3);
        feed3Panel.add(feed3Button);

        JPanel energypanel = new JPanel();
        energypanel.add(new JLabel("體力值: "));
        energypanel.add(energybar);
        
        satietybar=new JProgressBar(0,1000);
        //satietybar.setForeground(Color.ORANGE);
        //UIManager.put("satietybar.foreground", Color.ORANGE);
        satietybar.setValue(deskpet.getsatiety());
        satietybar.setStringPainted(true);
        
        JLabel coin=new JLabel("金幣: "+deskpet.getcoin());
        //JPanel coinpanel=new JPanel(new FlowLayout(FlowLayout.LEFT));
        //coinpanel.add(coin);

        JPanel satietypanel = new JPanel();
        satietypanel.add(new JLabel("飽食度: "));
        satietypanel.add(satietybar);

        JLabel medicine=new JLabel("貓貓用藥:");
        medicine.setForeground(Color.orange);
        
        JPanel med1=new JPanel();
        JLabel med1Label=new JLabel("貓貓看醫生");
        JLabel medeffect1=new JLabel(" 恢復速度加快                ");
        medeffect1.setForeground(Color.blue);
        medeffect1.setFont(new Font("新細明體",Font.ITALIC,10));
        lookdoctor=new JButton("選擇");
        med1.add(med1Label);
        med1.add(medeffect1);
        med1.add(lookdoctor);

        JPanel med2=new JPanel();
        JLabel med2Label=new JLabel("貓病特效藥");
        JLabel medeffect2=new JLabel(" 貓貓痊癒                       ");
        medeffect2.setForeground(Color.blue);
        medeffect2.setFont(new Font("新細明體",Font.ITALIC,10));
        specialmedicine=new JButton("選擇");
        med2.add(med2Label);
        med2.add(medeffect2);
        med2.add(specialmedicine);
        


        JPanel adventurepanel=new JPanel();
        JLabel adventurelabel=new JLabel("來一場探險吧？                     ");
        adventureButton=new JButton("探險！");
        adventurepanel.add(adventurelabel);
        adventurepanel.add(adventureButton);

        JMenuItem lifetime=new JMenuItem("存活時間（秒）: "+ deskpet.getlifetime());
        infoFrame.add(lifetime);
        infoFrame.add(coin);
        infoFrame.add(energypanel);
        infoFrame.add(satietypanel);
        infoFrame.add(feedLabel);
        infoFrame.add(feed1Panel);
        infoFrame.add(feed2Panel);
        infoFrame.add(feed3Panel);
        infoFrame.add(adventurepanel);
        infoFrame.add(medicine);
        infoFrame.add(med1);
        infoFrame.add(med2);
        infoFrame.setVisible(infoVisible);

        ActionListener listener=new MyEventListner();
        feed1Button.addActionListener(listener);
        feed2Button.addActionListener(listener);
        feed3Button.addActionListener(listener);
        adventureButton.addActionListener(listener);

        // 即時更新資訊
        new Thread(() -> {
            while (true) {
                SwingUtilities.invokeLater(() -> {
                    energybar.setValue(deskpet.getEnergy());
                    satietybar.setValue(deskpet.getsatiety());
                    coin.setText("金幣: "+deskpet.getcoin());
                    lifetime.setText("存活時間（秒）: "+ deskpet.getlifetime());
                });
                try {
                    Thread.sleep(350); 
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
        }).start();
    }

    public void notenough(){
        notenough=new JFrame();
        notenough.setSize(100, 50);
        notenough.setLayout(new FlowLayout(FlowLayout.CENTER));
        notenough.setAlwaysOnTop(true);
        notenough.setUndecorated(true);
        JLabel text=new JLabel("金幣不足");
        text.setForeground(Color.red);
        notenough.add(text);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int x = screenSize.width - notenough.getWidth()-100;
        int y = 150;
        notenough.setLocation(x, y);
        notenough.setVisible(true);
        Timer visible=new Timer(1500, new ActionListener() {
            public void actionPerformed(ActionEvent e){
                notenough.setVisible(false);
            }
        });
        visible.setRepeats(false);
        visible.start();
    }

    public void success(int what){
        successframe=new JFrame();
        successframe.setSize(100, 50);
        successframe.setLayout(new FlowLayout(FlowLayout.CENTER));
        successframe.setAlwaysOnTop(true);
        successframe.setUndecorated(true);
        if(what==1)
            text=new JLabel("購買成功");
        else if(what==2)
            text=new JLabel("診療完成");
        else if(what==3)
            text=new JLabel("等貓貓吃完！");
        text.setForeground(Color.blue);
        successframe.add(text);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int x = screenSize.width - successframe.getWidth()-100;
        int y = 150;
        successframe.setLocation(x, y);
        successframe.setVisible(true);
        Timer visible=new Timer(1500, new ActionListener() {
            public void actionPerformed(ActionEvent e){
                successframe.setVisible(false);
            }
        });
        visible.setRepeats(false);
        visible.start();
    }

    public void sick(){
        sick=true;
        infoFrame.setSize(280,410);
        infoFrame.setVisible(true);
        

        ActionListener listener=new MyEventListner();
        lookdoctor.addActionListener(listener);
        specialmedicine.addActionListener(listener);
    }

    public void sick(int what){
        sickFrame=new JFrame();
        sickFrame.setSize(120, 50);
        sickFrame.setLayout(new FlowLayout(FlowLayout.CENTER));
        sickFrame.setAlwaysOnTop(true);
        sickFrame.setUndecorated(true);
        if(what==1)
            text=new JLabel("貓貓沒食慾");
        else if(what==2)
            text=new JLabel("貓貓不舒服");
        text.setForeground(Color.red);
        sickFrame.add(text);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int x = screenSize.width - sickFrame.getWidth()-100;
        int y = 150;
        sickFrame.setLocation(x, y);
        sickFrame.setVisible(true);
        Timer visible=new Timer(1500, new ActionListener() {
            public void actionPerformed(ActionEvent e){
                sickFrame.setVisible(false);
            }
        });
        visible.setRepeats(false);
        visible.start();
    }

    public void dieframe(){
        dieframe = new JDialog((Frame) null, true);
        dieframe.setSize(185, 100);
        dieframe.setLayout(new FlowLayout(FlowLayout.CENTER));
        dieframe.setAlwaysOnTop(true);
        dieframe.setUndecorated(true);
        dieframe.setLocationRelativeTo(deskpet);
        JLabel text=new JLabel("   喵生已結束   ");
        JLabel notice=new JLabel("此生共活了"+deskpet.getlifetime()+"(秒)");
        notice.setForeground(Color.orange);

        JPanel buttoPanel=new JPanel();
        restartgameButton =new JButton("重新開始");
        leavegameButton=new JButton("結束遊戲");
        ActionListener listener=new MyEventListner();
        restartgameButton.addActionListener(listener);
        leavegameButton.addActionListener(listener);
        buttoPanel.add(restartgameButton);
        buttoPanel.add(leavegameButton);

        dieframe.add(text);
        dieframe.add(notice);
        dieframe.add(buttoPanel);

        
        dieframe.setVisible(true);
    }

    public void cannotadv(int which){
        cannotadv=new JFrame();
        cannotadv.setSize(100, 50);
        cannotadv.setLayout(new FlowLayout(FlowLayout.CENTER));
        cannotadv.setAlwaysOnTop(true);
        cannotadv.setUndecorated(true);
        JLabel text;
        if(which==1)
            text=new JLabel("體力值不足");
        else
            text=new JLabel("飽食度不足");
        text.setForeground(Color.red);
        cannotadv.add(text);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int x = screenSize.width - cannotadv.getWidth()-100;
        int y = 150;
        cannotadv.setLocation(x, y);
        cannotadv.setVisible(true);
        Timer visible=new Timer(1500, new ActionListener() {
            public void actionPerformed(ActionEvent e){
                cannotadv.setVisible(false);
            }
        });
        visible.setRepeats(false);
        visible.start();
    }
    
    public int giveadvcount(){
        return advcount;
    }

    public void recovery(){
        sick=false;
        infoFrame.setSize(280,290);
    }

    private class MyEventListner implements ActionListener {
		
		public void actionPerformed(ActionEvent e){
            boolean pay;
            if(successframe!=null) successframe.setVisible(false);
            if(cannotadv!=null) cannotadv.setVisible(false);
            if(sickFrame!=null) sickFrame.setVisible(false);
            if(notenough!=null) notenough.setVisible(false);
			if(e.getSource()==feed1Button){
                if(sick==true){
                    sick(1);
                }
                else if(deskpet.geteating()){
                    success(3);
                }
                else{
                    pay=deskpet.setcoin(-50);
                    if(pay){
                        success(1);
                        deskpet.eat(1);
                    }
                }
			}
			else if(e.getSource()==feed2Button){   
                if(sick==true){
                    sick(1);
                }
                else if(deskpet.geteating()){
                    success(3);
                }
                else{
                    pay=deskpet.setcoin(-50);
                    if(pay){
                        success(1);
                        deskpet.eat(2);
                    }
                }
			}
			else if(e.getSource()==feed3Button){
                if(sick==true){
                    sick(1);
                }
                else if(deskpet.geteating()){
                    success(3);
                }
                else{
                    pay=deskpet.setcoin(-50);
                    if(pay){
                        deskpet.eat(3);
                        success(1);
                    }
                }
			}
            else if(e.getSource()==adventureButton){
                if(sick==true){
                    sick(2);
                }
                else{
                    if(deskpet.getEnergy()<300){
                        cannotadv(1);
                    }
                    else if(deskpet.getsatiety()<200){
                        cannotadv(2);
                    }
                    else{
                        advcount++;
                        infoFrame.setVisible(false);
                        adv=new adventure(deskpet,infoFrame);
                        
                    }
                }
            }
            else if(e.getSource()==leavegameButton){
                deskpet.leavegame();
            }
            else if(e.getSource()==restartgameButton){
                dieframe.setVisible(false);
                deskpet.restartgame();
            }
            else if(e.getSource()==lookdoctor){
                pay=deskpet.setcoin(-300);
                if(pay){
                    success(2);
                    deskpet.lookdoctor();
                }
            }
            else if(e.getSource()==specialmedicine){
                pay=deskpet.setcoin(-1000);
                if(pay){
                    success(1);
                    deskpet.specialmedicine();
                }
            }

		}
	}
}
