package com.training.ojolusertraining.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.training.ojolusertraining.R;
import com.training.ojolusertraining.fragment.CompleteFragment;
import com.training.ojolusertraining.fragment.ProcessFragment;
import com.training.ojolusertraining.fragment.ProsesFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

public class History extends AppCompatActivity {

    @BindView(R.id.tablayout)
    TabLayout tablayout;
    @BindView(R.id.pager)
    ViewPager pager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        ButterKnife.bind(this);

        //membuat nama tabnya
        tablayout.addTab(tablayout.newTab().setText("Proses"));
        tablayout.addTab(tablayout.newTab().setText("Complete"));
        tablayout.addTab(tablayout.newTab().setText("Test1"));
        tablayout.addTab(tablayout.newTab().setText("Tesst2"));

        //dari masing tab layout itu ada classnya
        PagerAdapter adapter = new CustomPager(getSupportFragmentManager());

        pager.setAdapter(adapter);

        //kalau pager di geser tab nya juga ikut ganti
        pager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tablayout));



        //tabnya diklik fragment juga ganti
        tablayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                pager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });



    }

    private class CustomPager extends FragmentStatePagerAdapter {
        public CustomPager(FragmentManager supportFragmentManager) {
            super(supportFragmentManager);
        }

        @Override
        public Fragment getItem(int position) {
            if(position == 0){

                return new ProcessFragment();

            }
            else if(position == 1){

                return new CompleteFragment();

            }

            else if(position == 2){

                return new ProcessFragment();

            }

            else if(position == 3){

                return new CompleteFragment();

            }

            return null;
        }

        @Override
        public int getCount() {
            return 4;
        }
    }
}
