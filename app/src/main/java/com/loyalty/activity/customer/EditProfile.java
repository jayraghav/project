package com.loyalty.activity.customer;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.EGLDisplay;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.vision.text.Line;
import com.google.gson.Gson;
import com.loyalty.R;
import com.loyalty.cropimage.CropImage;
import com.loyalty.image.CircleTransformation;
import com.loyalty.restrofit.ApiClient;
import com.loyalty.restrofit.ApiInterface;
import com.loyalty.utils.AppConstant;
import com.loyalty.utils.CheckPermission;
import com.loyalty.utils.CommonUtils;
import com.loyalty.utils.TakePictureUtils;
import com.loyalty.webserivcemodel.EditProfileResponseModel;
import com.loyalty.webserivcemodel.RequestModel;
import com.loyalty.webserivcemodel.ResponseModel;
import com.loyalty.webserivcemodel.UserProfileModel;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class EditProfile extends AppCompatActivity implements View.OnClickListener,TextWatcher{
    private TextView tvFirstName,tvLastName,tvEmail,tvMobileNumber,tvCardType,tvCardNo,tvCvv,tvExpiryDate,tvChangePassword,tvExpiryDateValue,tvTitle,tv_errorfirstName,tv_errorlastName,tv_erroremail,tv_errormobile,tv_errorcardType,tv_errorcardNumber,tv_errorCvv;
    private EditText etFirstName,etLastName,etEmail,etMobileNumber,etCountryCode,etAreaCode,etCvv,etExpiryDate;
    private Context context;
    private boolean isEditable;
    private  boolean profile=true;
    private String imageName;
    private ImageView ivUploadImage,iv_toolbar_right2;
    private byte[] byteArrayImage;
    private Calendar currentCalender;
    private int currentDay = +1, currentMonth = +1, currentYear = +1;
    private int x=0;
    private ImageView ivProfileImage;
    private Toolbar toolbar;
    private LinearLayout llMobileNumber,llLastName,llCardNo,llExpiryDate;
    private String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    private String TAG=EditProfile.class.getSimpleName();
    private ProgressDialog progressDialog;
    String path = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        context=EditProfile.this;
        currentCalender = Calendar.getInstance(TimeZone.getDefault());
        currentDay = currentCalender.get(Calendar.DATE);
        currentMonth = currentCalender.get(Calendar.MONTH) + 1;
        currentYear = currentCalender.get(Calendar.YEAR);
        getIds();
        setToolbar();
        setFonts();
        setListeners();
        if(CommonUtils.isOnline(context)) {
            getProfileAPi();
        } else {
            CommonUtils.showToast(context, AppConstant.PLEASE_CHECK_INTERNET);
        }

        etEmail.setEnabled(false);
        etEmail.setClickable(false);
    }
    private void setListeners() {
        ivUploadImage.setOnClickListener(this);
        iv_toolbar_right2.setOnClickListener(this);
        iv_toolbar_right2.setImageResource(R.mipmap.save);
        tvExpiryDateValue.setOnClickListener(this);
        etFirstName.addTextChangedListener(this);
        etLastName.addTextChangedListener(this);
        etMobileNumber.addTextChangedListener(this);
        etCountryCode.addTextChangedListener(this);
        etAreaCode.addTextChangedListener(this);
        etEmail.addTextChangedListener(this);
        etCvv.addTextChangedListener(this);
    }
    private void getIds()
    {
        toolbar=(Toolbar) findViewById(R.id.toolbar);
        tvFirstName=(TextView)findViewById(R.id.tvFirstName);
        tvLastName=(TextView)findViewById(R.id.tvLastName);
        tvEmail=(TextView)findViewById(R.id.tvEmail);
        tvMobileNumber=(TextView)findViewById(R.id.tvMobileNumber);
        tvCardType=(TextView)findViewById(R.id.tvCardType);
        tvCardNo=(TextView)findViewById(R.id.tvCardNo);
        tvCvv=(TextView)findViewById(R.id.tvCvv);
        tvExpiryDate=(TextView)findViewById(R.id.tvExpiryDate);
        tvExpiryDateValue=(TextView)findViewById(R.id.tvExpiryDateValue);
        ivProfileImage=(ImageView)findViewById(R.id.ivProfileImage);
        tvTitle=(TextView)findViewById(R.id.toolbar_title);
        tv_errorfirstName=(TextView)findViewById(R.id.tv_errorfirstName);
        tv_errorlastName=(TextView)findViewById(R.id.tv_errorlastName);
        tv_erroremail=(TextView)findViewById(R.id.tv_erroremail);
        tv_errormobile=(TextView)findViewById(R.id.tv_errormobile);
        tv_errorcardType=(TextView)findViewById(R.id.tv_errorcardType);
        tv_errorcardNumber=(TextView)findViewById(R.id.tv_errorcardNumber);
        tv_errorCvv=(TextView)findViewById(R.id.tv_errorCvv);
        llLastName=(LinearLayout)findViewById(R.id.llLastName);
        llMobileNumber=(LinearLayout)findViewById(R.id.llMobileNumber);
        llCardNo=(LinearLayout)findViewById(R.id.llCardNo);
        llExpiryDate=(LinearLayout)findViewById(R.id.llExpiryDate);
        etFirstName=(EditText)findViewById(R.id.etFirstName);
        etLastName=(EditText)findViewById(R.id.etLastName);
        etEmail=(EditText)findViewById(R.id.etEmail);
        etMobileNumber=(EditText)findViewById(R.id.etMobileNumber);
        etCountryCode=(EditText)findViewById(R.id.etCountryCode);
        etAreaCode=(EditText)findViewById(R.id.etAreaCode);
        etCvv=(EditText)findViewById(R.id.etCvv);
        ivUploadImage=(ImageView)findViewById(R.id.ivUploadImage);
        iv_toolbar_right2=(ImageView)findViewById(R.id.iv_toolbar_right2);




    }
    public void setToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setNavigationIcon(R.mipmap.back);
        toolbar.setNavigationOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                }

        );
        tvTitle.setText("Edit Profile");
        iv_toolbar_right2.setVisibility(View.VISIBLE);

    }
    private void setFonts() {

        tvFirstName.setTypeface(CommonUtils.setBook(context));
        tvLastName.setTypeface(CommonUtils.setBook(context));
        tvEmail.setTypeface(CommonUtils.setBook(context));
        tvMobileNumber.setTypeface(CommonUtils.setBook(context));
        tvCardType.setTypeface(CommonUtils.setBook(context));
        tvCardNo.setTypeface(CommonUtils.setBook(context));
        tvCvv.setTypeface(CommonUtils.setBook(context));
        tvExpiryDate.setTypeface(CommonUtils.setBook(context));
        tvExpiryDateValue.setTypeface(CommonUtils.setBook(context));
        etFirstName.setTypeface(CommonUtils.setBook(context));
        etLastName.setTypeface(CommonUtils.setBook(context));
        etEmail.setTypeface(CommonUtils.setBook(context));
        etMobileNumber.setTypeface(CommonUtils.setBook(context));
        etCountryCode.setTypeface(CommonUtils.setBook(context));
        etAreaCode.setTypeface(CommonUtils.setBook(context));
        etCvv.setTypeface(CommonUtils.setBook(context));
        tv_errorfirstName.setTypeface(CommonUtils.setBook(context));
        tv_errorlastName.setTypeface(CommonUtils.setBook(context));
        tv_errormobile.setTypeface(CommonUtils.setBook(context));
        tv_errorcardType.setTypeface(CommonUtils.setBook(context));
        tv_errorcardNumber.setTypeface(CommonUtils.setBook(context));
        tv_errorCvv.setTypeface(CommonUtils.setBook(context));
        tv_erroremail.setTypeface(CommonUtils.setBook(context));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivUploadImage: {
                if (CheckPermission.checkIsMarshMallowVersion()) {
                    if (!CheckPermission.checkCameraPermission(context)) {
                        CheckPermission.requestCameraPermission((Activity) context, CheckPermission.REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
                    }
                    else {
                        addPhotoDialog();
                    }
                } else {
                    addPhotoDialog();
                }
                break;
            }
            case R.id.iv_toolbar_right2: {
                if(checkValidation()){
                    if (CommonUtils.isOnline(context)) {
                        editProfileApi();
                    }else {
                        CommonUtils.showToast(context, AppConstant.PLEASE_CHECK_INTERNET);
                    }
                }
            }
            break;
            case R.id.tvExpiryDateValue: {
                DatePickerDialog();
            }
            break;
        }
    }
    private void DatePickerDialog() {
        // Log.e(TAG, "DatePickerDialog");
        final Calendar c = Calendar.getInstance();
        final int mYear = c.get(Calendar.YEAR);
        final int mMonth = c.get(Calendar.MONTH);
        final int mDay = c.get(Calendar.DAY_OF_MONTH);

        final DatePickerDialog dpd = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {


            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                String myFormat = "MMM dd, yyyy"; //In which you need put here
                SimpleDateFormat sdformat = new SimpleDateFormat();
                c.set(Calendar.YEAR, year);
                c.set(Calendar.MONTH, monthOfYear);
                c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                tvExpiryDateValue.setText("" + dayOfMonth + "/" + (monthOfYear - 1) + "/" +year );
            }
        }, mYear, mMonth, mDay - 1);
        long setDate = Calendar.getInstance().getTimeInMillis();/*-1000*60*24;*/
        dpd.getDatePicker().setMinDate(setDate);
        dpd.setCanceledOnTouchOutside(true);

        dpd.show();
    }


    boolean checkIsDateValid(int selectedYear, int selectedMonth, int selectedDay) {

        if (currentDay != -1 && currentMonth != -1) {
            if (selectedYear < currentYear) {
                return true;
            } else if (selectedYear == currentYear) {
                if (selectedMonth < currentMonth) {
                    return true;
                } else if (selectedMonth == currentMonth) {
                    return selectedDay < currentDay;
                } else {
                    return false;
                }
            } else {
                return false;
            }
        }
        return false;
    }
    private boolean checkValidation() {
        if (etFirstName.getText().toString().trim().isEmpty()) {
            tv_errorfirstName.setText(getString(R.string.please_enter_first_name));
            tv_errorfirstName.setVisibility(View.VISIBLE);
            tv_errorlastName.setVisibility(View.GONE);
            tv_errormobile.setVisibility(View.GONE);
            tv_errorcardType.setVisibility(View.GONE);
            tv_errorcardNumber.setVisibility(View.GONE);
            tv_errorCvv.setVisibility(View.GONE);
            etFirstName.requestFocus();
            return false;
        } else if(etLastName.getText().toString().trim().equalsIgnoreCase("")) {
            tv_errorlastName.setText(getString(R.string.please_enter_last_name));
            tv_errorlastName.setVisibility(View.VISIBLE);
            tv_errorfirstName.setVisibility(View.GONE);
            tv_erroremail.setVisibility(View.GONE);
            tv_errormobile.setVisibility(View.GONE);
            tv_errorcardType.setVisibility(View.GONE);
            tv_errorcardNumber.setVisibility(View.GONE);
            tv_errorCvv.setVisibility(View.GONE);
            etLastName.requestFocus();
            return false;
        }
        else if(etEmail.getText().toString().trim().equalsIgnoreCase("")) {
            tv_erroremail.setText(getString(R.string.please_enter_email));
            tv_erroremail.setVisibility(View.VISIBLE);
            tv_errorfirstName.setVisibility(View.GONE);
            tv_errorlastName.setVisibility(View.GONE);
            tv_errorcardType.setVisibility(View.GONE);
            tv_errormobile.setVisibility(View.GONE);
            tv_errorcardNumber.setVisibility(View.GONE);
            tv_errorCvv.setVisibility(View.GONE);
            etEmail.requestFocus();
            return false;

        }else if(!(etEmail.getText().toString().trim().matches(EMAIL_PATTERN))) {
            tv_erroremail.setText(getString(R.string.enter_valid_emailid));
            tv_erroremail.setVisibility(View.VISIBLE);
            tv_errorfirstName.setVisibility(View.GONE);
            tv_errorlastName.setVisibility(View.GONE);
            tv_errorcardType.setVisibility(View.GONE);
            tv_errormobile.setVisibility(View.GONE);
            tv_errorcardNumber.setVisibility(View.GONE);
            tv_errorCvv.setVisibility(View.GONE);
            etEmail.requestFocus();
            return false;
        } else if(etMobileNumber.getText().toString().trim().equalsIgnoreCase("")) {
            tv_errormobile.setText(getString(R.string.please_enter_mobile_number));
            tv_errormobile.setVisibility(View.VISIBLE);
            tv_errorfirstName.setVisibility(View.GONE);
            tv_errorlastName.setVisibility(View.GONE);
            tv_erroremail.setVisibility(View.GONE);
            tv_errorcardType.setVisibility(View.GONE);
            tv_errorcardNumber.setVisibility(View.GONE);
            tv_errorCvv.setVisibility(View.GONE);
            etMobileNumber.requestFocus();
            return false;

        }else if(etMobileNumber.getText().length() < 10)  {
            tv_errormobile.setText("*Mobile number must be atleast of 10 characters");
            tv_errormobile.setVisibility(View.VISIBLE);
            tv_errorfirstName.setVisibility(View.GONE);
            tv_errorlastName.setVisibility(View.GONE);
            tv_erroremail.setVisibility(View.GONE);
            tv_errorcardType.setVisibility(View.GONE);
            tv_errorcardNumber.setVisibility(View.GONE);
            tv_errorCvv.setVisibility(View.GONE);
            etMobileNumber.requestFocus();
            return false;
        }
        else if(etCvv.getText().toString().trim().equalsIgnoreCase("")) {
            tv_errorCvv.setText(getString(R.string.please_enter_cvv));
            tv_errorCvv.setVisibility(View.VISIBLE);
            tv_errorfirstName.setVisibility(View.GONE);
            tv_errormobile.setVisibility(View.GONE);
            tv_errorcardType.setVisibility(View.GONE);
            tv_errorcardNumber.setVisibility(View.GONE);
            tv_errorlastName.setVisibility(View.GONE);
            etCvv.requestFocus();
            return false;
        }  else if(etCvv.getText().toString().trim().length()<3) {
            tv_errorCvv.setText(getString(R.string.please_enter_cvv_must));
            tv_errorCvv.setVisibility(View.VISIBLE);
            tv_errorfirstName.setVisibility(View.GONE);
            tv_errormobile.setVisibility(View.GONE);
            tv_errorcardType.setVisibility(View.GONE);
            tv_errorcardNumber.setVisibility(View.GONE);
            tv_errorlastName.setVisibility(View.GONE);
            etCvv.requestFocus();
            return false;

        }else {
            return true;
        }
    }
    /*................................open camera...........................*/
    private void addPhotoDialog() {
        final CharSequence[] items = {getString(R.string.take_a_photo), getString(R.string.from_gallery), getString(R.string.cancel)};
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(getString(R.string.choose_photo));
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals(getString(R.string.take_a_photo))) {
                    imageName = "picture";
                    TakePictureUtils.takePicture(EditProfile.this, imageName);
                } else if (items[item].equals(getString(R.string.from_gallery))) {
                    imageName = "picture";
                    TakePictureUtils.openGallery(EditProfile.this);

                } else {
                    dialog.dismiss();
                }
            }
        });
        builder.create().show();
        builder.setCancelable(true);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (requestCode == TakePictureUtils.PICK_GALLERY) {
            if (resultCode == Activity.RESULT_OK) {
                try {
                    InputStream inputStream = this.getContentResolver().openInputStream(intent.getData());
                    FileOutputStream fileOutputStream = new FileOutputStream(new File(this.getExternalFilesDir("temp"), imageName + ".jpg"));
                    TakePictureUtils.copyStream(inputStream, fileOutputStream);
                    fileOutputStream.close();
                    inputStream.close();
                    TakePictureUtils.startCropImage(this, imageName + ".jpg");
                } catch (Exception e) {
                    Toast.makeText(this, "Error in picture", Toast.LENGTH_SHORT).show();
                }
            }
        } else if (requestCode == TakePictureUtils.TAKE_PICTURE) {
            // imageName="picture";
            if (resultCode == Activity.RESULT_OK) {
                TakePictureUtils.startCropImage(this, imageName + ".jpg");
            }


        } else if (requestCode == TakePictureUtils.CROP_FROM_CAMERA) {
            //  imageName="picture";
            if (resultCode == Activity.RESULT_OK) {

                if (intent != null) {
                    path = intent.getStringExtra(CropImage.IMAGE_PATH);
                }
                if (path == null) {
                    return;
                }
                Bitmap bm = BitmapFactory.decodeFile(path);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bm.compress(Bitmap.CompressFormat.JPEG, 100, baos); //bm is the bitmap object
                byteArrayImage = baos.toByteArray();
                imageName= Base64.encodeToString(byteArrayImage, Base64.DEFAULT);
                ((CircleImageView) ivProfileImage).setImageBitmap(bm);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case CheckPermission.REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    addPhotoDialog();
                } else {
                    CommonUtils.showToast(context, getString(R.string.camera_permission_denial));
                }
                break;
        }
    }

    private void startCropImage(Activity context, String fileName) {
        Intent intent = new Intent(context, CropImage.class);
        intent.putExtra(CropImage.IMAGE_PATH, new File(context.getExternalFilesDir("temp"), fileName).getPath());
        intent.putExtra(CropImage.SCALE, true);
        intent.putExtra(CropImage.ASPECT_X, 1);
        intent.putExtra(CropImage.ASPECT_Y, 1);
        intent.putExtra(CropImage.OUTPUT_X, 200);
        intent.putExtra(CropImage.OUTPUT_Y, 200);
        startActivityForResult(intent, TakePictureUtils.CROP_FROM_CAMERA);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        if (etFirstName.getText().toString().length() > 0) {
            tv_errorfirstName.setVisibility(View.GONE);
        }if (etLastName.getText().toString().length() > 0) {
            tv_errorlastName.setVisibility(View.GONE);
        }if (etEmail.getText().toString().length() > 0) {
            tv_erroremail.setVisibility(View.GONE);
        }if (etCountryCode.getText().toString().length() > 0) {
            tv_errorcardType.setVisibility(View.GONE);
        }if (etAreaCode.getText().toString().length() > 0) {
            tv_errorcardNumber.setVisibility(View.GONE);
        }if (etCvv.getText().toString().length() > 0) {
            tv_errorCvv.setVisibility(View.GONE);
        }if (etMobileNumber.getText().toString().length() > 0) {
            tv_errormobile.setVisibility(View.GONE);
        }
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {

    }
    /*....................camera..........................*/


    public void getProfileAPi() {

        ApiInterface apiServices= ApiClient.getClient().create(ApiInterface.class);
        Call<UserProfileModel> call=apiServices.userInfo(AppConstant.BASIC_TOKEN,Integer.parseInt(CommonUtils.getPreferences(this, AppConstant.USER_ID)));
        startProgressBar(context);
        call.enqueue(new Callback<UserProfileModel>() {
            @Override
            public void onResponse(Call<UserProfileModel> call, Response<UserProfileModel> response) {
                stopProgressBar();
                Log.e(TAG,response.body().toString());
                if (response!=null)
                {
                    if(response.body().responseCode.equalsIgnoreCase("200"))
                    {
                        etFirstName.setText(response.body().firstName);
                        etLastName.setText(response.body().lastName);
                        etEmail.setText(response.body().email);
                        etMobileNumber.setText(response.body().phone);

                        if(response.body().countryCode!=null&& response.body().areaCode!=null||response.body()
                                .areaCode.equalsIgnoreCase("")&&response.body().countryCode.equalsIgnoreCase("")) {

                            etCountryCode.setText(response.body().countryCode);
                            etAreaCode.setText(response.body().areaCode);
                        }
                        etFirstName.setSelection(etFirstName.getText().length());
                        etLastName.setSelection(etLastName.getText().length());
                        etEmail.setSelection(etEmail.getText().length());
                        etMobileNumber.setSelection(etMobileNumber.getText().length());
                        etAreaCode.setSelection(etAreaCode.getText().length());
                        etCountryCode.setSelection(etCountryCode.getText().length());
                        etCvv.setSelection(etCvv.getText().length());
                        String profileImage=response.body().profileImage;
                        Log.e("Image","Images : "+profileImage);

                        if(profileImage!=null && profileImage.trim().length()!=0) {
                            Picasso.with(context).load(profileImage.trim()).error(R.mipmap.round).into(ivProfileImage);
                        }

                    }
                    else if(response.body().responseCode.equalsIgnoreCase("201"))
                    {
                       CommonUtils.showToast(context,response.body().responseMessage);
                    }
                    else {

                    }
                }
            }

            @Override
            public void onFailure(Call<UserProfileModel> call, Throwable t) {
               stopProgressBar();
                CommonUtils.showToast(context,"Please try again");
            }
        });
    }

    public void editProfileApi() {
        startProgressBar(context);
        ApiInterface apiServices=ApiClient.getClient().create(ApiInterface.class);
        RequestModel requestModel=new RequestModel();
        requestModel.userId=CommonUtils.getPreferences(context,AppConstant.USER_ID);
        requestModel.email=etEmail.getText().toString().trim();
        requestModel.firstName=etFirstName.getText().toString().trim();
        requestModel.lastName=etLastName.getText().toString().trim();
        requestModel.phone=etMobileNumber.getText().toString().trim();
        requestModel.areaCode=etAreaCode.getText().toString().trim();
        requestModel.countryCode=etCountryCode.getText().toString().trim();
        requestModel.profileImage = imageName;


            Call<EditProfileResponseModel> call=apiServices.editProfile(AppConstant.BASIC_TOKEN,requestModel);
        Log.e(TAG,"request of edit profile" +new Gson().toJson(requestModel));

        call.enqueue(new Callback<EditProfileResponseModel>() {
            @Override
            public void onResponse(Call<EditProfileResponseModel> call, Response<EditProfileResponseModel> response) {
                stopProgressBar();
                Log.e(TAG,"profile updated respponce "+new Gson().toJson(response));

                if (response!=null && response.body()!=null) {
                    if(response.body().responseCode.equalsIgnoreCase("200")) {
//                        CommonUtils.showToast(context,response.body().responseMessage);
                        Intent intent=new Intent(EditProfile.this,ProfileActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();
                    }
                    else if (response.body().responseCode.equalsIgnoreCase("201")) {
                        CommonUtils.showToast(context,response.body().responseMessage);
                    }
                    else if (response.body().responseCode.equalsIgnoreCase("400")) {
                        CommonUtils.showToast(context,response.body().responseMessage);
                    }
                    else {

                    }
                }
            }

            @Override
            public void onFailure(Call<EditProfileResponseModel> call, Throwable t) {
                stopProgressBar();
            }
        });

    }
    public void startProgressBar(Context context){
        progressDialog = ProgressDialog.show(context,null, AppConstant.PLEASE_WAIT,true,false);
    }

    public  void stopProgressBar(){
        if(progressDialog!=null){
            progressDialog.dismiss();
        }
    }
}

