import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IConversation } from 'app/shared/model/conversation.model';

@Component({
  selector: 'jhi-conversation-detail',
  templateUrl: './conversation-detail.component.html'
})
export class ConversationDetailComponent implements OnInit {
  conversation: IConversation;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ conversation }) => {
      this.conversation = conversation;
    });
  }

  previousState() {
    window.history.back();
  }
}
