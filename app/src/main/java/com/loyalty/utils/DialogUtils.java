package com.loyalty.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.loyalty.R;


public class DialogUtils {
    public static Dialog createCustomDialog(Context context, int layoutResourceId) {

        Dialog dialog = new Dialog(context, android.R.style.Theme_Translucent_NoTitleBar);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(layoutResourceId);
        WindowManager.LayoutParams layoutParams = dialog.getWindow().getAttributes();
        layoutParams.dimAmount = .7f;
        layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
        dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        dialog.getWindow().setAttributes(layoutParams);
        return dialog;
    }

    public static Dialog hide_keyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        return null;
    }
    public static void exitDialog(final Activity context) {
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

        View dialoglayout = inflater.inflate(R.layout.logout_dialog, null);
        mDialog.setContentView(dialoglayout);


        tvMsg = (TextView) mDialog.findViewById(R.id.tv_logout);
        tvYes = (TextView) mDialog.findViewById(R.id.tvYes);
        tvNo = (TextView) mDialog.findViewById(R.id.tvNo);
        tvMsg.setText(R.string.are_you_sure_want_to_exit);

        tvYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mDialog.dismiss();
                context.moveTaskToBack(true);

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
    }
    public static void LogOutDialog(final Activity context) {
        final TextView tvYes,tvNo;
        LayoutInflater inflater = LayoutInflater.from(context);
        final Dialog mDialog = new Dialog(context, android.R.style.Theme_Translucent_NoTitleBar);
        mDialog.setCanceledOnTouchOutside(true);
        mDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mDialog.getWindow().setGravity(Gravity.CENTER);
        WindowManager.LayoutParams lp = mDialog.getWindow().getAttributes();
        lp.dimAmount = 0.75f;
        mDialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialog.getWindow();
        View dialoglayout = inflater.inflate(R.layout.logout_dialog, null);
        mDialog.setContentView(dialoglayout);
        tvYes = (TextView) mDialog.findViewById(R.id.tvYes);
        tvNo = (TextView) mDialog.findViewById(R.id.tvNo);

        tvYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


//                Intent intent = new Intent(context, LoginActivity.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                context.startActivity(intent);
//                context.finish();
//                mDialog.dismiss();
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
    }
    public static void showAlertOk(String message, Context context) {


        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(message)
                .setCancelable(false)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();


                            }
                        });

        try {
            builder.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void buildAlertMessageNoGps(final Context context) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        context.startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }
}
