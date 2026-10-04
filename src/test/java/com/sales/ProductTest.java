package com.sales;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class ProductTest {

    @Test
    void testProduct_Valid() {
        Product p = new Product("P01", "Ao thun", 100, 2);
        assertEquals("P01", p.getProductId());
        assertEquals("Ao thun", p.getProductName());
        assertEquals(100, p.getPrice(), 0.001);
        assertEquals(2, p.getQuantity());
    }

    @Test
    void testProduct_InvalidId_ThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> new Product(null, "Ao thun", 100, 2));
        assertThrows(IllegalArgumentException.class, () -> new Product("", "Ao thun", 100, 2));
    }

    @Test
    void testProduct_InvalidName_ThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> new Product("P01", null, 100, 2));
        assertThrows(IllegalArgumentException.class, () -> new Product("P01", "", 100, 2));
    }

    @Test
    void testProduct_InvalidPriceAndQuantity_ThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> new Product("P01", "Ao thun", 0, 2));
        assertThrows(IllegalArgumentException.class, () -> new Product("P01", "Ao thun", -10, 2));
        assertThrows(IllegalArgumentException.class, () -> new Product("P01", "Ao thun", 100, 0));
        assertThrows(IllegalArgumentException.class, () -> new Product("P01", "Ao thun", 100, -5));
    }
}
