package app.hamcr7.mapr.cadinz;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.pddstudio.highlightjs.HighlightJsView;
import com.pddstudio.highlightjs.models.Language;
import com.pddstudio.highlightjs.models.Theme;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class ShowCode extends AppCompatActivity {

    HighlightJsView highlightJsView;
    FloatingActionButton shareBtn;
    FloatingActionButton downloadBtn;
    FloatingActionButton playBtn;
    String codeContent,filePath,showBtn;
    Bitmap bmp;
    private ProgressDialog progressDialog;
    private static final  int MY_PERMISSION_REQUEST_STORAGE=1;
    // Buffer size used.
    private final static int BUFFER_SIZE = 1024;
    public static final int progressType = 0;
    public static int pic=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_code);

        shareBtn = findViewById(R.id.fab_Share);
        downloadBtn = findViewById(R.id.fab_Download);
        playBtn=findViewById(R.id.fab_Play);
        playBtn.setVisibility(View.INVISIBLE);

        shareBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShareFile();
            }
        });
        downloadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DownloadFile();
            }
        });

        highlightJsView = findViewById(R.id.highlight_view);
        highlightJsView.setShowLineNumbers(true);
        highlightJsView.setTheme(Theme.ANDROID_STUDIO);
        highlightJsView.setHighlightLanguage(Language.C_SHARP);
        highlightJsView.setZoomSupportEnabled(true);

        Bundle bundle = getIntent().getExtras();
        codeContent = bundle.getString("content");
        filePath=bundle.getString("path");
        showBtn=bundle.getString("button");
        if (showBtn.equals("Yes"))
        {
            playBtn.setVisibility(View.VISIBLE);
            playBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ShowImage(filePath);
                }
            });
        }
        highlightJsView.setSource(codeContent);
        highlightJsView.refresh();

        if (ContextCompat.checkSelfPermission(ShowCode.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED)
        {
            if (ActivityCompat.shouldShowRequestPermissionRationale(ShowCode.this,Manifest.permission.WRITE_EXTERNAL_STORAGE))
            {
                ActivityCompat.requestPermissions(ShowCode.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},MY_PERMISSION_REQUEST_STORAGE);
            }
            else
            {
                ActivityCompat.requestPermissions(ShowCode.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},MY_PERMISSION_REQUEST_STORAGE);
            }
        }
        else
        {
            //do nothing
        }



    }
    public void ShowImage( String itemName) {
        Dialog builder = new Dialog(this);
        builder.requestWindowFeature(Window.FEATURE_NO_TITLE);
        builder.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));
        builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                //nothing;
            }
        });

        ImageView imageView = new ImageView(this);

        switch(itemName) {
            case"NewNXFile.txt":
                imageView.setImageResource(R.drawable.file_new);
                break;
            case"Block.txt":
                imageView.setImageResource(R.drawable.nx_block);
                break;
            case"Cylinder.txt":
                imageView.setImageResource(R.drawable.nx_cylinder);
                break;
            case"BlockExtrusion.txt":
                imageView.setImageResource(R.drawable.nx_blockextrusion);
                break;
            case"NewSketch.txt":
                imageView.setImageResource(R.drawable.nx_new_sketch);
                break;
            case"NewSketchAndExtrude.txt":
                imageView.setImageResource(R.drawable.nx_sketchextrude);
                break;
            case"NewDrawingFile.txt":
                imageView.setImageResource(R.drawable.nx_drawingfile);
                break;
            case"CreateDimension.txt":
                imageView.setImageResource(R.drawable.nx_dimension);
                break;
        }

        builder.addContentView(imageView, new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        builder.show();
    }
    public void ShareFile() {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, codeContent);
        sendIntent.setType("text/plain");
        Intent.createChooser(sendIntent, "Share via");
        startActivity(sendIntent);

    }

    public void DownloadFile() {

        copyAsset(filePath);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSION_REQUEST_STORAGE: {

                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(ShowCode.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED)
                    {
                        //do nothing
                    }
                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(ShowCode.this, "Permission denied to read your External storage", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
    private void copyAsset(String filename)
    {
        InputStream in = null;
        OutputStream out = null;
        try {
            String dirPath= Environment.getExternalStorageDirectory().getAbsolutePath()+"/CadInz";
            File dir=new File(dirPath);
            if (!dir.exists())
            {
                dir.mkdir();
            }
            AssetManager assetManager = getAssets();
            in = assetManager.open(filename);
            File outFile = new File(dirPath, filename);
            out = new FileOutputStream(outFile);
            copyAssetFiles(in, out);
            Toast.makeText(ShowCode.this, "File Saved", Toast.LENGTH_SHORT).show();
        } catch(IOException e) {
            Log.e("tag", "Failed to copy asset file: " + filename, e);
        }
        finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }
    private static void copyAssetFiles(InputStream in, OutputStream out) {
        try {

            byte[] buffer = new byte[BUFFER_SIZE];
            int read;

            while ((read = in.read(buffer)) != -1) {
                out.write(buffer, 0, read);
            }

            in.close();
            out.flush();
            out.close();


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
