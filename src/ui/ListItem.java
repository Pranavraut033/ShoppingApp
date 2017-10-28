package ui;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;
import product.*;

public class ListItem extends JTextArea implements ListCellRenderer<Product> {

    boolean isSelected;

    public void paint(Graphics g) {
        g.setColor(isSelected ? Color.WHITE : Color.decode("#eeeeee"));
        g.fillRect(5, 10, getWidth() - 10, getHeight() - 20);
        g.setColor(isSelected ? Color.BLUE : Color.BLACK);
        g.drawRect(4, 10, getWidth() - 8, getHeight() - 20);
        g.drawRect(5, 11, getWidth() - 10, getHeight() - 22);
        super.paint(g);
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

        Border paddingBorder = new EmptyBorder(10, 10, 10, 10);

        setBorder(paddingBorder);
        return this;
    }
}
