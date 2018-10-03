import { Component, OnInit, OnDestroy } from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';

import { Observable } from 'rxjs/Observable';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import {JhiEventManager, JhiAlertService, JhiDateUtils} from 'ng-jhipster';

import { Affectation } from './affectation.model';
import { AffectationService } from './affectation.service';
import { Travaux, TravauxService } from '../travaux';
import { Chantier, ChantierService } from '../chantier';
import { Employe, EmployeService } from '../employe';

import { Subscription } from 'rxjs/Subscription';

@Component({
    selector: 'jhi-affectation-dialog',
    templateUrl: './travaux-dialog-par-chantier.component.html'
})
export class TravauxDialogParChantierComponent implements OnInit, OnDestroy {

    affectation: Affectation;
    isSaving: boolean;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    travauxes: Travaux[];

    chantiers: Chantier[];

    employes: Employe[];
    dateDebutDp: any;
    dateFinDp: any;

    dropdownList = [];
    selectedItems: any[];
    dropdownSettings = {};
    constructor(
        private jhiAlertService: JhiAlertService,
        private affectationService: AffectationService,
        private travauxService: TravauxService,
        private chantierService: ChantierService,
        private employeService: EmployeService,
        private eventManager: JhiEventManager,
        private route: ActivatedRoute,
        private router: Router,
        private dateUtils: JhiDateUtils
    ) {}
    ngOnInit() {
        this.isSaving = false;
        this.travauxService.query()
            .subscribe((res: HttpResponse<Travaux[]>) => { this.travauxes = res.body; }, (res: HttpErrorResponse) => this.onError(res.message));
        this.chantierService.query()
            .subscribe((res: HttpResponse<Chantier[]>) => {
                this.chantiers = res.body;
                const id = +sessionStorage.getItem('idChantier');
                 let chantier1;
                chantier1 = new Chantier();
                  for (const chantier of this.chantiers) {
                     if (chantier.id === id) {
                         chantier1 = chantier;
                     }
                  }
                  this.chantiers = [];
                  this.chantiers.push(chantier1);
            }, (res: HttpErrorResponse) => this.onError(res.message));
        this.employeService.query()
            .subscribe((res: HttpResponse<Employe[]>) => { this.employes = res.body; }, (res: HttpErrorResponse) => this.onError(res.message));
        this.dropdownSettings = {
            singleSelection: false,
            idField: 'id',
            textField: 'nomEmploye',
            selectAllText: 'Select All',
            unSelectAllText: 'UnSelect All',
            itemsShowLimit: 3,
            allowSearchFilter: true
        };
        this.subscription = this.route.params.subscribe((params) => {
            if (params['id']) {
                this.load(params['id']);
            } else {
                this.affectation = new Affectation();
            }
        });
        this.registerChangeInAffectations();
    }
    load(id) {
        this.affectation = new Affectation();
        this.affectationService.find(id)
            .subscribe((affectationResponse: HttpResponse<Affectation>) => {
                this.affectation = affectationResponse.body;
                if (this.affectation.dateDebut) {
                    this.affectation.dateDebut = {
                        year: this.affectation.dateDebut.getFullYear(),
                        month: this.affectation.dateDebut.getMonth() + 1,
                        day: this.affectation.dateDebut.getDate()
                    };
                }
                if (this.affectation.dateFin) {
                    this.affectation.dateFin = {
                        year: this.affectation.dateFin.getFullYear(),
                        month: this.affectation.dateFin.getMonth() + 1,
                        day: this.affectation.dateFin.getDate()
                    };
                }
            });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInAffectations() {
        this.eventSubscriber = this.eventManager.subscribe(
            'affectationListModification',
            (response) => this.load(this.affectation.id)
        );
    }

    save() {
        if (!this.compareDate( this.affectation.dateDebut , this.affectation.dateFin )) {
            this.onSaveError();
            this.jhiAlertService.error('the start date must be less than the end date', null, null);
            this.router.navigate( ['affectations/' + +sessionStorage.getItem('idChantier')] );
        } else {
            this.isSaving = true;
           /* this.affectation.employes = this.selectedItems;*/
            if (this.affectation.id !== undefined ) {
                console.log(this.affectation.employes);
                this.subscribeToSaveResponse(
                    this.affectationService.update(this.affectation));
            } else {
                this.subscribeToSaveResponse(
                    this.affectationService.create(this.affectation));
            }
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<Affectation>>) {
        result.subscribe((res: HttpResponse<Affectation>) =>
            this.onSaveSuccess(res.body), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess(result: Affectation) {
        this.eventManager.broadcast({ name: 'affectationListModification'});
        this.isSaving = false;
        this.router.navigate(['affectations/' + +sessionStorage.getItem('idChantier')]);
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(error: any) {
        this.jhiAlertService.error(error.message, null, null);
    }

    trackTravauxById(index: number, item: Travaux) {
        return item.id;
    }

    trackChantierById(index: number, item: Chantier) {
        return item.id;
    }

    trackEmployeById(index: number, item: Employe) {
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
    compareDate(date1: any, date2: any) {
        const a1 = +date1['year'];
        const b1 = +date2['year'];
        const a2 = +date1['month'];
        const b2 = +date2['month'];
        const a3 = +date1['day'];
        const b3 = +date2['day'];
        if ( a1 <= b1 ) {
            if ( a2 <= b2 ) {
                  return true;
            } else {
                if ( a3 <= b3 ) {
                    return true;
                } else {
                    return false;
                }
            }
        } else {
            return false;
        }
    }
}
