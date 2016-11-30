package com.loyalty.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.daimajia.swipe.SwipeLayout;
import com.loyalty.Listeners.onDeleteListner;
import com.loyalty.R;
import com.loyalty.TotalPrice;
import com.loyalty.activity.customer.AddPaymentTwoActivity;
import com.loyalty.activity.customer.CatalogueActivity;
import com.loyalty.activity.customer.CatalougeDetailActivity;
import com.loyalty.interfaces.OnHistoryListener;
import com.loyalty.model.CardDetail;
import com.loyalty.restrofit.ApiClient;
import com.loyalty.restrofit.ApiInterface;
import com.loyalty.utils.AppConstant;
import com.loyalty.utils.CommonUtils;
import com.loyalty.webserivcemodel.CatalogueProductList;
import com.loyalty.webserivcemodel.ChangePasswordModel;
import com.loyalty.webserivcemodel.RequestModel;
import com.loyalty.webserivcemodel.ResponseModel;
import com.squareup.picasso.Picasso;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by keshavkumar on 22/11/16.
 */
public class PaymentCardAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>  {


    private Context mContext;
    private List<CardDetail> cardDetailList;
    private CardDetail cardDetail;
    private String  cardId;
    onDeleteListner ondeleteListner;

    public PaymentCardAdapter(Context context, List<CardDetail> cardDetailList,onDeleteListner ondeleteListner) {
        this.mContext = context;
        this.cardDetailList = cardDetailList;
        this.ondeleteListner = ondeleteListner;

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        RecyclerView.ViewHolder vh;

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_payment_card, parent, false);

        // set the view's size, margins, paddings and layout parameters
        vh = new ViewHolder(view);

        return vh;

    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        cardDetail = cardDetailList.get(position);


        String existingCCNmbr = cardDetail.cardNo;
        int i = 0;
        StringBuffer temp = new StringBuffer();
        while(i < (existingCCNmbr .length())){
            if(i > existingCCNmbr .length() -5){
                temp.append(existingCCNmbr.charAt(i));
            } else {
                temp.append("X");
            }
            i++;
        }
        ((ViewHolder) holder).tvCardNumber.setText(temp);
       // System.out.println();



    //  ((ViewHolder) holder).tvExpiryDate.setText(cardDetail.expiryDate);
        cardId = cardDetail.cardId;

        ((ViewHolder) holder).llRowCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, AddPaymentTwoActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.putExtra(AppConstant.CARDID,cardId);
                intent.putExtra(AppConstant.CARDNUMBER,cardDetail.cardNo);
                intent.putExtra(AppConstant.CARDEXPIRY,cardDetail.expiryDate);
                mContext.startActivity(intent);
            }
        });

        ((ViewHolder) holder).swipeLayout.setShowMode(SwipeLayout.ShowMode.PullOut);

        ((ViewHolder) holder).swipeLayout.addDrag(SwipeLayout.DragEdge.Right, ((ViewHolder) holder).swipeLayout.findViewById(R.id.bottom_wrapper));

        ((ViewHolder) holder).swipeLayout.addRevealListener(R.id.delete, new SwipeLayout.OnRevealListener() {
            @Override
            public void onReveal(View child, SwipeLayout.DragEdge edge, float fraction, int distance) {

            }
        });

        ((ViewHolder) holder).swipeLayout.findViewById(R.id.delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(CommonUtils.isNetworkConnected(mContext)){

                    deleteUploadBookService(position);
                }else {
                    CommonUtils.showAlert(mContext.getString(R.string.connection_error),v, mContext);
                }
            }

        });


    }

    private void deleteUploadBookService(final int position) {
        final ProgressDialog progressDialog = ProgressDialog.show(mContext, "", "Please wait...");
        ApiInterface apiServices= ApiClient.getClient().create(ApiInterface.class);

        Call<ResponseModel> call=apiServices.getDeleteCard(AppConstant.BASIC_TOKEN,cardId);

        call.enqueue(new retrofit2.Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                if (progressDialog != null && progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
                if (response!=null)
                {
                    if (response.body().responseCode.equalsIgnoreCase("200"))
                    {
                        cardDetailList.remove(position);
                        ondeleteListner.ondelete();
                        notifyDataSetChanged();
                    }
                    else {
                        CommonUtils.showToast(mContext,response.body().responseMessage);
                    }
                }

            }

            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {
                t.printStackTrace();
                if (progressDialog != null && progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
            }
        });
    }


    @Override
    public int getItemCount() {

        return cardDetailList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {


        SwipeLayout swipeLayout;
        LinearLayout llRowCard;
        TextView tvExpiryDate,tvCardNumber;

        public ViewHolder(View itemView) {
            super(itemView);
            swipeLayout = (SwipeLayout) itemView.findViewById(R.id.swipe);
            llRowCard = (LinearLayout) itemView.findViewById(R.id.llRowCard);
            tvExpiryDate = (TextView) itemView.findViewById(R.id.tvExpiryDate);
            tvCardNumber = (TextView) itemView.findViewById(R.id.tvCardNumber);

        }
    }
}
