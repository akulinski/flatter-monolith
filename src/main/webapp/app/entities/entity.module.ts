import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'offer',
        loadChildren: './offer/offer.module#FlatterservermonolithOfferModule'
      },
      {
        path: 'address',
        loadChildren: './address/address.module#FlatterservermonolithAddressModule'
      },
      {
        path: 'album',
        loadChildren: './album/album.module#FlatterservermonolithAlbumModule'
      },
      {
        path: 'photo',
        loadChildren: './photo/photo.module#FlatterservermonolithPhotoModule'
      },
      {
        path: 'message',
        loadChildren: './message/message.module#FlatterservermonolithMessageModule'
      },
      {
        path: 'conversation',
        loadChildren: './conversation/conversation.module#FlatterservermonolithConversationModule'
      },
      {
        path: 'match',
        loadChildren: './match/match.module#FlatterservermonolithMatchModule'
      },
      {
        path: 'questionnaire',
        loadChildren: './questionnaire/questionnaire.module#FlatterservermonolithQuestionnaireModule'
      },
      {
        path: 'review',
        loadChildren: './review/review.module#FlatterservermonolithReviewModule'
      },
      {
        path: 'profile-picture',
        loadChildren: './profile-picture/profile-picture.module#FlatterservermonolithProfilePictureModule'
      },
      {
        path: 'questionnaire',
        loadChildren: './questionnaire/questionnaire.module#FlatterservermonolithQuestionnaireModule'
      }
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ])
  ],
  declarations: [],
  entryComponents: [],
  providers: [],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class FlatterservermonolithEntityModule {}
