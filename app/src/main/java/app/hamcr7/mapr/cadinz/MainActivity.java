package app.hamcr7.mapr.cadinz;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private FirebaseAuth mAuth;
    String current_user_id;
    private FirebaseFirestore firebaseFirestore;
    DrawerLayout drawer;
    ActionBarDrawerToggle mDrawerToggle;

    Toolbar toolbar;
    ListView listView;
    String[] allTopics;
    ArrayList<String> listItem;
    StringBuilder totalText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        drawer =  findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        mAuth = FirebaseAuth.getInstance();
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
        {
            actionBar.setDisplayHomeAsUpEnabled(true);
            mDrawerToggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
            {
                public void onDrawerClosed(View view)
                {
                    supportInvalidateOptionsMenu();
                    //drawerOpened = false;
                }

                public void onDrawerOpened(View drawerView)
                {
                    supportInvalidateOptionsMenu();
                    //drawerOpened = true;
                }
            };
            mDrawerToggle.setDrawerIndicatorEnabled(true);
            drawer.addDrawerListener(mDrawerToggle);
            mDrawerToggle.syncState();
        }

        firebaseFirestore = FirebaseFirestore.getInstance();
        listView=findViewById(R.id.listView);

        allTopics = getResources().getStringArray(R.array.NXTopics);
        listItem=new ArrayList<>(Arrays.asList(allTopics));
        final NxListAdapter adapter = new NxListAdapter(this, listItem);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                // TODO Auto-generated method stub
                String value=adapter.getItem(position);

                switch(value) {
                    case "Introduction To NX Open":
                        OpenPDF("NxOpen IntroSlide.pdf");
                        break;
                    case "Visual Studio NX Wizard Setup":
                        OpenPDF("NX Wizard Setup.pdf");
                        break;
                    case "Default Code":
                        ReadText("Default NX Wizard Code.txt");
                        LaunchShowCode("Default NX Wizard Code.txt","No");
                        break;
                    case "Create New File":
                        ReadText("NewNXFile.txt");
                        LaunchShowCode("NewNXFile.txt","Yes");
                        break;
                    case "Create Block":
                        ReadText("Block.txt");
                        LaunchShowCode("Block.txt","Yes");
                        break;
                    case "Create Cylinder":
                        ReadText("Cylinder.txt");
                        LaunchShowCode("Cylinder.txt","Yes");
                        break;
                    case "Create Block And Extrude":
                        ReadText("BlockExtrusion.txt");
                        LaunchShowCode("BlockExtrusion.txt","Yes");
                        break;
                    case "Create Sketch":
                        ReadText("NewSketch.txt");
                        LaunchShowCode("NewSketch.txt","Yes");
                        break;
                    case "Create Sketch And Extrude":
                        ReadText("NewSketchAndExtrude.txt");
                        LaunchShowCode("NewSketchAndExtrude.txt","Yes");
                        break;
                    case "Create Drawing And Base View":
                        ReadText("NewDrawingFile.txt");
                        LaunchShowCode("NewDrawingFile.txt","Yes");
                        break;
                    case "Create Dimension":
                        ReadText("CreateDimension.txt");
                        LaunchShowCode("CreateDimension.txt","Yes");
                        break;
                }
            }
        });
    }
    @Override
    protected void onPostCreate(Bundle savedInstanceState)
    {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig)
    {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (drawer.isDrawerOpen(GravityCompat.END)) {
            drawer.closeDrawer(GravityCompat.END);
        } else {
            super.onBackPressed();
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_main_drawer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        //displayView(id);
        if (id == R.id.nav_about)
        {

        }
        else if (id == R.id.nav_feedback)
        {

        }
        else if (id == R.id.nav_rateUs)
        {

        }
        else if (id == R.id.nav_share) {

        }
        DrawerLayout drawer =  findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void LaunchShowCode(String textFile, String buttonVisible) {

        Intent sendStuff = new Intent(MainActivity.this, ShowCode.class);
        sendStuff.putExtra("content", totalText.toString());
        sendStuff.putExtra("path",textFile);
        sendStuff.putExtra("button",buttonVisible);
        startActivity(sendStuff);

    }
    private void OpenPDF(String assetFile) {


        Intent sendStuff = new Intent(MainActivity.this, PdfView.class);
        sendStuff.putExtra("path",assetFile);
        startActivity(sendStuff);
    }

    private void ReadText(String fileName) {
        try {

            InputStream is = getAssets().open(fileName);
            BufferedReader r = new BufferedReader(new InputStreamReader(is));
            totalText = new StringBuilder();
            String line;
            while ((line = r.readLine()) != null) {
                totalText.append(line).append('\n');
            }
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
        }
    }


    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        if(currentUser == null){

            sendToLogin();

        } else {

            current_user_id = mAuth.getCurrentUser().getUid();
            Toast.makeText(MainActivity.this, "Signed in as " + currentUser.getDisplayName(), Toast.LENGTH_LONG).show();
            firebaseFirestore.collection("users").document(current_user_id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                    if(task.isSuccessful()){

                    } else {

                        String errorMessage = task.getException().getMessage();
                        Toast.makeText(MainActivity.this, "Error : " + errorMessage, Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }

    private void sendToLogin() {

        Intent loginIntent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(loginIntent);
        finish();

    }


}
