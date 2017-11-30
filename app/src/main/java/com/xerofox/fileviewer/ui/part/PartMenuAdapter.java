package com.xerofox.fileviewer.ui.part;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import com.xero.dropdownmenu.MenuAdapter;
import com.xerofox.fileviewer.R;
import com.xerofox.fileviewer.vo.MenuFilter;

import java.util.List;

public class PartMenuAdapter implements MenuAdapter {

    private final Context context;
    private List<String> titles;
    private List<List<MenuFilter>> data;
    private OnFilterClickListener onFilterClickListener;

    public PartMenuAdapter(Context context, List<String> titles, List<List<MenuFilter>> data, OnFilterClickListener onFilterClickListener) {
        this.context = context;
        this.titles = titles;
        this.data = data;
        this.onFilterClickListener = onFilterClickListener;
    }

    @Override
    public int getMenuCount() {
        return titles == null ? 0 : titles.size();
    }

    @Override
    public String getMenuTitle(int position) {
        return titles.get(position);
    }

    @Override
    public View getView(int position, FrameLayout parentContainer) {
        RecyclerView recyclerView = (RecyclerView) LayoutInflater.from(context).inflate(R.layout.recycler_view, parentContainer, false);
        MenuFilterAdapter adapter = new MenuFilterAdapter(onFilterClickListener, position);
        adapter.replace(data.get(position));
        recyclerView.setAdapter(adapter);
        return recyclerView;
    }
}
