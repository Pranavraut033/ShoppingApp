/*
    <applet code="ui\ShopApplet.class" height="450" width="800">
    </applet>
 */
package ui;

import java.applet.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import product.*;
import product.ProductList.Category;

@SuppressWarnings({"override", "Convert2Lambda"})
public class ShopApplet extends Applet implements ActionListener {

    int BW = 80;
    int BH = 25;

    JButton back = new JButton("Back");
    JButton next = new JButton("Cart");
    JTextField searchBar = new JTextField("Search...");
    TextArea area = new TextArea();
    Label options = new Label("Filters");
    JComboBox<String> sortList = new JComboBox<>();
    JComboBox<Category> catList = new JComboBox<>();

    Applet sideMenu = new Applet();
    Applet topMenu = new Applet();
    Applet shop = new Applet();

    ProductList list = ProductList.Database();
    JButton[] btns = new JButton[]{
        next, back
    };

    public void init() {
        setLayout(new CardLayout(10, 10));

        area.setText(list.toString());
        area.setBackground(Color.decode("#ffffff"));

        options.setForeground(Color.decode("#EEEEEE"));
        options.setBackground(Color.decode("#757575"));
        options.setAlignment(Label.CENTER);

        for (JButton b : btns) {
            b.setBackground(Color.decode("#1976D2"));
            b.setForeground(Color.decode("#ffffff"));
        }

        searchBar.addKeyListener(new KeyListener() {
            public void keyTyped(KeyEvent ke) {
            }

            public void keyPressed(KeyEvent ke) {
            }

            public void keyReleased(KeyEvent ke) {
                search();
            }
        });
        searchBar.addFocusListener(new FocusListener() {
            public void focusGained(FocusEvent fe) {
                searchBar.selectAll();
            }

            public void focusLost(FocusEvent fe) {
                if (searchBar.getText().isEmpty()) {
                    searchBar.setText("Search...");
                }
            }
        });
        for (Category c : Category.values()) {
            catList.addItem(c);
        }
        catList.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent ie) {
                if (!isSearchFieldChange()) {
                    list = ProductList.Database();
                } else {
                    list = ProductList.Database().search(searchBar.getText());
                }
                if (ie.getItem().equals(Category.All)) {
                    area.setText(list.toString());
                    return;
                }
                if (ie.getStateChange() == ItemEvent.SELECTED) {
                    for (Category c : Category.values()) {
                        if (ie.getItem().equals(c)) {
                            area.setText((list = list.filter(c)).toString());
                            break;
                        }
                    }
                }
            }

        });

        sortList.addItem("Arrange...");
        sortList.addItem("Price: Low to High");
        sortList.addItem("Price: High to Low");
        sortList.addItem("Product Name: A - Z");
        sortList.addItem("Product Name: Z - A");
        sortList.addItem("Category: A - Z");
        sortList.addItem("Category: Z - A");
        sortList.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent ie) {
                if (ie.getStateChange() == ItemEvent.SELECTED) {
                    if (ie.getItem().equals("Price: Low to High")) {
                        list.sortByPrice(false);
                    } else if (ie.getItem().equals("Price: High to Low")) {
                        list.sortByPrice(true);
                    } else if (ie.getItem().equals("Product Name: A - Z")) {
                        list.sortByName(false);
                    } else if (ie.getItem().equals("Product Name: Z - A")) {
                        list.sortByName(true);
                    } else if (ie.getItem().equals("Category: A - Z")) {
                        list.sortByCat(false);
                    } else if (ie.getItem().equals("Category: Z - A")) {
                        list.sortByCat(true);
                    }
                }
                area.setText(list.toString());
            }
        });

        next.addActionListener(this);
        back.addActionListener(this);
        back.setVisible(false);

        topMenu.setLayout(new BorderLayout(10, 10));
        topMenu.add(back, BorderLayout.WEST);
        topMenu.add(searchBar, BorderLayout.CENTER);
        topMenu.add(next, BorderLayout.EAST);

        sideMenu.setLayout(new GridLayout(10, 1, 10, 10));
        sideMenu.add(options);
        sideMenu.add(sortList);
        sideMenu.add(catList);

        shop.setLayout(new BorderLayout(10, 10));
        shop.add(topMenu, BorderLayout.NORTH);
        shop.add(sideMenu, BorderLayout.WEST);
        shop.add(area, BorderLayout.CENTER);

        add(shop);
        setBackground(Color.decode("#eeeeee"));
        shop.setBackground(Color.decode("#eeeeee"));
        topMenu.setBackground(Color.decode("#eeeeee"));
        sideMenu.setBackground(Color.decode("#eeeeee"));
    }

    private void search() {
        String s = searchBar.getText();
        //TODO catList.getSelectedItem();
        if (s.isEmpty() || !isSearchFieldChange()) {
            area.setText(list.toString());
            actionPerformed(new ActionEvent(back, 0, ""));
            return;
        }
        list = list.search(s);
        area.setText(list.toString());
        back.setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        Object o = e.getSource();
        if (o == next) {
            if (isOnCart()) {
                next.setText("Check out");
                back.setVisible(true);
            } else {
            }
        } else if (o == back) {
            if (isOnHome()) {
                next.setText("Cart");
            }
            if (list.isEmpty()) {
                list = ProductList.Database();
            }
            area.setText(list.toString());
            back.setVisible(false);
        }
    }

    private boolean isSearchFieldChange() {
        return !searchBar.getText().equals("Search...");
    }

    private boolean isOnHome() {
        return next.getText().equals("Check out") && searchBar.getText().equals("Search...");
    }

    private boolean isOnCart() {
        return next.getText().equals("Cart");
    }
}
