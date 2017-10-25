/*
    <applet code="ui\ShopApplet.class" height="450" width="800">
    </applet>
 */
package ui;

import java.applet.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import product.*;
import product.ProductList.Category;

@SuppressWarnings({"override", "Convert2Lambda"})
public class ShopApplet extends Applet implements ActionListener {

    Container navBar = new Container();
    JButton back = new JButton("Back");
    JTextField searchBar = new JTextField("Search...");
    JButton buy = new JButton("Add to cart");
    JButton next = new JButton("Cart");

    Container sideMenu = new Container();
    Label sideTitle = new Label("Filter");
    JComboBox<String> sortOption = new JComboBox<>();
    JComboBox<Category> categoryOption = new JComboBox<>();
    JButton reset = new JButton("Reset");

    ProductList db = ProductList.getDatabase();
    ProductList list = new ProductList(db);
    JList<Product> shopItems = new JList<>();
    ProductList cartItems = new ProductList();
    String key = "";

    public void init() {
        setSize(900, 506);

        sideTitle.setForeground(Color.decode("#EEEEEE"));
        sideTitle.setBackground(Color.decode("#757575"));
        sideTitle.setAlignment(Label.CENTER);

        JButton[] btns = new JButton[]{
            next, back, reset, buy
        };
        for (JButton b : btns) {
            b.setBackground(Color.decode("#1976D2"));
            b.setForeground(Color.decode("#ffffff"));
            b.addActionListener(this);
        }

        searchBar.addKeyListener(new KeyListener() {
            public void keyTyped(KeyEvent ke) {

            }

            public void keyPressed(KeyEvent ke) {
            }

            public void keyReleased(KeyEvent ke) {
                if (ke.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    searchBar.setText("Search...");
                }
                key = searchBar.getText().replace("Search...", "");
                updateList();
            }
        });
        searchBar.addFocusListener(new FocusListener() {
            public void focusGained(FocusEvent fe) {
                searchBar.selectAll();
            }

            public void focusLost(FocusEvent fe) {
                if (!iskeyAvilable()) {
                    searchBar.setText("Search...");
                    key = "";
                }
            }
        });
        for (Category cat : Category.values()) {
            categoryOption.addItem(cat);
        }
        categoryOption.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent ie) {
                if (ie.getStateChange() == ItemEvent.SELECTED) {
                    updateList();
                }
            }
        });

        sortOption.addItem("Relevance");
        sortOption.addItem("Price: Low to High");
        sortOption.addItem("Price: High to Low");
        sortOption.addItem("Product Name: A - Z");
        sortOption.addItem("Product Name: Z - A");
        sortOption.addItem("Category: A - Z");
        sortOption.addItem("Category: Z - A");
        sortOption.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent ie) {
                if (ie.getStateChange() == ItemEvent.SELECTED) {
                    updateList();
                }
            }

        });

        back.setVisible(false);
        reset.setVisible(false);
        buy.setVisible(false);

        shopItems.setListData(list);
        shopItems.setCellRenderer(new ListItem());
        shopItems.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent lse) {
                int[] is = shopItems.getSelectedIndices();
                buy.setVisible(is.length != 0);
            }
        });

        Container c = new Container();
        c.setLayout(new BorderLayout(10, 10));
        c.add(next, BorderLayout.EAST);
        c.add(buy, BorderLayout.CENTER);

        navBar.setLayout(new BorderLayout(10, 10));
        navBar.add(back, BorderLayout.WEST);
        navBar.add(searchBar, BorderLayout.CENTER);
        navBar.add(c, BorderLayout.EAST);

        Container opts = new Container();
        opts.setLayout(new GridLayout(0, 1, 10, 10));
        opts.add(new JLabel("Sort: "));
        opts.add(sortOption);
        opts.add(new JLabel("Shop by Category:"));
        opts.add(categoryOption);
        opts.add(reset);

        Container sideOptions = new Container();
        sideOptions.setLayout(new BorderLayout(10, 10));
        sideOptions.add(opts, BorderLayout.NORTH);

        sideMenu.setLayout(new BorderLayout(10, 10));
        sideMenu.add(sideTitle, BorderLayout.NORTH);
        sideMenu.add(sideOptions, BorderLayout.CENTER);

        Container shop = new Container();
        shop.setLayout(new BorderLayout(10, 10));
        shop.add(navBar, BorderLayout.NORTH);
        shop.add(sideMenu, BorderLayout.WEST);
        shop.add(new JScrollPane(shopItems), BorderLayout.CENTER);

        setLayout(new CardLayout(10, 10));
        add(shop);

        setBackground(Color.decode("#eeeeee"));
        shop.setBackground(Color.decode("#eeeeee"));
        navBar.setBackground(Color.decode("#eeeeee"));
        sideMenu.setBackground(Color.decode("#eeeeee"));
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
            searchBar.setText("Search...");
            key = "";
            updateList();
            back.setVisible(false);
        } else if (o == reset) {
            categoryOption.setSelectedIndex(0);
            sortOption.setSelectedIndex(0);
            updateList();
        } else if (o == buy) {
            int[] is = shopItems.getSelectedIndices();

            if (is.length != 0) {
                ProductList pl = new ProductList();
                for (int i : is) {
                    pl.add(list.get(i));
                }

                cartItems.addAll(pl);
                db.removeAll(pl);
                list.removeAll(pl);
                shopItems.setListData(list);

                next.setText("Cart (" + cartItems.size() + ")");
            }
            buy.setVisible(false);
        }
    }

    private void filter() {
        list = new ProductList(db);
        int sI = sortOption.getSelectedIndex(), cI = categoryOption.getSelectedIndex();
        reset.setVisible(sI != 0 || cI != 0);

        if (!reset.isVisible()) {
            return;
        }

        if (cI != 0) {
            for (Category c : Category.values()) {
                if (cI == c.ordinal()) {
                    list.filter(c);
                    break;
                }
            }
        }

        switch (sI) {
            case 1:
            case 2:
                list.sortByPrice(sI == 2);
                break;
            case 3:
            case 4:
                list.sortByName(sI == 4);
                break;
            case 5:
            case 6:
                list.sortByCategory(sI == 6);
                break;
        }
    }

    private void updateList() {
        filter();
        if (!iskeyAvilable()) {
            back.setVisible(false);
        } else {
            list = list.search(key);
            back.setVisible(true);
        }

        shopItems.setListData(list);
    }

    private boolean iskeyAvilable() {
        return !key.isEmpty();
    }

    private boolean isOnHome() {
        return next.getText().contains("Check out") && searchBar.getText().equals("Search...");
    }

    private boolean isOnCart() {
        return next.getText().contains("Cart");
    }
}
