package com.midhun.hawkssolutions.activityinfo;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.midhun.hawkssolutions.Adapter.InfoAdapter;
import com.midhun.hawkssolutions.Utils.MidhunUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class HomeActivity extends AppCompatActivity {


    FirebaseAuth mAuth;
    FirebaseUser currentUser;
    String UID;
    ArrayList<HashMap<String, Object>> appList = new ArrayList<>();
    RecyclerView recyclerView;
    InfoAdapter infoAdapter;
    String currentDate;
    String currentTime;
    TextView user_name;
    ImageView user_profile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        getSupportActionBar().hide();
        user_name = findViewById(R.id.user_name);
        user_profile = findViewById(R.id.user_profile);
        mAuth = FirebaseAuth.getInstance();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
        currentTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());

        if (currentUser != null) {
            UID = currentUser.getUid();
            user_name.setText("Haii," + currentUser.getDisplayName());
            Glide.with(getApplicationContext())
                    .load(currentUser.getPhotoUrl())
                    .transition(withCrossFade())
                    .apply(new RequestOptions()
                            //.override(60, 60)
                            .placeholder(R.drawable.ic_launcher_background)
                            .error(R.drawable.ic_launcher_background).centerCrop()
                    )
                    .into(user_profile);

            DatabaseReference userinfo = FirebaseDatabase.getInstance().getReference("users/" + UID + "/userinfo/");
            HashMap<String, Object> mapList2= new HashMap<String, Object>();
            mapList2.put("name",currentUser.getDisplayName());
            mapList2.put("email",currentUser.getEmail());
            mapList2.put("profile",String.valueOf(currentUser.getPhotoUrl()));
            mapList2.put("phone",currentUser.getPhoneNumber());
            mapList2.put("date", currentDate);
            mapList2.put("time", currentTime);
            userinfo.child(UID).setValue(mapList2);
        }


        recyclerView = findViewById(R.id.rv);

        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(HomeActivity.this);
        layoutManager.setOrientation(layoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        infoAdapter = new InfoAdapter(appList, HomeActivity.this, HomeActivity.this);
        recyclerView.setAdapter(infoAdapter);
        appList = new ArrayList<>();
        installedApps();

    }

    public void installedApps() {
        List<PackageInfo> packList = getPackageManager().getInstalledPackages(0);

        for (int i = 0; i < packList.size(); i++) {
            PackageInfo packInfo = packList.get(i);
            if ((packInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
                String appName = packInfo.applicationInfo.loadLabel(getPackageManager()).toString();
                String pname = packInfo.packageName;
                String versionName = packInfo.versionName;
                int versionCode = packInfo.versionCode;
                Drawable icon = packInfo.applicationInfo.loadIcon(getPackageManager());
                String image = String.valueOf(icon);
                //Log.e("App № " + Integer.toString(i), appName);


                HashMap<String, Object> mapList = new HashMap<String, Object>();

                mapList.put("name", appName);
                mapList.put("sort", appName.toLowerCase());
                mapList.put("package", pname);
                mapList.put("versionname", versionName);
                mapList.put("versioncode", versionCode);
                mapList.put("icon", icon);
                mapList.put("uid", UID);

                appList.add(mapList);
                MidhunUtils.sortListMap(appList, "sort", false, true);

                DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference("users/" + UID + "/userapps/");
                HashMap<String, Object> mapList1 = new HashMap<String, Object>();
                String pushName = appName.replaceAll("[-+.^:,]","");
                mapList1.put("name", appName);
                String pushkey = reference1.push().getKey();
                mapList1.put("pushkey", pushkey);
                mapList1.put("uid", UID);
                mapList1.put("pname", pname);
                mapList1.put("versionName", versionName);
                mapList1.put("versionCode", versionCode);
                mapList1.put("image", image);
                mapList1.put("date", currentDate);
                mapList1.put("time", currentTime);
                reference1.child(pushName).setValue(mapList1);

                infoAdapter = new InfoAdapter(appList, HomeActivity.this, HomeActivity.this);
                recyclerView.setAdapter(infoAdapter);

//                Iterator<String> myVeryOwnIterator = mapList.keySet().iterator();
//                while (myVeryOwnIterator.hasNext()) {
//                    String key = (String) myVeryOwnIterator.next();
//                    Object value = mapList.get(key);
//                    System.out.println(key + " " + value);
//
//                }
            }
        }
    }
}
