package com.sahin.ecommerce;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ShoppingCartTest {

    private ShoppingCart shoppingCart;
    private DeliveryCostCalculator deliveryCostCalculator;


    @Before
    public void setup() {
        shoppingCart = new ShoppingCartImpl();

        deliveryCostCalculator = new DeliveryCostCalculatorImpl(EcommerceConstants.COST_PER_PRODUCT,
                EcommerceConstants.COST_PER_DELIVERY,
                EcommerceConstants.FIXED_COST);
    }

    @Test
    public void Given_Products_When_Same_Category_Then_Apply_Discounts_To_Chart() {
        Category giyim = new Category("giyim");

        Product kazak = new Product("kazak", 2.0, giyim);
        Product pantolon = new Product("pantolon", 4.0, giyim);

        shoppingCart.addItem(kazak, 4);
        shoppingCart.addItem(pantolon, 3);

        Campaign campaign1 = new Campaign(giyim, 5, 2, DiscountType.Rate);

        shoppingCart.applyDiscounts(campaign1);

        Assert.assertEquals(19.0, shoppingCart.getTotalAmountAfterDiscount(), 0.1);
    }

    @Test
    public void Given_Products_When_Different_Category_Then_Print_Chart() {
        Category giyim = new Category("giyim");
        Category beyazEsya = new Category("beyaz eşya");

        Product kazak = new Product("kazak", 2.0, giyim);
        Product pantolon = new Product("pantolon", 4.0, giyim);

        shoppingCart.addItem(kazak, 4);
        shoppingCart.addItem(pantolon, 3);

        Campaign campaign1 = new Campaign(giyim, 5, 2, DiscountType.Rate);

        shoppingCart.applyDiscounts(campaign1);

        shoppingCart.print();

    }
}
