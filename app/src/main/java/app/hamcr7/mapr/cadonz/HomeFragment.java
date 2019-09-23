package app.hamcr7.mapr.cadonz;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;



public class HomeFragment extends Fragment {

    ListView listView;
    String[] allTopics;
    ArrayList<String> listItem;
    StringBuilder totalText;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View myView=inflater.inflate(R.layout.fragment_home, container, false);
        listView=myView. findViewById(R.id.listView);
        allTopics = getResources().getStringArray(R.array.Topics);
        listItem=new ArrayList<>(Arrays.asList(allTopics));
        SharedPreferences preferences = getActivity().getSharedPreferences("MyPrefsFile", Context.MODE_PRIVATE);

        Boolean check = preferences.getBoolean("firstTime", false);
        final NxListAdapter adapter = new NxListAdapter(getContext(), listItem,check);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                // TODO Auto-generated method stub
                String value=adapter.getItem(position);

                switch(value) {
                    case "NX Open":
                        ShowInfo(getString(R.string.NXOpenTitle),getString(R.string.NXOpenIntro));
                        break;
                    case "Prerequisites":
                        ShowInfo(getString(R.string.PreReqTitle),getString(R.string.Prerequisites));
                        break;
                    case "Introduction To NX Open":
                        OpenPDF("NxOpen IntroSlide.pdf");
                        break;
                    case "Visual Studio NX Wizard Setup":
                        OpenPDF("NX Wizard Setup.pdf");
                        break;
                    case "Default Code":
                        ReadText("Default NX Wizard Code.txt");
                        LaunchShowCode("Default NX Wizard Code.txt","No",value);
                        break;
                    case "Create New File":
                        ReadText("NewNXFile.txt");
                        LaunchShowCode("NewNXFile.txt","Yes",value);
                        break;
                    case "Create Block":
                        ReadText("Block.txt");
                        LaunchShowCode("Block.txt", "Yes",value);
                        break;
                    case "Create Cylinder":
                        ReadText("Cylinder.txt");
                        LaunchShowCode("Cylinder.txt","Yes",value);
                        break;
                    case "Create Block And Extrude":
                        ReadText("BlockExtrusion.txt");
                        LaunchShowCode("BlockExtrusion.txt","Yes",value);
                        break;
                    case "Create Sketch":
                        ReadText("NewSketch.txt");
                        LaunchShowCode("NewSketch.txt","Yes",value);
                        break;
                    case "Create Sketch And Extrude":
                        ReadText("NewSketchAndExtrude.txt");
                        LaunchShowCode("NewSketchAndExtrude.txt","Yes",value);
                        break;
                    case "Create Drawing And Base View":
                        ReadText("NewDrawingFile.txt");
                        LaunchShowCode("NewDrawingFile.txt","Yes",value);
                        break;
                    case "Create Dimension":
                        ReadText("CreateDimension.txt");
                        LaunchShowCode("CreateDimension.txt","Yes",value);
                        break;
                }
            }
        });
        return myView;

    }

    private void ShowInfo(String title,String description)
    {
        Intent intent = new Intent(getActivity(),ShowInfo.class);
        intent.putExtra("Title", title);
        intent.putExtra("Content", description);
        startActivity(intent);

    }
    private void LaunchShowCode(String textFile, String buttonVisible,String mainTitle) {

        Intent sendStuff = new Intent(getActivity(), ShowCode.class);
        sendStuff.putExtra("title",mainTitle);
        sendStuff.putExtra("content", totalText.toString());
        sendStuff.putExtra("path",textFile);
        sendStuff.putExtra("button",buttonVisible);
        startActivity(sendStuff);

    }
    private void OpenPDF(String assetFile) {


        Intent sendStuff = new Intent(getActivity(), PdfView.class);
        sendStuff.putExtra("path",assetFile);
        startActivity(sendStuff);
    }

    private void ReadText(String fileName) {
        try {

            InputStream is = getActivity().getResources().getAssets().open(fileName);
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


}
