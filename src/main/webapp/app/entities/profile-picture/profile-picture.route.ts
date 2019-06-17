import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { ProfilePicture } from 'app/shared/model/profile-picture.model';
import { ProfilePictureService } from './profile-picture.service';
import { ProfilePictureComponent } from './profile-picture.component';
import { ProfilePictureDetailComponent } from './profile-picture-detail.component';
import { ProfilePictureUpdateComponent } from './profile-picture-update.component';
import { ProfilePictureDeletePopupComponent } from './profile-picture-delete-dialog.component';
import { IProfilePicture } from 'app/shared/model/profile-picture.model';

@Injectable({ providedIn: 'root' })
export class ProfilePictureResolve implements Resolve<IProfilePicture> {
  constructor(private service: ProfilePictureService) {}

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<IProfilePicture> {
    const id = route.params['id'] ? route.params['id'] : null;
    if (id) {
      return this.service.find(id).pipe(
        filter((response: HttpResponse<ProfilePicture>) => response.ok),
        map((profilePicture: HttpResponse<ProfilePicture>) => profilePicture.body)
      );
    }
    return of(new ProfilePicture());
  }
}

export const profilePictureRoute: Routes = [
  {
    path: '',
    component: ProfilePictureComponent,
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'ProfilePictures'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: ProfilePictureDetailComponent,
    resolve: {
      profilePicture: ProfilePictureResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'ProfilePictures'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: ProfilePictureUpdateComponent,
    resolve: {
      profilePicture: ProfilePictureResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'ProfilePictures'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: ProfilePictureUpdateComponent,
    resolve: {
      profilePicture: ProfilePictureResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'ProfilePictures'
    },
    canActivate: [UserRouteAccessService]
  }
];

export const profilePicturePopupRoute: Routes = [
  {
    path: ':id/delete',
    component: ProfilePictureDeletePopupComponent,
    resolve: {
      profilePicture: ProfilePictureResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'ProfilePictures'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  }
];
