package com.example.paintingsonline.Utils;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class SectionPagerAdapter extends FragmentPagerAdapter
{
    private final List<Fragment> mfragment = new ArrayList<>();

    public SectionPagerAdapter(FragmentManager fm)
    {
        super(fm);
    }

    @Override
    public Fragment getItem(int i)
    {
        return mfragment.get(i);
    }

    @Override
    public int getCount()
    {
        return mfragment.size();
    }

    public void addFragment(Fragment fragment)
    {
        mfragment.add(fragment);
    }
}
