package com.loyalty.model.customer;

import com.loyalty.webserivcemodel.LoyaltyHistory;
import com.loyalty.webserivcemodel.ObjectiveOption;
import com.loyalty.webserivcemodel.PendingOrdersDetails;

import java.util.List;

/**
 * Created by Arati Padhy on 01-11-2016.
 */
public class Questionnaire {
    public String questionnaireId;
    public String questionnaireName;
    public String points;
    public String isActive;
    public String questionId;
    public String question;
    public String responseType;
    public String objectiveOptionId;
    public List<ObjectiveOption> objectiveOption;

}
