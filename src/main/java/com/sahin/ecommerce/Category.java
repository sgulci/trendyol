package com.sahin.ecommerce;

import java.util.Objects;

public class Category {
    private String title;
    private Category parent;

    public Category(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Category getParent() {
        return parent;
    }

    public void setParent(Category parent) {
        this.parent = parent;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Category category = (Category) o;
        return title.equals(category.title) &&
                Objects.equals(parent, category.parent);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, parent);
    }
}
