package com.pinnacle.pos.Service;

import com.pinnacle.pos.Entity.Item;
import com.pinnacle.pos.Entity.OrderItem;
import com.pinnacle.pos.Entity.Tax;
import com.pinnacle.pos.Execption.ItemListEmptyException;
import com.pinnacle.pos.Execption.ItemNotFoundExecption;
import com.pinnacle.pos.Execption.LocationNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

@Service
public class OrderService {

    private static final int FIRST_COL_LENGTH = 15;
    private static final int SECOND_COL_LENGTH = 10;
    private static final int THIRD_COL_LENGTH = 10;
    private static final double ROUND_UP = 0.05;
    private static final HashMap<String, String> CURRENCY_MAP = new HashMap<>() {{
        put("USD", "$");
    }};

    private String currency = "USD";

    public OrderService() {
    }

    public OrderService(String currency) {
        this.currency = currency;
    }

    public String printReceipt(List<OrderItem> orderItemList, String location) throws ItemNotFoundExecption {
        /*
        firstCol, length 15, left align
        secondCol, length 10, right align
        thirdCol, length 10, right align
         */

        checkOrder(orderItemList, location);

        String currencySymbol = CURRENCY_MAP.get(currency);
        StringBuilder stringBuilder = new StringBuilder();
        TaxService taxService = new TaxService(location);
        double subTotal = 0, taxTotal = 0;

        //Order header
        String header = printLine("item", "price", "qty") + "\n";
        stringBuilder.append(header);

        //Order body
        for (OrderItem orderItem : orderItemList) {
            String line = printLine(orderItem.getItem().getName(),
                    currencySymbol + String.valueOf(orderItem.getItem().getPrice()),
                    String.valueOf(orderItem.getQuantity()));
            stringBuilder.append(line);

            subTotal += orderItem.getQuantity() * orderItem.getItem().getPrice();
            taxTotal += taxService.calculateTax(orderItem);
        }
        taxTotal = Math.ceil(taxTotal / ROUND_UP) * ROUND_UP;

        //Order footer
        String subTotalString = printLine("subtotal:", "", currencySymbol + String.format("%.2f", subTotal));
        String tax = printLine("tax:", "", currencySymbol + String.format("%.2f", taxTotal));
        String total = printLine("total:", "", currencySymbol + String.format("%.2f", subTotal + taxTotal));
        stringBuilder.append(subTotalString);
        stringBuilder.append(tax);
        stringBuilder.append(total);

        return stringBuilder.toString();
    }

    private void checkOrder(List<OrderItem> orderItemList, String location) {
        if (!Tax.LOCATION_LIST.contains(location)) {
            throw new LocationNotFoundException("Cannot find location: " + location);
        }
        if (orderItemList.isEmpty()) {
            throw new ItemListEmptyException("No item found");
        }
        for (OrderItem orderItem : orderItemList) {
            if (!Item.ITEMS.contains(orderItem.getItem().getName())) {
                throw new ItemNotFoundExecption("Cannot find item: " + orderItem.getItem().getName());
            }
        }
    }


    private String printLine(String firstCol, String secondCol, String thirdCol) {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append(wrapWord(firstCol));
        stringBuilder.append(String.format("%" + SECOND_COL_LENGTH + "s", secondCol));
        stringBuilder.append(String.format("%" + THIRD_COL_LENGTH + "s", thirdCol) + "\n");

        return stringBuilder.toString();
    }

    private String wrapWord(String firstCol) {
        StringBuilder stringBuilder = new StringBuilder();
        int length = firstCol.length();
        int count = 0;
        while (length > 15) {
            stringBuilder.append(firstCol.substring(count * 15, count * 15 + 15).trim() + "\n");
            length -= 15;
            count += 1;
        }
        stringBuilder.append(String.format("%-" + FIRST_COL_LENGTH + "s",
                firstCol.substring(count * 15, firstCol.length()).trim()));

        return stringBuilder.toString();
    }
}
