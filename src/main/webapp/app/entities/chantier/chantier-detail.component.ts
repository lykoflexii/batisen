import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs/Subscription';
import { JhiEventManager } from 'ng-jhipster';

import { Chantier } from './chantier.model';
import { ChantierService } from './chantier.service';

@Component({
    selector: 'jhi-chantier-detail',
    templateUrl: './chantier-detail.component.html'
})
export class ChantierDetailComponent implements OnInit, OnDestroy {

    chantier: Chantier;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private chantierService: ChantierService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInChantiers();
    }

    load(id) {
        this.chantierService.find(id)
            .subscribe((chantierResponse: HttpResponse<Chantier>) => {
                this.chantier = chantierResponse.body;
            });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInChantiers() {
        this.eventSubscriber = this.eventManager.subscribe(
            'chantierListModification',
            (response) => this.load(this.chantier.id)
        );
    }
}
