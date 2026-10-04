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
    // 1. calculateSubtotal (5 Test Cases - Khớp 100% Excel)
    // ========================================================
    @Test
    void testCalculateSubtotal_UTCID01_NormalOrder() {
        // UTCID01: Product("P01", 500.0, 3) -> 1500.0
        Product p = new Product("P01", "Ao thun", 500.0, 3);
        assertEquals(1500.0, salesService.calculateSubtotal(p), 0.001);
    }

    @Test
    void testCalculateSubtotal_UTCID02_SingleItem() {
        // UTCID02: Product("P02", 250.0, 1) -> 250.0 (Biên quantity = 1)
        Product p = new Product("P02", "Giay the thao", 250.0, 1);
        assertEquals(250.0, salesService.calculateSubtotal(p), 0.001);
    }

    @Test
    void testCalculateSubtotal_UTCID03_DecimalPrice() {
        // UTCID03: Product("P03", 100.5, 2) -> 201.0 (Giá thập phân)
        Product p = new Product("P03", "Phu kien", 100.5, 2);
        assertEquals(201.0, salesService.calculateSubtotal(p), 0.001);
    }

    @Test
    void testCalculateSubtotal_UTCID04_LargeQuantity() {
        // UTCID04: Product("P04", 1000.0, 10) -> 10000.0
        Product p = new Product("P04", "Ao khoac", 1000.0, 10);
        assertEquals(10000.0, salesService.calculateSubtotal(p), 0.001);
    }

    @Test
    void testCalculateSubtotal_UTCID05_NullProduct_ThrowsException() {
        // UTCID05: null -> IllegalArgumentException("Product cannot be null")
        IllegalArgumentException ex = assertThrows(
            IllegalArgumentException.class,
            () -> salesService.calculateSubtotal(null)
        );
        assertEquals("Product cannot be null", ex.getMessage());
    }

    // ========================================================
    // 2. calculateDiscount (7 Test Cases - Khớp 100% Excel)
    // ========================================================
    @ParameterizedTest
    @CsvSource({
        "999.99, 0.0",         // UTCID01: Biên dưới < 1,000 -> 0%
        "1000.0, 50.0",        // UTCID02: Biên 1,000 -> 5%
        "4999.99, 249.9995",   // UTCID03: Biên trên 5%
        "5000.0, 500.0",       // UTCID04: Biên 5,000 -> 10%
        "9999.99, 999.999",    // UTCID05: Biên trên 10%
        "10000.0, 1500.0"      // UTCID06: Biên >= 10,000 -> 15%
    })
    void testCalculateDiscount_UTCID01_to_06_Boundary(double subtotal, double expected) {
        assertEquals(expected, salesService.calculateDiscount(subtotal), 0.001);
    }

    @Test
    void testCalculateDiscount_UTCID07_NegativeSubtotal_ThrowsException() {
        // UTCID07: subtotal = -50 -> IllegalArgumentException("Subtotal cannot be negative")
        IllegalArgumentException ex = assertThrows(
            IllegalArgumentException.class,
            () -> salesService.calculateDiscount(-50.0)
        );
        assertEquals("Subtotal cannot be negative", ex.getMessage());
    }

    // ========================================================
    // 3. calculateShippingFee (5 Test Cases - Khớp 100% Excel)
    // ========================================================
    @Test
    void testCalculateShippingFee_UTCID01_ZeroSubtotal() {
        // UTCID01: subtotal = 0.0 -> 50.0
        assertEquals(50.0, salesService.calculateShippingFee(0.0), 0.001);
    }

    @Test
    void testCalculateShippingFee_UTCID02_Below2000() {
        // UTCID02: subtotal = 1999.99 -> 50.0
        assertEquals(50.0, salesService.calculateShippingFee(1999.99), 0.001);
    }

    @Test
    void testCalculateShippingFee_UTCID03_Exactly2000_Boundary() {
        // UTCID03: subtotal = 2000.0 -> 0.0 (Miễn ship từ 2000)
        assertEquals(0.0, salesService.calculateShippingFee(2000.0), 0.001);
    }

    @Test
    void testCalculateShippingFee_UTCID04_Above2000() {
        // UTCID04: subtotal = 3500.0 -> 0.0
        assertEquals(0.0, salesService.calculateShippingFee(3500.0), 0.001);
    }

    @Test
    void testCalculateShippingFee_UTCID05_NegativeSubtotal_ThrowsException() {
        // UTCID05: subtotal = -10.0 -> IllegalArgumentException("Subtotal cannot be negative")
        IllegalArgumentException ex = assertThrows(
            IllegalArgumentException.class,
            () -> salesService.calculateShippingFee(-10.0)
        );
        assertEquals("Subtotal cannot be negative", ex.getMessage());
    }

    // ========================================================
    // 4. calculateTotal (3 Test Cases - Khớp 100% Excel)
    // ========================================================
    @Test
    void testCalculateTotal_UTCID01_WithDiscountAndShipping() {
        // UTCID01: Balo (500*3=1500) -> Disc 75, Ship 50 -> Total 1475.0
        Product p = new Product("P03", "Balo", 500.0, 3);
        assertEquals(1475.0, salesService.calculateTotal(p), 0.001);
    }

    @Test
    void testCalculateTotal_UTCID02_LargeOrder_FreeShipping() {
        // UTCID02: Laptop Bag (1000*10=10000) -> Disc 1500, Ship 0 -> Total 8500.0
        Product p = new Product("P04", "Laptop Bag", 1000.0, 10);
        assertEquals(8500.0, salesService.calculateTotal(p), 0.001);
    }

    @Test
    void testCalculateTotal_UTCID03_NullProduct_ThrowsException() {
        // UTCID03: product null -> IllegalArgumentException("Product cannot be null")
        IllegalArgumentException ex = assertThrows(
            IllegalArgumentException.class,
            () -> salesService.calculateTotal(null)
        );
        assertEquals("Product cannot be null", ex.getMessage());
    }

    // ========================================================
    // 5. classifyCustomer (6 Test Cases - Khớp 100% Excel)
    // ========================================================
    @Test
    void testClassifyCustomer_UTCID01_Regular() {
        // UTCID01: total = 999.99 -> REGULAR
        assertEquals("REGULAR", salesService.classifyCustomer(999.99));
    }

    @Test
    void testClassifyCustomer_UTCID02_Silver_LowerBoundary() {
        // UTCID02: total = 1000.0 -> SILVER
        assertEquals("SILVER", salesService.classifyCustomer(1000.0));
    }

    @Test
    void testClassifyCustomer_UTCID03_Silver_UpperBoundary() {
        // UTCID03: total = 4999.99 -> SILVER
        assertEquals("SILVER", salesService.classifyCustomer(4999.99));
    }

    @Test
    void testClassifyCustomer_UTCID04_Gold_Boundary() {
        // UTCID04: total = 5000.0 -> GOLD
        assertEquals("GOLD", salesService.classifyCustomer(5000.0));
    }

    @Test
    void testClassifyCustomer_UTCID05_VIP_Boundary() {
        // UTCID05: total = 10000.0 -> VIP
        assertEquals("VIP", salesService.classifyCustomer(10000.0));
    }

    @Test
    void testClassifyCustomer_UTCID06_NegativeTotal_ThrowsException() {
        // UTCID06: total = -10.0 -> IllegalArgumentException("Total cannot be negative")
        IllegalArgumentException ex = assertThrows(
            IllegalArgumentException.class,
            () -> salesService.classifyCustomer(-10.0)
        );
        assertEquals("Total cannot be negative", ex.getMessage());
    }
}

