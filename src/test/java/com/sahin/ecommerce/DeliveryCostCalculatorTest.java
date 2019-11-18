package com.sahin.ecommerce;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


public class DeliveryCostCalculatorTest {

    DeliveryCostCalculator deliveryCostCalculator;
    ShoppingCart shoppingCart;

    Category giyim;
    Category beyazEsya;

    Product pantolon;
    Product televizyon;

    @Before
    public void setup(){
        deliveryCostCalculator =  new DeliveryCostCalculatorImpl(
                EcommerceConstants.COST_PER_DELIVERY,
                EcommerceConstants.COST_PER_PRODUCT,
                EcommerceConstants.FIXED_COST);

        shoppingCart = new ShoppingCartImpl();

        giyim = new Category("giyim");
        beyazEsya = new Category("beyaz eşya");

        pantolon = new Product("pantolon", 4.0, giyim);
        televizyon = new Product("televizyon", 12.0, beyazEsya);

    }

    @Test
    public void Given_Cart_Then_Calculate_Delivery_Cost(){
        shoppingCart.addItem(pantolon,4);
        shoppingCart.addItem(televizyon,1);

        Assert.assertEquals(24.99,shoppingCart.getDeliveryCost(),0.01);
    }

}
