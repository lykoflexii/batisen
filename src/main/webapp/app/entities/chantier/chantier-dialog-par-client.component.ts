import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';

import { Observable } from 'rxjs/Observable';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { Chantier } from './chantier.model';
import { ChantierPopupService } from './chantier-popup.service';
import { ChantierService } from './chantier.service';
import { Client, ClientService } from '../client';
import { User, UserService } from '../../shared';
import { Subscription } from '../../../../../../node_modules/rxjs';

@Component({
    selector: 'jhi-chantier-dialog-par-client',
    templateUrl: './chantier-dialog-par-client.component.html'
})
export class ChantierDialogParClientComponent implements OnInit {

    chantier: Chantier;
    isSaving: boolean;

    clients: Client[];

    users: User[];
    dateDebutReelleDp: any;
    dateFinReelleDp: any;
    dateDebutPrevuDp: any;
    dateFinPrevuDp: any;
    private eventSubscriber: Subscription;

    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private chantierService: ChantierService,
        private clientService: ClientService,
        private eventManager: JhiEventManager,
        private router: Router,
        private userService: UserService
    ) {
    }
    ngOnInit() {
        this.isSaving = false;
        const id1 = +sessionStorage.getItem('entreprise_id');
        this.clientService.findClient(id1)
            .subscribe((res: HttpResponse<Client[]>) => {
                this.clients = res.body;
                const id = +sessionStorage.getItem('idClient');
                let client1: Client = new Client();
                for (const client of this.clients) {
                    if (client.id === id) {
                        client1 = client;
                    }
                }
                this.clients = [];
                this.clients.push(client1);
            }, (res: HttpErrorResponse) => this.onError(res.message));
            this.userService.query()
            .subscribe((res: HttpResponse<User[]>) => { this.users = res.body; }, (res: HttpErrorResponse) => this.onError(res.message));
    }

    load(id) {
        this.chantier = new Chantier();
        this.chantierService.find(id)
            .subscribe((chantierResponse: HttpResponse<Chantier>) => {
                this.chantier = chantierResponse.body;
                if (this.chantier.dateDebutReelle) {
                    this.chantier.dateDebutReelle = {
                        year: this.chantier.dateDebutReelle.getFullYear(),
                        month: this.chantier.dateDebutReelle.getMonth() + 1,
                        day: this.chantier.dateDebutReelle.getDate()
                    };
                }
                if (this.chantier.dateFinReelle) {
                    this.chantier.dateFinReelle = {
                        year: this.chantier.dateFinReelle.getFullYear(),
                        month: this.chantier.dateFinReelle.getMonth() + 1,
                        day: this.chantier.dateFinReelle.getDate()
                    };
                }
                if (this.chantier.dateDebutPrevu) {
                    this.chantier.dateDebutPrevu = {
                        year: this.chantier.dateDebutPrevu.getFullYear(),
                        month: this.chantier.dateDebutPrevu.getMonth() + 1,
                        day: this.chantier.dateDebutPrevu.getDate()
                    };
                }
                if (this.chantier.dateFinPrevu) {
                    this.chantier.dateFinPrevu = {
                        year: this.chantier.dateFinPrevu.getFullYear(),
                        month: this.chantier.dateFinPrevu.getMonth() + 1,
                        day: this.chantier.dateFinPrevu.getDate()
                    };
                }
            });
    }
    clear() {
        this.activeModal.dismiss('cancel');
    }
    previousState() {
        window.history.back();
    }
    registerChangeInChantiers() {
        this.eventSubscriber = this.eventManager.subscribe(
            'chantierListModification',
            (response) => this.load(this.chantier.id)
        );
    }

    save() {
        if (!this.compareDate( this.chantier.dateDebutPrevu , this.chantier.dateFinPrevu )) {
            alert('ddddd');
            this.onSaveError();
            this.jhiAlertService.error('the start date must be less than the end date', null, null);
            this.router.navigate( ['chantiers/' + +sessionStorage.getItem('idClient')] );
        } else {
            this.isSaving = true;
            if (this.chantier.id !== undefined) {
                this.subscribeToSaveResponse(
                    this.chantierService.update(this.chantier));
            } else {
                this.chantier.clientId = +sessionStorage.getItem('idClient');
                this.subscribeToSaveResponse(
                    this.chantierService.create(this.chantier));
            }
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<Chantier>>) {
        result.subscribe((res: HttpResponse<Chantier>) =>
            this.onSaveSuccess(res.body), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess(result: Chantier) {
        this.eventManager.broadcast({ name: 'chantierListModification'});
        this.isSaving = false;
        this.router.navigate(['chantiers/' + this.clients[0].id]);
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(error: any) {
        this.jhiAlertService.error(error.message, null, null);
    }

    trackClientById(index: number, item: Client) {
        return item.id;
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
                if ( a3 <= b3 ) {
                    return true;
                } else {
                    return false;
                }
            } else {
                return false;
            }
        } else {
            return false;
        }
    }
}

@Component({
    selector: 'jhi-chantier-popup',
    template: ''
})
export class ChantierPopup2Component implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private chantierPopupService: ChantierPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.chantierPopupService
                    .open(ChantierDialogParClientComponent as Component, params['id']);
            } else {
                this.chantierPopupService
                    .open(ChantierDialogParClientComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
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
                if ( a3 <= b3 ) {
                    return true;
                } else {
                    return false;
                }
            } else {
                return false;
            }
        } else {
            return false;
        }
    }
}
