package ui;

import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.Graphics;
import java.net.URL;
import javax.swing.JEditorPane;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import product.Product;

public class ListItem extends JEditorPane implements ListCellRenderer<Product> {

    boolean isSelected;
    boolean onfeatured;
    private URL codeBase;
    private Product product;

    ListItem(URL codeBase, boolean b) {
        super();
        this.onfeatured = b;
        this.codeBase = codeBase;
    }

    public void paint(Graphics g) {
        g.setColor(isSelected ? Color.WHITE : Color.decode("#eeeeee"));
        g.fillRect(10, 5, getWidth() - 20, getHeight() - 10);
        g.setColor(isSelected ? Color.BLUE : Color.DARK_GRAY);
        /*
        Image a = null;
        try {
            a = ImageIO.read(new File(codeBase.toString() + product.name + ".png"));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        if (a != null) {
            g.drawImage(a, getHeight() - 20, getHeight() - 20, this);
        }
         */
        drawBorder(g, 3);
        super.paint(g);
    }

    private void drawBorder(Graphics g, int t) {
        for (int i = isSelected ? 9 : 10, j = isSelected ? 4 : 5, k = 0; k < t; i++, j++, k++) {
            g.drawRect(i, j, getWidth() - 2 * i, getHeight() - 2 * j);
        }
    }

    public Container getListCellRendererComponent(JList<? extends Product> jlist, Product product, int index, boolean isSelected, boolean cellHasFocus) {
        setContentType("text/html");
        product.onfeatured(onfeatured);
        this.isSelected = isSelected;
        this.product = product;
        setText(product.getHtmlString());
        if (onfeatured) {
            setToolTipText(product.description);
        }
        setEditable(false);
        setOpaque(false);
        setFont(new Font(null, Font.BOLD, 14));
        
        Border paddingBorder = new EmptyBorder(15, 20, 15, 20);
        setBorder(paddingBorder);
        return this;
    }
}
