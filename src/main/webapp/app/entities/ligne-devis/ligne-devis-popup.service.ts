import { Injectable, Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { HttpResponse } from '@angular/common/http';
import { LigneDevis } from './ligne-devis.model';
import { LigneDevisService } from './ligne-devis.service';

@Injectable()
export class LigneDevisPopupService {
    private ngbModalRef: NgbModalRef;

    constructor(
        private modalService: NgbModal,
        private router: Router,
        private ligneDevisService: LigneDevisService

    ) {
        this.ngbModalRef = null;
    }

    open(component: Component, id?: number | any): Promise<NgbModalRef> {
        return new Promise<NgbModalRef>((resolve, reject) => {
            const isOpen = this.ngbModalRef !== null;
            if (isOpen) {
                resolve(this.ngbModalRef);
            }

            if (id) {
                this.ligneDevisService.find(id)
                    .subscribe((ligneDevisResponse: HttpResponse<LigneDevis>) => {
                        const ligneDevis: LigneDevis = ligneDevisResponse.body;
                        this.ngbModalRef = this.ligneDevisModalRef(component, ligneDevis);
                        resolve(this.ngbModalRef);
                    });
            } else {
                // setTimeout used as a workaround for getting ExpressionChangedAfterItHasBeenCheckedError
                setTimeout(() => {
                    this.ngbModalRef = this.ligneDevisModalRef(component, new LigneDevis());
                    resolve(this.ngbModalRef);
                }, 0);
            }
        });
    }

    ligneDevisModalRef(component: Component, ligneDevis: LigneDevis): NgbModalRef {
        const modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static'});
        modalRef.componentInstance.ligneDevis = ligneDevis;
        modalRef.result.then((result) => {
            this.router.navigate([{ outlets: { popup: null }}], { replaceUrl: true, queryParamsHandling: 'merge' });
            this.ngbModalRef = null;
        }, (reason) => {
            this.router.navigate([{ outlets: { popup: null }}], { replaceUrl: true, queryParamsHandling: 'merge' });
            this.ngbModalRef = null;
        });
        return modalRef;
    }
}
