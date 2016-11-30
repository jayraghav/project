package com.loyalty.activity.customer;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.google.gson.Gson;
import com.loyalty.R;
import com.loyalty.adapter.customer.FeedBackQuestionAdapter;
import com.loyalty.fragment.customer.PendingOrdersFragment;
import com.loyalty.model.customer.Questionnaire;
import com.loyalty.restrofit.ApiClient;
import com.loyalty.restrofit.ApiInterface;
import com.loyalty.utils.AppConstant;
import com.loyalty.utils.CommonUtils;
import com.loyalty.webserivcemodel.EditProfileResponseModel;
import com.loyalty.webserivcemodel.OrderItems;
import com.loyalty.webserivcemodel.RequestModel;
import com.loyalty.webserivcemodel.ResponseModel;
import com.loyalty.webserivcemodel.UseranswerModel;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FeedBackQuestionActivity extends AppCompatActivity implements View.OnClickListener {

    private RecyclerView rvQuestion;
    private FeedBackQuestionAdapter questionAdapter;
    private Context context;
    private ImageView ivtoolbarleft;
    private TextView tvTitle,tvToolbarRight,tvSubmit;
    private Toolbar toolbar;
    private ProgressDialog progressDialog;
    private List<Questionnaire> allQuestionList;
    String storeId,orderId,businessId;
    private boolean isQuestionExist;
    private String navigate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        context=FeedBackQuestionActivity.this;
        allQuestionList = new ArrayList<>();
        storeId=CommonUtils.getPreferences(FeedBackQuestionActivity.this,"storeId");
        orderId=CommonUtils.getPreferences(FeedBackQuestionActivity.this,"orderId");
        businessId=CommonUtils.getPreferences(FeedBackQuestionActivity.this,"businessId");
        navigate=CommonUtils.getPreferences(FeedBackQuestionActivity.this,"navigate");

        findIds();
        setListener();
        setRecycler();
        questionAdapter=new FeedBackQuestionAdapter(context,allQuestionList);
        rvQuestion.setAdapter(questionAdapter);
        if (CommonUtils.isOnline(context)) {
            getQuestionnaireApi();
        }else {
            CommonUtils.showToast(context, AppConstant.PLEASE_CHECK_INTERNET);
        }
    }
    private void findIds() {
        rvQuestion=(RecyclerView)findViewById(R.id.rvQuestion);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        tvSubmit = (TextView) findViewById(R.id.tvSubmit);
        ivtoolbarleft = (ImageView) findViewById(R.id.iv_toolbar_left);
        tvToolbarRight = (TextView) findViewById(R.id.toolbar_title_right);
        tvTitle = (TextView) findViewById(R.id.toolbar_title);
        tvTitle.setText(R.string.feedback);
        tvToolbarRight.setText(R.string.skip);
        tvToolbarRight.setVisibility(View.VISIBLE);
        ivtoolbarleft.setVisibility(View.VISIBLE);
    }
    private void setListener() {
        tvToolbarRight.setOnClickListener(this);
        toolbar.setOnClickListener(this);
        tvSubmit.setOnClickListener(this);
        ivtoolbarleft.setOnClickListener(this);
    }
    public void setToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setNavigationIcon(R.mipmap.back);
    }
    private void setRecycler() {
        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(context);
        rvQuestion.setLayoutManager(layoutManager);
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.toolbar_title_right:
                if(navigate.equalsIgnoreCase("navigate"))
                {
                     CommonUtils.setFragment(new PendingOrdersFragment(),false,FeedBackQuestionActivity.this,R.id.flHome);
                }
                else {
                    CommonUtils.savePreferences(FeedBackQuestionActivity.this,"storeId","");
                    CommonUtils.savePreferences(FeedBackQuestionActivity.this,"businessId","");
                    CommonUtils.savePreferences(FeedBackQuestionActivity.this,"orderId","");
                    CommonUtils.savePreferences(FeedBackQuestionActivity.this,"amount","");
                    CommonUtils.savePreferences(FeedBackQuestionActivity.this,"navigate","");

                    startActivity(new Intent(context, HomeActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                }
                    break;
            case R.id.tvSubmit:
                if (CommonUtils.isOnline(context)) {
                    UserAnswerQuestionnairApi();
                }else {
                    CommonUtils.showToast(context, AppConstant.PLEASE_CHECK_INTERNET);
                }
                break;
            case R.id.toolbar:
                finish();
                break;

            case R.id.iv_toolbar_left:
                finish();
                break;
        }
    }
    public void getQuestionnaireApi() {
        ApiInterface apiServices= ApiClient.getClient().create(ApiInterface.class);
        String userId=CommonUtils.getPreferences(context,AppConstant.USER_ID);
        Call<ResponseModel> call=apiServices.getQuestionnaireApi(AppConstant.BASIC_TOKEN,userId,storeId,businessId);
        startProgressBar(context);
        call.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                stopProgressBar();
                if (response!=null && response.body()!=null) {
                    if(response.body().responseCode.equalsIgnoreCase("200")) {
//                        CommonUtils.showToast(context,response.body().responseMessage);
                        Log.e("tag","body of question"+response.body().toString());
                        Log.e("tag","json responce "+new Gson().toJson(response));
                        if(response.body().questionnaire!=null&&response.body().questionnaire.size()>0) {
                            allQuestionList.addAll(response.body().questionnaire);
                        } else {
                            CommonUtils.showToast(context,response.body().responseMessage);
                        }
                    }
                    questionAdapter.notifyDataSetChanged();
                }
            }
            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {
                stopProgressBar();
            }
        });
    }
    public void UserAnswerQuestionnairApi() {
        List<UseranswerModel> answers = new ArrayList<>();
        answers = questionAdapter.getData();
        ApiInterface apiServices=ApiClient.getClient().create(ApiInterface.class);
        RequestModel requestModel=new RequestModel();
        requestModel.userId=CommonUtils.getPreferences(context,AppConstant.USER_ID);
        requestModel.orderId=orderId;//CommonUtils.getPreferences(context,AppConstant.ORDER_ID);
        requestModel.businessId=CommonUtils.getPreferences(context,AppConstant.BUSINESS_ID);
        requestModel.questionnaireId=CommonUtils.getPreferences(context,AppConstant.QUESTIONNAIRE_ID);
        requestModel.setAnswers(answers);
        Call<ResponseModel> call=apiServices.getUserAnswerAPi(AppConstant.BASIC_TOKEN,requestModel);
      //  Log.e("tag", new Gson().toJson(requestModel));
        startProgressBar(context);
        call.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                stopProgressBar();
              //  Log.e("tag",new Gson().toJson(response));

                if (response!=null && response.body()!=null) {
                    if(response.body().responseCode.equalsIgnoreCase("200")) {
//                        CommonUtils.showToast(context,response.body().responseMessage);
//                        startActivity(new Intent(context,HomeActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                        if(navigate.equalsIgnoreCase("navigate"))
                        {
                            CommonUtils.savePreferences(FeedBackQuestionActivity.this,"storeId","");
                            CommonUtils.savePreferences(FeedBackQuestionActivity.this,"businessId","");
                            CommonUtils.savePreferences(FeedBackQuestionActivity.this,"orderId","");
                            CommonUtils.savePreferences(FeedBackQuestionActivity.this,"amount","");
                            CommonUtils.savePreferences(FeedBackQuestionActivity.this,"navigate","");

                           finish();
                        }
                        else {
                            CommonUtils.savePreferences(FeedBackQuestionActivity.this,"storeId","");
                            CommonUtils.savePreferences(FeedBackQuestionActivity.this,"businessId","");
                            CommonUtils.savePreferences(FeedBackQuestionActivity.this,"orderId","");
                            CommonUtils.savePreferences(FeedBackQuestionActivity.this,"amount","");
                            CommonUtils.savePreferences(FeedBackQuestionActivity.this,"navigate","");
                            startActivity(new Intent(context, HomeActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                        }


                    }
                }
            }
            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {
                stopProgressBar();
            }
        });

    }

    public void startProgressBar(Context context){
        progressDialog = ProgressDialog.show(context,null,AppConstant.PLEASE_WAIT,true,false);
    }

    public  void stopProgressBar(){
        if(progressDialog!=null){
            progressDialog.dismiss();
        }
    }

}
