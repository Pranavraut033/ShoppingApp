/*
    <applet code="ui\ShopApplet.class" height="450" width="800">
    </applet>
 */
package ui;

import java.util.*;
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
    Vector<Integer> selectedCat = new Vector<>();

    Container shop = new Container();

    Container navBar = new Container();
    JButton back;
    JTextField searchBar = new JTextField("Search...");
    JButton buy;
    JButton next;

    Container sideMenu = new Container();
    JLabel sideTitle = new JLabel("Filters");
    JComboBox<String> sortOption = new JComboBox<>();
    JCheckBox[] catOptions = new JCheckBox[7];

    //JComboBox<Category> categoryOption = new JComboBox<>();
    JButton reset = new JButton("Reset");

    JList<Product> shopItemList = new JList<>();
    JScrollPane pane = new JScrollPane(shopItemList);
    JLabel emptyMSG = new JLabel();
    private int page;

    public void init() {
        setSize(900, 506);

        back = new JButton(new ImageIcon(getImage(getCodeBase(), "back.png")));
        next = new JButton("Cart", new ImageIcon(getImage(getCodeBase(), "cart.png")));
        buy = new JButton(new ImageIcon(getImage(getCodeBase(), "buy.png")));

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
                sideMenu.setVisible(!key.isEmpty());
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
        opts.setLayout(new GridLayout(0, 1, 0, 5));
        opts.add(l1);
        opts.add(sortOption);
        opts.add(l2);
        for (int i = 0; i < catOptions.length; i++) {
            final int fi = i + 1;
            catOptions[i] = new JCheckBox(Category.values()[fi].name());
            catOptions[i].addItemListener(new ItemListener() {
                public void itemStateChanged(ItemEvent ie) {
                    if (ie.getStateChange() == ItemEvent.SELECTED) {
                        selectedCat.add(fi);
                    } else {
                        selectedCat.remove(fi);
                    }
                    updateList();
                    if (shopItems.isEmpty() && !empty) {
                        emptyMSG.setText("Nothing Found... Try changing filter selection");
                    }
                    if (selectedCat.isEmpty()) {
                        selectedCat.add(0);
                    }
                    System.out.println(selectedCat.toString());
                }
            });
            catOptions[i].setOpaque(false);
            catOptions[i].setForeground(Color.WHITE);
            opts.add(catOptions[i]);
        }
        opts.add(reset);

        Container sideOptions = new Container();
        sideOptions.setLayout(new BorderLayout(10, 10));
        sideOptions.add(opts, BorderLayout.NORTH);

        sideMenu.setLayout(new BorderLayout(10, 10));
        sideMenu.add(sideTitle, BorderLayout.NORTH);
        sideMenu.add(sideOptions, BorderLayout.CENTER);
        sideMenu.setVisible(false);

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
                next.setIcon(null);
                back.setVisible(true);
                shopItemList.setListData(cartItems);
            } else if (isOnCart()) {
                initDelivery();
            } else if (page == 3) {
                initPayment();
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
            // categoryOption.setSelectedIndex(0);
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
        int sI = sortOption.getSelectedIndex(), cI = 0;//categoryOption.getSelectedIndex();
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

    Container c1 = new Container();
    JLabel title = new JLabel("Delivery Options");

    private void initDelivery() {
        shop.removeAll();

        Container c2 = new Container();
        JRadioButton standardButton = new JRadioButton("Standard Delivery-Expect package to be delivered in 7 working days");
        JRadioButton expressButton = new JRadioButton("Express Delivery-Expect package to be delivered tomorrow: +Rs 250");
        //standardButton.setPreferredSize(new Dimension(425, 50));
        //expressButton.setPreferredSize(new Dimension(425, 50));
        standardButton.setOpaque(false);
        expressButton.setOpaque(false);
        standardButton.setForeground(Color.WHITE);
        expressButton.setForeground(Color.WHITE);
        Font f = new Font(null, Font.BOLD, 14);
        standardButton.setFont(f);
        expressButton.setFont(f);
        SpringLayout layout = new SpringLayout();
        c1.setLayout(layout);
        c2.setLayout(new BorderLayout(10, 10));
        layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, standardButton,
                0, SpringLayout.HORIZONTAL_CENTER, c1);
        layout.putConstraint(SpringLayout.VERTICAL_CENTER, standardButton,
                -30, SpringLayout.VERTICAL_CENTER, c1);
        layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, expressButton,
                0, SpringLayout.HORIZONTAL_CENTER, c1);
        layout.putConstraint(SpringLayout.VERTICAL_CENTER, expressButton,
                30, SpringLayout.VERTICAL_CENTER, c1);
        c1.add(standardButton);
        c1.add(expressButton);
        standardButton.setSelected(true);

        standardButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                expressButton.setSelected(false);
            }
        });
        expressButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                standardButton.setSelected(false);
            }
        });

        title.setFont(new Font(null, Font.BOLD, 20));
        title.setForeground(Color.WHITE);

        navBar.removeAll();
        navBar.add(back, BorderLayout.WEST);
        navBar.add(title, BorderLayout.CENTER);

        next.setIcon(new ImageIcon(getImage(getCodeBase(), "pay.png")));
        next.setText("Proceed To Payment");
        c2.add(next, BorderLayout.EAST);
        next.setVisible(true);
        title.setVisible(true);
        shop.add(navBar, BorderLayout.NORTH);
        shop.add(c1, BorderLayout.CENTER);
        shop.add(c2, BorderLayout.SOUTH);
        page = 3;
        repaint();
    }

    private void initPayment() {
        c1.removeAll();
        next.setText("Pay");
        title.setText("Payment");
        SpringLayout layout = new SpringLayout();
        c1.setLayout(layout);
        JPanel panel2 = new JPanel();
        panel2.setPreferredSize(new Dimension(440, 150));
        panel2.setOpaque(false);
        JLabel CCInfo = new JLabel("Credit Card Information");
        JLabel CCNumberText = new JLabel("Saved CC Number:");
        JLabel CCNumber = new JLabel("XXXX-XXXX-XXXX-6540");
        JLabel ENTERCVV = new JLabel("Enter CVV:");
        JPasswordField CVV = new JPasswordField(3);
        CCInfo.setForeground(Color.WHITE);
        CCNumber.setForeground(Color.WHITE);
        CCNumberText.setForeground(Color.WHITE);
        ENTERCVV.setForeground(Color.WHITE);
        layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, CCInfo,
                30, SpringLayout.HORIZONTAL_CENTER, c1);
        layout.putConstraint(SpringLayout.VERTICAL_CENTER, CCInfo,
                -50, SpringLayout.VERTICAL_CENTER, c1);
        layout.putConstraint(SpringLayout.VERTICAL_CENTER, ENTERCVV,
                20, SpringLayout.VERTICAL_CENTER, c1);
        layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, ENTERCVV,
                -100, SpringLayout.HORIZONTAL_CENTER, c1);
        layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, panel2,
                0, SpringLayout.HORIZONTAL_CENTER, c1);
        layout.putConstraint(SpringLayout.VERTICAL_CENTER, panel2,
                0, SpringLayout.VERTICAL_CENTER, c1);
        layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, CVV,
                20, SpringLayout.HORIZONTAL_CENTER, c1);
        layout.putConstraint(SpringLayout.VERTICAL_CENTER, CVV,
                20, SpringLayout.VERTICAL_CENTER, c1);
        layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, CCNumber,
                100, SpringLayout.HORIZONTAL_CENTER, c1);
        layout.putConstraint(SpringLayout.VERTICAL_CENTER, CCNumber,
                -20, SpringLayout.VERTICAL_CENTER, c1);
        layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, CCNumberText,
                -100, SpringLayout.HORIZONTAL_CENTER, c1);
        layout.putConstraint(SpringLayout.VERTICAL_CENTER, CCNumberText,
                -20, SpringLayout.VERTICAL_CENTER, c1);
        panel2.setBorder(BorderFactory.createLineBorder(Color.WHITE, 3));
        CCInfo.setPreferredSize(new Dimension(200, 40));
        CCNumber.setPreferredSize(new Dimension(200, 40));
        panel2.setPreferredSize(new Dimension(440, 150));
        c1.add(CVV);
        c1.add(CCNumber);
        c1.add(CCInfo);
        c1.add(CCNumberText);
        c1.add(ENTERCVV);
        c1.add(panel2);
        repaint();
    }
}
