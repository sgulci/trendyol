package com.sahin.ecommerce;

import java.util.*;
import java.util.stream.Collectors;

public class ShoppingCartImpl implements ShoppingCart {

    private Map<Category, Map<Product, Integer>> basket;
    private double totalPrice;
    private double discountPrice;
    private double couponPrice;
    private boolean isCampaignApplied = false;

    public ShoppingCartImpl() {
        basket = new HashMap<>();
    }

    @Override
    public void addItem(Product product, int numberOfProduct) {

        if (basket.get(product.getCategory()) == null) {

            Map<Product, Integer> productIntegerMap = new HashMap<>();
            productIntegerMap.put(product, numberOfProduct);

            basket.put(product.getCategory(), productIntegerMap);
        } else {
            Map<Product, Integer> productIntegerMap = basket.get(product.getCategory());
            productIntegerMap.put(product, numberOfProduct);
        }

        totalPrice += product.getPrice() * numberOfProduct;
        isCampaignApplied = false;
        discountPrice = 0;
        couponPrice = 0;
    }

    @Override
    public int getNUmberOfDeliveries() {

        return basket.keySet().size();
    }

    @Override
    public int getNumberOfDistinctProducts() {
        int productCount = 0;

        for (Category category : basket.keySet()) {
            Map<Product, Integer> productMap = basket.get(category);
            productCount += productMap.size();
        }

        return productCount;
    }

    @Override
    public void applyDiscounts(Campaign... campaigns) {

        double maximumDiscount = 0.0;

        for (Campaign cp : campaigns) {

            if (checkCartHasCategory(cp.getCategory())
                    && checkCartHasMinimumForCampaign(cp)) {

                double discount = calculateTotalProductCampaignDiscount(cp);

                if (maximumDiscount < discount)
                    maximumDiscount = discount;
            }
        }
        discountPrice = maximumDiscount;
        isCampaignApplied = true;
    }

    private boolean checkCartHasCategory(Category category) {

        if (basket.keySet().contains(category))
            return true;

        /// we have to check if parent category has a campaign
        for (Category cat : basket.keySet()) {
            Category currentCategory = cat.getParent();

            while (currentCategory != null) {
                if (currentCategory.getTitle().equals(category.getTitle()))
                    return true;

                currentCategory = currentCategory.getParent();
            }
        }

        return false;
    }

    private boolean checkCartHasMinimumForCampaign(Campaign campaign) {

        List<Map<Product, Integer>> productMapList = getProductMapFromCampaign(campaign);

        if (productMapList.size() == 0)
            return false;

        int itemCount = 0;

        for (Map<Product, Integer> productMap : productMapList) {
            for (int count : productMap.values()) {
                itemCount += count;
            }
        }

        if (itemCount >= campaign.getMinUnit())
            return true;

        return false;
    }

    private double calculateTotalProductCampaignDiscount(Campaign campaign) {

        List<Map<Product, Integer>> productMapList = getProductMapFromCampaign(campaign);

        if (productMapList.size() == 0)
            return 0;

        double totalAmountOfCategory = 0;

        for (Map<Product, Integer> productMap : productMapList) {
            for (Map.Entry<Product, Integer> map : productMap.entrySet()) {
                totalAmountOfCategory += (map.getKey().getPrice() * map.getValue());
            }
        }

        return calculateCampaignDiscount(campaign, totalAmountOfCategory);
    }

    private List<Map<Product, Integer>> getProductMapFromCampaign(Campaign campaign) {

        List<Map<Product, Integer>> productMapList = new ArrayList<>();

        Map<Product, Integer> productMap = basket.get(campaign.getCategory());

        /// we have to check if parent category has a campaign
        if (productMap != null)
            productMapList.add(productMap);

        for (Category cat : basket.keySet()) {
            Category currentCategory = cat.getParent();

            while (currentCategory != null) {
                if (currentCategory.getTitle().equals(campaign.getCategory().getTitle())) {
                    productMapList.add(basket.get(cat));
                }

                currentCategory = currentCategory.getParent();
            }
        }

        return productMapList;
    }

    private double calculateCampaignDiscount(Campaign campaign, double totalAmountOfCategory) {

        if (campaign.getDiscountType() == DiscountType.Amount) {
            return campaign.getDiscount();

        } else {
            return totalAmountOfCategory * (campaign.getDiscount() / 100);
        }
    }

    @Override
    public void applyCoupon(Coupon coupon) {
        //if no campaign is defined this control is useless
        // because there you can not apply discount without campaign
        if (!isCampaignApplied)
            return;

        double amountCheckForCoupon = totalPrice - discountPrice;

        if (amountCheckForCoupon > coupon.getMinimumAmount()) {
            if (coupon.getDiscountType() == DiscountType.Amount) {
                couponPrice = coupon.getDiscount();

            } else {
                couponPrice = amountCheckForCoupon * (coupon.getDiscount() / 100);
            }
        }
    }

    @Override
    public double getTotalAmountAfterDiscount() {
        return totalPrice - discountPrice - couponPrice;
    }

    @Override
    public double getCouponDiscount() {
        return couponPrice;
    }

    @Override
    public double getCampaignDiscount() {
        return discountPrice;
    }

    @Override
    public double getDeliveryCost() {

        //Normally this should be instantiated from IoC but in this context we let this happen
        DeliveryCostCalculator deliveryCostCalculator = new DeliveryCostCalculatorImpl(
                EcommerceConstants.COST_PER_DELIVERY,
                EcommerceConstants.COST_PER_PRODUCT,
                EcommerceConstants.FIXED_COST);
        return deliveryCostCalculator.calculatorFor(this);
    }

    @Override
    public void print() {
        for (Map.Entry<Category, Map<Product, Integer>> mapEntry : basket.entrySet()) {
            System.out.println("Category = " + mapEntry.getKey().getTitle() + "    ------------------------------------------------------");
            for (Map.Entry<Product, Integer> productMap : mapEntry.getValue().entrySet()) {
                System.out.println("Product Name = " + productMap.getKey().getTitle() + ",   Quantity = " + productMap.getValue() + ",  Price = " + productMap.getKey().getPrice());
            }
        }

        System.out.println("Total Price = " + totalPrice);
        System.out.println("Total Amount After Discount = " + getTotalAmountAfterDiscount());

        System.out.println("Delivery Cost  = " + getDeliveryCost());
        System.out.println("Total Amount  = " + getTotalAmountAfterDiscount() + getDeliveryCost());


    }
}
