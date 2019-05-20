import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IQuestionnaire } from 'app/shared/model/questionnaire.model';

@Component({
    selector: 'jhi-questionnaire-detail',
    templateUrl: './questionnaire-detail.component.html'
})
export class QuestionnaireDetailComponent implements OnInit {
    questionnaire: IQuestionnaire;

    constructor(protected activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ questionnaire }) => {
            this.questionnaire = questionnaire;
        });
    }

    previousState() {
        window.history.back();
    }
}
