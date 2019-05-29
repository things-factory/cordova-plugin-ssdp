package com.things.factory.plugins;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.PluginResult;
import org.json.JSONArray;
import org.json.JSONException;

import java.util.List;

public class SSDPLauncher extends CordovaPlugin {
    private static final String ACTION_SEARCH = "search";
    private static final String ACTION_LISTEN = "listen";

    private CallbackContext callbackContext = null;

    @Override
    public boolean execute(String action, JSONArray rawArgs, CallbackContext callbackContext) throws JSONException {
        this.callbackContext = callbackContext;
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
            try {
                SSDPClient.search();
            } catch(Exception e) {
                callbackContext.error("Searching Exception");
                PluginResult r = new PluginResult(PluginResult.Status.ERROR);
                callbackContext.sendPluginResult(r);
                return true;
            }

            callbackContext.success();
            return true;
        }

        return false;
    }

    public void updateResult(List<String[]> results) {
        PluginResult r = new PluginResult(PluginResult.Status.NO_RESULT);
        r.setKeepCallback(true);
        callbackContext.sendPluginResult(r);
    }
}
