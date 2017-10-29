package ui;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;
import product.*;

public class ListItem extends JTextArea implements ListCellRenderer<Product> {

    boolean isSelected;

    public void paint(Graphics g) {
        g.setColor(isSelected ? Color.WHITE : Color.decode("#eeeeee"));
        g.fillRect(10, 5, getWidth() - 20, getHeight() - 10);
        g.setColor(isSelected ? Color.BLUE : Color.DARK_GRAY);
        drawBorder(g, 3);
        super.paint(g);
    }

    private void drawBorder(Graphics g, int t) {
        for (int i = isSelected ? 9 : 10, j = isSelected ? 4 : 5, k = 0; k < t; i++, j++, k++) {
            g.drawRect(i, j, getWidth() - 2 * i, getHeight() - 2 * j);
        }
    }

    public Container getListCellRendererComponent(JList<? extends Product> jlist, Product product, int index, boolean isSelected, boolean cellHasFocus) {
        setText(product.toString());
        this.isSelected = isSelected;
        setEditable(false);
        setOpaque(false);
        setFont(new Font(null, Font.BOLD, 13));

        if (isSelected) {
            setForeground(Color.BLACK);
        } else {
            setForeground(Color.decode("#212121"));
        }

        Border paddingBorder = new EmptyBorder(0, 20, 0, 20);

        setBorder(paddingBorder);
        return this;
    }

}
