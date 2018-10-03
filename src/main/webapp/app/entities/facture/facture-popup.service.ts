import { Injectable, Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { HttpResponse } from '@angular/common/http';
import { Facture } from './facture.model';
import { FactureService } from './facture.service';

@Injectable()
export class FacturePopupService {
    private ngbModalRef: NgbModalRef;

    constructor(
        private modalService: NgbModal,
        private router: Router,
        private factureService: FactureService

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
                this.factureService.find(id)
                    .subscribe((factureResponse: HttpResponse<Facture>) => {
                        const facture: Facture = factureResponse.body;
                        if (facture.dateCreation) {
                            facture.dateCreation = {
                                year: facture.dateCreation.getFullYear(),
                                month: facture.dateCreation.getMonth() + 1,
                                day: facture.dateCreation.getDate()
                            };
                        }
                        this.ngbModalRef = this.factureModalRef(component, facture);
                        resolve(this.ngbModalRef);
                    });
            } else {
                // setTimeout used as a workaround for getting ExpressionChangedAfterItHasBeenCheckedError
                setTimeout(() => {
                    this.ngbModalRef = this.factureModalRef(component, new Facture());
                    resolve(this.ngbModalRef);
                }, 0);
            }
        });
    }

    factureModalRef(component: Component, facture: Facture): NgbModalRef {
        const modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static'});
        modalRef.componentInstance.facture = facture;
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
