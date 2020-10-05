package com.pinnacle.pos.Entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.HashSet;

@Data
@NoArgsConstructor
public class Item {

    public static final String BOOK = "book";
    public static final String POTATO_CHIPS = "potato chips";
    public static final String PENCIL = "pencil";
    public static final String SHIRT = "shirt";
    public static final String LONG_ITEM_NAME = "this is a long-item-name, with length greater than 15";
    public static final String LONG_ITEM_WORD = "thisisalongitemword,longerthan15";


    //Without Database Mapping
    public static final HashMap<String, String> CATEGORY_MAP = new HashMap<>() {{
        put(BOOK, Category.GOODS);
        put(POTATO_CHIPS, Category.FOOD);
        put(PENCIL, Category.GOODS);
        put(SHIRT, Category.CLOTHING);
    }};
    public static final HashSet<String> ITEMS = new HashSet<>() {{
        add(BOOK);
        add(POTATO_CHIPS);
        add(PENCIL);
        add(SHIRT);
        add(LONG_ITEM_NAME);
        add(LONG_ITEM_WORD);
    }};

    private String name;
    private double price;
    private String category;

    public Item(String name, double price) {
        this.name = name;
        this.price = price;
        category = CATEGORY_MAP.getOrDefault(name.toLowerCase(), "");
    }

}
