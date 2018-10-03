import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs/Subscription';
import { JhiEventManager } from 'ng-jhipster';

import { Facture } from './facture.model';
import { FactureService } from './facture.service';

@Component({
    selector: 'jhi-facture-detail',
    templateUrl: './facture-detail.component.html'
})
export class FactureDetailComponent implements OnInit, OnDestroy {

    facture: Facture;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private factureService: FactureService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInFactures();
    }

    load(id) {
        this.factureService.find(id)
            .subscribe((factureResponse: HttpResponse<Facture>) => {
                this.facture = factureResponse.body;
            });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInFactures() {
        this.eventSubscriber = this.eventManager.subscribe(
            'factureListModification',
            (response) => this.load(this.facture.id)
        );
    }
}
