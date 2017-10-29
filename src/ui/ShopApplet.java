/*
    <applet code="ui\ShopApplet.class" height="506" width="900">
    </applet>
 */
package ui;

import java.applet.Applet;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Vector;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SpringLayout;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import product.Product;
import product.ProductList;
import product.ProductList.Category;

@SuppressWarnings({"override", "Convert2Lambda"})
public class ShopApplet extends Applet implements ActionListener {

    final int HOME = 1;
    final int SEARCH = 2;
    final int CART = 3;
    final int DELIVERY_OPTIONS = 4;
    final int PAYMENT = 5;
    final String CVV = "453";
    final String FILTER_MSG = "Nothing Found... Try changing filter selection";
    final String CART_MSG = "Cart Empty :( Start Shopping...";
    final String HOME_MSG = "Done for today :)";

    int page = HOME;
    double total = 0d;
    int extra = 0;
    String key = "";
    ProductList db = ProductList.getDatabase();
    ProductList shopItems = new ProductList(db);
    ProductList cartItems = new ProductList();
    ProductList purchaseItems = new ProductList();
    ProductList featuredItems = new ProductList();
    Vector<Integer> selectedCat = new Vector<>();

    ImageIcon backIcon;
    ImageIcon cartIcon;
    ImageIcon addIcon;
    ImageIcon removeIcon;
    ImageIcon checkOutIcon;
    ImageIcon payIcon;

    Container shop = new Container();

    Container navBar = new Container();
    JButton back;
    JTextField searchBar = new JTextField("Search...");
    JLabel title = new JLabel();
    JButton action;
    JButton next;

    Container content = new Container();
    Container sideMenu = new Container();
    JLabel sideTitle = new JLabel("Filters");
    JComboBox<String> sortOption = new JComboBox<>();
    JCheckBox[] catOptions = new JCheckBox[7];
    JButton reset = new JButton("Reset");

    Container mainContainer = new Container();
    Container featured = new Container();
    JLabel featuredLabel = new JLabel("Featured Items");
    JList<Product> featuredItemsList = new JList();
    JScrollPane featuredPanel = new JScrollPane(featuredItemsList);
    JLabel appLabel = new JLabel("Shop Items");
    JList<Product> itemList = new JList<>();
    JScrollPane panel = new JScrollPane(itemList);
    JLabel emptyMSG = new JLabel();

    Container deliveryContainer = new Container();
    JRadioButton standardButton = new JRadioButton("Standard Delivery-Expect package to be delivered in 7 working days");
    JRadioButton expressButton = new JRadioButton("Express Delivery-Expect package to be delivered tomorrow: +Rs 250");

    Container paymentConatiner = new Container();
    JLabel ccInfo = new JLabel("Credit Card Information");
    JLabel sccNumber = new JLabel("Saved CC Number:");
    JLabel ccNumber = new JLabel("XXXX-XXXX-XXXX-6540");
    JLabel enterCC = new JLabel("Enter CVV:");
    JPasswordField cvvField = new JPasswordField(3);

    Container bottomBar = new Container();
    JButton bNext;
    JLabel summary = new JLabel();

