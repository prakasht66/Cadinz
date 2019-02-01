package app.hamcr7.mapr.cadinz;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ExpandableListView;
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
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    String current_user_id;
    private FirebaseFirestore firebaseFirestore;
    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;

    Toolbar toolbar;
    ListView listView;
    String[] allTopics;
    ArrayList<String> listItem;
    StringBuilder totalText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
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
