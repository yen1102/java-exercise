import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
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

    int inenergy, insatiety, eattimes;

    private int increase;
    boolean donteat;
    Firebase Ref =new Firebase ( "https://keepetogether-default-rtdb.asia-southeast1.firebasedatabase.app/> " );
    public Desktoppet() { // 初始設定
        
        setTitle("Desk Pet");
        Ref.child(IdWindow.storedID).child("live").setValue(true);
        live =true ;
        Ref.child(IdWindow.storedID).child("state").setValue(1);
        state = 1;
        sick = false;
        Ref.child(IdWindow.storedID).child("sick").setValue(false);
        dontsleep = 0;
        Ref.child(IdWindow.storedID).child("dontsleep").setValue(0);
        ifchange = false;
        Ref.child(IdWindow.storedID).child("ifchange").setValue(false);
        isstart = true;
        Ref.child(IdWindow.storedID).child("isstart").setValue(true);
        eating = false;
        Ref.child(IdWindow.storedID).child("eating").setValue(true);

        energy = 1000; // 要更改的部分
        Ref.child(IdWindow.storedID).child("energy").setValue(1000);
        satiety = 1000;
        Ref.child(IdWindow.storedID).child("satiety").setValue(1000);
        coin = 3000;
        Ref.child(IdWindow.storedID).child("coin").setValue(3000);
        lifetime = 0;
        Ref.child(IdWindow.storedID).child("lifetime").setValue(0);

        lifetime();
        Dimension screensize = Toolkit.getDefaultToolkit().getScreenSize();
        screenwidth = (int) screensize.getWidth();
        Ref.child(IdWindow.storedID).child("screenwidth").setValue(screenwidth);
        screenheight = (int) screensize.getHeight();
        Ref.child(IdWindow.storedID).child("screenheight").setValue(screenheight);
        setSize(screenwidth, screenheight);
        getRootPane().putClientProperty("apple.awt.draggableWindowBackground", false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);
        setUndecorated(true);
        setBackground(new Color(0, 0, 0, 0));
        setAlwaysOnTop(true);

        pc = new picture();
        petLabel1 = pc.loadpicture(screenwidth / 2, screenheight / 2, "/pic/move/move1.png");

        JLayeredPane.putLayer(petLabel1, Integer.MIN_VALUE);
        if (petLabel1 != null)
            add(petLabel1);

        random = new Random();
        
        if (petLabel1 != null)
            photomove1 = new PhotoMoving(petLabel1, this);
        info = photomove1.givedeskinfo();
        checkmove = true;
        Ref.child(IdWindow.storedID).child("checkmove").setValue(true);
        xDirection = random.nextInt(3) - 1; // 確認貓貓走的方位
        Ref.child(IdWindow.storedID).child("xDirection").setValue(xDirection);
        yDirection = random.nextInt(3) - 1;
        Ref.child(IdWindow.storedID).child("yDirection").setValue(yDirection);
        while (xDirection == 0 && yDirection == 0) {
            xDirection = random.nextInt(3) - 1;
            Ref.child(IdWindow.storedID).child("xDirection").setValue(xDirection);
            yDirection = random.nextInt(3) - 1;
            Ref.child(IdWindow.storedID).child("yDirection").setValue(yDirection);
        }

        timer = new Timer(350, new ActionListener() { // 貓咪各種型態設置
            @Override
            public void actionPerformed(ActionEvent e) {
                repaint();
                ifchange = false;
                Ref.child(IdWindow.storedID).child("ifchange").setValue(false);                
                dontsleep++;
                Ref.child(IdWindow.storedID).child("dontsleep").setValue(dontsleep);
                energy--;
                Ref.child(IdWindow.storedID).child("energy").setValue(energy);
                satiety--;
                Ref.child(IdWindow.storedID).child("satiety").setValue(satiety);
                if (energy <= 0 || satiety <= 0) { // 死亡設置
                    died();
                    timer.stop();
                }
                if (info.giveadvcount() > 50){ // 生病機率
                    ifsick = random.nextInt(250);
                    Ref.child(IdWindow.storedID).child("ifsick").setValue(ifsick);
                }else{
                    ifsick = random.nextInt(400 - (info.giveadvcount() * 3));
                    Ref.child(IdWindow.storedID).child("ifsick").setValue(ifsick);
                }
                    
                if (ifsick == 1 && lifetime > 100) {
                    sick();
                } else {
                    ifsleep = random.nextInt(50); // 睡覺
                    Ref.child(IdWindow.storedID).child("ifsick").setValue(ifsick);
                    if (ifsleep == 1 && dontsleep > 100) {
                        wakeup = false;
                        Ref.child(IdWindow.storedID).child("wakeup").setValue(false);
                        dontsleep = 0;
                        Ref.child(IdWindow.storedID).child("dontsleep").setValue(0);
                        enterSleepState();
                        ifchange = true;
                        Ref.child(IdWindow.storedID).child("ifchange").setValue(true);
                    } else {
                        updateDirection(); // 走路
                        if (!photomove1.isMoving()) {
                            if (!isstart) {
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

    public void lifetime() { // 存活時間
        life = new Timer(1000, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                lifetime++;
                Ref.child(IdWindow.storedID).child("lifetime").setValue(lifetime);
            }
        });
        life.start();
    }

    public void setwakeup() { // 以下各種get/set
        wakeup = true;
        Ref.child(IdWindow.storedID).child("wakeup").setValue(true);
    }

    public boolean getsleep() {
        return (ifsleep == 1);
    }

    public boolean geteating() {
        return eating;
    }

    public int getEnergy() {
        return energy;
    }

    public int getsatiety() {
        return satiety;
    }

    public int getlifetime() {
        return lifetime;
    }

    public int getcoin() {
        return coin;
    }

    public void setenergy(int increase) {
        energy += increase;
        Ref.child(IdWindow.storedID).child("energy").setValue(energy);
        if (energy > 1000) {
            energy = 1000;
            Ref.child(IdWindow.storedID).child("energy").setValue(1000);
        }
    }

    public void setsatiety(int increase) {
        satiety += increase;
        Ref.child(IdWindow.storedID).child("satierty").setValue(satiety);

        if (satiety > 1000) {
            satiety = 1000;
            Ref.child(IdWindow.storedID).child("satiety").setValue(1000);
        }
    }

    public boolean setcoin(int increase) {
        if (coin + increase < 0) {
            info.notenough();
            return false;
        } else {
            coin += increase;
            Ref.child(IdWindow.storedID).child("coin").setValue(coin);
            return true;
        }
    }

    public boolean getlive() {
        return live;
    }

    private void sick() { // 生病主程式
        // int tempState = state;
        state = 8;
        Ref.child(IdWindow.storedID).child("state").setValue(8);
        updatePetImage();
        ifchange = true;
        Ref.child(IdWindow.storedID).child("ifchange").setValue(true);
        timer.stop();
        if (sleepTimer != null)
            sleepTimer.stop();
        if (eatTimer != null)
            eatTimer.stop();
        info.sick();
        int sicklong = random.nextInt(100) + 400;
        Ref.child(IdWindow.storedID).child("sicklong").setValue(sicklong);
        sickTimer = new Timer(600, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (energy <= 0 || satiety <= 0) {
                    died();
                    sickTimer.stop();
                }
                energy++;
                Ref.child(IdWindow.storedID).child("energy").setValue(energy);
                satiety--;
                Ref.child(IdWindow.storedID).child("satiety").setValue(satiety);
                sicktime++;
                Ref.child(IdWindow.storedID).child("sicktime").setValue(sicktime);
                if (sicktime >= sicklong) {
                    sickTimer.stop();
                    // state = tempState;
                    timer.start();
                    recovery();
                }
            }
        });
        sickTimer.start();
    }

    public void specialmedicine() {
        sicktime = 1000;
        Ref.child(IdWindow.storedID).child("sicktime").setValue(1000);
    }

    public void lookdoctor() {
        sicktime += random.nextInt(50) + 100;
        Ref.child(IdWindow.storedID).child("sicktime").setValue(sicktime);
    }

    public void recovery() {
        updatePetImage();
        sick = false;
        Ref.child(IdWindow.storedID).child("sick").setValue(false);
        info.recovery();
    }

    private void died() {
        life.stop();
        energy = 0;
        Ref.child(IdWindow.storedID).child("energy").setValue(0);
        satiety = 0;
        Ref.child(IdWindow.storedID).child("satiety").setValue(satiety);
        state = 6;
        Ref.child(IdWindow.storedID).child("state").setValue(state);
        updatePetImage();
        live = false;
        Ref.child(IdWindow.storedID).child("live").setValue(false);
        if (info.getinfo() != null)
            info.getinfo().setVisible(false);
        info.dieframe();
        Ref.child(IdWindow.storedID).child("use").setValue(0);
    }

    public void adventurestart() { // 探險開始
        if (sleepTimer != null)
            sleepTimer.stop();
        if (eatTimer != null)
            eatTimer.stop();
        timer.stop();
        setVisible(false);
        energy -= 300;
        Ref.child(IdWindow.storedID).child("energy").setValue(energy);
        satiety -= 200;
        Ref.child(IdWindow.storedID).child("satiety").setValue(satiety);
    }

    public void adventureend() { // 探險結束
        isstart = true;
        Ref.child(IdWindow.storedID).child("isstart").setValue(true);
        setVisible(true);
        timer.start();
        add(petLabel1);
    }

    private void enterSleepState() { // 睡覺設置
        int tempState = state;
        state = 3;
        Ref.child(IdWindow.storedID).child("state").setValue(state);
        updatePetImage();
        ifchange = true;
        Ref.child(IdWindow.storedID).child("ifchange").setValue(true);
        timer.stop();
        sleeptime = 0;
        Ref.child(IdWindow.storedID).child("sleeptime").setValue(0);
        sleepTimer = new Timer(350, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (energy <= 0 || satiety <= 0) {
                    died();
                    sleepTimer.stop();
                }
                sleeptime++;
                Ref.child(IdWindow.storedID).child("sleeptime").setValue(sleeptime);
                energy += 2;
                Ref.child(IdWindow.storedID).child("energy").setValue(energy);
                satiety--;
                Ref.child(IdWindow.storedID).child("satiety").setValue(satiety);
                int ifwake = random.nextInt(20);
                Ref.child(IdWindow.storedID).child("ifwake").setValue(1);
                if ((ifwake == 1 && sleeptime > 30) || energy >= 1000 || wakeup) {
                    sleepTimer.stop();
                    state = tempState;
                    Ref.child(IdWindow.storedID).child("state").setValue(state);
                    updatePetImage();
                    timer.start();
                }
            }
        });
        sleepTimer.start();
    }

    private void updateDirection() { // 換方向跟狀態

        if (xDirection <= 0 && (energy < 200 || satiety < 200)) {
            if (state != 4)
                ifchange = true;
                Ref.child(IdWindow.storedID).child("ifchange").setValue(true);
            state = 4;
            Ref.child(IdWindow.storedID).child("state").setValue(state);
        } else if (xDirection <= 0) {
            if (state != 2)
                ifchange = true;
                Ref.child(IdWindow.storedID).child("ifchange").setValue(true);
            state = 2;
            Ref.child(IdWindow.storedID).child("state").setValue(state);

        } else if ((energy < 200 || satiety < 200)) {
            if (state != 5)
                ifchange = true;
                Ref.child(IdWindow.storedID).child("ifchange").setValue(true);
            state = 5;
            Ref.child(IdWindow.storedID).child("state").setValue(state);
        } else {
            if (state != 1)
                ifchange = true;
                Ref.child(IdWindow.storedID).child("ifchange").setValue(true);
            state = 1;
            Ref.child(IdWindow.storedID).child("state").setValue(state);
        }
    }

    private void updatePetImage() { // 動畫設置
        if ((state == 1 && ifchange) || (state == 1 && isstart)) {
            System.out.println("now is state" + state);
            if (isstart)
                isstart = false;
                Ref.child(IdWindow.storedID).child("ifstart").setValue(false);
            pc.cgJLabelImg(petLabel1, "/pic/moveright/moveright1.png", pc.moverightstring(), 8);
        } else if ((state == 2 && ifchange) || (state == 2 && isstart)) {
            System.out.println("now is state" + state);
            if (isstart)
                isstart = false;
                Ref.child(IdWindow.storedID).child("ifstart").setValue(false);
            pc.cgJLabelImg(petLabel1, "/pic/move/move1.png", pc.movestring(), 8);
        } else if (state == 3) {
            System.out.println("now is state" + state);
            pc.cgJLabelImg(petLabel1, "/pic/sleep/sleep1.png", pc.sleepstring(), 4);
        } else if ((state == 5 && ifchange) || (state == 5 && isstart)) {
            System.out.println("now is state" + state);
            if (isstart)
                isstart = false;
                Ref.child(IdWindow.storedID).child("ifstart").setValue(false);
            pc.cgJLabelImg(petLabel1, "/pic/tiredright/tiredright1.png", pc.tiredrightstring(), 2);
        } else if ((state == 4 && ifchange) || (state == 4 && isstart)) {
            System.out.println("now is state" + state);
            if (isstart)
                isstart = false;
                Ref.child(IdWindow.storedID).child("ifstart").setValue(false);
            pc.cgJLabelImg(petLabel1, "/pic/tired/tired1.png", pc.tiredstring(), 2);
        } else if ((state == 6)) {
            System.out.println("now is state" + state);
            pc.cgJLabelImg(petLabel1, "/pic/die/die1.png", pc.diestring(), 2);
        } else if ((state == 7)) {
            System.out.println("now is state" + state);
            pc.cgJLabelImg(petLabel1, "/pic/eat/eat1.png", pc.eatstring(), 8);
        } else if (state == 8) {
            System.out.println("now is state" + state);
            pc.cgJLabelImg(petLabel1, "/pic/sick/sick1.png", pc.sickstring(), 4);
        }
    }

    public void eat(int food) { // 吃東西
        wakeup = false;
        Ref.child(IdWindow.storedID).child("wakeup").setValue(false);
        donteat = false;
        Ref.child(IdWindow.storedID).child("donteat").setValue(false);
        if (eating)
            eatTimer.stop();
        temp = state;
        if (eatTimer != null)
            eatTimer.stop();
        ifchange = true;
        Ref.child(IdWindow.storedID).child("ifchange").setValue(true);
        timer.stop();
        if (sleepTimer != null)
            sleepTimer.stop();
        state = 7;
        Ref.child(IdWindow.storedID).child("state").setValue(state);
        updatePetImage();
        switch (food) {
            case 1:
                inenergy = 0;
                Ref.child(IdWindow.storedID).child("inenergy").setValue(inenergy);
                insatiety = 50;
                Ref.child(IdWindow.storedID).child("insatiety").setValue(insatiety);
                break;
            case 2:
                inenergy = 25;
                insatiety = 25;
                Ref.child(IdWindow.storedID).child("inenergy").setValue(inenergy);
                Ref.child(IdWindow.storedID).child("insatiety").setValue(insatiety);
                break;
            case 3:
                inenergy = 50;
                insatiety = 0;
                Ref.child(IdWindow.storedID).child("insatiety").setValue(insatiety);
                Ref.child(IdWindow.storedID).child("inenergy").setValue(inenergy);
                break;
        }
        eattimes = 0;
        Ref.child(IdWindow.storedID).child("eattimes").setValue(eattimes);
        eating = true;
        Ref.child(IdWindow.storedID).child("eating").setValue(true);
        increase = 0;
        Ref.child(IdWindow.storedID).child("increase").setValue(increase);
        eatTimer = new Timer(350, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (donteat) {
                    timer.start();
                    eatTimer.stop();
                    state = temp;
                    Ref.child(IdWindow.storedID).child("state").setValue(state);
                    updatePetImage();
                    eating = false;
                    Ref.child(IdWindow.storedID).child("eating").setValue(false);
                    if (energy > 1000)
                        energy = 1000;
                        Ref.child(IdWindow.storedID).child("energy").setValue(energy);
                    if (satiety > 1000)
                        satiety = 1000;
                        Ref.child(IdWindow.storedID).child("satiety").setValue(satiety);
                }
                if (eattimes == 9 || wakeup) {

                    timer.start();
                    eatTimer.stop();
                    state = temp;
                    Ref.child(IdWindow.storedID).child("state").setValue(state);
                    updatePetImage();
                    eating = false;
                    Ref.child(IdWindow.storedID).child("eating").setValue(eating);
                    if (energy > 1000)
                        energy = 1000;
                        Ref.child(IdWindow.storedID).child("energy").setValue(energy);
                    if (satiety > 1000)
                        satiety = 1000;
                        Ref.child(IdWindow.storedID).child("satiety").setValue(satiety);
                }
                energy += inenergy;
                Ref.child(IdWindow.storedID).child("energy").setValue(energy);
                satiety += insatiety;
                Ref.child(IdWindow.storedID).child("satiety").setValue(satiety);
                increase += inenergy;
                Ref.child(IdWindow.storedID).child("increase").setValue(increase);
                eattimes++;
                Ref.child(IdWindow.storedID).child("eattimes").setValue(eattimes);
                if (energy > 1000)
                    energy = 1000;
                    Ref.child(IdWindow.storedID).child("energy").setValue(energy);
                if (satiety > 1000)
                    satiety = 1000;
                    Ref.child(IdWindow.storedID).child("satiety").setValue(satiety);
            }
        });
        eatTimer.start();

    }

    private void movePet() { // 移動
        int x = petLabel1.getX() + xDirection * 15;
        Ref.child(IdWindow.storedID).child("x").setValue(x);
        int y = petLabel1.getY() + yDirection * 15;
        Ref.child(IdWindow.storedID).child("y").setValue(y);
        if (x < 0 || x > getWidth() - petLabel1.getWidth()) {
            xDirection = -xDirection;
            Ref.child(IdWindow.storedID).child("xDirection").setValue(xDirection);
            x = Math.max(0, Math.min(x, getWidth() - petLabel1.getWidth()));
            Ref.child(IdWindow.storedID).child("x").setValue(x);
        }
        if (y < 0 || y > getHeight() - petLabel1.getHeight()) {
            yDirection = -yDirection;
            Ref.child(IdWindow.storedID).child("yDirection").setValue(yDirection);
            y = Math.max(0, Math.min(y, getHeight() - petLabel1.getHeight()));
            Ref.child(IdWindow.storedID).child("y").setValue(y);
        }

        if (xDirection == 0 || yDirection == 0) {
            xDirection = random.nextInt(3) - 1;
            Ref.child(IdWindow.storedID).child("xDirection").setValue(xDirection);
            yDirection = random.nextInt(3) - 1;
            Ref.child(IdWindow.storedID).child("yDirection").setValue(yDirection);
            while (xDirection == 0 && yDirection == 0) {
                xDirection = random.nextInt(3) - 1;
                Ref.child(IdWindow.storedID).child("xDirection").setValue(xDirection);
                yDirection = random.nextInt(3) - 1;
                Ref.child(IdWindow.storedID).child("yDirection").setValue(yDirection);
            }
        }

        if (ifchange || isstart)
            checkmove = false;
            Ref.child(IdWindow.storedID).child("checkmove").setValue(false);

        if (!ifchange) {
            if (checkmove)
                petLabel1.setLocation(x, y);
            checkmove = true;
            Ref.child(IdWindow.storedID).child("checkmove").setValue(true);

        }
    }

    public void leavegame() {
        System.exit(0);
    }

    public void restartgame() {
        setVisible(false);
        new Desktoppet().setVisible(true);
    }
    
    public static void main(String[] args) {
        IdWindow inputIdWindow =new IdWindow();
    }

}