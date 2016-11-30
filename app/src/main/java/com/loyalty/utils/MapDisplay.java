package com.loyalty.utils;

import android.content.Context;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.loyalty.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by Sujeet on 08-07-2016.
 */

public class MapDisplay {


    public  void showRouteMap(String routes, GoogleMap map) throws Exception{

        PolylineOptions lineOptions  = new PolylineOptions();
        MarkerOptions startOptions = new MarkerOptions();
        MarkerOptions endOptions = new MarkerOptions();
        List<LatLng>  list=decodePoly(routes);

        lineOptions.addAll(list);
        lineOptions.width(25);
        lineOptions.color(Color.BLUE);
        map.addPolyline(lineOptions);

        // Creating MarkerOptions
        // Setting the position of the marker
        startOptions.position(list.get(0));
        endOptions.position(list.get(list.size()-1));
        startOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
        endOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));

        /*startOptions.icon(BitmapDescriptorFactory.fromResource(R.mipmap.map_icon1));
        endOptions.icon(BitmapDescriptorFactory.fromResource(R.mipmap.map_icon3));*/
        // Add new marker to the Google Map Android API V2
        map.addMarker(startOptions).setTitle("START");
        map.addMarker(endOptions).setTitle("FINISH");
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(list.get(0), 13));

    }

    public  List<LatLng> decodePoly(String encoded) {

        List<LatLng> poly = new ArrayList<LatLng>();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;

        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            LatLng p = new LatLng((((double) lat / 1E5)),
                    (((double) lng / 1E5)));
            poly.add(p);
        }

        return poly;
    }





    /**
     * Encodes a sequence of LatLngs into an encoded path string.
     */
    public  String encode(final List<LatLng> path) {
        long lastLat = 0;
        long lastLng = 0;

        final StringBuffer result = new StringBuffer();

        for (final LatLng point : path) {
            long lat = Math.round(point.latitude * 1e5);
            long lng = Math.round(point.longitude * 1e5);

            long dLat = lat - lastLat;
            long dLng = lng - lastLng;

            encode(dLat, result);
            encode(dLng, result);

            lastLat = lat;
            lastLng = lng;
        }
        return result.toString();
    }

    private  void encode(long v, StringBuffer result) {
        v = v < 0 ? ~(v << 1) : v << 1;
        while (v >= 0x20) {
            result.append(Character.toChars((int) ((0x20 | (v & 0x1f)) + 63)));
            v >>= 5;
        }
        result.append(Character.toChars((int) (v + 63)));
    }



    public static String getAddress(Context context, double latitude, double longitude){
        Geocoder geocoder;
        String strAddress= "";
        List<Address> addressList;
        geocoder = new Geocoder(context, Locale.getDefault());
        try {
            addressList = geocoder.getFromLocation(latitude, longitude, 1);

            if (addressList != null && addressList.size() > 0) {
                Address address = addressList.get(0);
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < address.getMaxAddressLineIndex(); i++) {
                    sb.append(address.getAddressLine(i)).append(" ");
                }
                sb.append(address.getLocality()).append(" ");
                sb.append(address.getPostalCode()).append(" ");
                sb.append(address.getCountryName());
                strAddress = sb.toString();

            }

            return strAddress;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "";
    }





    /** this method is used for showing map in Google API */
   /*public  void showActiveRouteMap(String routes, GoogleMap map, List<ActiveRiders> activeRiders) throws Exception{
        if(map!=null)map.clear();
      PolylineOptions lineOptions  = new PolylineOptions();
      MarkerOptions startOptions = new MarkerOptions();
      MarkerOptions endOptions = new MarkerOptions();
      List<LatLng>  list=decodePoly(routes);



         lineOptions.addAll(list);
         lineOptions.width(10);
         lineOptions.color(Color.BLACK);
         map.addPolyline(lineOptions);

         // Setting the position of the marker
         startOptions.position(list.get(0));
         endOptions.position(list.get(list.size()-1));
         *//*startOptions.snippet(rideId);
         endOptions.snippet(rideId);*//*
         startOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.pointer));
         endOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.pointer));
         // Add new marker to the Google Map Android API V2
         map.addMarker(startOptions).setTitle("START");
         map.addMarker(endOptions).setTitle("FINISH");

             CircleOptions co = new CircleOptions();
            co.center(list.get(0));
            co.radius(300);
            co.fillColor(Color.TRANSPARENT);
            co.strokeColor(Color.BLACK);
            co.strokeWidth(4.0f);
            map.addCircle(co);


           // map.moveCamera(CameraUpdateFactory.newLatLngZoom(list.get(0),16));




       for (ActiveRiders riders : activeRiders) {

              MarkerOptions mapMarkerOptions = new MarkerOptions();


              LatLng latLng = new LatLng(getDoubleValue(riders.lastRecordedLatitude),getDoubleValue(riders.LastRecordedLongitude) );


             mapMarkerOptions.position(latLng);
             mapMarkerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
             map.addMarker(mapMarkerOptions).setTitle(riders.name);

          }


   }*/



    private double getDoubleValue(String value){
        try {
            double d= Double.parseDouble(value);
            Log.e("double value","value"+d);

            return d;
        } catch (NumberFormatException e) {

            e.printStackTrace();
            return 0;
        }
    }


    public static double calculateDistance(double fromLatitude,double fromLongitude,double toLatitude,double toLongitude)
    {

        float results[] = new float[1];

        try {

            Location.distanceBetween(fromLatitude,fromLongitude, toLatitude, toLongitude, results);

        } catch (Exception e) {

            e.printStackTrace();

        }

        int dist = (int) results[0];
        if(dist<=0){return 0D;}

        return results[0];

    }

// public static void animateToMeters(int meters,Context context,GoogleMap googleMap, LatLng point, int mapHeight){
//
//    Resources r = context.getResources();
//    int mapSideInPixels = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, mapHeight, r.getDisplayMetrics());
//
////      LatLngBounds latLngBounds = calculateBounds(point, meters);
//    if(latLngBounds != null){
//       CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngBounds(latLngBounds, mapSideInPixels, mapSideInPixels, 0);
//       if(googleMap != null)
//          googleMap.moveCamera(cameraUpdate);
//    }
// }

// public static LatLngBounds calculateBounds(LatLng center, double radius) {
//    return new LatLngBounds.Builder().
//          include(SphericalUtil.computeOffset(center, radius, 0)).
//          include(SphericalUtil.computeOffset(center, radius, 90)).
//          include(SphericalUtil.computeOffset(center, radius, 180)).
//          include(SphericalUtil.computeOffset(center, radius, 270)).build();
    //}

}
