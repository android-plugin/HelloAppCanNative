package com.zywx.helloappcannative;

import android.app.Application;
import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.support.multidex.MultiDex;

import org.zywx.wbpalmstar.engine.AppCan;

/**
 * Created by ylt on 2017/1/13.
 */

public class MyApplication extends Application {

    private boolean init = false;

    public void onCreate() {
        super.onCreate();
        AppCan.getInstance().initSync(this.getApplicationContext());
        this.init = true;
    }

    protected void attachBaseContext(Context var1) {
        super.attachBaseContext(var1);
        MultiDex.install(this);
    }

    public AssetManager getAssets() {
        AssetManager var1;
        return !this.init?super.getAssets():((var1 = AppCan.getInstance().getThirdPlugins() == null?super.getAssets():AppCan.getInstance().getThirdPlugins().d()) == null?super.getAssets():var1);
    }

    public Resources getResources() {
        Resources var1;
        return !this.init?super.getResources():((var1 = AppCan.getInstance().getThirdPlugins() == null?super.getResources():AppCan.getInstance().getThirdPlugins().e()) == null?super.getResources():var1);
    }

    public ClassLoader getClassLoader() {
        ClassLoader var1;
        return !this.init?super.getClassLoader():((var1 = AppCan.getInstance().getThirdPlugins() == null?super.getClassLoader():AppCan.getInstance().getThirdPlugins().c()) == null?super.getClassLoader():var1);
    }
}
