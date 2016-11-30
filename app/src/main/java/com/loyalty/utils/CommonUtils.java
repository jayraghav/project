package com.loyalty.utils;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Typeface;
import android.media.ExifInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.format.DateFormat;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.facebook.appevents.internal.Constants;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;
import com.loyalty.activity.customer.HomeActivity;
import com.loyalty.activity.customer.LoginActivity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;


public class CommonUtils {
    public static final float ALPHA_LIGHT = 0.45f;
    public static final float ALPHA_DARK = 1.0f;
    public static boolean accept;
    public static String imageNameLocal;
    private static ProgressDialog progressDialog;
    public static final int PERMISSION_REQUEST_CODE = 200;
    public static SharedPreferences.Editor editor;

    public static boolean isNetworkConnected(Context mContext) {
        ConnectivityManager cm = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        return ni != null;
    }
    public static  void facebookSharing(ShareDialog shareDialog) {
        if (ShareDialog.canShow(ShareLinkContent.class)) {

            ShareLinkContent linkContent = new ShareLinkContent.Builder()
                    .setContentTitle("Loyalty App Testing")
                    .setContentDescription("Just testing the facebook sharing implementation")
                    .setContentUrl(Uri.parse("https://www.facebook.com/jay.raghav.1990"))
                    //.setImageUrl(Uri.parse("http://172.16.0.9/PROJECTS/FollowYourSport/trunk/admin/upload/article/thumbs/landscape-nature-sunset-trees.jpg"))
                    .build();

            shareDialog.show(linkContent);

        } else {


        }

    }
    public static void clearPreferences(){
        editor.clear();
    }

    public static void showAlertWithPositiveButton(int message, Context context, String field) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(message + " " + field);
        builder.setCancelable(true);
        builder.setPositiveButton("Ok",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert1 = builder.create();
        alert1.show();
    }

    public static void showAlertWithPositiveButton(String message, Context context, String field) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(message + " " + field);
        builder.setCancelable(true);
        builder.setPositiveButton("Ok",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert1 = builder.create();
        alert1.show();
    }

    public static void showAlertWithPositiveButton(int message, Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(message);
        builder.setCancelable(true);
        builder.setPositiveButton("Ok",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert1 = builder.create();
        alert1.show();
    }

    public static boolean checkPermission(Activity context){
        int result = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION);
        if (result == PackageManager.PERMISSION_GRANTED){

            return true;

        } else {
            return false;

        }
    }

   /* public static void requestPermission(Activity context){
        if (ActivityCompat.shouldShowRequestPermissionRationale(context, Manifest.permission.ACCESS_FINE_LOCATION)){
            ActivityCompat.requestPermissions(context, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, RequestURL.PERMISSION_REQUEST_CODE);
        } else {
            ActivityCompat.requestPermissions(context, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, RequestURL.PERMISSION_REQUEST_CODE);
        }
    }*/

