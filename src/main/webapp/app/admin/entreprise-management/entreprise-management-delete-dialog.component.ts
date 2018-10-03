import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { Entreprise } from './entreprise.model';
import { EntrepriseModalService } from './entreprise-modal.service';
import { EntrepriseService } from './entreprise.service';

@Component({
    selector: 'jhi-entreprise-mgmt-delete-dialog',
    templateUrl: './entreprise-management-delete-dialog.component.html'
})
export class EntrepriseMgmtDeleteDialogComponent {

    entreprise: Entreprise;

    constructor(
        private entrepriseService: EntrepriseService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.entrepriseService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({ name: 'entrepriseListModification',
                content: 'Deleted a entreprise'});
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-entreprise-delete-dialog',
    template: ''
})
export class EntrepriseDeleteDialogComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private entrepriseModalService: EntrepriseModalService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.entrepriseModalService.open(EntrepriseMgmtDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
