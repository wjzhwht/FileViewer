package com.xerofox.fileviewer.ui.part;

import android.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.xerofox.fileviewer.R;
import com.xerofox.fileviewer.databinding.MenuFilterItemBinding;
import com.xerofox.fileviewer.ui.common.DataBoundListAdapter;
import com.xerofox.fileviewer.util.Objects;
import com.xerofox.fileviewer.vo.MenuFilter;

public class MenuFilterAdapter extends DataBoundListAdapter<MenuFilter, MenuFilterItemBinding> {
    private OnFilterClickListener onFilterClickListener;
    private int position;

    public MenuFilterAdapter(OnFilterClickListener onFilterClickListener, int position) {
        this.onFilterClickListener = onFilterClickListener;
        this.position = position;
    }

    @Override
    protected MenuFilterItemBinding createBinding(ViewGroup parent) {

        MenuFilterItemBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.menu_filter_item, parent, false);
        binding.getRoot().setOnClickListener(v -> {
            if (onFilterClickListener != null) {
                onFilterClickListener.onFilterClick(position, binding.getMenuFilter());
            }
        });
        return binding;
    }

    @Override
    protected void bind(MenuFilterItemBinding binding, MenuFilter filter) {
        binding.setMenuFilter(filter);
    }

    @Override
    protected boolean areItemsTheSame(MenuFilter oldItem, MenuFilter newItem) {
        return Objects.equals(oldItem.getText(), newItem.getText());
    }

    @Override
    protected boolean areContentsTheSame(MenuFilter oldItem, MenuFilter newItem) {
        return Objects.equals(oldItem.getText(), newItem.getText());
    }
}
