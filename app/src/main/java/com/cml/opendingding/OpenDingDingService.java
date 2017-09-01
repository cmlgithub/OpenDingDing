package com.cml.opendingding;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

import java.util.Calendar;
import java.util.List;

public class OpenDingDingService extends Service {

    private boolean isNeedOpen = true;
    private String dingDingPackageName = "com.alibaba.android.rimet";
    private int intervalTime = 1000*60*30;

    public OpenDingDingService() {

    }

    @Override
    public void onCreate() {
        super.onCreate();

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        startTime();

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Log.e("CML","COUNT");
            Calendar instance = Calendar.getInstance();
            String currentTIme = instance.get(Calendar.HOUR_OF_DAY) +""+ instance.get(Calendar.MINUTE);
            Integer intCurrentTIme = Integer.valueOf(currentTIme);
            if(intCurrentTIme>0 && intCurrentTIme < 800){
                isNeedOpen = true;
                Log.e("CML","false");
            }else if(intCurrentTIme >= 800){
                if(isNeedOpen){
                    Log.e("CML","open");
                    openCLD(dingDingPackageName,getApplicationContext());
                    isNeedOpen = false;
                }
            }
            sendOpenMessage();
        }
    };

    private void startTime(){
        sendOpenMessage();
    }

    private void sendOpenMessage(){
        handler.sendEmptyMessageDelayed(1,intervalTime);
    }

    public static void openCLD(String packageName,Context context) {
        PackageManager packageManager = context.getPackageManager();
        PackageInfo pi = null;

        try {

            pi = packageManager.getPackageInfo(packageName, 0);
        } catch (Exception e) {
            Log.e("CML",e.toString()+"");
        }
        Intent resolveIntent = new Intent(Intent.ACTION_MAIN, null);
        resolveIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        resolveIntent.setPackage(pi.packageName);

        List<ResolveInfo> apps = packageManager.queryIntentActivities(resolveIntent, 0);

        ResolveInfo ri = apps.iterator().next();
        if (ri != null ) {
            String className = ri.activityInfo.name;

            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_LAUNCHER);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            ComponentName cn = new ComponentName(packageName, className);

            intent.setComponent(cn);
            context.startActivity(intent);
        }
    }
}
