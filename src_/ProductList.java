package product;

import java.util.*;
import utilites.*;

/*Category: 
    Appliances = 1
    Books = 2
    Electronics = 3
    Fashion = 4 
    Grocery = 5
    Medicines = 6
    Toys = 7
 */
public class ProductList extends Vector<Product> {

    public ProductList() {
        super();
    }

    public ProductList(ProductList list) {
        super();
        addAll(list);
    }

    public ProductList filter(Category category) {
        return Utils.filter(this, category);
    }

    //TODO: remove enum
    public enum Category {
        All, APPLIANCES, BOOKS, ELECTRONICS, GROCERY, MEDICINES, TOYS
    }

    public static ProductList Database() {
        ProductList all = new ProductList();
        all.add(new Product("Google Pixel 2",
                "A android smartphone by google", 61000, Category.APPLIANCES));
        all.add(new Product("Schindler's List",
                "Book about a greedy German businessman", 1000, Category.BOOKS));
        all.add(new Product("Whirlpool Single Door Refrigerators",
                "A SmartFridge with 3 star BEE energy rating", 10490, Category.APPLIANCES));
        all.add(new Product("Voltas 1.4 Ton 3 Star Split AC",
                "A split ac with Sleep mode and Turbo mode ", 27990, Category.APPLIANCES));
        all.add(new Product("HUL Pureit Advanced RO+UV 5-Litre Water Purifier",
                "The Pureit purifier guarantees that you will get as safe as boiled water",
                8399, Category.APPLIANCES));
        all.add(new Product("Saffire Kids Twist Scooter",
                "Children can get moving on the Saffire Twist Scooter and gain confidence with speed and balance",
                1299, Category.TOYS));
        all.add(new Product("Gencliq Flying Remote Control Helicopter",
                "Suitable for children for the first time engage on RC helicopter",
                1299, Category.TOYS));
        all.shuffle();
        return all;
    }

    public void sortByName(boolean dec) {
        Utils.sort(this, Utils.NAME_MODE, dec);
    }

    public void sortByPrice(boolean dec) {
        Utils.sort(this, Utils.PRICE_MODE, dec);
    }

    public void sortByCat(boolean dec) {
        Utils.sort(this, Utils.CAT_MODE, dec);
    }

    public ProductList search(String s) {
        return Utils.search(this, s);
    }

    public void shuffle() {
        Collections.shuffle(this);
    }

    public void swap(int a, int b) {
        Product pa = get(a), pb = get(b);
        remove(pa);
        remove(pb);
        add(a, pb);
        add(b, pa);
    }
}
