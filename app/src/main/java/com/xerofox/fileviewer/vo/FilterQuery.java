package com.xerofox.fileviewer.vo;

import java.util.ArrayList;
import java.util.List;

public class FilterQuery {
    private String name;
    private int type;
    private List<String> items;

    public FilterQuery() {
        this.items = new ArrayList<>();
    }

    public FilterQuery(String name, int type) {
        this.name = name;
        this.type = type;
        this.items = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public List<String> getItems() {
        return items;
    }

    public void setItems(List<String> items) {
        this.items = items;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FilterQuery that = (FilterQuery) o;

        if (type != that.type) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        return items != null ? items.equals(that.items) : that.items == null;
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + type;
        result = 31 * result + (items != null ? items.hashCode() : 0);
        return result;
    }
}
