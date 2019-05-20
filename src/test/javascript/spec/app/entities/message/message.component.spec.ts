/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { Observable, of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { FlatterservermonolithTestModule } from '../../../test.module';
import { MessageComponent } from 'app/entities/message/message.component';
import { MessageService } from 'app/entities/message/message.service';
import { Message } from 'app/shared/model/message.model';

describe('Component Tests', () => {
  describe('Message Management Component', () => {
    let comp: MessageComponent;
    let fixture: ComponentFixture<MessageComponent>;
    let service: MessageService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [FlatterservermonolithTestModule],
        declarations: [MessageComponent],
        providers: []
      })
        .overrideTemplate(MessageComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(MessageComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(MessageService);
    });

    it('Should call load all on init', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new Message(123)],
            headers
          })
        )
      );

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.messages[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
