package br.com.ownard.forgetme;


import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class Splash extends Activity {

    private static final int PERMISSIONS_REQUEST = 1234;
    private int idTv = 1;
    private Button buttonContinue;

    TextView presentation = null;
    String[] allPermissionNeeded = {
            Manifest.permission.READ_CALL_LOG,
            Manifest.permission.READ_CONTACTS
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_layout);

        buttonContinue = (Button) findViewById(R.id.btnGetPermissions);

        if(checkPermissions()){
            startNextActivity();
        }

        buttonContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= 23) {
                    ActivityCompat.requestPermissions((Activity) view.getContext(), allPermissionNeeded, PERMISSIONS_REQUEST);
                } else {
                    startNextActivity();
                }
            }
        });


//        if (Build.VERSION.SDK_INT >= 23) {
//            ActivityCompat.requestPermissions(this, allPermissionNeeded, PERMISSIONS_REQUEST);
//        } else {
//            startNextActivity();
//        }

    }

    public boolean checkPermissions() {
        String[] requestsBlock = {};
        int i = 0;
        boolean controlPermissions = true;
        for (String permission : allPermissionNeeded) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }

        return controlPermissions;

    }

    public void startNextActivity() {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (checkPermissions()) {
            Intent i = new Intent(this, MainActivity.class);
            startActivity(i);
        }
    }
}