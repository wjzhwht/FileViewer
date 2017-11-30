package com.xerofox.fileviewer.vo;

public class SpecificationMenuFilter implements MenuFilter {
    private int min;
    private int max;

    public SpecificationMenuFilter(int min, int max) {
        this.min = min;
        this.max = max;
    }

    @Override
    public String getText() {
        return min + "~" + max;
    }

    @Override
    public boolean match(TowerPart part) {
        return part.getWide() >= min && part.getWide() <= max;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SpecificationMenuFilter that = (SpecificationMenuFilter) o;

        if (min != that.min) return false;
        return max == that.max;
    }

    @Override
    public int hashCode() {
        int result = min;
        result = 31 * result + max;
        return result;
    }
}
