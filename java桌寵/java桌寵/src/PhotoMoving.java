import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;

public class PhotoMoving {

    private JLabel Pic;
    private Desktoppet deskpet;
    private boolean infoVisible;
    private info Info;
    MyMouseInputAdapter listener;
    
    public PhotoMoving(JLabel Pic,Desktoppet deskpet) {
        this.Pic = Pic;
        this.deskpet=deskpet;
        this.Info=new info(deskpet);
        infoVisible=false;
        listener = new MyMouseInputAdapter(); 
        Pic.addMouseListener(listener); 
        Pic.addMouseMotionListener(listener);
        
    }

    public info givedeskinfo(){
        return Info;
    }

    class MyMouseInputAdapter extends MouseInputAdapter {
        Point point = new Point(0, 0); 
        boolean dragging = false; // 是否正在拖動

        public void mousePressed(MouseEvent e) {
            if(SwingUtilities.isLeftMouseButton(e)){
                dragging = true;
                Info.getinfo().setVisible(false);
            }
            point = SwingUtilities.convertPoint(Pic, e.getPoint(), Pic.getParent()); // 當前座標
            if((deskpet.getsleep() && SwingUtilities.isLeftMouseButton(e))||(deskpet.geteating() && SwingUtilities.isLeftMouseButton(e))){
                deskpet.setwakeup();
            }
            if (SwingUtilities.isRightMouseButton(e)) {
                showInfoInTopRightCorner();
            }
        }

        public void mouseDragged(MouseEvent e) {
            if (!dragging) {
                return; 
            }
            Point newPoint = SwingUtilities.convertPoint(Pic, e.getPoint(), Pic.getParent()); 
            Pic.setLocation(Pic.getX() + (newPoint.x - point.x), Pic.getY() + (newPoint.y - point.y)); 
            point = newPoint; // 更改座標點
        }

        public void mouseReleased(MouseEvent e) {
            dragging = false; 
        }

        private void showInfoInTopRightCorner() {
            if(deskpet.getlive()){
                infoVisible = !infoVisible;
                if (infoVisible) {
                    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
                    int x = screenSize.width - Info.getinfo().getWidth();
                    int y = 0;
                    Info.getinfo().setLocation(x, y);
                }
                Info.getinfo().setVisible(infoVisible);
            }
        }
        
    }
    public boolean isMoving() {
        return listener.dragging;
    }

    
	
}
