package com.velvetcase.app.material;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;


import com.velvetcase.app.material.fragments.CreateFragment;
import com.velvetcase.app.material.fragments.Discover;
import com.velvetcase.app.material.fragments.FreeQuateFragment;

/**
 * Created by Akash on 4/14/2015.
 */
public class Raw_Activity extends ActionBarActivity  implements
        View.OnClickListener, ViewPager.OnPageChangeListener {

    RelativeLayout rl_featured, rl_all, rl_nearby;
    View strip_one, strip_two, strip_three;
    ViewPager pager;
    MyPagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.raw_main);

        rl_featured = (RelativeLayout) findViewById(R.id.rl_featured);
        rl_all = (RelativeLayout) findViewById(R.id.rl_all);
        rl_nearby = (RelativeLayout) findViewById(R.id.rl_nearby);
        strip_one = findViewById(R.id.strip_one);
        strip_two = findViewById(R.id.strip_two);
        strip_three = findViewById(R.id.strip_three);

        pager = (ViewPager) findViewById(R.id.pager);
        pager.setOnPageChangeListener(this);

        rl_featured.setOnClickListener(this);
        rl_all.setOnClickListener(this);
        rl_nearby.setOnClickListener(this);

        adapter = new MyPagerAdapter(getSupportFragmentManager());
        pager.setAdapter(adapter);
        pager.setOffscreenPageLimit(3);
        pager.setOnPageChangeListener(this);

    }

    public void switchToPage(int pos) {
        try {
            pager.setCurrentItem(pos, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void moveToPage(int pos) {
        callOnResumeOfRequiredFrag(pos);
        setTabSelection(pos);
    }

    public void settabstrip(int pos) {
        try {
            if (pos == 0) {
                strip_one.setVisibility(View.VISIBLE);
                strip_two.setVisibility(View.GONE);
                strip_three.setVisibility(View.GONE);
            }
            if (pos == 1) {
                strip_one.setVisibility(View.GONE);
                strip_two.setVisibility(View.VISIBLE);
                strip_three.setVisibility(View.GONE);
            }
            if (pos == 2) {
                strip_one.setVisibility(View.GONE);
                strip_two.setVisibility(View.GONE);
                strip_three.setVisibility(View.VISIBLE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void callOnResumeOfRequiredFrag(int pos) {
        try {
            if (pos == 0) {
                adapter.getFeatured_frag();
                settabstrip(0);
            }
            if (pos == 1) {
                adapter.getAll_frag();
                settabstrip(1);
            }
            if (pos == 2) {
                adapter.getNearby_frag();
                settabstrip(2);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public int getCurrentPage() {
        return pager.getCurrentItem();
    }


    private void setTabSelection(int pos) {
        try {

            if (pos == 0) {
                rl_featured.setSelected(true);
                rl_all.setSelected(false);
                rl_nearby.setSelected(false);
            } else if (pos == 1) {
                rl_featured.setSelected(false);
                rl_all.setSelected(true);
                rl_nearby.setSelected(false);

            } else if (pos == 2) {
                rl_featured.setSelected(false);
                rl_all.setSelected(false);
                rl_nearby.setSelected(true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        try {
            if (v == rl_featured) {

                switchToPage(0);
                settabstrip(0);
            } else if (v == rl_all) {
                switchToPage(1);
                settabstrip(1);
            } else if (v == rl_nearby) {

                switchToPage(2);
                settabstrip(2);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        settabstrip(position);
    }

    @Override
    public void onPageSelected(int position) {
        moveToPage(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }


    public class MyPagerAdapter extends FragmentPagerAdapter {


        private Discover featured_frag = new Discover();
        private CreateFragment all_frag = new CreateFragment();
        private FreeQuateFragment nearby_frag = new FreeQuateFragment();

        private Fragment mCurrentFragment;

        public Fragment getCurrentFragment() {
            return mCurrentFragment;
        }

        private String getFragmentTag(int pos){
            return "android:switcher:"+R.id.pager+":"+pos;
        }
        @Override
        public void setPrimaryItem(ViewGroup container, int position, Object object) {
            if (getCurrentFragment() != object) {
                this.mCurrentFragment = ((Fragment) object);
            }
            super.setPrimaryItem(container, position, object);
        }
        public Discover getFeatured_frag() {
            return featured_frag;
        }
        public void setFeatured_frag(Discover featured_frag) {
            this.featured_frag = featured_frag;
        }
        public CreateFragment getAll_frag() {
            return all_frag;
        }
        public void setAll_frag(CreateFragment all_frag) {
            this.all_frag = all_frag;
        }
        public FreeQuateFragment getNearby_frag() {
            return nearby_frag;
        }
        public void setNearby_frag(FreeQuateFragment nearby_frag) {
            this.nearby_frag = nearby_frag;
        }
        public MyPagerAdapter(android.support.v4.app.FragmentManager fm) {
            super(fm);
        }



        private final String[] TITLES = { "DISCOVER", " CREATE ", "FREEQUATE" };

        @Override
        public CharSequence getPageTitle(int position) {
            return TITLES[position];
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public int getItemPosition(Object object) {
            // TODO Auto-generated method stub
            return super.getItemPosition(object);
        }




        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0: // Fragment # 0 - This will show FirstFragment
                    return new Discover();
                case 1: // Fragment # 0 - This will show FirstFragment different
                    // title
                    return new CreateFragment();
                case 2: // Fragment # 1 - This will show SecondFragment
                    return new FreeQuateFragment();
                default:
                    return null;
            }
        }
    }


}
