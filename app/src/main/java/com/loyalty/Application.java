package com.loyalty;

import android.content.Context;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;

import com.loyalty.webserivcemodel.CatalogueProductDetails;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by rahul.gupta on 26/8/16.
 */
public class Application extends MultiDexApplication {

    public static List<CatalogueProductDetails> list=new ArrayList<>();

    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(getApplicationContext());
    }

    @Override
    public void onCreate() {
        super.onCreate();



    }


}
