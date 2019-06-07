package com.things.factory.ssdp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.util.Log;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaWebView;
import org.apache.cordova.PluginResult;
import org.json.JSONArray;
import org.json.JSONException;

public class SSDPLauncher extends CordovaPlugin {
    private static final String TAG = "SSDP";
    private static final String ACTION_SEARCH = "search";
    private static final String ACTION_LISTEN = "listen";

    CallbackContext callbackContext = null;
    BroadcastReceiver receiver;

    /**
     * Sets the context of the Command. This can then be used to do things like
     * get file paths associated with the Activity.
     *
     * @param cordova The context of the main Activity.
     * @param webView The CordovaWebView Cordova is running in.
     */
    public void initialize(CordovaInterface cordova, CordovaWebView webView) {
        super.initialize(cordova, webView);

        this.registerActionReceiver();
    }

    @Override
    public boolean execute(String action, JSONArray rawArgs, CallbackContext callbackContext) throws JSONException {
        if (action.equals(ACTION_LISTEN)) {
            try {
                SSDPServer.listen();
            } catch(Exception e) {
                callbackContext.error("Listening Exception");
                PluginResult r = new PluginResult(PluginResult.Status.ERROR);
                callbackContext.sendPluginResult(r);
                return true;
            }

            callbackContext.success();
            return true;
        } else if (action.equals(ACTION_SEARCH)) {
            this.callbackContext = callbackContext;
            try {
                String st = rawArgs.getString(0);
                SSDPClient.search(webView.getContext(), st, 2000);
            } catch(Exception e) {
                callbackContext.error("Searching Exception");
                PluginResult r = new PluginResult(PluginResult.Status.ERROR);
                callbackContext.sendPluginResult(r);
                return true;
            }

            PluginResult pluginResult = new PluginResult(PluginResult.Status.OK, "SSDP Search callback setting as continuous~~");
            pluginResult.setKeepCallback(true);
            callbackContext.sendPluginResult(pluginResult);
            //callbackContext.success();
            return true;
        }

        return false;
    }

    /**
     * Stop network receiver.
     */
    public void onDestroy() {
        this.unregisterReceiver();
    }

    @Override
    public void onPause(boolean multitasking) {
        this.unregisterReceiver();
    }

    @Override
    public void onResume(boolean multitasking) {
        super.onResume(multitasking);

        this.unregisterReceiver();
        this.registerActionReceiver();
    }

    private void registerActionReceiver() {
        // We need to listen to connectivity events to update navigator.connection
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(SSDPClient.ACTION_DISPATCH_DEVICE);
        if (this.receiver == null) {
            this.receiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    Log.d(TAG, "ACTION: "+ intent.getAction());
                    //if (!intent.getAction().equalsIgnoreCase(SSDPClient.ACTION_DISPATCH_DEVICE)) {
                    //    return;
                    //}

                    String json = intent.getStringExtra(SSDPClient.SSDP_MESSAGE);
                    if (json == null) {
                        Log.d(TAG, "json is null");
                        return;
                    }
                    Log.d(TAG, json);
                    PluginResult r = new PluginResult(PluginResult.Status.OK, json);
                    r.setKeepCallback(true);
                    callbackContext.sendPluginResult(r);
                    webView.postMessage("ssdpdevice", json);
                }
            };
        }

        webView.getContext().registerReceiver(this.receiver, intentFilter);
    }

    private void unregisterReceiver() {
        if (this.receiver != null) {
            try {
                webView.getContext().unregisterReceiver(this.receiver);
            } catch (Exception e) {
                Log.e(TAG, "Error unregistering ssdp: " + e.getMessage(), e);
            } finally {
                receiver = null;
            }
        }
    }

//    public void updateResult(Device device) {
//        String json = new Gson().toJson(device);
//        if (callbackContext == null) {
//            return;
//        }
//        PluginResult r = new PluginResult(PluginResult.Status.OK, json);
//        r.setKeepCallback(true);
//        callbackContext.sendPluginResult(r);
//    }
//
//    public void updateResult(SSDPMessage result) {
//        String json = new Gson().toJson(result);
//        Log.d("SSDP", json);
//        PluginResult r = new PluginResult(PluginResult.Status.OK, json);
//        r.setKeepCallback(true);
//        callbackContext.sendPluginResult(r);
//        webView.postMessage("ssdpdevice", json);
//    }
}
