/*Category 
           Appliances=1
           Books =2
           Electronics 3
           Fashion=4 
           Grocery =5
           Medicines=6
           Toys=7
*/
package product;
import java.util.*;
import java.util.Vector;
class ProductList {
	Vector<Product> products = new Vector<>();
	public static ProductList Database()
	{
	ProductList all = new ProductList();
	all.add(new Product("Google Pixel 2","A android smartphone by google",61000,3));
	all.add(new Product("Schindler's List","Book about a greedy German businessman",1000,2));
    }
    public void add(Product p) 
    {
    	products.add(p);
    }
    public staticProductList Shuffle() 
    {
     Collections.shuffle(products);
    }
    public Product get(int index)
    {
           products.elementAt(index);
    } 
    public Product swap(int a,int b)
    {

    }
    
}