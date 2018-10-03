import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { Facture } from './facture.model';
import { FacturePopupService } from './facture-popup.service';
import { FactureService } from './facture.service';

@Component({
    selector: 'jhi-facture-delete-dialog',
    templateUrl: './facture-delete-dialog.component.html'
})
export class FactureDeleteDialogComponent {

    facture: Facture;

    constructor(
        private factureService: FactureService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.factureService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'factureListModification',
                content: 'Deleted an facture'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-facture-delete-popup',
    template: ''
})
export class FactureDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private facturePopupService: FacturePopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.facturePopupService
                .open(FactureDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
