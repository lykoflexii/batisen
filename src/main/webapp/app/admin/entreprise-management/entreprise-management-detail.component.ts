import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';

import { Entreprise } from './entreprise.model';
import { EntrepriseService } from './entreprise.service';
import { HttpResponse } from '@angular/common/http';

@Component({
    selector: 'jhi-entreprise-mgmt-detail',
    templateUrl: './entreprise-management-detail.component.html'
})
export class EntrepriseMgmtDetailComponent implements OnInit, OnDestroy {

    entreprise: Entreprise;
    private subscription: Subscription;

    constructor(
        private entrepriseService: EntrepriseService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
    }

    load(id) {
        this.entrepriseService.find(id)
            .subscribe((entrepriseResponse: HttpResponse<Entreprise>) => {
                this.entreprise = entrepriseResponse.body;
        });
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
    }
}
