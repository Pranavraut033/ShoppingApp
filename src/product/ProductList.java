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

public class ProductList extends Vector<Product> {
    
    
    public static ProductList Database()
    {
        ProductList all = new ProductList();
        all.add(new Product("Google Pixel 2","A android smartphone by google",61000,3));
        all.add(new Product("Schindler's List","Book about a greedy German businessman",1000,2));
        return all;
    }
 
    public void shuffle() 
    {
        Collections.shuffle(this);
    }
    
    public void swap(int a,int b)
    {
        Product temp = get(a);
        add(a, get(b));
        add(b, temp);
    }

    public int getLength() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}