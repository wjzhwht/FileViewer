package com.xerofox.fileviewer.ui.search;

import android.databinding.DataBindingComponent;
import android.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;

import com.xerofox.fileviewer.R;
import com.xerofox.fileviewer.databinding.ProjectItemBinding;
import com.xerofox.fileviewer.databinding.TowerTypeItemBinding;
import com.xerofox.fileviewer.vo.Project;

import java.util.List;

class ProjectAdapter extends BaseExpandableListAdapter {
    private List<Project> projects;

    public void setProjects(List<Project> projects) {
        this.projects = projects;
    }

    private final android.databinding.DataBindingComponent dataBindingComponent;

    public ProjectAdapter(DataBindingComponent dataBindingComponent) {
        this.dataBindingComponent = dataBindingComponent;
    }

    @Override
    public int getGroupCount() {
        return projects == null ? 0 : projects.size();
    }

    @Override
    public int getChildrenCount(int i) {
        return projects.get(i).getTowerTypeArr().size();
    }

    @Override
    public Object getGroup(int i) {
        return projects.get(i);
    }

    @Override
    public Object getChild(int i, int i1) {
        return projects.get(i).getTowerTypeArr().get(i1);
    }

    @Override
    public long getGroupId(int i) {
        return projects.get(i).getId();
    }

    @Override
    public long getChildId(int i, int i1) {
        return projects.get(i).getTowerTypeArr().get(i1).getId();
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int i, boolean b, View convertView, ViewGroup group) {
        View view;
        GroupHolder groupholder;
        if (convertView != null) {
            view = convertView;
            groupholder = (GroupHolder) view.getTag();
        } else {
            LayoutInflater inflater = LayoutInflater.from(group.getContext());
            ProjectItemBinding binding = DataBindingUtil.inflate(inflater, R.layout.project_item, group, false, dataBindingComponent);
            view = binding.getRoot();
            groupholder = new GroupHolder(binding);
            view.setTag(groupholder);
        }
        groupholder.binding.setProject(projects.get(i));
        return view;
    }

    @Override
    public View getChildView(int i, int i1, boolean b, View convertView, ViewGroup group) {
        View view;
        ChildHolder childHolder;
        if (convertView != null) {
            view = convertView;
            childHolder = (ChildHolder) view.getTag();
        } else {
            LayoutInflater inflater = LayoutInflater.from(group.getContext());
            TowerTypeItemBinding binding = DataBindingUtil.inflate(inflater, R.layout.tower_type_item, group, false, dataBindingComponent);
            view = binding.getRoot();
            childHolder = new ChildHolder(binding);
            view.setTag(childHolder);
        }
        childHolder.binding.setTower(projects.get(i).getTowerTypeArr().get(i1));
        return view;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }

    static class GroupHolder {
        ProjectItemBinding binding;

        GroupHolder(ProjectItemBinding binding) {
            this.binding = binding;
        }
    }

    static class ChildHolder {
        TowerTypeItemBinding binding;

        ChildHolder(TowerTypeItemBinding binding) {
            this.binding = binding;
        }
    }
}
