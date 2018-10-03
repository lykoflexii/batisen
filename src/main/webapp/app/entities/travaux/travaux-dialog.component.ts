import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';

import { Observable } from 'rxjs/Observable';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { Travaux } from './travaux.model';
import { TravauxPopupService } from './travaux-popup.service';
import { TravauxService } from './travaux.service';

@Component({
    selector: 'jhi-travaux-dialog',
    templateUrl: './travaux-dialog.component.html'
})
export class TravauxDialogComponent implements OnInit {

    travaux: Travaux;
    isSaving: boolean;

    constructor(
        public activeModal: NgbActiveModal,
        private travauxService: TravauxService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.travaux.id !== undefined) {
            this.subscribeToSaveResponse(
                this.travauxService.update(this.travaux));
        } else {
            this.subscribeToSaveResponse(
                this.travauxService.create(this.travaux));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<Travaux>>) {
        result.subscribe((res: HttpResponse<Travaux>) =>
            this.onSaveSuccess(res.body), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess(result: Travaux) {
        this.eventManager.broadcast({ name: 'travauxListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }
}

@Component({
    selector: 'jhi-travaux-popup',
    template: ''
})
export class TravauxPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private travauxPopupService: TravauxPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.travauxPopupService
                    .open(TravauxDialogComponent as Component, params['id']);
            } else {
                this.travauxPopupService
                    .open(TravauxDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
