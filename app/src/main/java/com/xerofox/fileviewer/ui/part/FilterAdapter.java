package com.xerofox.fileviewer.ui.part;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.GridLayoutManager;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.xerofox.fileviewer.R;
import com.xerofox.fileviewer.databinding.FilterDialogFragmentItemBinding;
import com.xerofox.fileviewer.ui.common.DataBoundListAdapter;
import com.xerofox.fileviewer.util.Objects;
import com.xerofox.fileviewer.vo.Filter;

public class FilterAdapter extends DataBoundListAdapter<Filter, FilterDialogFragmentItemBinding> {
    private Context context;

    FilterAdapter(Context context) {
        this.context = context;
    }

    @Override
    protected FilterDialogFragmentItemBinding createBinding(ViewGroup parent) {
        return DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.filter_dialog_fragment_item, parent, false);
    }

    @Override
    protected void bind(FilterDialogFragmentItemBinding binding, Filter filter) {
        binding.setFilter(filter);
        FilterItemAdapter adapter = new FilterItemAdapter();
        binding.list.setLayoutManager(new GridLayoutManager(context, filter.getCountColumn()));
        binding.list.setAdapter(adapter);
        adapter.replace(filter.getItems());
    }

    @Override
    protected boolean areItemsTheSame(Filter oldItem, Filter newItem) {
        return Objects.equals(oldItem.getName(), newItem.getName())
                && oldItem.getType() == newItem.getType();
    }

    @Override
    protected boolean areContentsTheSame(Filter oldItem, Filter newItem) {
        return Objects.equals(oldItem.getName(), newItem.getName());
    }
}
