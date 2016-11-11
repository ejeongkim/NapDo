package org.androidtown.mergetutorial;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

/**
 * Created by seohyun on 2016-11-08.
 */
public class MainActivity extends AppCompatActivity {

    public static FragmentManager mFragmentManager;

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    private TabLayout mTabLayout;
    private int[] tabIcons = { android.R.drawable.ic_menu_agenda, android.R.drawable.ic_menu_slideshow, android.R.drawable.ic_menu_call };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setCurrentItem(1); // home으로 첫화면 지정

        /* Tab의 등록 및 텍스트색과 아이콘 지정*/
        mTabLayout = (TabLayout) findViewById(R.id.tabs);
        mTabLayout.setupWithViewPager(mViewPager);
        mTabLayout.setTabTextColors(R.color.colorText, R.color.colorPrimaryDark);
        setupTabIcons();
    }

    // 각 탭의 아이콘 Image 설정
    private void setupTabIcons() {
        mTabLayout.getTabAt(0).setIcon(tabIcons[0]);
        mTabLayout.getTabAt(1).setIcon(tabIcons[1]);
        mTabLayout.getTabAt(2).setIcon(tabIcons[2]);
    }

    /* 11.05 ejeong */
    @Override
    public void onBackPressed() {
        //Fragment fragment = new HOME();
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setCurrentItem(1); // home으로 첫화면 지정

        //getFragmentManager().beginTransaction().replace(R.id.fragment_home, fragment).addToBackStack(null).commit();
        //super.onBackPressed();

        int count = mFragmentManager.getBackStackEntryCount();

        if (count == 0) {
            Toast.makeText(this, Integer.toString(count), Toast.LENGTH_SHORT).show();
            return;//super.onBackPressed();
            //additional code
        } else {
            mFragmentManager.popBackStack();
   //         mFragmentManager.beginTransaction().add(R.id.fragment_home, mFragmentHomeResult).addToBackStack(null).commit();
        }
    }
}
