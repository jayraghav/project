package com.loyalty.adapter.customer;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.loyalty.R;
import com.loyalty.model.customer.Questionnaire;
import com.loyalty.utils.AppConstant;
import com.loyalty.utils.CommonUtils;
import com.loyalty.webserivcemodel.UseranswerModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Arati.padhy on 14/10/16.
 */

public class FeedBackQuestionAdapter extends RecyclerView.Adapter<FeedBackQuestionAdapter.MyViewHolder> {
    private Context context;
    private List<Questionnaire> questionList=new ArrayList<>();
    private List<UseranswerModel> answers=new ArrayList();
    private MultiChoiceQuestionAdapter multiChoiceAdapter;
    int serialNo = 0;

    public FeedBackQuestionAdapter(Context context, List<Questionnaire> questionList) {
        this.context = context;
        this.questionList = questionList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(this.context);
        View view = layoutInflater.inflate(R.layout.feedback_question, parent, false);
        serialNo+=1;

        return new MyViewHolder(view);
    }
    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        if (questionList.get(position).questionnaireId != null) {
            CommonUtils.savePreferences(context, AppConstant.QUESTIONNAIRE_ID,questionList.get(position).questionnaireId);
        }
        holder.tvQuesNumber.setText(String.valueOf(serialNo)+".");
        if(questionList.get(position).responseType.equalsIgnoreCase("EMOJI")) {
            holder.tvQuestion.setText(questionList.get(position).question);
            holder.llEmoji.setVisibility(View.VISIBLE);

            holder.cbHappy.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (holder.cbHappy.isChecked())
                    {
                        holder.cbJoke.setChecked(false);
                        holder.cbAngry.setChecked(false);
                        UseranswerModel useranswerModel = new UseranswerModel();
                        useranswerModel.objectiveOptionId = "";
                        useranswerModel.comments = "";
                        useranswerModel.questionId = questionList.get(position).questionId;
                        useranswerModel.emoji = "Good";
                        answers.add(useranswerModel);
                    }
                }
            });
            holder.cbJoke.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (holder.cbJoke.isChecked())
                    {
                        holder.cbHappy.setChecked(false);
                        holder.cbAngry.setChecked(false);
                        UseranswerModel useranswerModel = new UseranswerModel();
                        useranswerModel.questionId = questionList.get(position).questionId;
                        useranswerModel.emoji = "Normal";
                        useranswerModel.objectiveOptionId = "";
                        useranswerModel.comments = "";
                        answers.add(useranswerModel);
                    }
                }
            });
            holder.cbAngry.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (holder.cbAngry.isChecked())
                    {
                        holder.cbJoke.setChecked(false);
                        holder.cbHappy.setChecked(false);
                        UseranswerModel useranswerModel = new UseranswerModel();
                        useranswerModel.questionId = questionList.get(position).questionId;
                        useranswerModel.emoji = "Angry";
                        useranswerModel.objectiveOptionId = "";
                        useranswerModel.comments = "";
                        answers.add(useranswerModel);
                    }
                }
            });
        } else  if(questionList.get(position).responseType.equalsIgnoreCase("OBJECTIVE")) {
            holder.tvQuestion.setText(questionList.get(position).question);
            holder.llMultiChoice.setVisibility(View.VISIBLE);
            holder. rvMultiChoice.setLayoutManager(new LinearLayoutManager(context));
            multiChoiceAdapter=new MultiChoiceQuestionAdapter(context,questionList.get(position).objectiveOption, questionList.get(position).questionId);
            holder.rvMultiChoice.setAdapter(multiChoiceAdapter);

        } else  if(questionList.get(position).responseType.equalsIgnoreCase("COMMENT")){
            holder.tvQuestion.setText(questionList.get(position).question);
            holder.llComment.setVisibility(View.VISIBLE);
            UseranswerModel useranswerModel = new UseranswerModel();
            useranswerModel.questionId = questionList.get(position).questionId;
            useranswerModel.comments = holder.etAnswer.getText().toString();
            useranswerModel.objectiveOptionId = "";
            useranswerModel.emoji = "";
            answers.add(useranswerModel);
        }


    }
    @Override
    public int getItemViewType(int position) {
        return position;
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        private LinearLayout llEmoji,llMultiChoice,llComment;
        private CheckBox cbHappy,cbJoke,cbAngry;
        private TextView tvQuestion,tvQuesNumber;
        private RecyclerView rvMultiChoice;
        private EditText etAnswer;
        private ImageView ivHappy,ivJoke,ivAngry;

        public MyViewHolder(View itemView) {
            super(itemView);
            llEmoji=(LinearLayout)itemView.findViewById(R.id.llEmoji);
            llMultiChoice=(LinearLayout)itemView.findViewById(R.id.llMultiChoice);
            llComment=(LinearLayout)itemView.findViewById(R.id.llComment);
            tvQuestion=(TextView) itemView.findViewById(R.id.tvQuestion);
            tvQuesNumber=(TextView) itemView.findViewById(R.id.tvQuesNumber);

            cbHappy=(CheckBox)itemView.findViewById(R.id.cbHappy);
            cbJoke=(CheckBox)itemView.findViewById(R.id.cbJoke);
            cbAngry=(CheckBox)itemView.findViewById(R.id.cbAngry);

            ivHappy=(ImageView)itemView.findViewById(R.id.ivHappy);
            ivJoke=(ImageView)itemView.findViewById(R.id.ivJoke);
            ivAngry=(ImageView)itemView.findViewById(R.id.ivAngry);

            rvMultiChoice=(RecyclerView) itemView.findViewById(R.id.rvMultiChoice);
            etAnswer=(EditText)itemView.findViewById(R.id.etAnswer);
        }
    }

    @Override
    public int getItemCount() {
        return questionList.size();
    }

    public List<UseranswerModel> getData()
    {
        List<String> al=new ArrayList<>();
        al= multiChoiceAdapter.getOptionIds();
        Log.e("Size of Al::::", al.size()+"");
        for(int i=0;i<al.size();i++)
        {
            UseranswerModel useranswerModel = new UseranswerModel();
            String[] objective = al.get(i).split(",");
            if(al.size()==1){
                useranswerModel.objectiveOptionId = objective[0];
                useranswerModel.questionId = objective[1];
            }else{
                useranswerModel.objectiveOptionId = useranswerModel.objectiveOptionId +","+objective[0];
                useranswerModel.questionId = objective[1];
            }
            answers.add(useranswerModel);
        }

        return answers;

    }
}
