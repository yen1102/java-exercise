import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;


public class Desktoppet extends JFrame {
    private JLabel petLabel1;
    private Timer timer;
    private Timer sleepTimer;
    private Timer life;
    Timer eatTimer;
    private Random random;
    private picture pc;
    private info info;
    private int screenwidth;
    private int screenheight;
    private PhotoMoving photomove1;
    private int xDirection;
    private int yDirection;
    private int state;
    private boolean ifchange;
    private boolean checkmove;
    private int sleeptime;
    private int dontsleep;
    private boolean wakeup;
    private int ifsleep;
    private boolean isstart;
    private boolean eating;
    private boolean sick;
    private Timer sickTimer;
    private int sicktime;
    private int ifsick;
    private boolean live;
    private int temp;

    private int lifetime;
    private int energy;
    private int satiety;
    private int coin;

    int inenergy,insatiety,eattimes;

    private int increase;
    boolean donteat;


    public Desktoppet() {          //初始設定
        setTitle("Desk Pet");
        live=true;
        state = 1;
        sick=false;
        dontsleep=0;
        ifchange=false;
        isstart=true;
        eating=false;

        energy=1000;         //要更改的部分
        satiety=1000;
        coin=3000;
        lifetime=0;

        lifetime();
        Dimension screensize = Toolkit.getDefaultToolkit().getScreenSize();
        screenwidth = (int) screensize.getWidth();
        screenheight = (int) screensize.getHeight();
        setSize(screenwidth, screenheight);
        getRootPane().putClientProperty("apple.awt.draggableWindowBackground", false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);
        setUndecorated(true);
        setBackground(new Color(0, 0, 0, 0));
        setAlwaysOnTop(true);
        
        pc = new picture();
        petLabel1 = pc.loadpicture(screenwidth/2, screenheight/2, "/picture/move/move1.png");
        
        JLayeredPane.putLayer(petLabel1, Integer.MIN_VALUE);
        if (petLabel1 != null) add(petLabel1);

        random = new Random();

        if (petLabel1 != null) photomove1 = new PhotoMoving(petLabel1,this);
        info=photomove1.givedeskinfo();
        checkmove = true;
        xDirection = random.nextInt(3) - 1;             //確認貓貓走的方位
        yDirection = random.nextInt(3) - 1; 
        while (xDirection == 0 && yDirection == 0) {
            xDirection = random.nextInt(3) - 1;
            yDirection = random.nextInt(3) - 1;
        }

        timer = new Timer(350, new ActionListener() {   //貓咪各種型態設置
            @Override
            public void actionPerformed(ActionEvent e) {
                repaint();
                ifchange = false;
                dontsleep++;
                energy--;
                satiety--;
                if(energy<=0 || satiety<=0){                    //死亡設置
                    died();
                    timer.stop();
                }
                if(info.giveadvcount()>50)                      //生病機率
                    ifsick=random.nextInt(250); 
                else
                    ifsick=random.nextInt(400-(info.giveadvcount()*3)); 
                if(ifsick==1 && lifetime>100){
                    sick();
                }
                else{
                    ifsleep = random.nextInt(50);           //睡覺
                    if (ifsleep == 1 && dontsleep>100) {
                        wakeup=false;
                        dontsleep=0;
                        enterSleepState();
                        ifchange=true;
                    } else {
                        updateDirection();                          //走路
                        if (!photomove1.isMoving()) {
                            if(!isstart){
                                movePet();
                            }
                                
                            updatePetImage();
                        }
                    }
                }
            }
        });
        timer.start();
    }

    public void lifetime(){                 //存活時間
        life=new Timer(1000,new ActionListener() {
            public void actionPerformed(ActionEvent e){
                lifetime++;
            }
        });
        life.start();
    }

    public void setwakeup(){               //以下各種get/set
        wakeup=true;
    }

    public boolean getsleep(){
        return (ifsleep==1);
    }

    public boolean geteating(){
        return eating;
    }

    public int getEnergy(){
        return energy;
    }

    public int getsatiety(){
        return satiety;
    }

