package com.sahin.ecommerce;

import java.util.Objects;

public class Coupon {
    private double minimumAmount;
    private double discount;
    private DiscountType discountType;

    public Coupon(double minimumAmount, double discount, DiscountType discountType) {
        this.minimumAmount = minimumAmount;
        this.discount = discount;
        this.discountType = discountType;
    }

    public double getMinimumAmount() {
        return minimumAmount;
    }

    public void setMinimumAmount(double minimumAmount) {
        this.minimumAmount = minimumAmount;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public DiscountType getDiscountType() {
        return discountType;
    }

    public void setDiscountType(DiscountType discountType) {
        this.discountType = discountType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Coupon coupon = (Coupon) o;
        return Double.compare(coupon.minimumAmount, minimumAmount) == 0 &&
                Double.compare(coupon.discount, discount) == 0 &&
                discountType == coupon.discountType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(minimumAmount, discount, discountType);
    }
}
