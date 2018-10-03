import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { Travaux } from './travaux.model';
import { TravauxPopupService } from './travaux-popup.service';
import { TravauxService } from './travaux.service';

@Component({
    selector: 'jhi-travaux-delete-dialog',
    templateUrl: './travaux-delete-dialog.component.html'
})
export class TravauxDeleteDialogComponent {

    travaux: Travaux;

    constructor(
        private travauxService: TravauxService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.travauxService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'travauxListModification',
                content: 'Deleted an travaux'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-travaux-delete-popup',
    template: ''
})
export class TravauxDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private travauxPopupService: TravauxPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.travauxPopupService
                .open(TravauxDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
