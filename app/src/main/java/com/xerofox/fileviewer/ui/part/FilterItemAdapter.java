package com.xerofox.fileviewer.ui.part;

import android.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.xerofox.fileviewer.R;
import com.xerofox.fileviewer.databinding.FilterItemBinding;
import com.xerofox.fileviewer.ui.common.DataBoundListAdapter;
import com.xerofox.fileviewer.util.Objects;
import com.xerofox.fileviewer.vo.Filter;

public class FilterItemAdapter extends DataBoundListAdapter<Filter.Item, FilterItemBinding> {

    @Override
    protected FilterItemBinding createBinding(ViewGroup parent) {
        FilterItemBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.filter_item, parent, false);
        binding.checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            binding.getItem().setSelected(isChecked);
            binding.checkBox.setChecked(isChecked);
        });
        return binding;
    }

    @Override
    protected void bind(FilterItemBinding binding, Filter.Item item) {
        binding.setItem(item);
    }

    @Override
    protected boolean areItemsTheSame(Filter.Item oldItem, Filter.Item newItem) {
        return Objects.equals(oldItem.getName(), newItem.getName())
                && oldItem.isSelected() == newItem.isSelected();
    }

    @Override
    protected boolean areContentsTheSame(Filter.Item oldItem, Filter.Item newItem) {
        return Objects.equals(oldItem.getName(), newItem.getName())
                && oldItem.isSelected() == newItem.isSelected();
    }
}
