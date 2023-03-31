package com.midhun.hawkssolutions.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.midhun.hawkssolutions.Model.Info;
import com.midhun.hawkssolutions.activityinfo.HomeActivity;
import com.midhun.hawkssolutions.activityinfo.R;
import com.midhun.hawkssolutions.activityinfo.databinding.ActivityLoginBinding;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InfoAdapter extends RecyclerView.Adapter<InfoAdapter.InfoViewHolder> {


    ArrayList<HashMap<String, Object>> appList;

    public InfoAdapter( ArrayList<HashMap<String, Object>> appList, Context ctx, Activity activity) {
        this.appList = appList;
        this.ctx = ctx;
        this.activity = activity;
    }

    Context ctx;
    Activity activity;
    @NonNull
    @Override
    public InfoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View view = LayoutInflater.from(ctx).inflate(R.layout.custom_info_item,parent,false);
       return new InfoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull InfoViewHolder holder, int position) {

        holder.name.setText(appList.get(position).get("name").toString());
        holder.pname.setText(appList.get(position).get("package").toString());
        holder.vname.setText(appList.get(position).get("versionname").toString());
        holder.vcode.setText(appList.get(position).get("versioncode").toString());
        try
        {
            Drawable icon =ctx.getPackageManager().getApplicationIcon(appList.get(position).get("package").toString());
           holder.img.setImageDrawable(icon);
        }
        catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return appList.size();
    }

    class InfoViewHolder extends RecyclerView.ViewHolder{
         TextView name,pname,vname,vcode;
         ImageView img;
        public InfoViewHolder(@NonNull View itemView) {
            super(itemView);
            name =itemView.findViewById(R.id.name);
            img=itemView.findViewById(R.id.img);
            pname=itemView.findViewById(R.id.pname);
            vname=itemView.findViewById(R.id.vname);
            vcode=itemView.findViewById(R.id.vcode);
        }
    }
    public Bitmap StringToBitMap(String encodedString){
        try{
            byte [] encodeByte = Base64.decode(encodedString,Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        }
        catch(Exception e){
            e.getMessage();
            return null;
        }
    }
}
