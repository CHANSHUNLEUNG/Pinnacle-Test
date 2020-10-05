package com.pinnacle.pos.Service;

import com.pinnacle.pos.Entity.Item;
import com.pinnacle.pos.Entity.OrderItem;
import com.pinnacle.pos.Entity.Tax;

public class TaxService {

    private Tax taxLocation;

    public TaxService(String location) {
        this.taxLocation = new Tax(location.toUpperCase());
    }

    public double getTaxRate(Item item) {
        return isExemption(item) ? 0 : Tax.TAX_RATE_MAP.get(this.taxLocation.getLocation());
    }

    public boolean isExemption(Item item) {
        String category = item.getCategory();
        return Tax.EXEMPTION_MAP.containsKey(taxLocation.getLocation()) ?
                Tax.EXEMPTION_MAP.get(taxLocation.getLocation()).contains(category) : false;
    }

    public double calculateTax(OrderItem orderItem) {
        double taxRate = getTaxRate(orderItem.getItem());
        return taxRate * orderItem.getQuantity() * orderItem.getItem().getPrice();
    }
}
