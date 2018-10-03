import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';

import { Observable } from 'rxjs/Observable';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { Entreprise } from './entreprise.model';
import { EntreprisePopupService } from './entreprise-popup.service';
import { EntrepriseService } from './entreprise.service';

@Component({
    selector: 'jhi-entreprise-dialog',
    templateUrl: './entreprise-dialog.component.html'
})
export class EntrepriseDialogComponent implements OnInit {

    entreprise: Entreprise;
    isSaving: boolean;

    constructor(
        public activeModal: NgbActiveModal,
        private entrepriseService: EntrepriseService,
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
        if (this.entreprise.id !== undefined) {
            this.subscribeToSaveResponse(
                this.entrepriseService.update(this.entreprise));
        } else {
            this.subscribeToSaveResponse(
                this.entrepriseService.create(this.entreprise));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<Entreprise>>) {
        result.subscribe((res: HttpResponse<Entreprise>) =>
            this.onSaveSuccess(res.body), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess(result: Entreprise) {
        this.eventManager.broadcast({ name: 'entrepriseListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }
}

@Component({
    selector: 'jhi-entreprise-popup',
    template: ''
})
export class EntreprisePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private entreprisePopupService: EntreprisePopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.entreprisePopupService
                    .open(EntrepriseDialogComponent as Component, params['id']);
            } else {
                this.entreprisePopupService
                    .open(EntrepriseDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
