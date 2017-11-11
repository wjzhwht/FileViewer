package com.xerofox.fileviewer.ui.viewer;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.xerofox.fileviewer.api.FileHelper;
import com.xerofox.fileviewer.ui.common.PhotoView;
import com.xerofox.fileviewer.vo.TowerPart;

import java.io.File;
import java.util.List;

public class ViewerAdapter extends PagerAdapter {
    private List<TowerPart> data;
    private String path;

    public ViewerAdapter(String path, List<TowerPart> data) {
        this.path = path;
        this.data = data;
    }

    void setPath(String path) {
        this.path = path;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        PhotoView view = new PhotoView(container.getContext());
        view.enable();
        view.setScaleType(ImageView.ScaleType.FIT_CENTER);
        TowerPart part = data.get(position);
        String url = path + part.getPartFile().getFileType() + FileHelper.PART_FOLDER_SUFFIX + File.separator
                + part.getPartFile().getName();
        Glide.with(container.getContext()).load(url).into(view);
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
}
