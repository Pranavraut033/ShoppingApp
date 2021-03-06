package product;

import java.util.Collections;
import java.util.Vector;
import utilites.Utils;

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
        super(list);
    }

    public ProductList filter(Category category) {
        return Utils.filter(this, category);
    }

    public enum Category {
        Appliances, Books, Electronics, Fashion, Grocery, Medicines, Toys
    }

    public static ProductList getDatabase() {
        ProductList all = new ProductList();
        all.add(new Product("Google Pixel 2",
                "A android smartphone by google",
                61000, Category.Electronics));
        all.add(new Product("Schindler's List",
                "Book about a greedy German businessman",
                1000, Category.Books));
        all.add(new Product("Whirlpool Single Door Refrigerators",
                "A SmartFridge with 3 star BEE energy rating",
                10490, Category.Appliances));
        all.add(new Product("Voltas 1.4 Ton 3 Star Split AC",
                "A split ac with Sleep mode and Turbo mode ",
                27990, Category.Appliances));
        all.add(new Product("HUL Pureit Advanced RO+UV 5-Litre Water Purifier",
                "The Pureit purifier guarantees that you will get as safe as boiled water",
                8399, Category.Appliances));
        all.add(new Product("Saffire Kids Twist Scooter",
                "Children can get moving on the Saffire Twist Scooter and gain confidence with speed and balance",
                1299, Category.Toys));
        all.add(new Product("Gencliq Flying Remote Control Helicopter",
                "Suitable for children for the first time engage on RC helicopter",
                800, Category.Toys));
        all.add(new Product("Google Home",
                "A hands-free voice assistant which lets you play music, get sports scores, wheather & more",
                5999, Category.Electronics));
        all.add(new Product("Asus VivoBook Laptop ",
                "A powerful laptop with an Intel 7th Gen Processor, 8GB RAM, Nvidia GTX 1050 Ti & a stunning 1080p display",
                47999, Category.Electronics));
        all.add(new Product("Catcher in the Rye",
                "The Catcher in the Rye is a 1951 novel by J. D. Salinger describing a story of a man undegoing treatment in a mental hospital",
                499, Category.Books));
        all.add(new Product("FastTrack Men's Watch",
                "High quality leather buckle with a black quartz dial",
                2399, Category.Fashion));
        all.add(new Product("Cadbury Celebrations- Small Pack",
                "A sweet gift for a sweet ocassion",
                399, Category.Grocery));
        all.add(new Product("Miranda 600ml Pack of 50",
                "Soft-Drink with no artificial flavour",
                1199, Category.Grocery));
        all.add(new Product("Bandages-10x10cm",
                "First aid medicinal bandages",
                200, Category.Medicines));
        all.add(new Product("First Aid Travel Kit ",
                "A light & compact first aid kit which consists of essentials supplies like bandages,cotton buds, sterile guaze, alcohol wipes etc",
                999, Category.Medicines));
        all.add(new Product("LG OLED 4K TV",
                "A wifi enabled smart TV with 60 Hz refresh rate & an incredible sound system",
                89499, Category.Electronics));
        all.add(new Product("Sandisk 16GB Pendrive",
                "Portable & a high speed storage device",
                349, Category.Electronics));
        all.add(new Product("Sennheiser HD 180 Over-Ear Headphones (Black)",
                "Over the head earphones with superior sound quality",
                990, Category.Electronics));
        all.add(new Product("HP DeskJet Ink Advantage 2135 All-in-One Printer",
                "Copy, scan & print through a single compact device",
                4790, Category.Electronics));
        all.add(new Product("iPad Mini 2 Tablet",
                "The smallest & the fastest tablet with a powerful A8X chip & touch-ID",
                34990, Category.Electronics));
        all.add(new Product("Core Python Programming",
                "The complete reference for Python",
                599, Category.Books));
        all.add(new Product("Da-Vinci Code",
                "A book about a mysterious & a bizzare murder",
                599, Category.Books));
        all.add(new Product("The Power Of Sub-Conscious Mind",
                "This book provides a complete guide for controlling the superior mind",
                399, Category.Books));
        all.add(new Product("To Kill a Mockingbird",
                "An all-time best seller about growing up in extraordinary circumstances",
                699, Category.Books));
        all.add(new Product("The Martian",
                "A fiction based novel about an astronaut",
                449, Category.Books));
        all.add(new Product("Philips Coffee Maker",
                "A budget coffee brewer",
                849, Category.Appliances));
        all.add(new Product("Godrej Fully Automatic Washing Machine",
                "Top load easy to use washing machine",
                14449, Category.Appliances));
        all.add(new Product("Bajaj 1000 Watt Iron",
                "A plug & play appliance",
                1449, Category.Appliances));
        all.add(new Product("LG Microwave",
                "A microwave with 30 liters of capacity & upto 20 auto cooking options",
                6449, Category.Appliances));
        all.add(new Product("Solimo Permium Almonds 500gm",
                "High Quality Dry Fruits",
                449, Category.Appliances));
        all.shuffle();
        return all;
    }

    public void sortByName(boolean dec) {
        Utils.sort(this, Utils.NAME_MODE, dec);
    }

    public void sortByPrice(boolean dec) {
        Utils.sort(this, Utils.PRICE_MODE, dec);
    }

    public void sortByCategory(boolean dec) {
        Utils.sort(this, Utils.CAT_MODE, dec);
    }

    public ProductList getSimilar(ProductList list, int length) {
        return Utils.getSimilarProducts(this, list, length);
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
