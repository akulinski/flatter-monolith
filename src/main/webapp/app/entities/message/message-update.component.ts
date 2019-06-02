import { Component, OnInit } from '@angular/core';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { JhiAlertService, JhiDataUtils } from 'ng-jhipster';
import { IMessage, Message } from 'app/shared/model/message.model';
import { MessageService } from './message.service';
import { IConversation } from 'app/shared/model/conversation.model';
import { ConversationService } from 'app/entities/conversation';

@Component({
  selector: 'jhi-message-update',
  templateUrl: './message-update.component.html'
})
export class MessageUpdateComponent implements OnInit {
  message: IMessage;
  isSaving: boolean;

  conversations: IConversation[];

  editForm = this.fb.group({
    id: [],
    creationDate: [],
    content: [null, [Validators.required]],
    isSeen: [],
    conversation: []
  });

  constructor(
    protected dataUtils: JhiDataUtils,
    protected jhiAlertService: JhiAlertService,
    protected messageService: MessageService,
    protected conversationService: ConversationService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ message }) => {
      this.updateForm(message);
      this.message = message;
    });
    this.conversationService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<IConversation[]>) => mayBeOk.ok),
        map((response: HttpResponse<IConversation[]>) => response.body)
      )
      .subscribe((res: IConversation[]) => (this.conversations = res), (res: HttpErrorResponse) => this.onError(res.message));
  }

  updateForm(message: IMessage) {
    this.editForm.patchValue({
      id: message.id,
      creationDate: message.creationDate != null ? message.creationDate.format(DATE_TIME_FORMAT) : null,
      content: message.content,
      isSeen: message.isSeen,
      conversation: message.conversation
    });
  }

  byteSize(field) {
    return this.dataUtils.byteSize(field);
  }

  openFile(contentType, field) {
    return this.dataUtils.openFile(contentType, field);
  }

  setFileData(event, field: string, isImage) {
    return new Promise((resolve, reject) => {
      if (event && event.target && event.target.files && event.target.files[0]) {
        const file = event.target.files[0];
        if (isImage && !/^image\//.test(file.type)) {
          reject(`File was expected to be an image but was found to be ${file.type}`);
        } else {
          const filedContentType: string = field + 'ContentType';
          this.dataUtils.toBase64(file, base64Data => {
            this.editForm.patchValue({
              [field]: base64Data,
              [filedContentType]: file.type
            });
          });
        }
      } else {
        reject(`Base64 data was not set as file could not be extracted from passed parameter: ${event}`);
      }
    }).then(
      () => console.log('blob added'), // sucess
      this.onError
    );
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const message = this.createFromForm();
    if (message.id !== undefined) {
      this.subscribeToSaveResponse(this.messageService.update(message));
    } else {
      this.subscribeToSaveResponse(this.messageService.create(message));
    }
  }

  private createFromForm(): IMessage {
    const entity = {
      ...new Message(),
      id: this.editForm.get(['id']).value,
      creationDate:
        this.editForm.get(['creationDate']).value != null ? moment(this.editForm.get(['creationDate']).value, DATE_TIME_FORMAT) : undefined,
      content: this.editForm.get(['content']).value,
      isSeen: this.editForm.get(['isSeen']).value,
      conversation: this.editForm.get(['conversation']).value
    };
    return entity;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IMessage>>) {
    result.subscribe((res: HttpResponse<IMessage>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
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

  trackConversationById(index: number, item: IConversation) {
    return item.id;
  }
}
