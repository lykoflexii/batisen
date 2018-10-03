import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { Entreprise } from './entreprise.model';
import { EntrepriseModalService } from './entreprise-modal.service';
import { EntrepriseService } from './entreprise.service';

@Component({
    selector: 'jhi-entreprise-mgmt-dialog',
    templateUrl: './entreprise-management-dialog.component.html'
})
export class EntrepriseMgmtDialogComponent implements OnInit {

    entreprise: Entreprise;
    isSaving: boolean;
    isEditing: boolean;

    constructor(
        public activeModal: NgbActiveModal,
        private alertService: JhiAlertService,
        private entrepriseService: EntrepriseService,
        private eventManager: JhiEventManager
    ) {}

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
            this.subscribeToSaveResponse(this.entrepriseService.create(this.entreprise));
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

    private onError(error) {
        this.alertService.error(error.message, null, null);
    }
}

@Component({
    selector: 'jhi-entreprise-dialog',
    template: ''
})
export class EntrepriseDialogComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private entrepriseModalService: EntrepriseModalService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.entrepriseModalService
                    .open(EntrepriseMgmtDialogComponent as Component, params['id']);
            } else {
                this.entrepriseModalService
                    .open(EntrepriseMgmtDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
