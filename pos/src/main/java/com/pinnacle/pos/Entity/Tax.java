package com.pinnacle.pos.Entity;

import lombok.Data;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

@Data
public class Tax {

    public static final String CALIFORNIA = "CA";
    public static final String NEWYORK = "NY";

    public static final HashSet<String> LOCATION_LIST = new HashSet<>() {{
        add(CALIFORNIA);
        add(NEWYORK);
    }};

    public static final Map<String, HashSet<String>> EXEMPTION_MAP = new HashMap<>() {{
        HashSet<String> californiaSet = new HashSet<>() {{
            add(Category.FOOD);
        }};
        put(CALIFORNIA, californiaSet);

        HashSet<String> newYorkSet = new HashSet<>() {{
            add(Category.FOOD);
            add(Category.CLOTHING);
        }};
        put(NEWYORK, newYorkSet);
    }};

    public static final Map<String, Double> TAX_RATE_MAP = new HashMap<>() {{
        put(CALIFORNIA, 0.0975);
        put(NEWYORK, 0.08875);
    }};

    private String location;

    public Tax(String location) {
        this.location = location;
    }
}
