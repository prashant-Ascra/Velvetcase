package com.velvetcase.app.material;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.RecyclerView;

import android.transition.Explode;
import android.transition.Fade;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.velvetcase.app.material.DBHandlers.WishListDBHelper;

import com.velvetcase.app.material.adapters.CustomListAdapter;
import com.velvetcase.app.material.fragments.CreateFragment;
import com.velvetcase.app.material.fragments.DesignerFragment;
import com.velvetcase.app.material.fragments.Discover;
import com.velvetcase.app.material.fragments.FreeQuateFragment;
import com.velvetcase.app.material.fragments.SavesLooksFragment;
import com.velvetcase.app.material.fragments.StaticTemplateScreen;
import com.velvetcase.app.material.util.SessionManager;
import com.facebook.FacebookButtonBase;

import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;


public class MainActivity extends ActionBarActivity implements View.OnClickListener, ViewPager.OnPageChangeListener {
    private Toolbar toolbar;                              // Declaring the Toolbar Object
    DrawerLayout Drawer;                                  // Declaring DrawerLayout
    ActionBarDrawerToggle mDrawerToggle;
    RelativeLayout reclyclerview_wrapper;
FacebookButtonBase fb;
    //First We Declare Titles And Icons For Our Navigation Drawer List View
    //This Icons And Titles Are holded in an Array as you can see

    String TITLES[] = {"DISCOVER","CREATE A LOOK","ASK A FRIEND","GET A FREE QUATE","DESIGNERS","","My ACCOUNT","CART(2)","LOVE LIST","NOTIFICATION"};
    RecyclerView LeftDrawerRecyclerView;                           // Declaring RecyclerView
    RecyclerView.Adapter LeftDrawerRowAdapter;                        // Declaring Adapter For Recycler View
//    RecyclerView.LayoutManager LeftDrawerLayoutManager;
    static TextView pagecount;
    RelativeLayout animation_layout;
    ImageButton win_btn;
    Boolean winflag = true;
    Boolean Layoutflag = true;
    RecyclerView recList;
    CustomListAdapter ca;
    View strip_one, strip_two, strip_three;
    RelativeLayout rl_discover, rl_create, rl_freequate;
    TextView Spinner_two,Spinner_three;
    ViewPager pager;
    MyPagerAdapter adapter;

    TextView txt_discover,txt_products,txt_child_designers,txt_looks,txt_create_a_look,txt_ask_a_friend,txt_get_a_free_quote,txt_menu_designers,txt_my_account,txt_orders,txt_saveed_looks,txt_wishlist,txt_favourite_designs,txt_notifications,text_signout,text_try_and_buy;
    LinearLayout Left_Discover_wrapper,Left_Myaccount_wrapper;
    Boolean LeftDiscoverFlag = true;
    Boolean LeftMyaccountFlag = true;
    ImageView arrow1,arrow2;
    Spinner discoverSpinner;
    TextView discover_text;
    SessionManager session;
    WishListDBHelper dbhelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        discoverSpinner = (Spinner) findViewById(R.id.toolbar_spinner);
        session = new SessionManager(this);
        dbhelper=new WishListDBHelper(MainActivity.this);
        reclyclerview_wrapper = (RelativeLayout) findViewById(R.id.reclyclerview_wrapper);

        rl_discover = (RelativeLayout) findViewById(R.id.rl_discover);
        discover_text = (TextView) findViewById(R.id.txt_discover_title);
        rl_create = (RelativeLayout) findViewById(R.id.rl_create);
        rl_freequate = (RelativeLayout) findViewById(R.id.rl_freequaote);
        strip_one = findViewById(R.id.strip_one);
        strip_two = findViewById(R.id.strip_two);
        strip_three = findViewById(R.id.strip_three);

        pager = (ViewPager) findViewById(R.id.pager);
        pager.setOnPageChangeListener(this);
        rl_discover.setOnClickListener(this);
        rl_create.setOnClickListener(this);
        rl_freequate.setOnClickListener(this);

        adapter = new MyPagerAdapter(getSupportFragmentManager());
        pager.setAdapter(adapter);
        pager.setOffscreenPageLimit(3);
        pager.setOnPageChangeListener(this);

        toolbar = (Toolbar) findViewById(R.id.tool_bar); // Attaching the layout to the toolbar object
        setSupportActionBar(toolbar);                   // Setting toolbar as the ActionBar with setSupportActionBar() call
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        CustomSpinnerAdapter spinnerAdapter1 = new CustomSpinnerAdapter(this);
        spinnerAdapter1.addItems(getDiscoverSpinnerData());
        discoverSpinner.setAdapter(spinnerAdapter1);

        FindLeftDrawerUI();

