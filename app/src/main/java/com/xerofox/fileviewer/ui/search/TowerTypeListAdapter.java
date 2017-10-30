package com.xerofox.fileviewer.ui.search;

import android.databinding.DataBindingComponent;
import android.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.xerofox.fileviewer.R;
import com.xerofox.fileviewer.api.OnItemClickListener;
import com.xerofox.fileviewer.databinding.TowerTypeItemBinding;
import com.xerofox.fileviewer.ui.common.DataBoundListAdapter;
import com.xerofox.fileviewer.util.Objects;
import com.xerofox.fileviewer.vo.TowerType;

/**
 * A RecyclerView adapter for {@link TowerType} class.
 */
public class TowerTypeListAdapter extends DataBoundListAdapter<TowerType, TowerTypeItemBinding> {
    private final DataBindingComponent dataBindingComponent;
    private OnItemClickListener<TowerType> onItemClickListener;

    public TowerTypeListAdapter(DataBindingComponent dataBindingComponent,
                                OnItemClickListener<TowerType> onItemClickListener) {
        this.dataBindingComponent = dataBindingComponent;
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    protected TowerTypeItemBinding createBinding(ViewGroup parent) {
        TowerTypeItemBinding binding = DataBindingUtil
                .inflate(LayoutInflater.from(parent.getContext()), R.layout.tower_type_item,
                        parent, false, dataBindingComponent);
        binding.getRoot().setOnClickListener(v -> {
            TowerType tower = binding.getTower();
            if (tower != null && onItemClickListener != null) {
                onItemClickListener.onClick(tower);
            }
        });
        return binding;
    }

    @Override
    protected void bind(TowerTypeItemBinding binding, TowerType towerType) {
        binding.setTower(towerType);
    }

    @Override
    protected boolean areItemsTheSame(TowerType oldItem, TowerType newItem) {
        return Objects.equals(oldItem.getId(), newItem.getId()) &&
                Objects.equals(oldItem.getName(), newItem.getName());
    }

    @Override
    protected boolean areContentsTheSame(TowerType oldItem, TowerType newItem) {
        return Objects.equals(oldItem.getAttachFileFlag(), newItem.getAttachFileFlag()) &&
                oldItem.getPartArr() == newItem.getPartArr();
    }
}
