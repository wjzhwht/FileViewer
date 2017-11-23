package com.xerofox.fileviewer.vo;

import java.util.ArrayList;
import java.util.List;

public class Filter {
    private static final String NAME_TOWER_TYPE = "塔型";
    private static final String NAME_SEG_STR = "段号";
    private static final String NAME_MATERIAL_MARK = "材质";
    private static final String NAME_SPECIFICATION = "规格";
    private static final String NAME_MANU = "工艺";

    public static final int TYPE_TOWER_TYPE = 1;
    public static final int TYPE_SEG_STR = 2;
    public static final int TYPE_MATERIAL_MARK = 3;
    public static final int TYPE_SPECIFICATION = 4;
    public static final int TYPE_MANU = 5;

    private static final int COLUMN_COUNT_TOWER_TYPE = 2;
    private static final int COLUMN_COUNT_SEG_STR = 2;
    private static final int COLUMN_COUNT_MATERIAL_MARK = 2;
    private static final int COLUMN_COUNT_SPECIFICATION = 3;
    private static final int COLUMN_COUNT_MANU = 2;

    private String name;
    private int type;
    private int countColumn;
    private List<Item> items;

    public Filter(int type) {
        this.type = type;
        this.items = new ArrayList<>();
        switch (type) {
            case TYPE_TOWER_TYPE:
                this.name = NAME_TOWER_TYPE;
                this.countColumn = COLUMN_COUNT_TOWER_TYPE;
                break;
            case TYPE_SEG_STR:
                this.name = NAME_SEG_STR;
                this.countColumn = COLUMN_COUNT_SEG_STR;
                break;
            case TYPE_MATERIAL_MARK:
                this.name = NAME_MATERIAL_MARK;
                this.countColumn = COLUMN_COUNT_MATERIAL_MARK;
                break;
            case TYPE_SPECIFICATION:
                this.name = NAME_SPECIFICATION;
                this.countColumn = COLUMN_COUNT_SPECIFICATION;
                break;
            case TYPE_MANU:
                this.name = NAME_MANU;
                this.countColumn = COLUMN_COUNT_MANU;
                break;
            default:
                break;
        }
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

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public int getCountColumn() {
        return countColumn;
    }

    public void setCountColumn(int countColumn) {
        this.countColumn = countColumn;
    }

    public static class Item {
        private String name;
        private boolean selected;

        public Item(String name, boolean selected) {
            this.name = name;
            this.selected = selected;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public boolean isSelected() {
            return selected;
        }

        public void setSelected(boolean selected) {
            this.selected = selected;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Item item = (Item) o;

            if (selected != item.selected) return false;
            return name != null ? name.equals(item.name) : item.name == null;
        }

        @Override
        public int hashCode() {
            int result = name != null ? name.hashCode() : 0;
            result = 31 * result + (selected ? 1 : 0);
            return result;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Filter filter = (Filter) o;

        if (type != filter.type) return false;
        if (countColumn != filter.countColumn) return false;
        if (name != null ? !name.equals(filter.name) : filter.name != null) return false;
        return items != null ? items.equals(filter.items) : filter.items == null;
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + type;
        result = 31 * result + countColumn;
        result = 31 * result + (items != null ? items.hashCode() : 0);
        return result;
    }
}
