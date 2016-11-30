package com.loyalty.adapter.customer;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.loyalty.R;
import com.loyalty.TotalPrice;
import com.loyalty.activity.customer.CatalogueActivity;
import com.loyalty.interfaces.OnHistoryListener;
import com.loyalty.webserivcemodel.CatalogueProductList;
import com.loyalty.webserivcemodel.ObjectiveOption;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by Arati.Padhy on 07-11-2016.
 */
public class MultiChoiceQuestionAdapter extends RecyclerView.Adapter<MultiChoiceQuestionAdapter.ViewHolder> {
    private Context mContext;
    private List<ObjectiveOption> objectiveList = new ArrayList<>();
    private List<String> list = new ArrayList<>();
    private String questionId = "";


    public MultiChoiceQuestionAdapter(Context context, List<ObjectiveOption> objectiveList, String questionId) {
        this.mContext = context;
        this.objectiveList = objectiveList;
        this.questionId = questionId;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(this.mContext);
        View view = layoutInflater.inflate(R.layout.row_multichoice_question, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        if(objectiveList.get(position).objectiveOptionValue!=null && objectiveList.get(position).objectiveOptionValue.length()>=0){
            holder.cbMultiPleAns.setText(objectiveList.get(position).objectiveOptionValue);
            Log.e("tag","objective values "+objectiveList.get(position).objectiveOptionValue);
        }

        holder.cbMultiPleAns.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    String questionOptionId="";
                    questionOptionId = objectiveList.get(position).objectiveOptionId +","+ questionId;
                    list.add(questionOptionId);
                }else {
                    String questionOptionId="";
                    questionOptionId = objectiveList.get(position).objectiveOptionId +","+ questionId;
                    list.remove(questionOptionId);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return objectiveList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private CheckBox cbMultiPleAns;

        public ViewHolder(View itemView) {
            super(itemView);
            cbMultiPleAns=(CheckBox)itemView.findViewById(R.id.cbMultiPleAns);
        }
    }

    public List<String> getOptionIds(){

        return list;
    }
}

