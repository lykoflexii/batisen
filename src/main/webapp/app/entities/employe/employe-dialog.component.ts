import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';

import { Observable } from 'rxjs/Observable';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { Employe } from './employe.model';
import { EmployePopupService } from './employe-popup.service';
import { EmployeService } from './employe.service';
import { Entreprise, EntrepriseService } from '../entreprise';
import { Affectation, AffectationService } from '../affectation';

@Component({
    selector: 'jhi-employe-dialog',
    templateUrl: './employe-dialog.component.html'
})
export class EmployeDialogComponent implements OnInit {

    employe: Employe;
    isSaving: boolean;

    entreprises: Entreprise[];

    affectations: Affectation[];
    dateNaissanceDp: any;

    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private employeService: EmployeService,
        private entrepriseService: EntrepriseService,
        private affectationService: AffectationService,
        private eventManager: JhiEventManager,
        private router: Router
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        // this.entrepriseService.query()
        //     .subscribe((res: HttpResponse<Entreprise[]>) => { this.entreprises = res.body; }, (res: HttpErrorResponse) => this.onError(res.message));
        this.affectationService.query()
            .subscribe((res: HttpResponse<Affectation[]>) => { this.affectations = res.body; }, (res: HttpErrorResponse) => this.onError(res.message));
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.employe.id !== undefined) {
            this.subscribeToSaveResponse(
                this.employeService.update(this.employe));
        } else {
            this.employe.entrepriseId = +sessionStorage.getItem('entreprise_id');
            this.subscribeToSaveResponse(
                this.employeService.create(this.employe));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<Employe>>) {
        result.subscribe((res: HttpResponse<Employe>) =>
            this.onSaveSuccess(res.body), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess(result: Employe) {
        this.eventManager.broadcast({ name: 'employeListModification', content: 'OK'});
        this.isSaving = false;
        // this.router.navigate(['/employe']);
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(error: any) {
        this.jhiAlertService.error(error.message, null, null);
    }

    trackEntrepriseById(index: number, item: Entreprise) {
        return item.id;
    }

    trackAffectationById(index: number, item: Affectation) {
        return item.id;
    }

    getSelected(selectedVals: Array<any>, option: any) {
        if (selectedVals) {
            for (let i = 0; i < selectedVals.length; i++) {
                if (option.id === selectedVals[i].id) {
                    return selectedVals[i];
                }
            }
        }
        return option;
    }
}

@Component({
    selector: 'jhi-employe-popup',
    template: ''
})
export class EmployePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private employePopupService: EmployePopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.employePopupService
                    .open(EmployeDialogComponent as Component, params['id']);
            } else {
                this.employePopupService
                    .open(EmployeDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
