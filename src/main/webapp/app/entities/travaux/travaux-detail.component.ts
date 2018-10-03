import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs/Subscription';
import { JhiEventManager } from 'ng-jhipster';

import { Travaux } from './travaux.model';
import { TravauxService } from './travaux.service';

@Component({
    selector: 'jhi-travaux-detail',
    templateUrl: './travaux-detail.component.html'
})
export class TravauxDetailComponent implements OnInit, OnDestroy {

    travaux: Travaux;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private travauxService: TravauxService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInTravauxes();
    }

    load(id) {
        this.travauxService.find(id)
            .subscribe((travauxResponse: HttpResponse<Travaux>) => {
                this.travaux = travauxResponse.body;
            });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInTravauxes() {
        this.eventSubscriber = this.eventManager.subscribe(
            'travauxListModification',
            (response) => this.load(this.travaux.id)
        );
    }
}
