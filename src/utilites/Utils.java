package utilites;

import java.util.Random;
import product.ProductList;
import product.Product;

public class Utils {

    public static final int PRICE_MODE = 1;
    public static final int NAME_MODE = 2;
    public static final int CAT_MODE = 3;

    public static void sort(ProductList list, int mode, boolean dec) {
        int n = list.size();

        if(mode  == CAT_MODE)
            sort(list, NAME_MODE, dec);

        for (int i = 0; i < n - 1; i++) {
            for(int j = 0; j  < n - 1 - i; j++) {
                Product pj = list.get(j), pj1 = list.get(j + 1);
                switch (mode) {
                    case NAME_MODE:
                        if(pj.getName().compareTo(pj1.getName()) >  0)
                            list.swap(j, j + 1);
                        break;
                    case PRICE_MODE:
                        if(pj.getPrice() > pj1.getPrice())
                            list.swap(j, j + 1);
                        break;
                    case CAT_MODE:
                        if(pj.getCategory() > pj1.getCategory())
                            list.swap(j, j + 1);
                        break;
                    default:
                        break;
                }
            }
        }

        /*
        Swapping:
            Product temp = get(a);
            list.add(a, get(b));
            list.add(b, temp);
        */

        if(dec)
            for (int i = 0; i < n / 2; i++)
                list.swap(i, n - i - 1);
    }

    public static ProductList search(ProductList list, String in) {
        ProductList result = new ProductList();
        in = in.replace(" ", ":");
        String s[] = in.split(":");
        return result;
    }
    
    public static int randInt(int min, int max) {
        Random rand = new Random();
        return  rand.nextInt((max - min) + 1) + min;
    }
}
