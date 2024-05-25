import java.awt.Image;
import javax.swing.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class picture {
    private ExecutorService executorService = Executors.newSingleThreadExecutor();
    private volatile boolean stopAnimation = false;

    public JLabel loadpicture(int x, int y, String url) {
        JLabel label = new JLabel();
        ImageIcon icon = new ImageIcon(getClass().getResource(url));
        int picWidth = icon.getIconWidth(), picHeight = icon.getIconHeight();

        icon.setImage(icon.getImage().getScaledInstance(picWidth / 3, picHeight / 3, Image.SCALE_SMOOTH));
        label.setBounds(x, y, picWidth / 3, picHeight / 3);
        label.setIcon(icon);
        return label;
    }

    public void cgJLabelImg(JLabel label, String imgUrl, String whichString,int picturenumbers) {
        stopAnimation = true;
        executorService.execute(() -> {
            stopAnimation = false;
            int i = 1;
            try {
                while (!stopAnimation) {
                    Thread.sleep(350);
                    ImageIcon icon = new ImageIcon(getClass().getResource(whichString + i + ".png"));
                    int picWidth = icon.getIconWidth() / 3, picHeight = icon.getIconHeight() / 3;
                    icon.setImage(icon.getImage().getScaledInstance(picWidth, picHeight, Image.SCALE_DEFAULT));
                    label.setIcon(icon);
                    if (i >= picturenumbers) i = 1;
                    else i++;
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
    }

    public String movestring() {
        return "/picture/move/move";
    }

    public String moverightstring() {
        return "/picture/moveright/moveright";
    }

    public String sleepstring(){
        return "/picture/sleep/sleep";
    }

    public String tiredstring(){
        return "/picture/tired/tired";
    }

    public String tiredrightstring(){
        return "/picture/tiredright/tiredright";
    }

    public String diestring(){
        return "/picture/die/die";
    }

    public String eatstring(){
        return "/picture/eat/eat";
    }

    public String sickstring(){
        return "/picture/sick/sick";
    }
}
