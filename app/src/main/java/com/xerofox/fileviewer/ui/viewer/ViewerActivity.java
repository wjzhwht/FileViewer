package com.xerofox.fileviewer.ui.viewer;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.xerofox.fileviewer.R;
import com.xerofox.fileviewer.api.FileHelper;
import com.xerofox.fileviewer.vo.Task;
import com.xerofox.fileviewer.vo.TowerPart;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ViewerActivity extends AppCompatActivity {
    private static final String ARG_TOWER_PARTS = "tower parts";
    private static final String ARG_TASK = "task";
    private static final String ARG_POSITION = "position";
    //    File directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
    File directory = Environment.getExternalStorageDirectory();

    public static Intent newIntent(Context context, Task task, ArrayList<TowerPart> towerParts, int position) {
        Intent intent = new Intent(context, ViewerActivity.class);
        intent.putExtra(ARG_TOWER_PARTS, towerParts);
        intent.putExtra(ARG_TASK, task);
        intent.putExtra(ARG_POSITION, position);
        return intent;
    }

    private List<TowerPart> getTowerParts() {
        return getIntent().getParcelableArrayListExtra(ARG_TOWER_PARTS);
    }

    private Task getTask() {
        return getIntent().getParcelableExtra(ARG_TASK);
    }

    private int getPosition() {
        return getIntent().getIntExtra(ARG_POSITION, 0);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewer);
        setSupportActionBar(findViewById(R.id.tool_bar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ViewPager viewPager = findViewById(R.id.pager);
        String path = directory.getPath() + File.separator
                + FileHelper.PATH_ROOT + File.separator
                + FileHelper.TASK_PRIFIX + getTask().getId() + FileHelper.TASK_SEPARATION + getTask().getName() + File.separator;
        ViewerAdapter adapter = new ViewerAdapter(path, getTowerParts());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                setIndex(position);
            }
        });
        adapter.notifyDataSetChanged();
        viewPager.setCurrentItem(getPosition());
        setIndex(getPosition());
    }

    private void setIndex(int position) {
        TowerPart part = getTowerParts().get(position);
        String title = getString(R.string.image_index,
                getTowerParts().size(),
                position + 1,
                part.getPartNo(),
                part.getMaterialMark(),
                part.getSpecification(),
                part.getManuString()
        );
        getSupportActionBar().setTitle(title);
    }
}
