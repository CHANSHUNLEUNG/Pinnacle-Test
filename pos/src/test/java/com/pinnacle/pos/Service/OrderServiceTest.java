package com.pinnacle.pos.Service;

import com.pinnacle.pos.Entity.Item;
import com.pinnacle.pos.Entity.OrderItem;
import com.pinnacle.pos.Entity.Tax;
import com.pinnacle.pos.Execption.ItemListEmptyException;
import com.pinnacle.pos.Execption.ItemNotFoundExecption;
import com.pinnacle.pos.Execption.LocationNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class OrderServiceTest {

    private static final String header = "item           " + "     price" + "       qty\n\n";

    private List<OrderItem> orderItems;
    private OrderService orderService = new OrderService();

    @BeforeEach
    void setUp() {
        orderItems = new ArrayList<>();
    }

    @Test
    void should_calculate_right_when_location_is_ca_1() {
        orderItems.add(new OrderItem(new Item(Item.BOOK, 17.99), 1));
        orderItems.add(new OrderItem(new Item(Item.POTATO_CHIPS, 3.99), 1));

        String answer = header +
                "book           " + "    $17.99" + "         1\n" +
                "potato chips   " + "     $3.99" + "         1\n" +
                "subtotal:      " + "          " + "    $21.98\n" +
                "tax:           " + "          " + "     $1.80\n" +
                "total:         " + "          " + "    $23.78\n";

        assertEquals(answer, orderService.printReceipt(orderItems, Tax.CALIFORNIA));
    }

    @Test
    void should_calculate_right_when_location_is_newyork_1() {
        orderItems.add(new OrderItem(new Item(Item.BOOK, 17.99), 1));
        orderItems.add(new OrderItem(new Item(Item.PENCIL, 2.99), 3));

        String answer = header +
                "book           " + "    $17.99" + "         1\n" +
                "pencil         " + "     $2.99" + "         3\n" +
                "subtotal:      " + "          " + "    $26.96\n" +
                "tax:           " + "          " + "     $2.40\n" +
                "total:         " + "          " + "    $29.36\n";

        assertEquals(answer, orderService.printReceipt(orderItems, Tax.NEWYORK));
    }

    @Test
    void should_calculate_right_when_location_is_newyork_2() {
        orderItems.add(new OrderItem(new Item("pencil", 2.99), 2));
        orderItems.add(new OrderItem(new Item("shirt", 29.99), 1));

        String answer = header +
                "pencil         " + "     $2.99" + "         2\n" +
                "shirt          " + "    $29.99" + "         1\n" +
                "subtotal:      " + "          " + "    $35.97\n" +
                "tax:           " + "          " + "     $0.55\n" +
                "total:         " + "          " + "    $36.52\n";

        assertEquals(answer, orderService.printReceipt(orderItems, Tax.NEWYORK));
    }

    @Test
    void should_item_wrap_to_next_line_when_item_name_too_long() {
        orderItems.add(new OrderItem(new Item(Item.BOOK, 17.99), 1));
        orderItems.add(new OrderItem(new Item(Item.LONG_ITEM_NAME, 3.99), 1));

        String answer = header +
                "book           " + "    $17.99" + "         1\n" +
                "this is a long-\n" +
                "item-name, with\n" +
                "length greater\n" +
                "than 15        " + "     $3.99" + "         1\n" +
                "subtotal:      " + "          " + "    $21.98\n" +
                "tax:           " + "          " + "     $2.15\n" +
                "total:         " + "          " + "    $24.13\n";

        assertEquals(answer, orderService.printReceipt(orderItems, "CA"));
    }


    //    public static final String LONG_ITEM_NAME = "this is a long-item-name, with length greater than 15";
//    public static final String LONG_ITEM_WORD = "thisisalongitemword,longerthan15";
    @Test
    void should_exceed_first_colume_when_item_word_longer_than_15() {
        orderItems.add(new OrderItem(new Item(Item.BOOK, 17.99), 1));
        orderItems.add(new OrderItem(new Item(Item.LONG_ITEM_WORD, 3.99), 1));

        String answer = header +
                "book           " + "    $17.99" + "         1\n" +
                "thisisalongitem\n" +
                "word,longerthan\n" +
                "15             " + "     $3.99" + "         1\n" +
                "subtotal:      " + "          " + "    $21.98\n" +
                "tax:           " + "          " + "     $2.15\n" +
                "total:         " + "          " + "    $24.13\n";

        assertEquals(answer, orderService.printReceipt(orderItems, "CA"));
    }

    @Test
    void should_throw_item_empty_execption_when_no_order_item() {
        Exception execption = assertThrows(ItemListEmptyException.class, () -> {
            orderService.printReceipt(orderItems, Tax.CALIFORNIA);
        });
        assertEquals("No item found", execption.getMessage());
    }

    @Test
    void should_throw_item_not_found_execption_when_order_item_is_not_exist() {
        orderItems.add(new OrderItem(new Item("ABC", 17.99), 1));

        Exception execption = assertThrows(ItemNotFoundExecption.class, () -> {
            orderService.printReceipt(orderItems, Tax.CALIFORNIA);
        });

        assertEquals("Cannot find item: ABC", execption.getMessage());
    }

    @Test
    void should_throw_location_not_found_execption_when_location_is_not_supported() {
        orderItems.add(new OrderItem(new Item("potato chips", 3.99), 1));

        Exception execption = assertThrows(LocationNotFoundException.class, () -> {
            orderService.printReceipt(orderItems, "ABC");
        });

        assertEquals("Cannot find location: ABC", execption.getMessage());
    }
}