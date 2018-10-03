import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { Entreprise } from './entreprise.model';
import { EntreprisePopupService } from './entreprise-popup.service';
import { EntrepriseService } from './entreprise.service';

@Component({
    selector: 'jhi-entreprise-delete-dialog',
    templateUrl: './entreprise-delete-dialog.component.html'
})
export class EntrepriseDeleteDialogComponent {

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
            this.eventManager.broadcast({
                name: 'entrepriseListModification',
                content: 'Deleted an entreprise'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-entreprise-delete-popup',
    template: ''
})
export class EntrepriseDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private entreprisePopupService: EntreprisePopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.entreprisePopupService
                .open(EntrepriseDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
