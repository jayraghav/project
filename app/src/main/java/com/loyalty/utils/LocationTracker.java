package com.loyalty.utils;


import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.loyalty.utils.LocationResult;


/** This class is used for Getting location form GPS */

public class LocationTracker implements LocationListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {


	private LocationRequest mLocationRequest;
	private GoogleApiClient mLocationClient;
	private Context context;
	private LocationResult locationResult;

	public LocationTracker(Context context, LocationResult locationResult) {
		super();
		this.context = context;
		this.locationResult=locationResult;


	}


	public void onUpdateLocation(){

		mLocationRequest = new LocationRequest();
		mLocationRequest.setInterval(1000);
		mLocationRequest.setFastestInterval(5000);
		mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

		mLocationClient = new GoogleApiClient.Builder(context,LocationTracker.this, LocationTracker.this)
				.addConnectionCallbacks(this)
				.addOnConnectionFailedListener(this)
				.addApi(LocationServices.API)
				.build();

		if(mLocationClient.isConnected()){
			Log.d("Location Tracker", "Location client is connected...");
			startUpdates();
		}else{
			mLocationClient.connect();
			Log.d("Location Tracker", "Location client is going to connected...");
		}

	}

	@Override
	public void onConnectionFailed(ConnectionResult result) {


	}

	@Override
	public void onConnected(Bundle connectionHint) {

		startUpdates();



	}

	@Override
	public void onLocationChanged(Location location) {

		if(location!=null){

			if(location.getLatitude()!=0 && location.getLongitude()!=0){

				locationResult.gotLocation(location);

				if(mLocationClient!=null && mLocationClient.isConnected()){
					stopPeriodicUpdates();

				}

			}
		}

	}
	/**
	 * In response to a request to start updates, send a request
	 * to Location Services
	 */
	private void startPeriodicUpdates() {
		try{
			LocationServices.FusedLocationApi.requestLocationUpdates(mLocationClient, mLocationRequest, LocationTracker.this);

		}catch (Exception e){
			e.printStackTrace();
		}

	}

	/**
	 * In response to a request to stop updates, send a request to
	 * Location Services
	 */
	private void stopPeriodicUpdates() {
		LocationServices.FusedLocationApi.removeLocationUpdates(mLocationClient,LocationTracker.this);

	}



	private void startUpdates() {
		startPeriodicUpdates();

	}

	@Override
	public void onConnectionSuspended(int arg0) {
		// TODO Auto-generated method stub

	}







}
