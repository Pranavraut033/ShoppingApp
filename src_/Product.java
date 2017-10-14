package product;

import product.ProductList.Category;

public class Product {

    public String name, description;
    public double price;
    public Category category;

    public Product(String a, String b, double c, Category d) {
        name = a;
        description = b;
        price = c;
        category = d;
    }

    public String toString() {
        return "\nName: " + name + "\nDescription: " + description + "\nPrice: " + price + "\nCategory: " + category + "\n";
    }
}