    public static void showAlertWithPositiveButton(String message, Context context, final EditText editText) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(message);
        builder.setCancelable(true);
        builder.setPositiveButton("Ok",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        editText.requestFocus();
                        dialog.dismiss();
                    }
                });
        AlertDialog alert1 = builder.create();
        alert1.show();
    }


    public static boolean validateFirstName(String firstName) {
        return firstName.matches("[A-Z][a-zA-Z]*");
    } // end method validateFirstName

    // validate last name
    public static boolean validateLastName(String lastName) {
        return lastName.matches("[a-zA-z]+([ '-][a-zA-Z]+)*");
    }
  /*public static boolean validateUserNameEmail(String nameEmail) {
        return nameEmail.matches("(/^(?=[a-z0-9.]{3,20}$)[a-z0-9]+\\.?[a-z0-9]+$/i)|(/^(?=[a-z0-9.]{3,20}$)[a-z0-9]+\\.?[a-z0-9]+$|^.*@\\w+\\.[\\w.]+$/)");
    }
*/

    public static void hideKeyboard(Activity activity) {
        try {
            InputMethodManager in = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            View view = activity.findViewById(android.R.id.content);
            in.hideSoftInputFromWindow(view.getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
        } catch (Throwable e) {
        }

    }

    public final static boolean isValidEmail(CharSequence target) {
        if (target == null) {
            return false;
        } else {

            return Patterns.EMAIL_ADDRESS.matcher(target)
                    .matches();
        }
    }


    public static Typeface setBold(Context context) {
        return Typeface.createFromAsset(context.getAssets(), "fonts/ITCKabelStd-Bold.otf");
    }

    public static Typeface setBook(Context context) {
        return Typeface.createFromAsset(context.getAssets(), "fonts/ITCKabelStd-Book.otf");
    }

    public static Typeface setDemi(Context context) {
        return Typeface.createFromAsset(context.getAssets(), "fonts/ITCKabelStd-Demi.otf");
    }

    public static Typeface setUltra(Context context) {
        return Typeface.createFromAsset(context.getAssets(), "fonts/ITCKabelStd-Ultra.otf");
    }


    public static String getDateDiffString(String startDate, String endDate) {/*
        long timeOne = dateOne.getTime();
        long timeTwo = dateTwo.getTime();
        long oneDay = 1000 * 60 * 60 * 24;
        long delta = (timeTwo - timeOne) / oneDay;

        if (delta > 0) {
            return "d" + delta + "d";
        }
        else {
            delta *= -1;
            return "d" + delta + " days before dateOne";
        }
*/
        java.text.DateFormat formatter = new SimpleDateFormat("dd-MM-yyy");

        try {
            Date date1 = (Date) formatter.parse(startDate);
            Date date2 = (Date) formatter.parse(endDate);
            long diff = date2.getTime() - date1.getTime();
            long days = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
            if (days < 0)
                days = -days;
            return String.valueOf(days);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void showToast(Context context, String string) {
        Toast.makeText(context, string, Toast.LENGTH_SHORT).show();
    }

    public static String getDateOfTimestamp(long timeStamp) {
        java.text.DateFormat objFormatter = new SimpleDateFormat("dd-MM-yyy");

        Calendar objCalendar = Calendar.getInstance();

        objCalendar.setTimeInMillis(timeStamp * 1000);//edit
        String result = objFormatter.format(objCalendar.getTime());
        objCalendar.clear();
        return result;

    }

    public static String getDateOfTimestampMmDdYy(long timeStamp) {
        java.text.DateFormat objFormatter = new SimpleDateFormat("MM-dd-yyy");

        Calendar objCalendar = Calendar.getInstance();

        objCalendar.setTimeInMillis(timeStamp * 1000);//edit
        String result = objFormatter.format(objCalendar.getTime());
        objCalendar.clear();
        return result;

    }
    public static String getTimeOfTimestampHhMmAa(long timeStamp) {
        java.text.DateFormat objFormatter = new SimpleDateFormat("hh:mm aa");

        Calendar objCalendar = Calendar.getInstance();

        objCalendar.setTimeInMillis(timeStamp * 1000);//edit
        String result = objFormatter.format(objCalendar.getTime());
        objCalendar.clear();
        return result;

    }
public static void showKeyboard(Activity activity, EditText editText)
{
    try {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
    } catch (Exception e) {
        e.printStackTrace();
    }
}
    public static void hide_keyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static String getTimestampOfDate(String str_date) {
        java.text.DateFormat formatter = new SimpleDateFormat("dd-MM-yyy");
        Date date = null;
        try {
            date = (Date) formatter.parse(str_date);

        } catch (ParseException e1) {
            e1.printStackTrace();
        }
        long value = (date.getTime()) / 1000L;
        String timestampValue = String.valueOf(value);
        return timestampValue;
    }

    public static String getTimestampOfDateYYYMMDD(String str_date) {
        java.text.DateFormat formatter = new SimpleDateFormat("yyy-MM-dd");
        Date date = null;
        try {
            date = (Date) formatter.parse(str_date);

        } catch (ParseException e1) {
            e1.printStackTrace();
        }
        long value = (date.getTime()) / 1000L;
        String timestampValue = String.valueOf(value);
        return timestampValue;
    }

    public static String addDayToDate(String date, int noOfDay) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyy");
        Date dtStartDate = null;
        try {
            dtStartDate = sdf.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar c = Calendar.getInstance();
        c.setTime(dtStartDate);
        c.add(Calendar.DATE, noOfDay);  // number of days to add
        String resultDate = sdf.format(c.getTime());  // dt is now the new date
        return resultDate;
    }

    public static String addDayToDateOtherFormat(String date, int noOfDay) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyy");
        Date dtStartDate = null;
        try {
            dtStartDate = sdf.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar c = Calendar.getInstance();
        c.setTime(dtStartDate);
        c.add(Calendar.DATE, noOfDay);  // number of days to add
        String resultDate = sdf.format(c.getTime());  // dt is now the new date
        return resultDate;
    }


    public static void savePreferencesString(Context context, String key, String value) {

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.commit();

    }
    public static void saveLoginStatus(Context context, boolean login_status) {
        SharedPreferences info = context.getSharedPreferences(AppConstant.PREF_USER_INFO, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = info.edit();
        editor.putBoolean(AppConstant.PREF_KEYS.LOGIN_STATUS, login_status);
        editor.commit();
    }
    public static void deleteAllPreferences(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.commit();
    }
    public static int getIntPreferences(Context context, String key)
    {

        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        return sharedPreferences.getInt(key, 0);

    }
    public static boolean getPreferencesBoolean(Context context, String key) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPreferences.getBoolean(key, false);
    }

    public static File createDir() throws FileNotFoundException {
        File sdCard = Environment.getExternalStorageDirectory();
        File dir = new File(sdCard.getAbsolutePath() + "/SccOfflineApp/");
        if (!dir.exists()) {
            dir.mkdirs();
        }
        File file = new File(dir, "scc_database.sqlite");

        return file;

    }

    public static void showAlertExit(Context context, int titleId, int messageId,
                                     CharSequence positiveButtontxt,
                                     DialogInterface.OnClickListener positiveListener,
                                     CharSequence negativeButtontxt,
                                     DialogInterface.OnClickListener negativeListener) {
        Dialog dlg = new AlertDialog.Builder(context)
                // .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle(titleId)
                .setPositiveButton(positiveButtontxt, positiveListener)
                .setNegativeButton(negativeButtontxt, negativeListener)
                .setMessage(messageId).setCancelable(false).create();

        dlg.show();
    }

    public static void showAlert(String message, final View v, Context context) {

        if (v != null) {
            v.setEnabled(false);
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(message)
                .setCancelable(false)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();

                                if (v != null) {
                                    v.setEnabled(true);
                                }
                            }
                        });

        try {
            builder.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void showAlertNew(String title, String message,
                                    final Context context) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(message).setCancelable(false).setTitle(title)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });

        try {
            builder.show();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public static void showAlertLogin(String message, final Activity context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(message)
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setPositiveButton("Ok",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                Intent intent=new Intent(context, LoginActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                context.startActivity(intent);
                                dialog.dismiss();
                            }
                        });

        try {
            builder.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void ProgressDialog(String tittle, String message, Activity context) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        // set title
        alertDialogBuilder.setTitle(tittle);
        // set dialog message
        alertDialogBuilder.setMessage(message).setCancelable(false);
        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();
        // show it
        alertDialog.show();
        // After some action
        alertDialog.dismiss();
    }
/*
    public final static boolean isValidEmail(CharSequence target) {
		if (target == null) {  
			return false;
		} else {

			return android.util.Patterns.EMAIL_ADDRESS.matcher(target)
					.matches();
		}
	}*/


    public final static boolean isValidPhone(CharSequence target) {
        if (target == null) {
            return false;
        } else {

            return Patterns.PHONE.matcher(target)
                    .matches() && (target.length() >= 10 && target.length() <= 20);
        }
    }

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }

        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight
                + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }

    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);

        if (activity.getCurrentFocus() != null) {
            inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
        }
    }

    public static void SendEmail(Activity context, String To) {
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.setType("text/plain");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, To);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "");
        emailIntent.putExtra(Intent.EXTRA_TEXT, "");
        try {
            context.startActivity(Intent.createChooser(emailIntent, "Send mail..."));
            Log.i("Finished sending email", "");
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(context,
                    "There is no email client installed.", Toast.LENGTH_SHORT).show();
        }
    }

    public static String getTimeStamp() {

        long timestamp = (System.currentTimeMillis() / 1000L);
        String tsTemp = "" + timestamp;

        return "" + tsTemp;

    }



   /* public static boolean isUsername(CharSequence target) {
        if (target == null) {
            return false;
        } else {

            return android.util.Patterns.Con.matcher(target)
                    .matches();
        }
    }*/

    public static boolean isOnline(Context context) {
        ConnectivityManager conMgr = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = conMgr.getActiveNetworkInfo();

        if (netInfo == null || !netInfo.isConnected() || !netInfo.isAvailable()) {

            return false;
        }
        return true;
    }

    /* public static void savePreferencesString(Context context, String key, String value)
     {

         SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
         Editor editor = sharedPreferences.edit();
         editor.putString(key, value);
         editor.commit();

     }*/
    public static void savePreferences(Context context, String key, String value) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static void savePreferencesBoolean(Context context, String key, boolean value) {

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    public static String getPreferences(Context context, String key) {

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPreferences.getString(key, "");
    }



    public static void removePreferences(Activity context, String key) {

        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(key);

    }

    public static boolean getPreferencesBoolean(Activity context, String key) {

        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        return sharedPreferences.getBoolean(key, false);

    }


    public static String getDate(Context context, String timestamp_in_string) {
        long dv = (Long.valueOf(timestamp_in_string)) * 1000;// its need to be in milisecond
        Date df = new Date(dv);
        String vv = new SimpleDateFormat("MMM dd/yyyy,hh:mma").format(df);

        return vv;
    }
    public static String getDateWithFormat(Context context, String timestamp_in_string, String format) {
        long dv = (Long.valueOf(timestamp_in_string)) * 1000;// its need to be in milisecond
        Date df = new Date(dv);
        String vv = new SimpleDateFormat(format).format(df);

        return vv;
    }

    public static String getTime(String timestamp_in_string) {
        long dv = Long.valueOf(timestamp_in_string) * 1000;// its need to be in milisecond
        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        cal.setTimeInMillis(dv);
        String date = DateFormat.format("hh:mm:ss aa", cal).toString();
        return date;

    }
    public static String getTimeWithFormat(String timestamp_in_string, String format) {
        long dv = Long.valueOf(timestamp_in_string) * 1000;// its need to be in milisecond
        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        cal.setTimeInMillis(dv);
        String date = DateFormat.format(format, cal).toString();
        return date;

    }

    public static boolean isDateToday(long milliSeconds) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        Date getDate = calendar.getTime();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);

        Date startDate = calendar.getTime();

        return getDate.compareTo(startDate) > 0;

    }

    public static void showAlertTitle(String title, String message,
                                      final Context context) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(message).setCancelable(false).setTitle(title)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });

        try {
            builder.show();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public static String getHoursFromMillis(long milliseconds) {
        return "" + (int) ((milliseconds / (1000 * 60 * 60)) % 24);
    }

    public static Bitmap getBitMapFromImageURl(String imagepath, Activity activity) {

        Bitmap bitmapFromMapActivity = null;
        Bitmap bitmapImage = null;
        try {

            File file = new File(imagepath);
            // We need to recyle unused bitmaps
            if (bitmapImage != null) {
                bitmapImage.recycle();
            }
            bitmapImage = reduceImageSize(file, activity);
            int exifOrientation = 0;
            try {
                ExifInterface exif = new ExifInterface(imagepath);
                exifOrientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            } catch (IOException e) {
                e.printStackTrace();
            }

            int rotate = 0;

            switch (exifOrientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    rotate = 90;
                    break;

                case ExifInterface.ORIENTATION_ROTATE_180:
                    rotate = 180;
                    break;

                case ExifInterface.ORIENTATION_ROTATE_270:
                    rotate = 270;
                    break;
            }

            if (rotate != 0) {
                int w = bitmapImage.getWidth();
                int h = bitmapImage.getHeight();

                // Setting pre rotate
                Matrix mtx = new Matrix();
                mtx.preRotate(rotate);

                // Rotating Bitmap & convert to ARGB_8888, required by
                // tess

                Bitmap myBitmap = Bitmap.createBitmap(bitmapImage, 0, 0, w, h,
                        mtx, false);
                bitmapFromMapActivity = myBitmap;
            } else {
                int SCALED_PHOTO_WIDTH = 150;
                int SCALED_PHOTO_HIGHT = 200;
                Bitmap myBitmap = Bitmap.createScaledBitmap(bitmapImage,
                        SCALED_PHOTO_WIDTH, SCALED_PHOTO_HIGHT, true);
                bitmapFromMapActivity = myBitmap;
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return bitmapFromMapActivity;

    }

    public static Bitmap reduceImageSize(File f, Context context) {

        Bitmap m = null;
        try {

            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(new FileInputStream(f), null, o);

            final int REQUIRED_SIZE = 150;

            int width_tmp = o.outWidth, height_tmp = o.outHeight;

            int scale = 1;
            while (true) {
                if (width_tmp / 2 < REQUIRED_SIZE
                        || height_tmp / 2 < REQUIRED_SIZE)
                    break;
                width_tmp /= 2;
                height_tmp /= 2;
                scale *= 2;
            }

            // Decode with inSampleSize
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            o2.inPreferredConfig = Bitmap.Config.ARGB_8888;
            m = BitmapFactory.decodeStream(new FileInputStream(f), null, o2);
        } catch (FileNotFoundException e) {
            // Toast.makeText(context,
            // "Image File not found in your phone. Please select another image.",
            // Toast.LENGTH_LONG).show();
        } catch (Exception e) {
        }
        return m;
    }


    public static boolean checkEmailId(String emailId) {
        return Patterns.EMAIL_ADDRESS.matcher(emailId).matches();
    }


  /*  public static void setFragment(Fragment fragment, boolean removeStack, FragmentActivity activity, FrameLayout mContainer) {
        FragmentManager fragmentManager = activity.getSupportFragmentManager();
        FragmentTransaction ftTransaction = fragmentManager.beginTransaction();
        if (removeStack) {
            fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            ftTransaction.replace(mContainer.getId(), fragment);
        } else {
            ftTransaction.replace(mContainer.getId(), fragment);
            ftTransaction.addToBackStack(null);
        }
        ftTransaction.commit();
    }*/
    public static void setFragment(Fragment fragment, boolean removeStack, FragmentActivity activity, int mContainer) {
        FragmentManager fragmentManager = activity.getSupportFragmentManager();
        FragmentTransaction ftTransaction = fragmentManager.beginTransaction();
        if (removeStack) {
            fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            ftTransaction.replace(mContainer, fragment);
        } else {
            ftTransaction.replace(mContainer, fragment);
            ftTransaction.addToBackStack(null);
        }
        Log.e("TAG", "Fragment transition is completetd");
        ftTransaction.commit();
    }


    /**
     * This method is used for Enable View
     */
    public static void setEnableState(View view) {
        view.setAlpha(ALPHA_DARK);
        view.setEnabled(true);
        view.setClickable(true);
    }

    /**
     * This method is used for Disable View
     */

    public static void setDisableState(View view) {
        view.setAlpha(ALPHA_LIGHT);
        view.setEnabled(false);
        view.setClickable(false);
    }





    /*public static void logoutDialog(final Activity context) {
        TextView tvYes,tvNo,tvMsg;
        LayoutInflater inflater = LayoutInflater.from(context);
        final Dialog mDialog = new Dialog(context,
                android.R.style.Theme_Translucent_NoTitleBar);
        mDialog.setCanceledOnTouchOutside(true);
        mDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        mDialog.getWindow().setGravity(Gravity.CENTER);
//        mDialog.getWindow().getAttributes().windowAnimations = R.anim;
        WindowManager.LayoutParams lp = mDialog.getWindow().getAttributes();
        lp.dimAmount = 0.75f;
        mDialog.getWindow()
                .addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialog.getWindow();

        View dialoglayout = inflater.inflate(R.layout.dialog_log_out, null);
        mDialog.setContentView(dialoglayout);


        tvMsg = (TextView) mDialog.findViewById(R.id.tvMsg);
//        tvMsg.setTypeface(CommonUtils.setFontText(mContext));
        tvYes = (TextView) mDialog.findViewById(R.id.tvYes);
        tvNo = (TextView) mDialog.findViewById(R.id.tvNo);

        tvYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommonUtils.savePreferences(context, Constant.login, "");
                Intent intent = new Intent(context, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                ComponentName cn = intent.getComponent();
                Intent mainIntent = IntentCompat.makeRestartActivityTask(cn);
                context.startActivity(mainIntent);
                mDialog.dismiss();
            }
        });
        mDialog.show();
        tvNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
            }
        });

        mDialog.show();
    }*/
    /*public static void exitDialog(final Activity context) {
        TextView tvYes,tvNo,tvMsg;
        LayoutInflater inflater = LayoutInflater.from(context);
        final Dialog mDialog = new Dialog(context,
                android.R.style.Theme_Translucent_NoTitleBar);
        mDialog.setCanceledOnTouchOutside(true);
        mDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        mDialog.getWindow().setGravity(Gravity.CENTER);
//        mDialog.getWindow().getAttributes().windowAnimations = R.anim;
        WindowManager.LayoutParams lp = mDialog.getWindow().getAttributes();
        lp.dimAmount = 0.75f;
        mDialog.getWindow()
                .addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialog.getWindow();

        View dialoglayout = inflater.inflate(R.layout.dialog_exit, null);
        mDialog.setContentView(dialoglayout);


        tvMsg = (TextView) mDialog.findViewById(R.id.tvMsg);
//        tvMsg.setTypeface(CommonUtils.setFontText(mContext));
        tvYes = (TextView) mDialog.findViewById(R.id.tvYes);
        tvNo = (TextView) mDialog.findViewById(R.id.tvNo);

        tvYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mDialog.dismiss();
                context.finish();
            }
        });
        mDialog.show();
        tvNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
            }
        });

        mDialog.show();
    }*/


    /*.............font style................*/
    public static Typeface setHelveticaNeue(Context context) {

        Typeface TextFont = Typeface.createFromAsset(context.getAssets(), "fonts/HelveticaNeue.ttf");
        return TextFont;
    }

  /*  public static Typeface setHelveticaNeueLight(Context context) {
        Typeface TextFont = Typeface.createFromAsset(context.getAssets(), "fonts/HelveticaNeue-Light.ttf");
        return TextFont;
    }*/
    public static Typeface setHelvetica_Neue_LTComUltLt(Context context) {
        Typeface TextFont = Typeface.createFromAsset(context.getAssets(), "fonts/HelveticaNeueLTCom-UltLt.ttf");
        return TextFont;
    }
    public static Typeface sethelvetica_neue_ultralight(Context context) {
        Typeface TextFont = Typeface.createFromAsset(context.getAssets(), "fonts/ufonts.com_helvetica_neue_ultralight.ttf");
        return TextFont;
    }
    public static Typeface setHelveticaNeueCondensedBold(Context context) {
        Typeface TextFont = Typeface.createFromAsset(context.getAssets(), "fonts/HelveticaNeueCondensedBold.ttf");
        return TextFont;
    }
    public static Typeface setHelveticaNeueLTComTh(Context context) {
        Typeface TextFont = Typeface.createFromAsset(context.getAssets(), "fonts/HelveticaNeueLTCom-Th.ttf");
        return TextFont;
    }



    public static  boolean checkPermissionStorage(Activity context){
        int result = ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int result1 = ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE);
        if (result  == PackageManager.PERMISSION_GRANTED){

            if(result1 == PackageManager.PERMISSION_GRANTED){
                return true;
            }else {
                return false;
            }

        } else {
            return false;

        }
    }

    public static  void requestPermissionCamera(Activity activity){
        if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.CAMERA)){
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.CAMERA}, AppConstants.PERMISSION_REQUEST_CODE);
        } else {
            ActivityCompat.requestPermissions(activity,new String[]{Manifest.permission.CAMERA}, AppConstants.PERMISSION_REQUEST_CODE);
        }
    }


    public static boolean checkPermissionCamera(Activity context){
        int result = ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA);
        if (result  == PackageManager.PERMISSION_GRANTED){

            return true;

        } else {
            return false;

        }
    }
    public static   void requestPermissionStorage( Activity activity){
        if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE)|| ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.READ_EXTERNAL_STORAGE)){
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, AppConstants.PERMISSION_REQUEST_CODE);
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, AppConstants.PERMISSION_REQUEST_CODE);
        } else {
            ActivityCompat.requestPermissions(activity,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, AppConstants.PERMISSION_REQUEST_CODE);
            ActivityCompat.requestPermissions(activity,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, AppConstants.PERMISSION_REQUEST_CODE);
        }
    }

    public class AppConstants {
        public static final int PERMISSION_REQUEST_CODE=100;
        public static final String SENDER_ID = "479209210545"; // Project ID of App from Google Console
        public static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
        public static final String TOGGLE="not_check";
        public static final String CARD_DETAILS="card_details";
        public static final String FILTER="filter";
        public static final String COMMUNITY="expand";
        public static final String OFFICIAL_EVENTS="expand";
        public static final String HOME_PUBLIC="true_home_public";
        public static final String HOME_EVENTS="false_home_events";
        public static final String HOME_HISTORY="false_home_history";
        public static final String EVENT_FRAGMENT="event_fragment";
        public static final String HISTORY_FRAGMENT="event_fragment";
        public static final String PUBLIC_FRAGMENT="home_fragment";

        public static final String EVENT_ID="event_id";
        public static final String USER_ID="user_id";
        public static final String IS_USER="is_user";
        public static final String USER_PHONE="user_phone";
        public static final String USER_EMAIL="user_email";
        public final static String DEVICE_TYPE="android";
        public static final String DEVICE_TOKEN = "device_id";

        public static final String DATE_POOLING="0";
        public static final String LATITUDE = "latitude";
        public static final String LONGITUDE = "longitude";
        public static final String FCM_REGISTRATION_ID = "device_token";
    }

}