        Drawer = (DrawerLayout) findViewById(R.id.my_drawer_layout);        // Drawer object Assigned to the view
        mDrawerToggle = new ActionBarDrawerToggle(this,Drawer,toolbar,R.string.openDrawer,R.string.closeDrawer){

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                // code here will execute once the drawer is opened( As I dont want anything happened whe drawer is
                // open I am not going to put anything here)
            }
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                // Code here will execute once drawer is closed
            }
        }; // Drawer Toggle Object Made
        Drawer.setDrawerListener(mDrawerToggle); // Drawer Listener set to the Drawer toggle
        mDrawerToggle.syncState();
        if (savedInstanceState == null) {
            selectItem(0);
            settabstrip(0);
        }
    }

    private void FindLeftDrawerUI() {
        txt_discover = (TextView) findViewById(R.id.txt_Discover);
        txt_products = (TextView) findViewById(R.id.txt_product);
        txt_child_designers = (TextView) findViewById(R.id.txt_child_designers);
        txt_looks= (TextView) findViewById(R.id.txt_looks);
        txt_create_a_look = (TextView) findViewById(R.id.txt_create_a_look);
        txt_ask_a_friend = (TextView) findViewById(R.id.txt_ask_a_friend);
        text_try_and_buy=(TextView)findViewById(R.id.txt_try_and_buy);
        txt_get_a_free_quote = (TextView) findViewById(R.id.txt_get_a_free_quote);
        txt_menu_designers = (TextView) findViewById(R.id.txt_menu_designers);
        txt_my_account = (TextView) findViewById(R.id.txt_myaccount);
        txt_orders = (TextView) findViewById(R.id.txt_orders);
        txt_saveed_looks = (TextView) findViewById(R.id.txt_saveed_looks);
        txt_wishlist = (TextView) findViewById(R.id.txt_wishlist);
        txt_favourite_designs = (TextView) findViewById(R.id.txt_favourite_designers);
        txt_notifications = (TextView) findViewById(R.id.txt_Notifications);
        Left_Discover_wrapper = (LinearLayout) findViewById(R.id.discover_child_wrapper);
        Left_Myaccount_wrapper = (LinearLayout) findViewById(R.id.myaccount_child_wrapper);
        text_signout=(TextView)findViewById(R.id.txt_signout);
        arrow1 = (ImageView) findViewById(R.id.arrow1);
        arrow2 = (ImageView) findViewById(R.id.arrow2);

        txt_discover.setOnClickListener(this);
        txt_products.setOnClickListener(this);
        txt_child_designers.setOnClickListener(this);
        txt_looks.setOnClickListener(this);
        txt_create_a_look.setOnClickListener(this);
        text_try_and_buy.setOnClickListener(this);
        txt_ask_a_friend.setOnClickListener(this);
        txt_get_a_free_quote.setOnClickListener(this);
        txt_menu_designers.setOnClickListener(this);
        txt_my_account.setOnClickListener(this);
        txt_orders.setOnClickListener(this);
        txt_saveed_looks.setOnClickListener(this);
        txt_wishlist.setOnClickListener(this);
        txt_favourite_designs.setOnClickListener(this);
        txt_notifications.setOnClickListener(this);
        Left_Discover_wrapper.setOnClickListener(this);
        Left_Myaccount_wrapper.setOnClickListener(this);
        arrow1.setOnClickListener(this);
        arrow2.setOnClickListener(this);
        text_signout.setOnClickListener(this);

    }

    public void SetDiscoverTitle(String title){
        discover_text.setText(title);
    }

    private View.OnTouchListener CreateOnTouch = new View.OnTouchListener() {
        public boolean onTouch(View v, MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                selectItem(1);
                settabstrip(1);
            }
            return true;
        }
    };
    private View.OnTouchListener FreeQuateOnTouch = new View.OnTouchListener() {
        public boolean onTouch(View v, MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                selectItem(2);
                settabstrip(2);
            }
            return true;
        }
    };
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

    public void switchToPage(int pos) {
//        try {
//            pager.setCurrentItem(pos, true);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        switch (pos) {
                case 0:
                    addFragmentInsideContainer(Discover.newInstance(),
                            Discover.TAG);
                    break;
                case 1:
                    addFragmentInsideContainer(CreateFragment.newInstance(),
                            CreateFragment.TAG);
                    break;
                case 2:
                    addFragmentInsideContainer(FreeQuateFragment.newInstance(),
                            FreeQuateFragment.TAG);
                    break;

                default:
                    addFragmentInsideContainer(Discover.newInstance(),
                            Discover.TAG);
                    break;
                    }
    }

    public void moveToPage(int pos) {
        callOnResumeOfRequiredFrag(pos);
        setTabSelection(pos);
    }

    private void setTabSelection(int pos) {
        try {
            if (pos == 0) {
                rl_discover.setSelected(true);
                rl_create.setSelected(false);
                rl_freequate.setSelected(false);
            } else if (pos == 1) {
                rl_discover.setSelected(false);
                rl_create.setSelected(true);
                rl_freequate.setSelected(false);

            } else if (pos == 2) {
                rl_discover.setSelected(false);
                rl_create.setSelected(false);
                rl_freequate.setSelected(true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void selectItem(int position) {
        switchToPage(0);
        settabstrip(0);
    }

    public void SpinnerItemSelection(int pos){
        switch (pos) {
            case 0:
                discoverSpinner.clearFocus();
                addFragmentInsideContainer(Discover.newInstance(),
                        Discover.TAG);
                settabstrip(0);
                discoverSpinner.setVisibility(View.GONE);
                break;
            case 1:
                addFragmentInsideContainer(DesignerFragment.newInstance(),
                        DesignerFragment.TAG);
                settabstrip(0);
                discoverSpinner.setVisibility(View.GONE);
                break;
            case 2:
                addFragmentInsideContainer(StaticTemplateScreen.newInstance(),
                        StaticTemplateScreen.TAG);
                settabstrip(0);
                discoverSpinner.setVisibility(View.GONE);
                break;
            default:
                addFragmentInsideContainer(Discover.newInstance(),
                        Discover.TAG);
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
    public void addFragmentInsideContainer(Fragment fm, String tag) {
        Drawer.closeDrawers();
        getSupportFragmentManager().beginTransaction().addToBackStack(null)
                .replace(R.id.content, fm, tag).commit();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void setupWindowAnimations() {
        Explode explode = new Explode();
        explode.setDuration(2000);
        getWindow().setExitTransition(explode);
        Fade fade = new Fade();
        fade.setDuration(2000);
        getWindow().setReenterTransition(fade);

    }

    private List<ListItem> createList(int size) {
        List<ListItem> result = new ArrayList<ListItem>();
        for (int i=1; i <= size; i++) {
            ListItem ci = new ListItem();
            ci.name = ListItem.NAME_PREFIX + i;
            result.add(ci);
        }
        return result;
    }


    public List<String>  getDiscoverSpinnerData() {
        List<String> list = new ArrayList<String>();
        list.add("Products");
        list.add("Designers");
        list.add("Looks");
        return list;
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onClick(View v) {
        try {
            if (v == rl_discover) {
                discoverSpinner.setVisibility(View.VISIBLE);
                discoverSpinner.performClick();
            } else if (v == rl_create) {
                switchToPage(1);
                settabstrip(1);
            } else if (v == rl_freequate) {
                switchToPage(2);
                settabstrip(2);
            }

            else if (v == txt_discover || v == arrow1) {
                if (LeftDiscoverFlag){
                    Left_Discover_wrapper.setVisibility(View.VISIBLE);
                    LeftDiscoverFlag = false;
                    arrow1.setImageResource(R.drawable.uparrow);
                }else{
                    Left_Discover_wrapper.setVisibility(View.GONE);
                    LeftDiscoverFlag = true;
                    arrow1.setImageResource(R.drawable.downarrow);
                }
            }
            else if (v == txt_my_account || v == arrow2) {
                if (LeftMyaccountFlag){
                    Left_Myaccount_wrapper.setVisibility(View.VISIBLE);
                    LeftMyaccountFlag = false;
                    arrow2.setImageResource(R.drawable.uparrow);
                }else{
                    Left_Myaccount_wrapper.setVisibility(View.GONE);
                    LeftMyaccountFlag = true;
                    arrow2.setImageResource(R.drawable.downarrow);
                }
            }
            else if (v == txt_products) {
                addFragmentInsideContainer(Discover.newInstance(),
                        Discover.TAG);
                settabstrip(0);
            }
            else if (v == txt_child_designers) {
                addFragmentInsideContainer(DesignerFragment.newInstance(),
                        DesignerFragment.TAG);
                settabstrip(0);
            }
            else if (v == txt_menu_designers) {
                addFragmentInsideContainer(DesignerFragment.newInstance(),
                        DesignerFragment.TAG);
                settabstrip(0);
            }
            else if (v == txt_looks) {
                addFragmentInsideContainer(StaticTemplateScreen.newInstance(),
                        StaticTemplateScreen.TAG);
                settabstrip(0);
            }
            else if (v == txt_create_a_look) {
                addFragmentInsideContainer(CreateFragment.newInstance(),
                        CreateFragment.TAG);
                settabstrip(1);
            }
            else if (v == txt_ask_a_friend) {
                Toast.makeText(MainActivity.this,"Coming Soon",Toast.LENGTH_LONG).show();
            }
            else if (v == txt_get_a_free_quote) {
                addFragmentInsideContainer(FreeQuateFragment.newInstance(),
                        FreeQuateFragment.TAG);
                settabstrip(2);
            }
            else if (v == txt_menu_designers) {
                Toast.makeText(MainActivity.this,"Coming Soon",Toast.LENGTH_LONG).show();
            }
            else if (v == txt_notifications) {
                Toast.makeText(MainActivity.this,"Coming Soon",Toast.LENGTH_LONG).show();
            } else if (v == txt_favourite_designs) {
                Drawer.closeDrawers();
                Intent i =new Intent(MainActivity.this, Favorite_Designer.class);
                startActivity(i);
            }else if (v == txt_orders) {
//                Drawer.closeDrawers();
//                Intent order_intent =new Intent(MainActivity.this, Order.class);
//                startActivity(order_intent);
            }
            else if (v == txt_saveed_looks) {
                Drawer.closeDrawers();
                addFragmentInsideContainer(SavesLooksFragment.newInstance(),
                        SavesLooksFragment.TAG);
                settabstrip(0);
            }
            else if (v == txt_wishlist) {
                Drawer.closeDrawers();
                Intent order_intent =new Intent(MainActivity.this, WishList.class);
                startActivity(order_intent);
            }else if (v == text_signout) {
                Drawer.closeDrawers();
                show_alert_logout();
            }else if (v == text_try_and_buy) {
               Toast.makeText(MainActivity.this,"Coming Soon",Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


   public  void  show_alert_logout(){
       AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
               MainActivity.this);

       // set title
       alertDialogBuilder.setTitle("Logout");

       // set dialog message
       alertDialogBuilder
               .setMessage("Click yes to exit!")
               .setCancelable(false)
               .setPositiveButton("Yes",new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog,int id) {
                       // if this button is clicked, close
                       // current activity


//                       if(dbhelper!=null) {
//                           dbhelper.deletetabledata();
//                       }

                       session.logoutUser(session.getUserID());
                        session.set_first_Time(true);

                       MainActivity.this.finish();
                   }
               })
               .setNegativeButton("No",new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog,int id) {
                       // if this button is clicked, just close
                       // the dialog box and do nothing
                       dialog.cancel();
                   }
               });

       // create alert dialog
       AlertDialog alertDialog = alertDialogBuilder.create();

       // show it
       alertDialog.show();

   }
    public void TransferNextActivtiy(){
        Intent i = new Intent(MainActivity.this,ActivityDetails.class);
        startActivity(i);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        switchToPage(position);
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

        private final String[] TITLES = { "DISCOVER", " CREATE ", "FREEQUOTE" };

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

    public void ReplaceFragment(int pos){
                 Drawer.closeDrawers();
        switch (pos){
            case 0:
//                switchToPage(0);
                addFragmentInsideContainer(Discover.newInstance(),
                        Discover.TAG);
                settabstrip(0);
                break;
            case 1:
                addFragmentInsideContainer(CreateFragment.newInstance(),
                        CreateFragment.TAG);
                settabstrip(1);
                break;
            case 2:
                Toast.makeText(MainActivity.this,"Comming Soon",Toast.LENGTH_LONG).show();
                break;
            case 3:
                addFragmentInsideContainer(FreeQuateFragment.newInstance(),
                        FreeQuateFragment.TAG);
                settabstrip(2);
                break;
            case 4:
                Toast.makeText(MainActivity.this,"Coming Soon",Toast.LENGTH_LONG).show();
                break;
            case 5:
                Toast.makeText(MainActivity.this,"Coming Soon",Toast.LENGTH_LONG).show();
                break;
            case 6:
                Toast.makeText(MainActivity.this,"Coming Soon",Toast.LENGTH_LONG).show();
                break;
            case 7:
                Toast.makeText(MainActivity.this,"Coming Soon",Toast.LENGTH_LONG).show();
                break;
            case 8:
                Toast.makeText(MainActivity.this,"Coming Soon",Toast.LENGTH_LONG).show();
                break;
        }
    }

    @Override
    public void onBackPressed() {

        int backStackEntryCount = getSupportFragmentManager()
                .getBackStackEntryCount();
        if (backStackEntryCount == 0) {
            new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                    .setTitleText("Exit App")
                    .setContentText("Are you sure you want to exit?")
                    .setCancelText("Cancel")
                    .setConfirmText("Ok")
                    .showCancelButton(true)
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {
                            MainActivity.this.finish();
                        }
                    })
                    .show();
//            new AlertDialog.Builder(this)
//                    .setMessage("Are you sure you want to exit?")
//                    .setCancelable(false)
//                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//                        public void onClick(DialogInterface dialog, int id) {
//                            MainActivity.this.finish();
//                        }
//                    })
//                    .setNegativeButton("No", null)
//                    .show();

        } else {
            super.onBackPressed();
        }

    }
}
