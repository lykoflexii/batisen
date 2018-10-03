import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs/Subscription';
import { JhiEventManager } from 'ng-jhipster';

import { Entreprise } from './entreprise.model';
import { EntrepriseService } from './entreprise.service';

@Component({
    selector: 'jhi-entreprise-detail',
    templateUrl: './entreprise-detail.component.html'
})
export class EntrepriseDetailComponent implements OnInit, OnDestroy {

    entreprise: Entreprise;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private entrepriseService: EntrepriseService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInEntreprises();
    }

    load(id) {
        this.entrepriseService.find(id)
            .subscribe((entrepriseResponse: HttpResponse<Entreprise>) => {
                this.entreprise = entrepriseResponse.body;
            });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInEntreprises() {
        this.eventSubscriber = this.eventManager.subscribe(
            'entrepriseListModification',
            (response) => this.load(this.entreprise.id)
        );
    }
}
