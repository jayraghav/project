package com.loyalty.activity.customer;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.loyalty.R;
import com.loyalty.utils.HttpConnection;
import com.loyalty.utils.MapDisplay;
import com.loyalty.utils.PathJSONParser;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TakeMeThereActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private String lat;
    private String longi;
    private  double dlat;
    private double dlongi;
    private double crntDlat;
    private double crnDtLng;
    private String crntLat;
    private String crntLng;
    private ImageView ivToolbarLeft;
    private TextView tvTitle;
    private List<Marker> allMarkers=new ArrayList<>();;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_me_there);
        setToolbar();
        lat=getIntent().getStringExtra("Latitude");
        longi=getIntent().getStringExtra("logi");
        crntLat=getIntent().getStringExtra("crntLat");
        crntLng=getIntent().getStringExtra("crntLng");

        dlat= Double.parseDouble(lat);
        dlongi= Double.parseDouble(longi);

        crntDlat=Double.parseDouble(crntLat);
        crnDtLng=Double.parseDouble(crntLng);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }
    public void setToolbar() {

        tvTitle = (TextView) findViewById(R.id.toolbar_title);
        ivToolbarLeft=(ImageView) findViewById(R.id.iv_toolbar_left);

        ivToolbarLeft.setVisibility(View.VISIBLE);
        ivToolbarLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tvTitle.setText("Take Me There");
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (mMap != null) {
            MapDisplay mapDisplay = new MapDisplay();
            String url = getDirectionsUrl(new LatLng(crntDlat,crnDtLng), new LatLng(dlat,dlongi));
            List<LatLng> list = new ArrayList<LatLng>();
            list.add(new LatLng(crntDlat,crnDtLng));
            list.add(new LatLng(dlat, dlongi));
            Marker firstMarker =  mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(crntDlat, crnDtLng))
                    .title(""));
            allMarkers.add(firstMarker);

            Marker marker = mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(dlat, dlongi))
                    .title("").icon(BitmapDescriptorFactory.fromResource(R.mipmap.location)));
            allMarkers.add(marker);

            mMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
                @Override
                public void onMapLoaded() {
                    try {
                        LatLngBounds.Builder builder = new LatLngBounds.Builder();


                        for (Marker marker : allMarkers) {
                            builder.include(marker.getPosition());
                        }
                        LatLngBounds bounds = builder.build();
                        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds,13);
                        mMap.animateCamera(cu);
                        final float maxZoom = 13.0f;
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                if (mMap.getCameraPosition().zoom > maxZoom)
                                    mMap.animateCamera(CameraUpdateFactory.zoomTo(maxZoom));
                            }
                        },1000);

                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            });
            try {
                mapDisplay.showRouteMap(url, mMap);
            } catch (Exception e) {
                e.printStackTrace();
            }
            ReadTask downloadTask = new ReadTask();
            downloadTask.execute(url);


        }
    }
    private class ReadTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... url) {
            String data = "";
            try {
                HttpConnection http = new HttpConnection();
                data = http.readUrl(url[0]);
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            new ParserTask().execute(result);
        }
    }
    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

        @Override
        protected List<List<HashMap<String, String>>> doInBackground(
                String... jsonData) {
            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;
            try {
                jObject = new JSONObject(jsonData[0]);
                PathJSONParser parser = new PathJSONParser();
                routes = parser.parse(jObject);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return routes;
        }
        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> routes) {
            ArrayList<LatLng> points = null;
            PolylineOptions polyLineOptions = null;
            if (routes != null)
                for (int i = 0; i < routes.size(); i++) {
                    points = new ArrayList<LatLng>();
                    polyLineOptions = new PolylineOptions();
                    List<HashMap<String, String>> path = routes.get(i);
                    for (int j = 0; j < path.size(); j++) {
                        HashMap<String, String> point = path.get(j);
                        double lat = Double.parseDouble(point.get("lat"));
                        double lng = Double.parseDouble(point.get("lng"));
                        LatLng position = new LatLng(lat, lng);
                        points.add(position);
                    }
                    polyLineOptions.addAll(points);
                    polyLineOptions.width(5);
                    polyLineOptions.color(Color.BLUE);
                }
            if (polyLineOptions != null) {
                mMap.addPolyline(polyLineOptions);
            }

        }

    }
    private String getDirectionsUrl(LatLng origin, LatLng dest) {
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;
        String sensor = "sensor=false";
        String parameters = str_origin + "&" + str_dest + "&" + sensor;
        String output = "json";
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;
        return url;
    }
}
