package com.sahin.ecommerce;

public interface ShoppingCart {
    void addItem(Product product, int numberOfProduct);

    int getNUmberOfDeliveries();

    int getNumberOfDistinctProducts();

    void applyDiscounts(Campaign... campaigns);

    void applyCoupon(Coupon coupon);

    double getTotalAmountAfterDiscount();

    double getCouponDiscount();

    double getCampaignDiscount();

    double getDeliveryCost();

    void print();
}
