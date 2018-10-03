import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs/Subscription';
import { JhiEventManager } from 'ng-jhipster';

import { LigneDevis } from './ligne-devis.model';
import { LigneDevisService } from './ligne-devis.service';

@Component({
    selector: 'jhi-ligne-devis-detail',
    templateUrl: './ligne-devis-detail.component.html'
})
export class LigneDevisDetailComponent implements OnInit, OnDestroy {

    ligneDevis: LigneDevis;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private ligneDevisService: LigneDevisService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInLigneDevis();
    }

    load(id) {
        this.ligneDevisService.find(id)
            .subscribe((ligneDevisResponse: HttpResponse<LigneDevis>) => {
                this.ligneDevis = ligneDevisResponse.body;
            });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInLigneDevis() {
        this.eventSubscriber = this.eventManager.subscribe(
            'ligneDevisListModification',
            (response) => this.load(this.ligneDevis.id)
        );
    }
}
