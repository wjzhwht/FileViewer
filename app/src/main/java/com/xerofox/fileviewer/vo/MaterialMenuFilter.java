package com.xerofox.fileviewer.vo;

import android.text.TextUtils;

public class MaterialMenuFilter implements MenuFilter {
    private String text;

    public MaterialMenuFilter(String text) {
        this.text = text;
    }

    @Override
    public String getText() {
        return text;
    }

    @Override
    public boolean match(TowerPart part) {
        return TextUtils.equals(part.getMaterialMark(), text);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MaterialMenuFilter that = (MaterialMenuFilter) o;

        return text != null ? text.equals(that.text) : that.text == null;
    }

    @Override
    public int hashCode() {
        return text != null ? text.hashCode() : 0;
    }
}
