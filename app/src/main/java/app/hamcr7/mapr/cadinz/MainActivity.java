package app.hamcr7.mapr.cadinz;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;

    String current_user_id;
    private FirebaseFirestore firebaseFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

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
