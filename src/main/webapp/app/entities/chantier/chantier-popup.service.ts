import { Injectable, Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { HttpResponse } from '@angular/common/http';
import { Chantier } from './chantier.model';
import { ChantierService } from './chantier.service';

@Injectable()
export class ChantierPopupService {
    private ngbModalRef: NgbModalRef;

    constructor(
        private modalService: NgbModal,
        private router: Router,
        private chantierService: ChantierService

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
                this.chantierService.find(id)
                    .subscribe((chantierResponse: HttpResponse<Chantier>) => {
                        const chantier: Chantier = chantierResponse.body;
                        if (chantier.dateDebutReelle) {
                            chantier.dateDebutReelle = {
                                year: chantier.dateDebutReelle.getFullYear(),
                                month: chantier.dateDebutReelle.getMonth() + 1,
                                day: chantier.dateDebutReelle.getDate()
                            };
                        }
                        if (chantier.dateFinReelle) {
                            chantier.dateFinReelle = {
                                year: chantier.dateFinReelle.getFullYear(),
                                month: chantier.dateFinReelle.getMonth() + 1,
                                day: chantier.dateFinReelle.getDate()
                            };
                        }
                        if (chantier.dateDebutPrevu) {
                            chantier.dateDebutPrevu = {
                                year: chantier.dateDebutPrevu.getFullYear(),
                                month: chantier.dateDebutPrevu.getMonth() + 1,
                                day: chantier.dateDebutPrevu.getDate()
                            };
                        }
                        if (chantier.dateFinPrevu) {
                            chantier.dateFinPrevu = {
                                year: chantier.dateFinPrevu.getFullYear(),
                                month: chantier.dateFinPrevu.getMonth() + 1,
                                day: chantier.dateFinPrevu.getDate()
                            };
                        }
                        this.ngbModalRef = this.chantierModalRef(component, chantier);
                        resolve(this.ngbModalRef);
                    });
            } else {
                // setTimeout used as a workaround for getting ExpressionChangedAfterItHasBeenCheckedError
                setTimeout(() => {
                    this.ngbModalRef = this.chantierModalRef(component, new Chantier());
                    resolve(this.ngbModalRef);
                }, 0);
            }
        });
    }

    chantierModalRef(component: Component, chantier: Chantier): NgbModalRef {
        const modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static'});
        modalRef.componentInstance.chantier = chantier;
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
