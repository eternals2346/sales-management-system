package com.sales;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

public class SalesServiceTest {

    private SalesService salesService;

    @BeforeEach
    void setUp() {
        salesService = new SalesService();
    }

    // ========================================================
    // 1. Tests for calculateSubtotal() (Yêu cầu ít nhất 3 test)
    // ========================================================
    @Test
    void testCalculateSubtotal_NormalOrder() {
        // Price = 500, Quantity = 3 -> Subtotal = 1500
        Product p = new Product("P01", "Ao thun", 500, 3);
        assertEquals(1500.0, salesService.calculateSubtotal(p), 0.001);
    }

    @Test
    void testCalculateSubtotal_SingleItem() {
        // Price = 250, Quantity = 1 -> Subtotal = 250
        Product p = new Product("P02", "Giay the thao", 250, 1);
        assertEquals(250.0, salesService.calculateSubtotal(p), 0.001);
    }

    @Test
    void testCalculateSubtotal_NullProduct_ThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> salesService.calculateSubtotal(null));
    }

    // ========================================================
    // 2. Tests for calculateDiscount() & Task 3 Parameterized Test
    // (Yêu cầu ít nhất 6 test gồm Boundary: 999.99, 1000, 4999.99, 5000, 9999.99, 10000)
    // ========================================================
    @ParameterizedTest
    @CsvSource({
        "999.99, 0.0",         // < 1,000 -> 0%
        "1000.0, 50.0",        // 1,000 - < 5,000 -> 5%
        "4999.99, 249.9995",   // Biên trên của 5%
        "5000.0, 500.0",       // 5,000 - < 10,000 -> 10%
        "9999.99, 999.999",    // Biên trên của 10%
        "10000.0, 1500.0"      // >= 10,000 -> 15%
    })
    void testCalculateDiscount_BoundaryAndNormalCases(double subtotal, double expected) {
        assertEquals(expected, salesService.calculateDiscount(subtotal), 0.001);
    }

    @Test
    void testCalculateDiscount_NegativeSubtotal_ThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> salesService.calculateDiscount(-50));
    }

    // ========================================================
    // 3. Tests for calculateShippingFee() (Yêu cầu ít nhất 3 test)
    // Rules: < 2000 -> 50, >= 2000 -> 0
    // ========================================================
    @Test
    void testCalculateShippingFee_Below2000() {
        assertEquals(50.0, salesService.calculateShippingFee(1999.99), 0.001);
    }

    @Test
    void testCalculateShippingFee_Exactly2000_Boundary() {
        // Mốc biên 2000: theo quy tắc >= 2000 là 0
        assertEquals(0.0, salesService.calculateShippingFee(2000.0), 0.001);
    }

    @Test
    void testCalculateShippingFee_Above2000() {
        assertEquals(0.0, salesService.calculateShippingFee(3000.0), 0.001);
    }

    @Test
    void testCalculateShippingFee_NegativeSubtotal_ThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> salesService.calculateShippingFee(-10));
    }

    // ========================================================
    // 4. Tests for calculateTotal() (Yêu cầu ít nhất 2 test)
    // Formula: Total = Subtotal - Discount + Shipping
    // ========================================================
    @Test
    void testCalculateTotal_WithDiscountAndShipping() {
        // Price = 500, Quantity = 3 -> Subtotal = 1500
        // Discount = 1500 * 5% = 75
        // Shipping = 50 (vì 1500 < 2000)
        // Total = 1500 - 75 + 50 = 1475
        Product p = new Product("P03", "Balo", 500, 3);
        assertEquals(1475.0, salesService.calculateTotal(p), 0.001);
    }

    @Test
    void testCalculateTotal_LargeOrder_FreeShipping() {
        // Price = 1000, Quantity = 10 -> Subtotal = 10000
        // Discount = 10000 * 15% = 1500
        // Shipping = 0 (vì 10000 >= 2000)
        // Total = 10000 - 1500 + 0 = 8500
        Product p = new Product("P04", "Laptop Bag", 1000, 10);
        assertEquals(8500.0, salesService.calculateTotal(p), 0.001);
    }

    // ========================================================
    // 5. Tests for classifyCustomer() (Yêu cầu ít nhất 4 test)
    // < 1000: REGULAR | 1000 - < 5000: SILVER | 5000 - < 10000: GOLD | >= 10000: VIP
    // ========================================================
    @Test
    void testClassifyCustomer_Regular() {
        assertEquals("REGULAR", salesService.classifyCustomer(999.99));
    }

    @Test
    void testClassifyCustomer_Silver() {
        assertEquals("SILVER", salesService.classifyCustomer(1000.0));
        assertEquals("SILVER", salesService.classifyCustomer(4999.99));
    }

    @Test
    void testClassifyCustomer_Gold() {
        assertEquals("GOLD", salesService.classifyCustomer(5000.0));
        assertEquals("GOLD", salesService.classifyCustomer(9999.99));
    }

    @Test
    void testClassifyCustomer_VIP_Boundary10000() {
        // Mốc biên 10000: theo quy tắc >= 10000 là VIP
        assertEquals("VIP", salesService.classifyCustomer(10000.0));
        assertEquals("VIP", salesService.classifyCustomer(15000.0));
    }
}
