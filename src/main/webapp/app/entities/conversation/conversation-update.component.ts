import { Component, OnInit } from '@angular/core';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiAlertService } from 'ng-jhipster';
import { IConversation, Conversation } from 'app/shared/model/conversation.model';
import { ConversationService } from './conversation.service';
import { IUser, UserService } from 'app/core';

@Component({
  selector: 'jhi-conversation-update',
  templateUrl: './conversation-update.component.html'
})
export class ConversationUpdateComponent implements OnInit {
  conversation: IConversation;
  isSaving: boolean;

  users: IUser[];

  editForm = this.fb.group({
    id: [],
    sender: [],
    reciver: []
  });

  constructor(
    protected jhiAlertService: JhiAlertService,
    protected conversationService: ConversationService,
    protected userService: UserService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ conversation }) => {
      this.updateForm(conversation);
      this.conversation = conversation;
    });
    this.userService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<IUser[]>) => mayBeOk.ok),
        map((response: HttpResponse<IUser[]>) => response.body)
      )
      .subscribe((res: IUser[]) => (this.users = res), (res: HttpErrorResponse) => this.onError(res.message));
  }

  updateForm(conversation: IConversation) {
    this.editForm.patchValue({
      id: conversation.id,
      sender: conversation.sender,
      reciver: conversation.reciver
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const conversation = this.createFromForm();
    if (conversation.id !== undefined) {
      this.subscribeToSaveResponse(this.conversationService.update(conversation));
    } else {
      this.subscribeToSaveResponse(this.conversationService.create(conversation));
    }
  }

  private createFromForm(): IConversation {
    const entity = {
      ...new Conversation(),
      id: this.editForm.get(['id']).value,
      sender: this.editForm.get(['sender']).value,
      reciver: this.editForm.get(['reciver']).value
    };
    return entity;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IConversation>>) {
    result.subscribe((res: HttpResponse<IConversation>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
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
