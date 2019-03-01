import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { Questionnaire } from 'app/shared/model/questionnaire.model';
import { QuestionnaireService } from './questionnaire.service';
import { QuestionnaireComponent } from './questionnaire.component';
import { QuestionnaireDetailComponent } from './questionnaire-detail.component';
import { QuestionnaireUpdateComponent } from './questionnaire-update.component';
import { QuestionnaireDeletePopupComponent } from './questionnaire-delete-dialog.component';
import { IQuestionnaire } from 'app/shared/model/questionnaire.model';

@Injectable({ providedIn: 'root' })
export class QuestionnaireResolve implements Resolve<IQuestionnaire> {
    constructor(private service: QuestionnaireService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<IQuestionnaire> {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(
                filter((response: HttpResponse<Questionnaire>) => response.ok),
                map((questionnaire: HttpResponse<Questionnaire>) => questionnaire.body)
            );
        }
        return of(new Questionnaire());
    }
}

export const questionnaireRoute: Routes = [
    {
        path: '',
        component: QuestionnaireComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Questionnaires'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: ':id/view',
        component: QuestionnaireDetailComponent,
        resolve: {
            questionnaire: QuestionnaireResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Questionnaires'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'new',
        component: QuestionnaireUpdateComponent,
        resolve: {
            questionnaire: QuestionnaireResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Questionnaires'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: ':id/edit',
        component: QuestionnaireUpdateComponent,
        resolve: {
            questionnaire: QuestionnaireResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Questionnaires'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const questionnairePopupRoute: Routes = [
    {
        path: ':id/delete',
        component: QuestionnaireDeletePopupComponent,
        resolve: {
            questionnaire: QuestionnaireResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Questionnaires'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
