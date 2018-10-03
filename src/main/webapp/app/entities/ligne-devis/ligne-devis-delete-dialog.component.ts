import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { LigneDevis } from './ligne-devis.model';
import { LigneDevisPopupService } from './ligne-devis-popup.service';
import { LigneDevisService } from './ligne-devis.service';

@Component({
    selector: 'jhi-ligne-devis-delete-dialog',
    templateUrl: './ligne-devis-delete-dialog.component.html'
})
export class LigneDevisDeleteDialogComponent {

    ligneDevis: LigneDevis;

    constructor(
        private ligneDevisService: LigneDevisService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.ligneDevisService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'ligneDevisListModification',
                content: 'Deleted an ligneDevis'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-ligne-devis-delete-popup',
    template: ''
})
export class LigneDevisDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private ligneDevisPopupService: LigneDevisPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.ligneDevisPopupService
                .open(LigneDevisDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
