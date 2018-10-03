import { Component, OnInit, EventEmitter, Output, Input } from '@angular/core';
import { LigneDevis } from '../ligne-devis/ligne-devis.model';
import { JhiAlertService, JhiEventManager } from 'ng-jhipster';
import { LigneDevisService } from '../ligne-devis';
import { DevisService, Devis } from '.';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import { Router } from '@angular/router';

@Component({
  selector: 'jhi-ligne-devis',
  templateUrl: './ligne-devis.component.html',
  styles: []
})
export class LigneDevisComponent implements OnInit {

    // ligneDevis: LigneDevis = new LigneDevis();
    // isSaving: boolean;
    // devis: Devis[];
    // @Input()
    // aggree: boolean;
    // @Output()
    // eventSendData: EventEmitter<any> = new EventEmitter<any>()
  constructor(
        // private jhiAlertService: JhiAlertService,
        // private ligneDevisService: LigneDevisService,
        // private devisService: DevisService,
        // private eventManager: JhiEventManager,
        // private router: Router
  ) { }

  ngOnInit() {
    // if(this.aggree) {
    //     this.eventSendData.emit("Bonnjour tout le monde");
    // }
    // this.isSaving = false;
    // this.devisService.query()
    //     .subscribe((res: HttpResponse<Devis[]>) => { this.devis = res.body; }, (res: HttpErrorResponse) => this.onError(res.message));
  }

//   save() {
//     this.isSaving = true;
//     if (this.ligneDevis.id !== undefined) {
//         this.subscribeToSaveResponse(
//             this.ligneDevisService.update(this.ligneDevis));
//     } else {
//         this.subscribeToSaveResponse(
//             this.ligneDevisService.create(this.ligneDevis));
//     }
// }

// private subscribeToSaveResponse(result: Observable<HttpResponse<LigneDevis>>) {
//     result.subscribe((res: HttpResponse<LigneDevis>) =>
//         this.onSaveSuccess(res.body), (res: HttpErrorResponse) => this.onSaveError());
// }

// private onSaveSuccess(result: LigneDevis) {
//     this.eventSendData.emit("bonjour tout le monde");
//     this.eventManager.broadcast({ name: 'ligneDevisListModification', content: 'OK'});
//     this.isSaving = false;
//    // this.router.navigated[''];
// }

// private onSaveError() {
//     this.isSaving = false;
// }

//   private onError(error: any) {
//     this.jhiAlertService.error(error.message, null, null);
// }

// trackDevisById(index: number, item: Devis) {
//   return item.id;
// }

}
