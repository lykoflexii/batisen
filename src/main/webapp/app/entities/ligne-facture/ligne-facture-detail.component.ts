import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs/Subscription';
import { JhiEventManager } from 'ng-jhipster';

import { LigneFacture } from './ligne-facture.model';
import { LigneFactureService } from './ligne-facture.service';

@Component({
    selector: 'jhi-ligne-facture-detail',
    templateUrl: './ligne-facture-detail.component.html'
})
export class LigneFactureDetailComponent implements OnInit, OnDestroy {

    ligneFacture: LigneFacture;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private ligneFactureService: LigneFactureService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInLigneFactures();
    }

    load(id) {
        this.ligneFactureService.find(id)
            .subscribe((ligneFactureResponse: HttpResponse<LigneFacture>) => {
                this.ligneFacture = ligneFactureResponse.body;
            });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInLigneFactures() {
        this.eventSubscriber = this.eventManager.subscribe(
            'ligneFactureListModification',
            (response) => this.load(this.ligneFacture.id)
        );
    }
}
