package com.sahin.ecommerce;

import org.junit.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.hamcrest.core.StringContains.containsString;


public class ShoppingCartTest {

    private ShoppingCart shoppingCart;
    private DeliveryCostCalculator deliveryCostCalculator;
    private PrintStream sysOut;
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();

    Category giyim;
    Category beyazEsya;
    Category ayakkabi;
    Category erkekAyakkabi;
    Category kadinAyakkabi;

    Product kazak;
    Product pantolon;
    Product televizyon;
    Product buzdolabi;
    Product bot;
    Product cizme;
    Product sandalet;

    Campaign campaignGiyim;
    Campaign campaignBeyazEsya;
    Campaign campaignAyakkabi;
    Campaign campaignKadinAyakkabi;

    Coupon couponRate;
    Coupon couponAmount;


    @Before
    public void setup() {
        sysOut = System.out;
        System.setOut(new PrintStream(outContent));


        shoppingCart = new ShoppingCartImpl();

        deliveryCostCalculator = new DeliveryCostCalculatorImpl(
                EcommerceConstants.COST_PER_PRODUCT,
                EcommerceConstants.COST_PER_DELIVERY,
                EcommerceConstants.FIXED_COST);

        giyim = new Category("giyim");
        beyazEsya = new Category("beyaz eşya");
        ayakkabi = new Category("ayakkabı");
        erkekAyakkabi = new Category("erkek ayakkabı");
        kadinAyakkabi = new Category("kadın ayakkabı");
        erkekAyakkabi.setParent(ayakkabi);
        kadinAyakkabi.setParent(ayakkabi);

        kazak = new Product("kazak", 2.0, giyim);
        pantolon = new Product("pantolon", 4.0, giyim);
        televizyon = new Product("televizyon", 12.0, beyazEsya);
        buzdolabi = new Product("buzdolabı", 15.6, beyazEsya);
        bot = new Product("bot", 9.0, erkekAyakkabi);
        cizme = new Product("cizme", 4.0, kadinAyakkabi);
        sandalet = new Product("sandalet", 2.5, ayakkabi);

        campaignGiyim = new Campaign(giyim, 5, 2, DiscountType.Rate);
        campaignBeyazEsya = new Campaign(beyazEsya, 4, 2, DiscountType.Amount);
        campaignAyakkabi = new Campaign(ayakkabi, 10, 4, DiscountType.Rate);
        campaignKadinAyakkabi = new Campaign(kadinAyakkabi, 2, 2, DiscountType.Amount);

        couponRate = new Coupon(20,10,DiscountType.Rate);
        couponAmount = new Coupon(15,2,DiscountType.Amount);
    }

    @After
    public void revertStreams() {
        System.setOut(sysOut);
    }

    @Test
    public void Given_Products_When_Same_Category_Then_Apply_Discounts_To_Chart() {

        shoppingCart.addItem(kazak, 4);
        shoppingCart.addItem(pantolon, 3);

        shoppingCart.applyDiscounts(campaignGiyim);

        Assert.assertEquals(19.0, shoppingCart.getTotalAmountAfterDiscount(), 0.01);
    }

    @Test
    public void Given_Products_When_Different_Category_Then_Apply_Discounts_To_Chart() {

        shoppingCart.addItem(kazak, 1);
        shoppingCart.addItem(pantolon, 1);
        shoppingCart.addItem(buzdolabi, 2);

        shoppingCart.applyDiscounts(campaignGiyim,campaignBeyazEsya);

        Assert.assertEquals(33.2, shoppingCart.getTotalAmountAfterDiscount(), 0.01);
    }

    @Test
    public void Given_Products_When_Same_Category_Then_Apply_Coupon_To_Chart() {

        shoppingCart.addItem(bot, 1);
        shoppingCart.addItem(cizme, 1);
        shoppingCart.addItem(sandalet, 1);

        shoppingCart.applyDiscounts(campaignAyakkabi);
        shoppingCart.applyCoupon(couponAmount);

        Assert.assertEquals(13.5, shoppingCart.getTotalAmountAfterDiscount(), 0.01);
    }

    @Test
    public void Given_Products_When_Parent_Category_Then_Apply_Discount_To_Chart() {

        shoppingCart.addItem(bot, 2);
        shoppingCart.addItem(cizme, 2);

        shoppingCart.applyDiscounts(campaignAyakkabi);

        Assert.assertEquals(23.4, shoppingCart.getTotalAmountAfterDiscount(), 0.01);
    }

    @Test
    public void Given_Products_When_Parent_And_Child_Category_Then_Apply_Discount_To_Chart() {

        shoppingCart.addItem(bot, 1);
        shoppingCart.addItem(cizme, 2);
        shoppingCart.addItem(sandalet, 1);

        shoppingCart.applyDiscounts(campaignAyakkabi);

        Assert.assertEquals(17.55, shoppingCart.getTotalAmountAfterDiscount(), 0.01);
    }

    @Test
    public void Given_Products_When_Parent_And_Child_Category_Then_Apply_Maximum_Discount_To_Chart() {

        shoppingCart.addItem(bot, 1);
        shoppingCart.addItem(cizme, 2);
        shoppingCart.addItem(sandalet, 1);

        shoppingCart.applyDiscounts(campaignAyakkabi,campaignKadinAyakkabi);

        Assert.assertEquals(17.5, shoppingCart.getTotalAmountAfterDiscount(), 0.01);
    }

    @Test
    public void Given_Products_When_Same_Category_Then_Apply_Discount_And_Coupon_To_Chart() {

        shoppingCart.addItem(kazak, 1);
        shoppingCart.addItem(pantolon, 1);

        shoppingCart.applyDiscounts(campaignBeyazEsya);
        shoppingCart.applyCoupon(couponRate);

        Assert.assertEquals(21.2, shoppingCart.getTotalAmountAfterDiscount(), 0.01);
    }

    @Test
    public void Given_Products_When_Different_Category_Then_Calculate_Delivery_Cost_To_Chart() {

        shoppingCart.addItem(kazak, 1);
        shoppingCart.addItem(pantolon, 2);
        shoppingCart.addItem(televizyon, 1);


        Assert.assertEquals(29.99, shoppingCart.getDeliveryCost(), 0.01);
    }

    @Test
    public void Given_Products_And_Category_Then_Print_Chart() {
        shoppingCart.addItem(kazak, 4);
        shoppingCart.addItem(pantolon, 3);

        shoppingCart.applyDiscounts(campaignGiyim);

        shoppingCart.print();

        Assert.assertThat(outContent.toString(), containsString("giyim"));
    }
}
