package org.androidtown.mergetutorial;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by ejeong on 2016-11-08.
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {

    // fragment = 선택된 fragment
    public Fragment fragment = null;
    //
    private Context context;

    //
    public SectionsPagerAdapter(FragmentManager fm, Context context_){
        super(fm); context = context_;
    }

    // 생성자 - 부모 클래스 받음
    public SectionsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    // 선택된 fragment 를 postion으로 받음
    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).
       // 선택된 아이콘은 왼쪽부터 순서대로 0, 1, 2
        switch(position) {
            case 0:
                fragment = new RECORD(); break;
            case 1:
                fragment = new HOME(); break;
            case 2:
                fragment = new COUPON(); break;
        }

        // change
       // ((Activity)context).getFragmentManager().beginTransaction().replace(R.id.fragment_home, fragment).addToBackStack(null).commit();

        return fragment;
    }

    // 아이콘의 갯수를 알려줌.
    @Override
    public int getCount() {
        // Show 3 total pages.
        return 3;
    }

    // 아이콘 position에 대한 아이콘 이름을 반환
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "RECORD";
            case 1:
                return "HOME";
            case 2:
                return "COUPON";
        }
        return null;
    }
}