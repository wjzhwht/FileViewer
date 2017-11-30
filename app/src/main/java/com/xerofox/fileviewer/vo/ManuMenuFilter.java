package com.xerofox.fileviewer.vo;

abstract public class ManuMenuFilter implements MenuFilter {
    private String text;

    public ManuMenuFilter(String text) {
        this.text = text;
    }

    @Override
    public String getText() {
        return text;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ManuMenuFilter that = (ManuMenuFilter) o;

        return text != null ? text.equals(that.text) : that.text == null;
    }

    @Override
    public int hashCode() {
        return text != null ? text.hashCode() : 0;
    }
}
