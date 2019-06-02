import { Component, OnInit } from '@angular/core';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiAlertService } from 'ng-jhipster';
import { IQuestionnaire, Questionnaire } from 'app/shared/model/questionnaire.model';
import { QuestionnaireService } from './questionnaire.service';
import { IUser, UserService } from 'app/core';

@Component({
  selector: 'jhi-questionnaire-update',
  templateUrl: './questionnaire-update.component.html'
})
export class QuestionnaireUpdateComponent implements OnInit {
  questionnaire: IQuestionnaire;
  isSaving: boolean;

  users: IUser[];

  editForm = this.fb.group({
    id: [],
    pets: [],
    smokingInside: [],
    isFurnished: [],
    roomAmountMin: [],
    roomAmountMax: [],
    sizeMin: [],
    sizeMax: [],
    constructionYearMin: [],
    constructionYearMax: [],
    type: [],
    totalCostMin: [],
    totalCostMax: [],
    user: []
  });

  constructor(
    protected jhiAlertService: JhiAlertService,
    protected questionnaireService: QuestionnaireService,
    protected userService: UserService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ questionnaire }) => {
      this.updateForm(questionnaire);
      this.questionnaire = questionnaire;
    });
    this.userService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<IUser[]>) => mayBeOk.ok),
        map((response: HttpResponse<IUser[]>) => response.body)
      )
      .subscribe((res: IUser[]) => (this.users = res), (res: HttpErrorResponse) => this.onError(res.message));
  }

  updateForm(questionnaire: IQuestionnaire) {
    this.editForm.patchValue({
      id: questionnaire.id,
      pets: questionnaire.pets,
      smokingInside: questionnaire.smokingInside,
      isFurnished: questionnaire.isFurnished,
      roomAmountMin: questionnaire.roomAmountMin,
      roomAmountMax: questionnaire.roomAmountMax,
      sizeMin: questionnaire.sizeMin,
      sizeMax: questionnaire.sizeMax,
      constructionYearMin: questionnaire.constructionYearMin,
      constructionYearMax: questionnaire.constructionYearMax,
      type: questionnaire.type,
      totalCostMin: questionnaire.totalCostMin,
      totalCostMax: questionnaire.totalCostMax,
      user: questionnaire.user
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const questionnaire = this.createFromForm();
    if (questionnaire.id !== undefined) {
      this.subscribeToSaveResponse(this.questionnaireService.update(questionnaire));
    } else {
      this.subscribeToSaveResponse(this.questionnaireService.create(questionnaire));
    }
  }

  private createFromForm(): IQuestionnaire {
    const entity = {
      ...new Questionnaire(),
      id: this.editForm.get(['id']).value,
      pets: this.editForm.get(['pets']).value,
      smokingInside: this.editForm.get(['smokingInside']).value,
      isFurnished: this.editForm.get(['isFurnished']).value,
      roomAmountMin: this.editForm.get(['roomAmountMin']).value,
      roomAmountMax: this.editForm.get(['roomAmountMax']).value,
      sizeMin: this.editForm.get(['sizeMin']).value,
      sizeMax: this.editForm.get(['sizeMax']).value,
      constructionYearMin: this.editForm.get(['constructionYearMin']).value,
      constructionYearMax: this.editForm.get(['constructionYearMax']).value,
      type: this.editForm.get(['type']).value,
      totalCostMin: this.editForm.get(['totalCostMin']).value,
      totalCostMax: this.editForm.get(['totalCostMax']).value,
      user: this.editForm.get(['user']).value
    };
    return entity;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IQuestionnaire>>) {
    result.subscribe((res: HttpResponse<IQuestionnaire>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
  }

  protected onSaveSuccess() {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError() {
    this.isSaving = false;
  }
  protected onError(errorMessage: string) {
    this.jhiAlertService.error(errorMessage, null, null);
  }

  trackUserById(index: number, item: IUser) {
    return item.id;
  }
}
