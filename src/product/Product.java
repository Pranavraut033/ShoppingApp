package product;

import product.ProductList.Category;

public class Product {

    public String name, description;
    public double price;
    public Category category;
    public String image;
    private boolean onfeatured;
    
    public Product(String a, String b, double c, Category d) {
        name = a;
        description = b;
        price = c;
        category = d;
    }

    public String getHtmlString(){
        return toString().replace("\n", "<br>").replace("%b", "<strong>").replace("%/b", "</strong>").replace("%i", "<i>").replace("%/p ","</i>");
    }
    public String toString() {
        return "%bName: " + name + (onfeatured ? "%/b" : "%/b\nDescription: " + description) + "\nCategory: " + category + "\n%i%bPrice: " + price + "%/b%i\n";
    }

    public void onfeatured(boolean onfeatured) {
        this.onfeatured = onfeatured;
    }
}
