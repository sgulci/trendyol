package com.sahin.ecommerce;

public class Campaign {
    private Category category;
    private double discount;
    private int minUnit;
    private DiscountType discountType;

    public Campaign(Category category, double discount, int minUnit, DiscountType discountType) {
        this.category = category;
        this.discount = discount;
        this.minUnit = minUnit;
        this.discountType = discountType;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
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

    public double getMinUnit() {
        return minUnit;
    }

    public void setMinUnit(int minUnit) {
        this.minUnit = minUnit;
    }
}
