package app.hamcr7.mapr.cadonz;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class ShowInfo extends AppCompatActivity {

    TextView titleView;

    TextView contentView;

    String title,description;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_info);

        titleView=findViewById(R.id.title_showInfo);
        contentView=findViewById(R.id.show_InfoText);

        Bundle bundle = getIntent().getExtras();
        title= bundle.getString("Title");
        description=bundle.getString("Content");

        titleView.setText(title);
        contentView.setText(description);
    }
}
