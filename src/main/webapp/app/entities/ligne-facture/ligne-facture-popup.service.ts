import { Injectable, Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { HttpResponse } from '@angular/common/http';
import { LigneFacture } from './ligne-facture.model';
import { LigneFactureService } from './ligne-facture.service';

@Injectable()
export class LigneFacturePopupService {
    private ngbModalRef: NgbModalRef;

    constructor(
        private modalService: NgbModal,
        private router: Router,
        private ligneFactureService: LigneFactureService

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
                this.ligneFactureService.find(id)
                    .subscribe((ligneFactureResponse: HttpResponse<LigneFacture>) => {
                        const ligneFacture: LigneFacture = ligneFactureResponse.body;
                        this.ngbModalRef = this.ligneFactureModalRef(component, ligneFacture);
                        resolve(this.ngbModalRef);
                    });
            } else {
                // setTimeout used as a workaround for getting ExpressionChangedAfterItHasBeenCheckedError
                setTimeout(() => {
                    this.ngbModalRef = this.ligneFactureModalRef(component, new LigneFacture());
                    resolve(this.ngbModalRef);
                }, 0);
            }
        });
    }

    ligneFactureModalRef(component: Component, ligneFacture: LigneFacture): NgbModalRef {
        const modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static'});
        modalRef.componentInstance.ligneFacture = ligneFacture;
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
