import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { LigneFacture } from './ligne-facture.model';
import { LigneFacturePopupService } from './ligne-facture-popup.service';
import { LigneFactureService } from './ligne-facture.service';

@Component({
    selector: 'jhi-ligne-facture-delete-dialog',
    templateUrl: './ligne-facture-delete-dialog.component.html'
})
export class LigneFactureDeleteDialogComponent {

    ligneFacture: LigneFacture;

    constructor(
        private ligneFactureService: LigneFactureService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.ligneFactureService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'ligneFactureListModification',
                content: 'Deleted an ligneFacture'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-ligne-facture-delete-popup',
    template: ''
})
export class LigneFactureDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private ligneFacturePopupService: LigneFacturePopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.ligneFacturePopupService
                .open(LigneFactureDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
