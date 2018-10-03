import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { Chantier } from './chantier.model';
import { ChantierPopupService } from './chantier-popup.service';
import { ChantierService } from './chantier.service';

@Component({
    selector: 'jhi-chantier-delete-dialog',
    templateUrl: './chantier-delete-dialog.component.html'
})
export class ChantierDeleteDialogComponent {

    chantier: Chantier;

    constructor(
        private chantierService: ChantierService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.chantierService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'chantierListModification',
                content: 'Deleted an chantier'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-chantier-delete-popup',
    template: ''
})
export class ChantierDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private chantierPopupService: ChantierPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.chantierPopupService
                .open(ChantierDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
