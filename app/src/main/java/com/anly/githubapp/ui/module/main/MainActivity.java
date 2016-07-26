package com.anly.githubapp.ui.module.main;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.anly.githubapp.GithubApplication;
import com.anly.githubapp.R;
import com.anly.githubapp.common.config.MainMenuConfig;
import com.anly.githubapp.di.HasComponent;
import com.anly.githubapp.di.component.DaggerMainComponent;
import com.anly.githubapp.di.component.MainComponent;
import com.anly.githubapp.di.module.ActivityModule;
import com.anly.githubapp.ui.base.BaseActivity;
import com.anly.githubapp.ui.module.main.adapter.MainMenuListAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity implements HasComponent<MainComponent> {

    @BindView(R.id.content_frame)
    FrameLayout mContentFrame;

    @BindView(R.id.left_menu)
    ListView mLeftMenuListView;

    @BindView(R.id.left_drawer)
    LinearLayout mLeftDrawer;

    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        initViews();
    }

    private void initViews() {
        MainMenuListAdapter adapter = new MainMenuListAdapter(this, MainMenuConfig.MENUS);
        mLeftMenuListView.setAdapter(adapter);

        mLeftMenuListView.setOnItemClickListener(mMenuItemClickListener);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_drawer);
    }

    private AdapterView.OnItemClickListener mMenuItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
            MainMenuConfig.MainMenu menu = (MainMenuConfig.MainMenu) adapterView.getAdapter().getItem(position);
            selectItem(menu, position);
        }
    };

    private void selectItem(MainMenuConfig.MainMenu menu, int pos) {
        // Create a new fragment and specify the planet to show based on position
        Fragment fragment = Fragment.instantiate(this, menu.fragmentClass);

        // Insert the fragment by replacing any existing fragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.content_frame, fragment)
                .commit();

        // Highlight the selected item, update the title, and close the drawer
        mLeftMenuListView.setItemChecked(pos, true);
        setTitle(menu.labelResId);
        mDrawerLayout.closeDrawer(mLeftDrawer);
    }

    @Override
    public MainComponent getComponent() {
        return DaggerMainComponent.builder()
                .applicationComponent(GithubApplication.get(this).getComponent())
                .activityModule(new ActivityModule(this))
                .build();
    }
}
