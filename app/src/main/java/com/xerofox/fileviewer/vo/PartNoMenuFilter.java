package com.xerofox.fileviewer.vo;

public class PartNoMenuFilter implements MenuFilter {

    private String partNo;

    public PartNoMenuFilter(String partNo) {
        this.partNo = partNo;
    }

    @Override
    public String getText() {
        return null;
    }

    @Override
    public boolean match(TowerPart part) {
        return part.getPartNo().contains(partNo);
    }
}