    public void init() {
        setSize(900, 506);

        backIcon = new ImageIcon(getImage(getCodeBase(), "back.png"));
        cartIcon = new ImageIcon(getImage(getCodeBase(), "cart.png"));
        addIcon = new ImageIcon(getImage(getCodeBase(), "add.png"));
        removeIcon = new ImageIcon(getImage(getCodeBase(), "remove.png"));
        checkOutIcon = new ImageIcon(getImage(getCodeBase(), "check_out.png"));
        payIcon = new ImageIcon(getImage(getCodeBase(), "pay.png"));

        back = new JButton(backIcon);
        next = new JButton("Cart (0)", cartIcon);
        action = new JButton(addIcon);
        bNext = new JButton(payIcon);

        back.setToolTipText("Back");
        sideTitle.setFont(new Font(null, Font.BOLD, 13));
        sideTitle.setForeground(Color.WHITE);
        featuredLabel.setFont(new Font(null, Font.BOLD, 18));
        featuredLabel.setForeground(Color.WHITE);
        appLabel.setFont(new Font(null, Font.BOLD, 18));
        appLabel.setForeground(Color.WHITE);

        cvvField.addKeyListener(new KeyListener() {
            public void keyTyped(KeyEvent ke) {
                if ((int) ke.getKeyChar() == KeyEvent.VK_ENTER) {
                    actionPerformed(new ActionEvent(bNext, 0, null));
                }
                if (!Character.isDigit(ke.getKeyChar()) || cvvField.getText().length() == 3) {
                    ke.consume();
                }
            }

            public void keyPressed(KeyEvent ke) {
            }

            public void keyReleased(KeyEvent ke) {
            }
        });

        for (JLabel l : new JLabel[]{title, summary}) {
            l.setFont(new Font(null, Font.BOLD, 24));
            l.setForeground(Color.WHITE);
        }

        for (JButton b : new JButton[]{next, back, reset, action, bNext}) {
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
                if (page == CART) {
                    initHome();
                }
                page = SEARCH;
                key = searchBar.getText();
                sideMenu.setVisible(true);
                if (ke.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    initHome();
                }
                updateList();

                if (shopItems.isEmpty()) {
                    if (key.length() < 3) {
                        emptyMSG.setText("Nothing Found... Try Typing a longer word...");
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
                    initHome();
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
                    if (shopItems.isEmpty()) {
                        emptyMSG.setText("Nothing Found... Try changing filter selection");
                    }
                }
            }

        });
        for (Container c : new Container[]{back, reset, action, bNext, featuredPanel, featuredLabel}) {
            c.setVisible(false);
        }
        panel.setOpaque(false);
        panel.getViewport().setOpaque(false);

