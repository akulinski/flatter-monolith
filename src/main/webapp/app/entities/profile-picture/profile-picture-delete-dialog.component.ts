import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IProfilePicture } from 'app/shared/model/profile-picture.model';
import { ProfilePictureService } from './profile-picture.service';

@Component({
  selector: 'jhi-profile-picture-delete-dialog',
  templateUrl: './profile-picture-delete-dialog.component.html'
})
export class ProfilePictureDeleteDialogComponent {
  profilePicture: IProfilePicture;

  constructor(
    protected profilePictureService: ProfilePictureService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  clear() {
    this.activeModal.dismiss('cancel');
  }

  confirmDelete(id: number) {
    this.profilePictureService.delete(id).subscribe(response => {
      this.eventManager.broadcast({
        name: 'profilePictureListModification',
        content: 'Deleted an profilePicture'
      });
      this.activeModal.dismiss(true);
    });
  }
}

@Component({
  selector: 'jhi-profile-picture-delete-popup',
  template: ''
})
export class ProfilePictureDeletePopupComponent implements OnInit, OnDestroy {
  protected ngbModalRef: NgbModalRef;

  constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ profilePicture }) => {
      setTimeout(() => {
        this.ngbModalRef = this.modalService.open(ProfilePictureDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
        this.ngbModalRef.componentInstance.profilePicture = profilePicture;
        this.ngbModalRef.result.then(
          result => {
            this.router.navigate(['/profile-picture', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          },
          reason => {
            this.router.navigate(['/profile-picture', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          }
        );
      }, 0);
    });
  }

  ngOnDestroy() {
    this.ngbModalRef = null;
  }
}
