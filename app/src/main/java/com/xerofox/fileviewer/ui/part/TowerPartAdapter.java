package com.xerofox.fileviewer.ui.part;

import android.databinding.DataBindingComponent;
import android.databinding.DataBindingUtil;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.xerofox.fileviewer.R;
import com.xerofox.fileviewer.api.FileHelper;
import com.xerofox.fileviewer.api.OnItemClickListener;
import com.xerofox.fileviewer.databinding.TowerPartItemBinding;
import com.xerofox.fileviewer.ui.common.DataBoundListAdapter;
import com.xerofox.fileviewer.util.Objects;
import com.xerofox.fileviewer.vo.TowerPart;

import java.io.File;

public class TowerPartAdapter
        extends DataBoundListAdapter<TowerPart, TowerPartItemBinding> {

    private final DataBindingComponent dataBindingComponent;
    private final OnItemClickListener<TowerPart> onItemClickListener;
    private String rootPath;

    public void setRootPath(String rootPath) {
        this.rootPath = rootPath;
        notifyDataSetChanged();
    }

    public TowerPartAdapter(DataBindingComponent dataBindingComponent,
                            OnItemClickListener<TowerPart> onItemClickListener) {
        this.dataBindingComponent = dataBindingComponent;
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    protected TowerPartItemBinding createBinding(ViewGroup parent) {
        TowerPartItemBinding binding = DataBindingUtil
                .inflate(LayoutInflater.from(parent.getContext()),
                        R.layout.tower_part_item, parent, false,
                        dataBindingComponent);
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
        if (!TextUtils.isEmpty(rootPath)) {
            String url = rootPath + part.getPartFile().getFileType() + FileHelper.PART_FOLDER_SUFFIX + File.separator
                    + part.getPartFile().getName();
            binding.setUrl(url);

        }
    }

    @Override
    protected boolean areItemsTheSame(TowerPart oldItem, TowerPart newItem) {
        return Objects.equals(oldItem.getSegNo(), newItem.getSegNo());
    }

    @Override
    protected boolean areContentsTheSame(TowerPart oldItem, TowerPart newItem) {
        return Objects.equals(oldItem.getSegNo(), newItem.getSegNo())
                && oldItem.getPartFile() == newItem.getPartFile();
    }
}
