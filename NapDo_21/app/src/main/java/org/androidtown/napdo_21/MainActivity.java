package org.androidtown.napdo_21;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class MainActivity extends AppCompatActivity {

    public static FragmentManager mFragmentManager;

    public static Context mContext;
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    private TabLayout tabLayout;

    final int[] selectedIcons = new int[] { R.drawable.select_record, R.drawable.select_home, R.drawable.select_coupon };
    final int[] unselectedIcons = new int[] { R.drawable.unselect_record, R.drawable.unselect_home, R.drawable.unselect_coupon };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // -----------------------------------------------------
        // ejeong - Driving record, Coupon item의 갯수를 초기화한다.
        Utility.setCouponNum();
        Utility.setRecordNum();
        // -----------------------------------------------------

        startActivity(new Intent(this, SplashActivity.class)); //로딩화면(Splash) 띄우기

        mContext = this;
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setCurrentItem(1, false);

        /* Tab의 등록 및 텍스트색과 아이콘 지정*/
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
        tabLayout.setOnTabSelectedListener(onTabSelectedListener(mViewPager));

        tabLayout.getTabAt(0).setIcon(unselectedIcons[0]);
        tabLayout.getTabAt(1).setIcon(selectedIcons[1]);
        tabLayout.getTabAt(2).setIcon(unselectedIcons[2]);

    } // onCreate()

    private TabLayout.OnTabSelectedListener onTabSelectedListener(final ViewPager viewPager) {

        return new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int select = tab.getPosition();

                for(int i=0;i<3;i++) {
                    if (i != select)
                        tabLayout.getTabAt(i).setIcon(unselectedIcons[i]);
                    else
                        tabLayout.getTabAt(i).setIcon(selectedIcons[i]);
                } // for
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        };
    } // TabLayout.OnTabSelectedListener

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() { }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            return rootView;
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        private Fragment fragment = null;
        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }


        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            switch(position) {
                case 0:
                    fragment = new RECORD();
                    break;
                case 1:
                    fragment = new HOME();
                    break;
                case 2:
                    fragment = new COUPON();
                    break;
            }
            return fragment;
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "주행기록";
                case 1:
                    return "홈";
                case 2:
                    return "선물함";
            }
            return null;
        }

    } // SectionsPagerAdapter class
}
