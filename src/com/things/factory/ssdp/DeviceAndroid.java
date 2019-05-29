package com.things.factory.ssdp;

import android.os.Build;

public class DeviceAndroid extends Device {
    private String id;  // for android
    private String manufacture; // for android
    private String model;   // for android
    private String device;   // for android
//    @TargetApi(Build.VERSION_CODES.O)
//    private String serial;
    private String osVersion;

    public String getId() {
        return Build.ID;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getManufacture() {
        return Build.MANUFACTURER;
    }

    public void setManufacture(String manufacture) {
        this.manufacture = manufacture;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getModel() {
        return Build.MODEL;
    }

    public String getDevice() {
        return Build.DEVICE;
    }

    public void setDevice(String device) {
        this.device = device;
    }

//    @TargetApi(Build.VERSION_CODES.O)
//    public String getSerial() {   //api level 26
//        //activity = this.cordova.getActivity().getApplicationContext();
//        if (ContextCompat.checkSelfPermission(this.context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
//            // We do not have this permission. Let's ask the user
//            ActivityCompat.requestPermissions(this.context, new String[]{Manifest.permission.READ_PHONE_STATE}, 0);
//            return null;
//        }
//
//        return Build.getSerial();
//    }

    public String getOsName() {
        return "Android";
    }

    public String getOsVersion() {
        return Build.VERSION.RELEASE;
    }

    public void setOsVersion(String osVersion) {
        this.osVersion = osVersion;
    }
}

//Log.d("SSDP", System.getProperty("os.name"));   // Linux
//Log.d("SSDP", String.valueOf(Build.VERSION.SDK_INT));   // 26
//Log.d("SSDP", Build.VERSION.RELEASE);   // release version 8.0.0
//Log.d("SSDP", Build.VERSION.CODENAME);  // REL
//Log.d("SSDP", Build.DEVICE);    // gts3lwifi
//Log.d("SSDP", Build.MODEL);     // SM-T820
//Log.d("SSDP", Build.MANUFACTURER);  // samsung
//Log.d("SSDP", Build.ID);    // R16NW