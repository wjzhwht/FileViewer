package com.xerofox.fileviewer.ui.part;

import android.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.xerofox.fileviewer.R;
import com.xerofox.fileviewer.api.OnItemClickListener;
import com.xerofox.fileviewer.databinding.TowerPartItemBinding;
import com.xerofox.fileviewer.ui.common.DataBoundListAdapter;
import com.xerofox.fileviewer.util.Objects;
import com.xerofox.fileviewer.vo.TowerPart;

public class TowerPartAdapter extends DataBoundListAdapter<TowerPart, TowerPartItemBinding> {

    private final OnItemClickListener<TowerPart> onItemClickListener;

    public TowerPartAdapter(OnItemClickListener<TowerPart> onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    protected TowerPartItemBinding createBinding(ViewGroup parent) {
        TowerPartItemBinding binding = DataBindingUtil
                .inflate(LayoutInflater.from(parent.getContext()),
                        R.layout.tower_part_item, parent, false);
        binding.getRoot().setOnClickListener(v -> {
            TowerPart part = binding.getPart();
            if (part != null && onItemClickListener != null) {
                onItemClickListener.onClick(part);
            }
        });
        return binding;
    }

    @Override
    protected void bind(TowerPartItemBinding binding, TowerPart part) {
        binding.setPart(part);
    }

    @Override
    protected boolean areItemsTheSame(TowerPart oldItem, TowerPart newItem) {
        return Objects.equals(oldItem.getId(), newItem.getId());
    }

    @Override
    protected boolean areContentsTheSame(TowerPart oldItem, TowerPart newItem) {
        return Objects.equals(oldItem.getPartNo(), newItem.getPartNo())
                && oldItem.getSegStr() == newItem.getSegStr();
    }
}
