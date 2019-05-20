import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IMatch } from 'app/shared/model/match.model';

@Component({
    selector: 'jhi-match-detail',
    templateUrl: './match-detail.component.html'
})
export class MatchDetailComponent implements OnInit {
    match: IMatch;

    constructor(protected activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ match }) => {
            this.match = match;
        });
    }

    previousState() {
        window.history.back();
    }
}
