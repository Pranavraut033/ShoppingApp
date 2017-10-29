package ui;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;
import product.*;

public class ListItem extends JTextArea implements ListCellRenderer<Product> {

    boolean isSelected;

    public void paint(Graphics g) {
        g.setColor(isSelected ? Color.WHITE : Color.decode("#eeeeee"));
        g.fillRect(10, 10, getWidth() - 20, getHeight() - 20);
        g.setColor(isSelected ? Color.BLUE : Color.BLACK);
        g.drawRect(8, 10, getWidth() - 18, getHeight() - 20);
        g.drawRect(10, 11, getWidth() - 20, getHeight() - 22);
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

        Border paddingBorder = new EmptyBorder(5, 20, 5, 20);

        setBorder(paddingBorder);
        return this;
    }
}
