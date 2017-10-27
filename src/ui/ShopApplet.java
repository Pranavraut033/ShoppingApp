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

    String key = "";
    boolean empty;
    ProductList db = ProductList.getDatabase();
    ProductList shopItems = new ProductList(db);
    ProductList cartItems = new ProductList();

    Container shop = new Container();

    Container navBar = new Container();
    JButton back = new JButton("Back");
    JTextField searchBar = new JTextField("Search...");
    JButton buy = new JButton("Add to cart");
    JButton next = new JButton("Cart (" + cartItems.size() + ")");

    Container sideMenu = new Container();
    JLabel sideTitle = new JLabel("Filters");
    JComboBox<String> sortOption = new JComboBox<>();
    JComboBox<Category> categoryOption = new JComboBox<>();
    JButton reset = new JButton("Reset");

    JList<Product> shopItemList = new JList<>();
    JScrollPane pane = new JScrollPane(shopItemList);
    JLabel emptyMSG = new JLabel();

    public void init() {
        setSize(900, 506);

        sideTitle.setForeground(Color.decode("#FFFFFF"));
        sideTitle.setFont(new Font(null, Font.BOLD, 13));

        pane.setOpaque(false);
        pane.getViewport().setOpaque(false);
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

                if (shopItems.isEmpty() && !empty) {
                    if (key.length() == 1) {
                        emptyMSG.setText("Write a longer word...");
                    } else {
                        emptyMSG.setText("Nothing Found for \"" + key + "\"");
                    }
                }
            }
        });
        searchBar.addFocusListener(new FocusListener() {
            public void focusGained(FocusEvent fe) {
                searchBar.selectAll();
            }

            public void focusLost(FocusEvent fe) {
                if (!iskeyAvilable()) {
                    searchBar.setText("Search...");
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
                    if (shopItems.isEmpty() && !empty) {
                        emptyMSG.setText("Nothing Found... Try changing filter selection");
                    }
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
                    if (shopItems.isEmpty() && !empty) {
                        emptyMSG.setText("Nothing Found... Try changing filter selection");
                    }
                }
            }

        });

        back.setVisible(false);
        reset.setVisible(false);

        buy.setVisible(false);

        shopItemList.setListData(shopItems);
        shopItemList.setCellRenderer(new ListItem());
        shopItemList.setOpaque(false);
        shopItemList.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent lse) {
                int[] is = shopItemList.getSelectedIndices();
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

        JLabel l1 = new JLabel("Sort: ");
        l1.setForeground(Color.decode("#BDBDBD"));
        JLabel l2 = new JLabel("Shop by Category:");
        l2.setForeground(Color.decode("#BDBDBD"));

        Container opts = new Container();
        opts.setLayout(new GridLayout(0, 1, 10, 10));
        opts.add(l1);
        opts.add(sortOption);
        opts.add(l2);
        opts.add(categoryOption);
        opts.add(reset);

        Container sideOptions = new Container();
        sideOptions.setLayout(new BorderLayout(10, 10));
        sideOptions.add(opts, BorderLayout.NORTH);

        sideMenu.setLayout(new BorderLayout(10, 10));
        sideMenu.add(sideTitle, BorderLayout.NORTH);
        sideMenu.add(sideOptions, BorderLayout.CENTER);

        emptyMSG.setAlignmentX(CENTER_ALIGNMENT);
        emptyMSG.setAlignmentY(CENTER_ALIGNMENT);
        emptyMSG.setFont(new Font(null, Font.BOLD, 20));
        emptyMSG.setForeground(Color.WHITE);

        shop.setLayout(new BorderLayout(10, 10));
        shop.add(navBar, BorderLayout.NORTH);
        shop.add(sideMenu, BorderLayout.WEST);
        shop.add(pane, BorderLayout.CENTER);
        setLayout(new CardLayout(10, 10));
        add(shop);
    }

    public void paint(Graphics graphics) {
        Image image = getImage(getCodeBase(), "background.jpg");
        graphics.drawImage(image, 0, 0, (int) getBounds().getWidth(), (int) getBounds().getHeight(), this);
        super.paint(graphics);
    }

    public void actionPerformed(ActionEvent e) {
        Object o = e.getSource();
        if (o == next) {
            if (isOnHome()) {
                next.setText("Check out");
                back.setVisible(true);
                shopItemList.setListData(cartItems);
            } else {
            }
        } else if (o == back) {
            searchBar.setText("Search...");
            key = "";
            updateList();
            back.setVisible(false);
            if (isOnHome()) {
                next.setText("Cart (" + cartItems.size() + ")");
            }
        } else if (o == reset) {
            categoryOption.setSelectedIndex(0);
            sortOption.setSelectedIndex(0);
            updateList();
        } else if (o == buy) {
            int[] is = shopItemList.getSelectedIndices();

            if (is.length != 0) {
                ProductList pl = new ProductList();
                for (int i : is) {
                    pl.add(shopItems.get(i));
                }

                cartItems.addAll(pl);
                db.removeAll(pl);
                shopItems.removeAll(pl);
                shopItemList.setListData(shopItems);
                next.setText("Cart (" + cartItems.size() + ")");
            }
            buy.setVisible(false);
        }

        if (isOnHome()) {
            if (shopItems.isEmpty()) {
                emptyMSG.setText("Shop Empty...  Come back later");
                shop.remove(pane);
                shop.add(emptyMSG, BorderLayout.CENTER);
                repaint();
                empty = true;
            } else {
                shop.remove(emptyMSG);
                shop.add(pane, BorderLayout.CENTER);
                repaint();
            }
        }
    }

    private void filter() {
        shopItems = new ProductList(db);
        int sI = sortOption.getSelectedIndex(), cI = categoryOption.getSelectedIndex();
        reset.setVisible(sI != 0 || cI != 0);

        if (!reset.isVisible()) {
            return;
        }

        if (cI != 0) {
            for (Category c : Category.values()) {
                if (cI == c.ordinal()) {
                    shopItems.filter(c);
                    break;
                }
            }
        }

        switch (sI) {
            case 1:
            case 2:
                shopItems.sortByPrice(sI == 2);
                break;
            case 3:
            case 4:
                shopItems.sortByName(sI == 4);
                break;
            case 5:
            case 6:
                shopItems.sortByCategory(sI == 6);
                break;
        }

    }

    private void updateList() {
        filter();
        if (!iskeyAvilable()) {
            back.setVisible(false);
        } else {
            shopItems = shopItems.search(key);
            back.setVisible(true);
        }

        shopItemList.setListData(shopItems);

        if (shopItems.isEmpty()) {
            shop.remove(pane);
            shop.add(emptyMSG, BorderLayout.CENTER);
            repaint();
        } else {
            shop.remove(emptyMSG);
            shop.add(pane, BorderLayout.CENTER);
            repaint();
        }
    }

    private boolean iskeyAvilable() {
        return !key.isEmpty();
    }

    private boolean isOnCart() {
        return next.getText().contains("Check out") && searchBar.getText().equals("Search...");
    }

    private boolean isOnHome() {
        return next.getText().contains("Cart");
    }
}
