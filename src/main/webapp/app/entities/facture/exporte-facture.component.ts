import { Component, OnInit, OnDestroy } from '@angular/core';
import { Facture } from './facture.model';
import { FactureService } from './facture.service';
import { JhiEventManager } from '../../../../../../node_modules/ng-jhipster';
import { Subscription } from '../../../../../../node_modules/rxjs';
import { ActivatedRoute } from '../../../../../../node_modules/@angular/router';
import { HttpResponse } from '../../../../../../node_modules/@angular/common/http';
import { LigneFacture, LigneFactureService } from '../ligne-facture';
import * as jsPDF from 'jspdf';
import { DatePipe } from '../../../../../../node_modules/@angular/common';

@Component({
  selector: 'jhi-exporte-facture',
  templateUrl: './exporte-facture.component.html',
  styles: []
})
export class ExporteFactureComponent implements OnInit, OnDestroy {

  facture: Facture;
  ligneFacture: LigneFacture[];
  private subscription: Subscription;
  private eventSubscriber: Subscription;
  total = 0.0;
  totaltva = 0.0;
  totalTTC = 0.0;
  texteFacture: string;
  texteClient: string;
  titre: string;
  ligne: string;
  constructor(
    private eventManager: JhiEventManager,
        private factureService: FactureService,
        private route: ActivatedRoute,
        private ligneFactureService: LigneFactureService,
        private datePipe: DatePipe
      ) { }

  ngOnInit() {
    this.subscription = this.route.params.subscribe((params) => {
      this.load(params['id']);
  });
  this.registerChangeInFactures();
  }
  load(id) {
    this.factureService.find(id)
        .subscribe((factureResponse: HttpResponse<Facture>) => {
            this.facture = factureResponse.body;
        this.texteFacture = '\n\n\n\n\n N° Facture: ' + this.facture.chantierId;
        this.texteFacture += '\n\n Date de creation: ' + this.datePipe.transform(this.facture.dateCreation, 'dd-MM-yyyy');
        this.texteFacture += '\n\n Valider: ' + this.facture.valider;
        this.titre = '\n\n\n\n\n Titre: ' + this.facture.travauxNomTrav;
        this.ligne = '\n Designetion\t  Prix Unitaire\t  Quantité\t  Total';
        this.texteClient = '\n\n\n\n\n N° Client: ';
        this.texteClient += '\n\n N° chantier: ' + this.facture.chantierId;
              this.texteClient += '\n\n Travau: ' + this.facture.travauxNomTrav;
            this.ligneFactureService.ligneFactureByFactureId(id)
                 .subscribe((rep: HttpResponse<LigneFacture[]>) => {
                   this.ligneFacture = rep.body;
                   this.ligneFacture.map((ligne) => {
                   this.total = this.total + ( ligne.prixUnitaire * ligne.quantite );
               });
                 });
        });
}
previousState() {
    window.history.back();
}

ngOnDestroy() {
    this.subscription.unsubscribe();
    this.eventManager.destroy(this.eventSubscriber);
}

registerChangeInFactures() {
    this.eventSubscriber = this.eventManager.subscribe(
        'factureListModification',
        (response) => this.load(this.facture.id)
    );
}
download() {
  const doc = new jsPDF();
  doc.text(this.texteFacture, 10, 10);
  doc.text(this.texteClient, 100, 10);
  doc.text(this.titre, 55, 50);
  doc.text(this.ligne, 10, 90);
  let i = 110;
  this.ligneFacture.map((ligne) => {
      doc.text( ligne.designation, 10, i);
      doc.text((ligne.prixUnitaire).toString(), 55, i);
      doc.text((ligne.quantite).toString(), 105, i);
      doc.text( (ligne.prixUnitaire * ligne.quantite).toString(), 140, i);
      i = i + 10;
 });
 doc.text('Total HT: ' + this.total, 110, i + 10);
 doc.text('Total TVA: ' + this.totaltva, 110, i + 20);
 doc.text('Total TTC: ' + this.totalTTC, 110, i + 30);
  doc.save('facture.pdf');
}

}
