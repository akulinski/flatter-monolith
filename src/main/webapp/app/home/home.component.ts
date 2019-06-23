import {Component, OnInit} from '@angular/core';
import {NgbModalRef} from '@ng-bootstrap/ng-bootstrap';
import {JhiEventManager} from 'ng-jhipster';

import {Account, AccountService, LoginModalService} from 'app/core';
import {UserCheckService} from 'app/home/user-check.service';
import {Router} from '@angular/router';
import {OfferService} from "app/entities/offer";
import {IOffer} from "app/shared/model/offer.model";

@Component({
  selector: 'jhi-home',
  templateUrl: './home.component.html',
  styleUrls: ['home.scss']
})
export class HomeComponent implements OnInit {
  account: Account;
  modalRef: NgbModalRef;
  offers: IOffer[];

  constructor(
    private offerService: OfferService,
    private accountService: AccountService,
    private loginModalService: LoginModalService,
    private eventManager: JhiEventManager,
    private userCheckService: UserCheckService,
    private router: Router
  ) {
  }

  ngOnInit() {
    this.accountService.identity().then((account: Account) => {
      this.account = account;
    });

    this.offerService.getTopOffers().subscribe(data => {
      this.offers = data.body;
      console.log(this.offers)
    });

    this.registerAuthenticationSuccess();

    this.userCheckService.check().subscribe(data => {
      if (!data.body.questionnaireFilled) {
        this.router.navigate(['/questionnaire/new', {outlets: {popup: null}}]);
      }
    })
  }

  registerAuthenticationSuccess() {
    this.eventManager.subscribe('authenticationSuccess', message => {
      this.accountService.identity().then(account => {
        this.account = account;
      });
    });
  }

  isAuthenticated() {
    return this.accountService.isAuthenticated();
  }

  login() {
    this.modalRef = this.loginModalService.open();
  }
}
