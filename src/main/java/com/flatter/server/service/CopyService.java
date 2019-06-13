package com.flatter.server.service;

import com.flatter.server.domain.Questionnaire;
import com.flatter.server.domain.User;
import domain.QuestionnaireableUser;
import org.springframework.stereotype.Service;

@Service
public class CopyService {
    public CopyService() {
    }

    public QuestionnaireableUser createUserFromDTO(com.flatter.server.domain.Questionnaire questionnaire) {
        QuestionnaireableUser questionnaireableUser = new QuestionnaireableUser();
        copyUserFromDTO(questionnaire.getUser(), questionnaireableUser);
        copyOfferFromDTO(questionnaire, questionnaireableUser);
        return questionnaireableUser;
    }

    private void copyOfferFromDTO(Questionnaire userWithQuestionnaireDTO, QuestionnaireableUser questionnaireableUser) {
        questionnaireableUser.setConstructionYearMax(userWithQuestionnaireDTO.getConstructionYearMax());
        questionnaireableUser.setConstructionYearMin(userWithQuestionnaireDTO.getConstructionYearMin());
        questionnaireableUser.setFurnished(userWithQuestionnaireDTO.isIsFurnished());
        questionnaireableUser.setName("user");
        questionnaireableUser.setPets(userWithQuestionnaireDTO.isPets());
        questionnaireableUser.setRoomAmountMax(userWithQuestionnaireDTO.getRoomAmountMax());
        questionnaireableUser.setRoomAmountMin(userWithQuestionnaireDTO.getRoomAmountMin());
        questionnaireableUser.setType(userWithQuestionnaireDTO.getType());
        questionnaireableUser.setSmokingInside(userWithQuestionnaireDTO.isSmokingInside());
        questionnaireableUser.setTotalCostMax(userWithQuestionnaireDTO.getTotalCostMax());
        questionnaireableUser.setTotalCostMin(userWithQuestionnaireDTO.getTotalCostMin());
    }

    private void copyUserFromDTO(User user, QuestionnaireableUser questionnaireableUser) {
        questionnaireableUser.setEmail(user.getEmail());
        questionnaireableUser.setFirstName(user.getFirstName());
        questionnaireableUser.setLastName(user.getLastName());
        questionnaireableUser.setLogin(user.getLogin());
    }
}
