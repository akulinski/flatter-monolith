import { Component, OnInit } from '@angular/core';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiAlertService } from 'ng-jhipster';
import { IReview, Review } from 'app/shared/model/review.model';
import { ReviewService } from './review.service';
import { IUser, UserService } from 'app/core';

@Component({
  selector: 'jhi-review-update',
  templateUrl: './review-update.component.html'
})
export class ReviewUpdateComponent implements OnInit {
  review: IReview;
  isSaving: boolean;

  users: IUser[];

  editForm = this.fb.group({
    id: [],
    rate: [],
    description: [],
    issuer: [],
    receiver: []
  });

  constructor(
    protected jhiAlertService: JhiAlertService,
    protected reviewService: ReviewService,
    protected userService: UserService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ review }) => {
      this.updateForm(review);
      this.review = review;
    });
    this.userService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<IUser[]>) => mayBeOk.ok),
        map((response: HttpResponse<IUser[]>) => response.body)
      )
      .subscribe((res: IUser[]) => (this.users = res), (res: HttpErrorResponse) => this.onError(res.message));
  }

  updateForm(review: IReview) {
    this.editForm.patchValue({
      id: review.id,
      rate: review.rate,
      description: review.description,
      issuer: review.issuer,
      receiver: review.receiver
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const review = this.createFromForm();
    if (review.id !== undefined) {
      this.subscribeToSaveResponse(this.reviewService.update(review));
    } else {
      this.subscribeToSaveResponse(this.reviewService.create(review));
    }
  }

  private createFromForm(): IReview {
    const entity = {
      ...new Review(),
      id: this.editForm.get(['id']).value,
      rate: this.editForm.get(['rate']).value,
      description: this.editForm.get(['description']).value,
      issuer: this.editForm.get(['issuer']).value,
      receiver: this.editForm.get(['receiver']).value
    };
    return entity;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IReview>>) {
    result.subscribe((res: HttpResponse<IReview>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
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
