package es.source.code.adapter;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

import java.util.List;

import es.source.code.fragment.BaseFragment;
import es.source.code.fragment.OrderedFoodFragment;

public class FragPagerAdapter extends FragmentPagerAdapter {

    private List<Fragment> fragments;
    private String[] tabTitle ;
    private int mchildCount;

    public FragPagerAdapter(FragmentManager fm, List<Fragment> fragments, String[] tabTitle) {
        super(fm);
        this.fragments = fragments;
        this.tabTitle = tabTitle;
    }

    @Override
    //该函数的目的为生成新的 Fragment 对象
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        if (mchildCount>0){
            //mchildCount--;
            return POSITION_NONE;
        }
        else return super.getItemPosition(object);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitle[position];
    }

    @Override
    public void notifyDataSetChanged() {
        //mchildCount = getCount();
        super.notifyDataSetChanged();
    }
}
