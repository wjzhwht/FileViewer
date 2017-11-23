package com.xerofox.fileviewer.ui.viewer;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.xerofox.fileviewer.R;
import com.xerofox.fileviewer.vo.TowerPart;
import com.xerofox.fileviewer.vo.TowerType;

import java.io.File;
import java.util.ArrayList;

public class ViewerActivity extends AppCompatActivity {
    private static final String ARG_TOWER_TPYE = "tower type";
    private static final String PATH_ROOT = "xerofox";
    private static final String PROJECT_PRIFIX = "n_";
    private static final String PROJECT_SEPARATION = "@";
    private static final String TOWER_PRIFIX = "n_";
    private static final String TOWER_SEPARATION = "#";
    private static final String TOWER_FILE_EXSTENSION = ".tpp";
    //    File directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
    File directory = Environment.getExternalStorageDirectory();

    public static Intent newIntent(Context context, ArrayList<TowerPart> towerParts) {
        Intent intent = new Intent(context, ViewerActivity.class);
        intent.putExtra(ARG_TOWER_TPYE, towerParts);
        return intent;
    }

    public TowerType getTowerType() {
        return getIntent().getParcelableExtra(ARG_TOWER_TPYE);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewer);
        ViewPager viewPager = findViewById(R.id.pager);
        String path = directory.getPath() + File.separator
                + PATH_ROOT + File.separator
                + PROJECT_PRIFIX + getTowerType().getProjectId() + PROJECT_SEPARATION + getTowerType().getProjectName() + File.separator
                + TOWER_PRIFIX + getTowerType().getId() + TOWER_SEPARATION + getTowerType().getName() + File.separator;
        ViewerAdapter adapter = new ViewerAdapter(path, getTowerType().getPartArr());
        viewPager.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }
}
