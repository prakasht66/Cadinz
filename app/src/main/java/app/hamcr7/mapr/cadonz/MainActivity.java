package app.hamcr7.mapr.cadonz;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;

import androidx.fragment.app.Fragment;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.view.MenuItem;



public class MainActivity extends AppCompatActivity {


    public Fragment fragment = null;
    DrawerLayout drawerLayout;
    public  String TAG_FRAGMENT="TAG_FRAGMENT";
    public static final String MY_PREFS_NAME = "MyPrefsFile";
    public SharedPreferences.Editor editor;
    NavigationView navigationView;
    SharedPreferences mSharedPreferences=null;
    Toolbar toolbar;




    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSharedPreferences = getSharedPreferences(MY_PREFS_NAME, Context.MODE_PRIVATE);
        editor = mSharedPreferences.edit();
        editor.putBoolean("firstTime",true);
        editor.apply();

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        TabLayout tabLayout =  findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("Code"));
        tabLayout.addTab(tabLayout.newTab().setText("Videos"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final ViewPager viewPager = findViewById(R.id.view_pager);
        TabsAdapter tabsAdapter = new TabsAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(tabsAdapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }
            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


        navigationView = findViewById(R.id.nav_view);
        final DrawerLayout drawerLayout =  findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        if (savedInstanceState==null)
        {
            TAG_FRAGMENT="Home";
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new HomeFragment(),TAG_FRAGMENT).addToBackStack("test").commit();

            navigationView.setCheckedItem(R.id.home);
        }

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                switch (menuItem.getItemId()) {

                    case R.id.nav_home:
                        fragment = getSupportFragmentManager().findFragmentByTag("Home");
                        editor.putBoolean("firstTime",false );
                        editor.commit();
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
                        break;
                    case R.id.nav_about:
                        fragment = new AboutUs();
                        TAG_FRAGMENT="About";
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment,TAG_FRAGMENT).addToBackStack(TAG_FRAGMENT).commit();
                        break;
                    case R.id.nav_feedback:
                        fragment = new Feedback();
                        TAG_FRAGMENT="Feedback";
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment,TAG_FRAGMENT).addToBackStack(TAG_FRAGMENT).commit();
                        break;
                    case R.id.nav_share:
                        ShareApplication();
                        break;
                    case R.id.nav_rateUs:
                        RateApplication();//
                        break;

                }
                drawerLayout.closeDrawer(GravityCompat.START);

                return true;
            }
        });
    }


    private void RateApplication()
    {

        Uri uri = Uri.parse("market://details?id=" + getPackageName());
        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
        // To count with Play market backstack, After pressing back button,
        // to taken back to our application, we need to add following flags to intent.
        goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET |
                Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        try {
            startActivity(goToMarket);
        } catch (ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://play.google.com/store/apps/details?id=" + getPackageName())));
        }
    }
    private void ShareApplication() {

        try {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, "My application name");
            String shareMessage= "\nLet me recommend you this application\n\n";
            shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID +"\n\n";
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
            startActivity(Intent.createChooser(shareIntent, "choose one"));
        } catch(Exception e) {
            //e.toString();
        }
    }


    @Override
    public void onBackPressed() {
        drawerLayout = findViewById(R.id.drawer_layout);
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        else {

            if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
                Fragment myFragment = getSupportFragmentManager().findFragmentByTag("Home");
                if (myFragment != null && myFragment.isVisible()) {
                    finish();
                    //super.onBackPressed();
                }
                else {
                    getSupportFragmentManager().popBackStack("test",0);
                    navigationView.setCheckedItem(R.id.nav_home);
                }

            } else {
                super.onBackPressed();
            }
        }
    }
    @Override
    public void onResume(){
        super.onResume();

        editor.putBoolean("firstTime",false );
        editor.commit();
    }


    @Override
    protected void onStart() {
        super.onStart();


    }




}
