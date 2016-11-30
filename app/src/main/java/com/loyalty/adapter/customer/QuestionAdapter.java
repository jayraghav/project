package com.loyalty.adapter.customer;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import com.loyalty.R;
import com.loyalty.model.customer.Questionnaire;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Arati.padhy on 14/10/16.
 */

public class QuestionAdapter extends RecyclerView.Adapter<QuestionAdapter.MyViewHolder> {
    private Context context;
    private List<Questionnaire> questionList=new ArrayList<>();
    public static final int FIRST = 0;
    public static final int SECOND = 1;
    public static final int THIRD = 2;
    FirstViewHolder firstViewHolder;
    SecondViewHolder secondViewHolder;
    ThirdViewHolder thirdViewHolder;

    public QuestionAdapter(Context context, List<Questionnaire> questionList) {
        this.context = context;
        this.questionList = questionList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if(viewType==FIRST)
        {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row1, parent, false);
            return new FirstViewHolder(view);
        }
        else if(viewType==SECOND)
        {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row2, parent, false);
            return new SecondViewHolder(view);
        }
        else if(viewType==THIRD)
        {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row3, parent, false);
            return new SecondViewHolder(view);
        }
        else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row2, parent, false);
            return new SecondViewHolder(view);
        }
    }

    private void setCheckBoxListener() {

    }
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

//        if(holder.getItemViewType()==FIRST)
        if(questionList.get(position).responseType.equalsIgnoreCase("EMOJI"))
        {
            firstViewHolder = (FirstViewHolder) holder;
            firstViewHolder.cbHappy.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (firstViewHolder.cbHappy.isChecked())
                    {
                        firstViewHolder.cbJoke.setChecked(false);
                        firstViewHolder.cbAngry.setChecked(false);
                    }
                }
            });
            firstViewHolder.cbJoke.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (firstViewHolder.cbJoke.isChecked())
                    {
                        firstViewHolder.cbHappy.setChecked(false);
                        firstViewHolder.cbAngry.setChecked(false);
                    }
                }
            });
            firstViewHolder.cbAngry.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (firstViewHolder.cbAngry.isChecked())
                    {
                        firstViewHolder.cbJoke.setChecked(false);
                        firstViewHolder.cbHappy.setChecked(false);
                    }
                }
            });
        }
//        else if(holder.getItemViewType()==SECOND)
        else  if(questionList.get(position).responseType.equalsIgnoreCase("OBJECTIVE")) {
            secondViewHolder = (SecondViewHolder) holder;
            secondViewHolder.tvMultiChoiceQues.setText(questionList.get(position).question);
            secondViewHolder. rvMultiChoice.setLayoutManager(new LinearLayoutManager(context));

           /* secondViewHolder.cbA.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (secondViewHolder.cbA.isChecked())
                    {
                        secondViewHolder.cbB.setChecked(false);
                        secondViewHolder.cbC.setChecked(false);
                        secondViewHolder.cbD.setChecked(false);
                    }
                }
            });
            secondViewHolder.cbB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (secondViewHolder.cbB.isChecked())
                    {
                        secondViewHolder.cbA.setChecked(false);
                        secondViewHolder.cbC.setChecked(false);
                        secondViewHolder.cbD.setChecked(false);
                    }
                }
            });
            secondViewHolder.cbC.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (secondViewHolder.cbC.isChecked())
                    {
                        secondViewHolder.cbA.setChecked(false);
                        secondViewHolder.cbB.setChecked(false);
                        secondViewHolder.cbD.setChecked(false);
                    }
                }
            });
            secondViewHolder.cbD.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (secondViewHolder.cbD.isChecked())
                    {
                        secondViewHolder.cbA.setChecked(false);
                        secondViewHolder.cbB.setChecked(false);
                        secondViewHolder.cbC.setChecked(false);
                    }
                }
            });*/
        } else  if(questionList.get(position).responseType.equalsIgnoreCase("COMMENT")){
            thirdViewHolder.tvMultiTextAns.setText(questionList.get(position).question);

        }


    }
    @Override
    public int getItemViewType(int position) {
        return position;
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public MyViewHolder(View itemView) {
            super(itemView);

        }
    }
    public class FirstViewHolder extends MyViewHolder {

        private CheckBox cbHappy,cbJoke,cbAngry;
        private TextView tvEmojiText;
        public FirstViewHolder(View itemView) {
            super(itemView);
            cbHappy=(CheckBox)itemView.findViewById(R.id.cbHappy);
            cbJoke=(CheckBox)itemView.findViewById(R.id.cbJoke);
            cbAngry=(CheckBox)itemView.findViewById(R.id.cbAngry);
            tvEmojiText=(TextView) itemView.findViewById(R.id.tvEmojiText);
        }
    }
    public class SecondViewHolder extends MyViewHolder {

        private CheckBox cbA,cbB,cbC,cbD;
        private TextView tvMultiChoiceQues;
        private RecyclerView rvMultiChoice;
        public SecondViewHolder(View itemView) {
            super(itemView);
            cbA=(CheckBox)itemView.findViewById(R.id.cbA);
            cbB=(CheckBox)itemView.findViewById(R.id.cbB);
            cbC=(CheckBox)itemView.findViewById(R.id.cbC);
            cbD=(CheckBox)itemView.findViewById(R.id.cbD);
            tvMultiChoiceQues=(TextView) itemView.findViewById(R.id.tvMultiChoiceQues);
            rvMultiChoice=(RecyclerView) itemView.findViewById(R.id.rvMultiChoice);
        }
    }
    public class ThirdViewHolder extends MyViewHolder {

        private EditText etAnswer;
        private TextView tvMultiTextAns;
        public ThirdViewHolder(View itemView) {
            super(itemView);
            etAnswer=(EditText)itemView.findViewById(R.id.etAnswer);
            tvMultiTextAns=(TextView)itemView.findViewById(R.id.tvMultiTextAns);
        }
    }
    @Override
    public int getItemCount() {
        return questionList.size();
    }
}
