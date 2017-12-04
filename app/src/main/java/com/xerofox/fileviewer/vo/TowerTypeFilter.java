package com.xerofox.fileviewer.vo;

public class TowerTypeFilter implements MenuFilter {
    private String name;

    public TowerTypeFilter(String name) {
        this.name = name;
    }

    @Override
    public String getText() {
        return name;
    }

    @Override
    public boolean match(TowerPart part) {
        return part.getTowerTypeName().equals(name);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TowerTypeFilter that = (TowerTypeFilter) o;

        return name != null ? name.equals(that.name) : that.name == null;
    }

    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }
}
