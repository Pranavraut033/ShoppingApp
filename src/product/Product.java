package product;

public class Product {
    String name, description;
    double price;
    int category;
    
    public Product(String a,String b,double c,int d){
        name=a;
        description=b;
        price=c;
        category=d;
    }
    
    public String getName()
    {
    	return name;

    }
    
    public double getPrice()
    {
    	return price;
    }
    
    public int getCategory()
    {
    	return category;
    }
    
    public String getDescription()
    {
    	return description;
    }
 }
