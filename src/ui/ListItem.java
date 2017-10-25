package ui;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;
import product.*;

public class ListItem extends JTextArea implements ListCellRenderer<Product> {

    public Container getListCellRendererComponent(JList<? extends Product> jlist, Product product, int index, boolean isSelected, boolean cellHasFocus) {
        setText(product.toString());
        setEditable(false);
        Border border;
        if (isSelected) {
            border = new LineBorder(Color.BLUE, 3, false);
        } else {
            border = new LineBorder(Color.gray, 1, false);
        }
        Border paddingBorder = new EmptyBorder(2, 6, 2, 6);
        border = new CompoundBorder(paddingBorder, border);
        paddingBorder = new EmptyBorder(2, 6, 2, 6);
        border = new CompoundBorder(border, paddingBorder);

        setBorder(border);
        return this;
    }

}