        itemList.setListData(shopItems);
        itemList.setCellRenderer(new ListItem());
        itemList.setOpaque(false);
        itemList.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent lse) {
                int[] is = itemList.getSelectedIndices();
                action.setVisible(is.length != 0);
            }
        });

        Container c = new Container();
        c.setLayout(new BorderLayout(7, 7));
        c.add(next, BorderLayout.EAST);
        c.add(action, BorderLayout.CENTER);

        navBar.setLayout(new BorderLayout(7, 7));
        navBar.add(back, BorderLayout.WEST);
        navBar.add(searchBar, BorderLayout.CENTER);
        navBar.add(c, BorderLayout.EAST);

        bottomBar.setLayout(new BorderLayout(7, 7));
        content.setLayout(new BorderLayout(7, 7));

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
            catOptions[i] = new JCheckBox(Category.values()[i].name());
            catOptions[i].addItemListener(new ItemListener() {
                public void itemStateChanged(ItemEvent ie) {
                    updateList();
                    if (shopItems.isEmpty()) {
                        emptyMSG.setText(FILTER_MSG);
                    }
                }
            });
            catOptions[i].setOpaque(false);
            catOptions[i].setForeground(Color.WHITE);
            opts.add(catOptions[i]);
        }
        opts.add(reset);

        Container sideOptions = new Container();
        sideOptions.setLayout(new BorderLayout(7, 7));
        sideOptions.add(opts, BorderLayout.NORTH);

        sideMenu.setLayout(new BorderLayout(7, 7));
        sideMenu.add(sideTitle, BorderLayout.NORTH);
        sideMenu.add(sideOptions, BorderLayout.CENTER);
        sideMenu.setVisible(false);

        emptyMSG.setFont(new Font(null, Font.BOLD, 20));
        emptyMSG.setForeground(Color.WHITE);
        emptyMSG.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        featuredPanel.setOpaque(false);
        featuredPanel.getViewport().setOpaque(false);

        featured.setLayout(new BorderLayout(7, 7));
        featured.add(featuredLabel, BorderLayout.NORTH);
        featured.add(featuredPanel, BorderLayout.CENTER);
        featured.add(appLabel, BorderLayout.SOUTH);

        featuredItemsList.setVisibleRowCount(1);
        featuredItemsList.setCellRenderer(new ListItem());
        featuredItemsList.setOpaque(false);
        featuredItemsList.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent lse) {
                int[] is = featuredItemsList.getSelectedIndices();
                action.setVisible(is.length != 0);
            }
        });
        mainContainer.setLayout(new BorderLayout(7, 7));
        mainContainer.add(featured, BorderLayout.NORTH);
        mainContainer.add(panel, BorderLayout.CENTER);

        content.add(sideMenu, BorderLayout.WEST);
        content.add(mainContainer, BorderLayout.CENTER);

        standardButton.setOpaque(false);
        standardButton.setForeground(Color.WHITE);
        standardButton.setFont(new Font(null, Font.BOLD, 14));
        standardButton.setSelected(true);

        expressButton.setOpaque(false);
        expressButton.setForeground(Color.WHITE);
        expressButton.setFont(new Font(null, Font.BOLD, 14));

        standardButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                extra = 0;
                summary.setText("Total: " + total);
                expressButton.setSelected(false);
            }
        });
        expressButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                extra = 250;
                summary.setText("Total: " + total + " +" + extra);
                standardButton.setSelected(false);
            }
        });

        SpringLayout layout1 = new SpringLayout();
        layout1.putConstraint(SpringLayout.HORIZONTAL_CENTER, standardButton,
                0, SpringLayout.HORIZONTAL_CENTER, deliveryContainer);
        layout1.putConstraint(SpringLayout.VERTICAL_CENTER, standardButton,
                -30, SpringLayout.VERTICAL_CENTER, deliveryContainer);
        layout1.putConstraint(SpringLayout.HORIZONTAL_CENTER, expressButton,
                0, SpringLayout.HORIZONTAL_CENTER, deliveryContainer);
        layout1.putConstraint(SpringLayout.VERTICAL_CENTER, expressButton,
                30, SpringLayout.VERTICAL_CENTER, deliveryContainer);
        deliveryContainer.setLayout(layout1);
        deliveryContainer.add(standardButton);
        deliveryContainer.add(expressButton);

        SpringLayout layout2 = new SpringLayout();

        JPanel panel2 = new JPanel();
        panel2.setPreferredSize(new Dimension(440, 150));
        panel2.setBorder(BorderFactory.createLineBorder(Color.WHITE, 3));
        panel2.setOpaque(false);

        ccInfo.setForeground(Color.WHITE);
        ccInfo.setPreferredSize(new Dimension(200, 40));
        sccNumber.setForeground(Color.WHITE);
        ccNumber.setForeground(Color.WHITE);
        ccNumber.setPreferredSize(new Dimension(200, 40));
        enterCC.setForeground(Color.WHITE);

        layout2.putConstraint(SpringLayout.HORIZONTAL_CENTER, panel2,
                0, SpringLayout.HORIZONTAL_CENTER, paymentConatiner);
        layout2.putConstraint(SpringLayout.VERTICAL_CENTER, panel2,
                0, SpringLayout.VERTICAL_CENTER, paymentConatiner);
        layout2.putConstraint(SpringLayout.HORIZONTAL_CENTER, ccInfo,
                30, SpringLayout.HORIZONTAL_CENTER, paymentConatiner);
        layout2.putConstraint(SpringLayout.VERTICAL_CENTER, ccInfo,
                -50, SpringLayout.VERTICAL_CENTER, paymentConatiner);
        layout2.putConstraint(SpringLayout.HORIZONTAL_CENTER, sccNumber,
                -100, SpringLayout.HORIZONTAL_CENTER, paymentConatiner);
        layout2.putConstraint(SpringLayout.VERTICAL_CENTER, sccNumber,
                -20, SpringLayout.VERTICAL_CENTER, paymentConatiner);
        layout2.putConstraint(SpringLayout.VERTICAL_CENTER, enterCC,
                20, SpringLayout.VERTICAL_CENTER, paymentConatiner);
        layout2.putConstraint(SpringLayout.HORIZONTAL_CENTER, ccNumber,
                100, SpringLayout.HORIZONTAL_CENTER, paymentConatiner);
        layout2.putConstraint(SpringLayout.VERTICAL_CENTER, ccNumber,
                -20, SpringLayout.VERTICAL_CENTER, paymentConatiner);
        layout2.putConstraint(SpringLayout.HORIZONTAL_CENTER, enterCC,
                -100, SpringLayout.HORIZONTAL_CENTER, paymentConatiner);
        layout2.putConstraint(SpringLayout.HORIZONTAL_CENTER, cvvField,
                20, SpringLayout.HORIZONTAL_CENTER, paymentConatiner);
        layout2.putConstraint(SpringLayout.VERTICAL_CENTER, cvvField,
                20, SpringLayout.VERTICAL_CENTER, paymentConatiner);
        paymentConatiner.setLayout(layout2);
        for (Container l : new Container[]{ccInfo, cvvField, ccNumber, sccNumber, enterCC, panel2}) {
            paymentConatiner.add(l);
        }

        Container c1 = new Container();
        c1.setLayout(new BorderLayout(7, 7));
        c1.add(summary, BorderLayout.EAST);
        bottomBar.add(c1, BorderLayout.CENTER);
        bottomBar.add(bNext, BorderLayout.EAST);
        bottomBar.setVisible(false);

        shop.setLayout(new BorderLayout(7, 7));
        shop.add(navBar, BorderLayout.NORTH);
        shop.add(content, BorderLayout.CENTER);
        shop.add(bottomBar, BorderLayout.SOUTH);

        setLayout(new CardLayout(7, 7));
        add(shop);
    }

    public void paint(Graphics graphics) {
        Image image = getImage(getCodeBase(), "background.jpg");
        graphics.drawImage(image, 0, 0, (int) getBounds().getWidth(), (int) getBounds().getHeight(), this);
        super.paint(graphics);
    }

    public void actionPerformed(ActionEvent e) {
        Object o = e.getSource();
        if (o == next || o == bNext) {
            switch (page) {
                case HOME:
                    initCart();
                    break;
                case SEARCH:
                    initHome();
                    initCart();
                    break;
                case CART:
                    initDelivery();
                    break;
                case DELIVERY_OPTIONS:
                    initPayment();
                    break;
                case PAYMENT:
                    JOptionPane optionPane;
                    if (cvvField.getText().equals(CVV)) {
                        optionPane = new JOptionPane("Thank you for purching", JOptionPane.PLAIN_MESSAGE,
                                JOptionPane.CLOSED_OPTION);
                        db.removeAll(cartItems);
                        purchaseItems.addAll(cartItems);
                        featuredItems.addAll(cartItems);
                        cartItems.clear();
                        cvvField.setText("");
                        initHome();
                    } else {
                        optionPane = new JOptionPane("Invalid CVV", JOptionPane.WARNING_MESSAGE,
                                JOptionPane.DEFAULT_OPTION);
                    }
                    final JDialog dialog = new JDialog((Frame) null, "Message", true);
                    optionPane.addPropertyChangeListener(new PropertyChangeListener() {
                        public void propertyChange(PropertyChangeEvent e) {
                            if (dialog.isVisible() && e.getPropertyName().equals(JOptionPane.VALUE_PROPERTY)) {
                                dialog.setVisible(false);
                            }
                        }
                    });
                    dialog.setContentPane(optionPane);
                    dialog.setLocationRelativeTo(null);
                    dialog.pack();
                    dialog.setVisible(true);

                    break;
            }
        } else if (o == back) {
            switch (page) {
                case CART:
                case SEARCH:
                    initHome();
                    break;
                case PAYMENT:
                    initDelivery();
                    break;
                case DELIVERY_OPTIONS:
                    initCart();
                    break;

            }
        } else if (o == reset) {
            resetFilter();
            updateList();
        } else if (o.equals(action)) {
            int[] is = itemList.getSelectedIndices(), i1s = featuredItemsList.getSelectedIndices();
            if (is.length != 0 || i1s.length != 0) {
                ProductList pl = new ProductList();
                switch (page) {
                    case SEARCH:
                    case HOME:
                        if (is.length != 0) {
                            for (int i : is) {
                                pl.add(shopItems.get(i));
                            }
                        } else {
                            for (int i : i1s) {
                                pl.add(featuredItems.get(i));
                            }
                        }
                        cartItems.addAll(pl);
                        db.removeAll(pl);
                        featuredItems.removeAll(pl);
                        shopItems.removeAll(pl);
                        next.setText("Cart (" + cartItems.size() + ")");
                        itemList.setListData(shopItems);
                        if (page == HOME) {
                            if (shopItems.isEmpty()) {
                                emptyMSG.setText(HOME_MSG);
                                shop.remove(mainContainer);
                                shop.add(emptyMSG, BorderLayout.CENTER);
                            } else {
                                shop.add(mainContainer, BorderLayout.CENTER);
                                shop.remove(emptyMSG);
                            }
                        }
                        break;
                    case CART:
                        for (int i : is) {
                            pl.add(cartItems.get(i));
                        }
                        cartItems.removeAll(pl);
                        db.addAll(pl);
                        shopItems.addAll(pl);
                        next.setText("Check out (" + cartItems.size() + ")");
                        itemList.setListData(cartItems);
                        if (cartItems.isEmpty()) {
                            emptyMSG.setText(CART_MSG);
                            next.setVisible(false);
                            shop.remove(mainContainer);
                            shop.add(emptyMSG, BorderLayout.CENTER);
                            summary.setVisible(false);
                        } else {
                            total = 0;
                            for (Product p : cartItems) {
                                total += p.price;
                            }
                            next.setVisible(true);
                            summary.setText("Total: " + total);
                            summary.setVisible(true);
                            shop.add(mainContainer, BorderLayout.CENTER);
                            shop.remove(emptyMSG);
                        }
                        break;
                }
                repaint();
                action.setVisible(false);
            }
        }
    }

    private void resetFilter() {
        for (JCheckBox box : catOptions) {
            box.setSelected(false);
        }
        sortOption.setSelectedIndex(0);
    }

    private void filter() {
        shopItems = new ProductList(db);
        int sI = sortOption.getSelectedIndex();
        selectedCat.clear();
        for (int i = 0; i < catOptions.length; i++) {
            if (catOptions[i].isSelected()) {
                selectedCat.add(i);
            }
        }
        reset.setVisible(sI != 0 || !selectedCat.isEmpty());

        if (!reset.isVisible()) {
            return;
        }

        if (!selectedCat.isEmpty()) {
            shopItems.clear();
            for (int i : selectedCat) {
                for (Category c : Category.values()) {
                    if (i == c.ordinal()) {
                        ProductList temp = new ProductList(db);
                        shopItems.addAll(temp.filter(c));
                        break;
                    }
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
            page = SEARCH;
            shopItems = shopItems.search(key);
            back.setVisible(true);
        }

        itemList.setListData(shopItems);
        if (shopItems.isEmpty()) {
            content.remove(mainContainer);
            content.add(emptyMSG, BorderLayout.CENTER);
        } else {
            content.remove(emptyMSG);
            content.add(mainContainer, BorderLayout.CENTER);
        }
        repaint();
    }

    private boolean iskeyAvilable() {
        return !key.isEmpty();
    }

    private void initHome() {
        next.setIcon(cartIcon);
        next.setText("Cart (" + cartItems.size() + ")");
        action.setIcon(addIcon);
        action.setToolTipText("Add to Cart");

        switch (page) {
            case CART:
                itemList.setListData(shopItems);
                if (cartItems.isEmpty()) {
                    next.setVisible(true);
                    shop.remove(emptyMSG);
                    shop.add(mainContainer, BorderLayout.CENTER);
                }
                if (shopItems.isEmpty()) {
                    emptyMSG.setText(HOME_MSG);
                    shop.remove(mainContainer);
                    shop.add(emptyMSG, BorderLayout.CENTER);
                }
                bottomBar.setVisible(false);
                break;
            case SEARCH:
                searchBar.setText("Search...");
                key = "";
                sideMenu.setVisible(false);
                resetFilter();
                updateList();
                break;
            case PAYMENT:
                itemList.setListData(shopItems);
                for (Container c : new Container[]{next, mainContainer}) {
                    c.setVisible(true);
                }
                shop.remove(paymentConatiner);
                navBar.remove(title);
                navBar.add(searchBar, BorderLayout.CENTER);
                bottomBar.setVisible(false);
                updateList();
                if (!featuredItems.isEmpty()) {
                    featuredLabel.setVisible(true);
                    featuredPanel.setVisible(true);
                    featuredItemsList.setListData(featuredItems);
                }
                break;
        }
        back.setVisible(false);
        page = HOME;
        repaint();
    }

    private void initCart() {
        switch (page) {
            case HOME:
                action.setToolTipText("Remove from Cart");
                back.setVisible(true);
                next.setText("Check out (" + cartItems.size() + ")");
                next.setIcon(checkOutIcon);
                itemList.setListData(cartItems);
                action.setIcon(removeIcon);

                if (cartItems.isEmpty()) {
                    next.setVisible(false);
                    emptyMSG.setText(CART_MSG);
                    shop.remove(mainContainer);
                    shop.add(emptyMSG, BorderLayout.CENTER);
                } else {
                    total = 0;
                    for (Product p : cartItems) {
                        total += p.price;
                    }
                    bottomBar.setVisible(true);
                    next.setVisible(true);
                    summary.setVisible(true);
                    summary.setText("Total: " + total);
                    shop.add(mainContainer, BorderLayout.CENTER);
                    shop.remove(emptyMSG);
                }
                break;
            case DELIVERY_OPTIONS:
                if (itemList.getSelectedIndices().length > 0) {
                    action.setVisible(true);
                }
                bNext.setVisible(false);
                next.setVisible(true);
                mainContainer.setVisible(true);
                shop.remove(deliveryContainer);
                navBar.add(searchBar, BorderLayout.CENTER);
                navBar.remove(title);
        }
        page = CART;
        bNext.setVisible(false);

        repaint();
    }

    private void initDelivery() {
        switch (page) {
            case CART:
                for (Container c : new Container[]{action, next, mainContainer}) {
                    c.setVisible(false);
                }
                navBar.remove(searchBar);
                navBar.add(title, BorderLayout.CENTER);
                bNext.setVisible(true);
                break;
            case PAYMENT:
                shop.remove(paymentConatiner);
                break;

        }

        bNext.setIcon(payIcon);
        bNext.setText("Proceed To Payment");
        title.setText("Delivery Options");
        shop.add(deliveryContainer, BorderLayout.CENTER);

        page = DELIVERY_OPTIONS;
        repaint();
    }

    private void initPayment() {
        page = PAYMENT;
        summary.setText("Total: " + (total + extra));
        bNext.setText("Pay");
        title.setText("Payment");
        shop.remove(deliveryContainer);
        shop.add(paymentConatiner, BorderLayout.CENTER);

        repaint();
    }
}
