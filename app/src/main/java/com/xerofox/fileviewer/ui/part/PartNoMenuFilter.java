package com.xerofox.fileviewer.ui.part;

import com.xerofox.fileviewer.vo.MenuFilter;
import com.xerofox.fileviewer.vo.TowerPart;

public class PartNoMenuFilter implements MenuFilter {
    private String text;

    public PartNoMenuFilter(String text) {
        this.text = text;
    }

    @Override
    public String getText() {
        return text;
    }

    @Override
    public boolean match(TowerPart part) {
        return part.getPartNo().contains(text);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PartNoMenuFilter that = (PartNoMenuFilter) o;

        return text != null ? text.equals(that.text) : that.text == null;
    }

    @Override
    public int hashCode() {
        return text != null ? text.hashCode() : 0;
    }
}
