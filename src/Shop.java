
import javax.swing.JFrame;
import ui.ShopApplet;

public class Shop {

    public static void main(String[] args) {
        ShopApplet applet = new ShopApplet();
        applet.init();
        JFrame frame = new JFrame();
        frame.setSize(900, 506);
        frame.setTitle("Shop.in");
        frame.setAlwaysOnTop(true);
        frame.add(applet);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