    public int getlifetime(){
        return lifetime;
    }

    public int getcoin(){
        return coin;
    }

    public void setenergy(int increase){
        energy+=increase;
        if(energy>1000){
            energy=1000;
        }
    }

    public void setsatiety(int increase){
        satiety+=increase;
        if(satiety>1000){
            satiety=1000;
        }
    }

    public boolean setcoin(int increase){
        if(coin+increase<0){
            info.notenough();
            return false;
        }
        else{
            coin+=increase;
            return true;
        }
    }

    public boolean getlive(){
        return live;
    }

    private void sick(){                        //生病主程式
        //int tempState = state;
        state = 8;
        updatePetImage();
        ifchange = true;
        timer.stop();
        if(sleepTimer!=null)
            sleepTimer.stop();
        if(eatTimer!=null)
            eatTimer.stop();
        info.sick();
        int sicklong=random.nextInt(100)+400;
        sickTimer = new Timer(600, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(energy<=0 || satiety<=0){
                    died();
                    sickTimer.stop();
                }
                energy++;
                satiety--;
                sicktime++;
                if (sicktime>=sicklong ) {   
                    sickTimer.stop();
                    //state = tempState;
                    timer.start();
                    recovery();
                }
            }
        });
        sickTimer.start();
    }

    public void specialmedicine(){
        sicktime=1000;
    }

    public void lookdoctor(){
        sicktime+=random.nextInt(50)+100;
    }

    public void recovery(){
        updatePetImage();
        sick=false;
        info.recovery();
    }

    private void died(){
        life.stop();
        energy=0;
        satiety=0;
        state=6;
        updatePetImage();
        live=false;
        if(info.getinfo()!=null)
            info.getinfo().setVisible(false);
        info.dieframe();
    }

    public void adventurestart(){       //探險開始
        if(sleepTimer!=null)
            sleepTimer.stop();
        if(eatTimer!=null)
            eatTimer.stop();
        timer.stop();
        setVisible(false);
        energy-=300;
        satiety-=200;
    }

    public void adventureend(){         //探險結束
        isstart=true;
        setVisible(true);
        timer.start();
        add(petLabel1);
    }

    private void enterSleepState() {        //睡覺設置
        int tempState = state;
        state = 3;
        updatePetImage();
        ifchange = true;
        timer.stop();
        sleeptime = 0;
        sleepTimer = new Timer(350, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(energy<=0 || satiety<=0){
                    died();
                    sleepTimer.stop();
                }
                sleeptime++;
                energy+=2;
                satiety--;
                int ifwake = random.nextInt(20);
                if ((ifwake == 1 && sleeptime > 30) || energy>=1000 ||wakeup ) {   
                    sleepTimer.stop();
                    state = tempState;
                    updatePetImage();
                    timer.start();
                }
            }
        });
        sleepTimer.start();
    }

    private void updateDirection() {    //換方向跟狀態
        
        if(xDirection<=0 && (energy<200 || satiety <200)){
            if(state!=4) ifchange=true;
            state=4;
        }
        else if (xDirection <= 0 ) {
            if (state != 2) ifchange = true;
            state = 2;}
        else if((energy<200||satiety<200)){
            if(state!=5) ifchange=true;
            state=5;
        }
         else {
            if (state != 1) ifchange = true;
            state = 1;
        }
    }

    private void updatePetImage() {                 //動畫設置
        if ((state == 1 && ifchange) || (state==1 && isstart)) {
            System.out.println("now is state"+state);
            if(isstart)
                isstart=false;
            pc.cgJLabelImg(petLabel1, "/picture/moveright/moveright1.png", pc.moverightstring(),8);
        } else if ((state == 2 && ifchange)|| (state==2 && isstart)) {
            System.out.println("now is state"+state);
            if(isstart)
                isstart=false;
            pc.cgJLabelImg(petLabel1, "/picture/move/move1.png", pc.movestring(),8);
        } else if (state == 3) {
            System.out.println("now is state"+state);
            pc.cgJLabelImg(petLabel1, "/picture/sleep/sleep1.png", pc.sleepstring(),4);
        }
        else if ((state == 5 && ifchange)|| (state==5 && isstart)) {
            System.out.println("now is state"+state);
            if(isstart)
                isstart=false;
            pc.cgJLabelImg(petLabel1, "/picture/tiredright/tiredright1.png", pc.tiredrightstring(),2);
        }
        else if ((state == 4 && ifchange)|| (state==4 && isstart)) {
            System.out.println("now is state"+state);
            if(isstart)
                isstart=false;
            pc.cgJLabelImg(petLabel1, "/picture/tired/tired1.png", pc.tiredstring(),2);
        }
        else if((state==6)){
            System.out.println("now is state"+state);
            pc.cgJLabelImg(petLabel1, "/picture/die/die1.png", pc.diestring(),2);
        }
        else if((state==7)){
            System.out.println("now is state"+state);
            pc.cgJLabelImg(petLabel1, "/picture/eat/eat1.png", pc.eatstring(),8);
        }
        else if(state==8){
            System.out.println("now is state"+state);
            pc.cgJLabelImg(petLabel1, "/picture/sick/sick1.png", pc.sickstring(), 4);
        }
    }

    


    public void eat(int food){          //吃東西
        wakeup=false;
        donteat=false;
        if(eating)
            eatTimer.stop();
        temp=state;
        if(eatTimer!=null)
            eatTimer.stop();
        ifchange=true;
        timer.stop();
        if(sleepTimer!=null)
            sleepTimer.stop();
        state=7;
        updatePetImage();
        switch(food){
            case 1:
                inenergy=0;
                insatiety=50;
                break;
            case 2:
                inenergy=25;
                insatiety=25;
                break;
            case 3:
                inenergy=50;
                insatiety=0;
                break;
        }
        eattimes=0;
        eating=true;
        increase=0;
        eatTimer=new Timer(350, new ActionListener(){
            public void actionPerformed(ActionEvent e){
                if(donteat){
                    timer.start();
                    eatTimer.stop();
                    state=temp;
                    updatePetImage();
                    eating=false;
                    if(energy>1000)
                        energy=1000;
                    if(satiety>1000)
                        satiety=1000;
                }
                if(eattimes==9||wakeup){
                    
                    timer.start();
                    eatTimer.stop();
                    state=temp;
                    updatePetImage();
                    eating=false;
                    if(energy>1000)
                        energy=1000;
                    if(satiety>1000)
                        satiety=1000;
                }
                energy+=inenergy;
                satiety+=insatiety;
                increase+=inenergy;
                eattimes++;
                if(energy>1000)
                    energy=1000;
                if(satiety>1000)
                    satiety=1000;
            }
        });
        eatTimer.start();
        
    }

    private void movePet() {            //移動
        int x = petLabel1.getX() + xDirection * 15;
        int y = petLabel1.getY() + yDirection * 15;

        if (x < 0 || x > getWidth() - petLabel1.getWidth()) {
            xDirection = -xDirection;
            x = Math.max(0, Math.min(x, getWidth() - petLabel1.getWidth()));
        }
        if (y < 0 || y > getHeight() - petLabel1.getHeight()) {
            yDirection = -yDirection;
            y = Math.max(0, Math.min(y, getHeight() - petLabel1.getHeight()));
        }

        if (xDirection == 0 || yDirection == 0) {
            xDirection = random.nextInt(3) - 1;
            yDirection = random.nextInt(3) - 1;
            while (xDirection == 0 && yDirection == 0) {
                xDirection = random.nextInt(3) - 1;
                yDirection = random.nextInt(3) - 1;
            }
        }

        if (ifchange || isstart) 
            checkmove = false;

        if (!ifchange) {
            if (checkmove) petLabel1.setLocation(x, y);
            checkmove = true;
        }
    }

    public void leavegame(){
        System.exit(0);
    }

    public void restartgame(){
        setVisible(false);
        new Desktoppet().setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Desktoppet().setVisible(true);
            }
        });
    }
}
