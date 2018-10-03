import { Injectable, Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { Entreprise } from './entreprise.model';
import { EntrepriseService } from './entreprise.service';
import { HttpResponse } from '@angular/common/http';

@Injectable()
export class EntrepriseModalService {
    private ngbModalRef: NgbModalRef;

    constructor(
        private modalService: NgbModal,
        private router: Router,
        private entrepriseService: EntrepriseService
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
                this.entrepriseService.find(id).subscribe((entreprise) => {
                    this.ngbModalRef = this.entrepriseModalRef(component, entreprise);
                    resolve(this.ngbModalRef);
                });
            } else {
                // setTimeout used as a workaround for getting ExpressionChangedAfterItHasBeenCheckedError
                setTimeout(() => {
                    this.ngbModalRef = this.entrepriseModalRef(component, new HttpResponse<Entreprise>());
                    resolve(this.ngbModalRef);
                }, 0);
            }
        });
    }

    entrepriseModalRef(component: Component, entreprise: HttpResponse<Entreprise>): NgbModalRef {
        const modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static'});
        modalRef.componentInstance.entreprise = entreprise;
        modalRef.result.then((result) => {
            this.router.navigate([{ outlets: { popup: null }}], { replaceUrl: true });
            this.ngbModalRef = null;
        }, (reason) => {
            this.router.navigate([{ outlets: { popup: null }}], { replaceUrl: true });
            this.ngbModalRef = null;
        });
        return modalRef;
    }
}
