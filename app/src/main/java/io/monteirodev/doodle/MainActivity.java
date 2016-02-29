package io.monteirodev.doodle;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MainActivity extends AppCompatActivity {
    public static MainActivity instance;
    private FragmentDoodle doodleOne;
    private FragmentDoodle doodleTwo;

    private boolean hasDoodleOne;
    private boolean hasDoodleTwo;
    private TabLayout allTabs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        instance=this;
        getAllWidgets();
        bindWidgetsWithAnEvent();
        setupTabLayout();
    }
    public static MainActivity getInstance() {
        return instance;
    }
    private void getAllWidgets() {
        allTabs = (TabLayout) findViewById(R.id.tabs);
    }
    private void setupTabLayout() {
        hasDoodleOne = false;
        hasDoodleTwo = false;
        doodleOne = new FragmentDoodle();
        doodleTwo = new FragmentDoodle();
        allTabs.addTab(allTabs.newTab().setText("Doodle One"),true);
        allTabs.addTab(allTabs.newTab().setText("Doodle Two"));
    }
    private void bindWidgetsWithAnEvent()
    {
        allTabs.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                setCurrentTabFragment(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
    }
    private void setCurrentTabFragment(int tabPosition)
    {
        switch (tabPosition)
        {
            case 1 :
                replaceFragment(doodleTwo,doodleOne,tabPosition);
                hasDoodleTwo = true;
                break;
            default:
                replaceFragment(doodleOne,doodleTwo,tabPosition);
                hasDoodleOne = true;
                break;
        }
    }
    public void replaceFragment(Fragment fragment,Fragment hideFragment,int tabPosition) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        if (tabPosition == 0 && !hasDoodleOne) {
            ft.add(R.id.frame_container, fragment);
        } else if (tabPosition == 1 && !hasDoodleTwo) {
            ft.add(R.id.frame_container, fragment);
        } else if (hasDoodleOne && hasDoodleTwo){
            ft.hide(hideFragment);
            ft.show(fragment);
        } else {
            //if none of the cases. discard and show fragment
            ft.replace(R.id.frame_container, fragment);
        }
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ft.commit();
    }
    public void paintClicked(View view){
        FragmentDoodle instanceFragment =
                (FragmentDoodle)getSupportFragmentManager().findFragmentById(R.id.frame_container);
        if (instanceFragment.getId() == doodleTwo.getId()) doodleTwo.paintClicked(view);
        else doodleOne.paintClicked(view);
    }
}