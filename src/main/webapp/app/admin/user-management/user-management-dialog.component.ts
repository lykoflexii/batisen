import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { UserModalService } from './user-modal.service';
import { JhiLanguageHelper, User, UserService, Principal } from '../../shared';

import { Entreprise } from './../entreprise-management/entreprise.model';
import { EntrepriseService } from './../entreprise-management/entreprise.service';

@Component({
    selector: 'jhi-user-mgmt-dialog',
    templateUrl: './user-management-dialog.component.html'
})
export class UserMgmtDialogComponent implements OnInit {

    currentAccount: any;
    user: User;
    languages: any[];
    authorities: any[];
    entreprises: Entreprise[];
    isSaving: Boolean;

    constructor(
        public activeModal: NgbActiveModal,
        private languageHelper: JhiLanguageHelper,
        private principal: Principal,
        private userService: UserService,
        private entrepriseService: EntrepriseService,
        private eventManager: JhiEventManager
    ) {
        this.principal.identity().then((account) => {
            this.currentAccount = account;
        });
    }

    ngOnInit() {
        this.isSaving = false;
        this.authorities = [];
        this.userService.authorities().subscribe((authorities) => {
            this.authorities = authorities;
        });
        this.languageHelper.getAll().then((languages) => {
            this.languages = languages;
        });

        this.entrepriseService.query().subscribe(
            (res) => {
                this.entreprises = res.body;
                this.entreprises = [];
                this.entreprises.push(this.currentAccount.entreprise);
            });
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.currentAccount.entreprise) {
            this.user.entreprise = this.currentAccount.entreprise;
        }
        if (this.user.id !== null) {
            this.userService.update(this.user).subscribe((response) => this.onSaveSuccess(response), () => this.onSaveError());
        } else {
            this.userService.create(this.user).subscribe((response) => this.onSaveSuccess(response), () => this.onSaveError());
        }
    }

    private onSaveSuccess(result) {
        this.eventManager.broadcast({ name: 'userListModification', content: 'OK' });
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }
}

@Component({
    selector: 'jhi-user-dialog',
    template: ''
})
export class UserDialogComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private userModalService: UserModalService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['login'] ) {
                this.userModalService.open(UserMgmtDialogComponent as Component, params['login']);
            } else {
                this.userModalService.open(UserMgmtDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
