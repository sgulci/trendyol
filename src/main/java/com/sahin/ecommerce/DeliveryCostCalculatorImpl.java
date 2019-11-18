package com.sahin.ecommerce;

import java.util.Objects;

public class DeliveryCostCalculatorImpl implements DeliveryCostCalculator {
    private double costPerProduct;
    private double costPerDelivery;
    private  double fixedCost;

    public DeliveryCostCalculatorImpl(double costPerProduct, double costPerDelivery, double fixedCost) {
        this.costPerProduct = costPerProduct;
        this.costPerDelivery = costPerDelivery;
        this.fixedCost = fixedCost;
    }

    @Override
    public double calculatorFor(ShoppingCart cart){

        return (cart.getNUmberOfDeliveries() * this.costPerDelivery
                + (cart.getNumberOfDistinctProducts() * this.costPerProduct
                + this.fixedCost));
    }

    public double getCostPerProduct() {
        return costPerProduct;
    }

    public void setCostPerProduct(double costPerProduct) {
        this.costPerProduct = costPerProduct;
    }

    public double getCostPerDelivery() {
        return costPerDelivery;
    }

    public void setCostPerDelivery(double costPerDelivery) {
        this.costPerDelivery = costPerDelivery;
    }

    public double getFixedCost() {
        return fixedCost;
    }

    public void setFixedCost(double fixedCost) {
        this.fixedCost = fixedCost;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DeliveryCostCalculatorImpl that = (DeliveryCostCalculatorImpl) o;
        return Double.compare(that.costPerProduct, costPerProduct) == 0 &&
                Double.compare(that.costPerDelivery, costPerDelivery) == 0 &&
                Double.compare(that.fixedCost, fixedCost) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(costPerProduct, costPerDelivery, fixedCost);
    }
}
