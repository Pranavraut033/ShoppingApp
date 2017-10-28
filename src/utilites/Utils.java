package utilites;

import java.util.*;
import product.*;
import product.ProductList.Category;

public class Utils {

    public static final int PRICE_MODE = 1;
    public static final int NAME_MODE = 2;
    public static final int CAT_MODE = 3;

    public static void sort(ProductList list, int mode, boolean dec) {
        int n = list.size();

        if (mode == CAT_MODE) {
            sort(list, NAME_MODE, dec);
        }

        /*
        Swapping:
            Product pa = get(j), pb = get(j + 1);
            remove(pa);
            remove(pb);
            add(a, pb);
            add(b, pa);
         */
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - 1 - i; j++) {
                Product pj = list.get(j), pj1 = list.get(j + 1);
                switch (mode) {
                    case NAME_MODE:
                        if (pj.name.compareTo(pj1.name) > 0) {
                            list.swap(j, j + 1);
                        }
                        break;
                    case PRICE_MODE:
                        if (pj.price > pj1.price) {
                            list.swap(j, j + 1);
                        }
                        break;
                    case CAT_MODE:
                        if (pj.category.ordinal() > pj1.category.ordinal()) {
                            list.swap(j, j + 1);
                        }
                        break;
                    default:
                        break;
                }
            }
        }

        if (dec) {
            for (int i = 0; i < n / 2; i++) {
                list.swap(i, n - i - 1);
            }
        }
    }

    private static String[] COMMON_TERMS = new String[]{
        "in", "with", "on", "by", "under", "a", "the",
        "an", "then", "that", "when", "also", "as",
        "get", "you", "will", "used", "gain", "can",
        ""
    };

    public static ProductList filter(ProductList list, Category c) {
        for (Product p : list.toArray(new Product[list.size()])) {
            if (!p.category.equals(c)) {
                list.remove(p);
            }
        }
        return list;
    }

    //This function does not check mispelled undr
    public static ProductList search(ProductList list, String s) {
        ProductList result = new ProductList();

        if (s.isEmpty()) {
            return result;
        }

        String in = s.toLowerCase();
        String a[] = in.split(" ");
        Vector<String> keys = new Vector<>();
        keys.addAll(Arrays.asList(a));
        keys.removeAll(Arrays.asList(COMMON_TERMS));

        for (Product p : list) {
            for (String key : keys) {
                String c = p.name + p.description + p.price + p.category.toString();
                c = c.toLowerCase();
                if (c.contains(key) && result.indexOf(p) == -1) {
                    result.add(p);
                }
            }
        }

        //Special block for finding product under a certain price
        if (in.contains("under")) {
            if (result.isEmpty()) {
                result = new ProductList(list);
            }
            Long num = 0L;
            String number = "";
            char[] chars = in.toCharArray();

            // 5 is length of word "under" so the loop start after word under
            for (int i = in.indexOf("under") + 5; i < in.length(); i++) {
                char c = chars[i];
                if (Character.isDigit(c)) {
                    while (i < in.length() && Character.isDigit(c = chars[i++])) {
                        number += c;
                    }
                    break;
                }
            }
            if (!number.isEmpty()) {
                num = Long.parseLong(number);
            }

            if (num != 0) {
                for (Product p : result.toArray(new Product[result.size()])) {
                    if (p.price > num) {
                        result.remove(p);
                    }
                }
            }
        }
        return result;
    }

    public static int randomInt(int min, int max) {
        Random r = new Random();
        return r.nextInt((max - min) + 1) + min;
    }
}
