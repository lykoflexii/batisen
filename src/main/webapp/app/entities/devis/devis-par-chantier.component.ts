import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs/Subscription';
import { JhiEventManager } from 'ng-jhipster';

import { Devis } from './devis.model';
import { DevisService } from './devis.service';
import { Entreprise } from '../entreprise';
import { LigneDevisService, LigneDevis } from '../ligne-devis';
import { Client, ClientService } from '../client';
import * as jsPDF from 'jspdf';
import * as html2canvas from 'html2canvas';
import { DomSanitizer } from '@angular/platform-browser';
import { DatePipe } from '@angular/common';

@Component({
  selector: 'jhi-devis-par-chantier',
  templateUrl: './devis-par-chantier.component.html',
  styles: []
})
export class DevisParChantierComponent implements OnInit, OnDestroy {

    devis: Devis;
    ligneDevis: LigneDevis[];
    entreprise: Entreprise;
    private subscription: Subscription;
    private eventSubscriber: Subscription;
    id: number;
    client: Client;
    total = 0.0;
    totaltva = 0.0;
    totalTTC = 0.0;
    texteDevis: string;
    texteClient: string;
    titre: string;
    ligne: string;

  constructor(
        private eventManager: JhiEventManager,
        private devisService: DevisService,
        private route: ActivatedRoute,
        private router: Router,
        private ligneDevisService: LigneDevisService,
        private clientService: ClientService,
        private sanitizer: DomSanitizer,
        private datePipe: DatePipe
  ) { }
  ngOnInit() {
    this.subscription = this.route.params.subscribe((params) => {
        this.id = params['id'];
        this.load(params['id']);
    });
    this.registerChangeInDevis();
}

load(id) {
//    this.devisService.devisByChantierId(id)
//         .subscribe((devisResponse: HttpResponse<Devis>) => {
//             this.devis = devisResponse.body;
//             this.ligneDevisService.ligneDevisByDevisId(this.devis.id).subscribe((ligneDevisResponse: HttpResponse<LigneDevis[]>) => {
//               this.ligneDevis = ligneDevisResponse.body;
//               this.ligneDevis.map((ligne) => {
//                    this.total = this.total + ( ligne.prixUnitaire * ligne.quantite );
//               });
//               this.totaltva = this.total * this.devis.tva / 100;
//               this.totalTTC = this.total + this.totaltva ;
//             });
//             this.clientService.find(this.devis.chantierId)
//             .subscribe((clientResponse: HttpResponse<Client>) => {
//                 this.client = clientResponse.body;
//             });
//         });
    this.devisService.find(id)
    .subscribe((devisResponse: HttpResponse<Devis>) => {
        this.devis = devisResponse.body;
        this.texteDevis = '\n\n\n\n\n N° Devis: ' + this.devis.chantierId;
        this.texteDevis += '\n\n Date: ' + this.datePipe.transform(this.devis.dateDeCreation, 'dd-MM-yyyy');
        this.texteDevis += '\n\n Validite: ' + this.datePipe.transform(this.devis.validite, 'dd-MM-yy');
        this.titre = '\n\n\n\n\n Titre: ' + this.devis.titre;
        this.ligne = '\n Designetion\t  Prix Unitaire\t  Quantité\t  Total';
        this.ligneDevisService.ligneDevisByDevisId(this.devis.id).subscribe((ligneDevisResponse: HttpResponse<LigneDevis[]>) => {
            this.ligneDevis = ligneDevisResponse.body;
            this.ligneDevis.map((ligne) => {
                 this.total = this.total + ( ligne.prixUnitaire * ligne.quantite );
                //  this.ligne += '\n\n ' + ligne.designation + '\t\t' + ligne.prixUnitaire +'\t\t' + ligne.quantite + '\t\t' + ( ligne.prixUnitaire * ligne.quantite );
            });
            this.totaltva = this.total * this.devis.tva / 100;
            this.totalTTC = this.total + this.totaltva ;
          });
          this.clientService.find(this.devis.clientId)
          .subscribe((clientResponse: HttpResponse<Client>) => {
              this.client = clientResponse.body;
              this.texteClient = '\n\n\n\n\n N° Client: ' + this.client.id;
              this.texteClient += '\n\n Adresse: ' + this.client.adresseClient;
              this.texteClient += '\n\n Email: ' + this.client.emailClient;
          });
    });
}
previousState() {
  this.router.navigate(['/affectations/', +sessionStorage.getItem('idChantier') ]);
}

ngOnDestroy() {
    this.subscription.unsubscribe();
    this.eventManager.destroy(this.eventSubscriber);
}

registerChangeInDevis() {
    this.eventSubscriber = this.eventManager.subscribe(
        'devisListModification',
        (response) => this.load(this.id)
    );
}
download() {
//     html2canvas(document.getElementById('dev'), { useCORS: true  }).then((canvas) =>  {
//       const img = new Image();
//       img.crossOrigin = 'Anonymous';
//       img.src = canvas.toDataURL('image/png');
//       const doc = new jsPDF();
//       doc.addImage(img, 'JPEG', 10, 10, 180, 150);
//       doc.save('test.pdf');
//   });
    const doc = new jsPDF();
    doc.text(this.texteDevis, 10, 10);
    doc.text(this.texteClient, 100, 10);
    doc.text(this.titre, 55, 50);
    doc.text(this.ligne, 10, 90);
    let i = 110;
    this.ligneDevis.map((ligne) => {
        doc.text( ligne.designation, 10, i);
        doc.text((ligne.prixUnitaire).toString(), 55, i);
        doc.text((ligne.quantite).toString(), 105, i);
        doc.text( (ligne.prixUnitaire * ligne.quantite).toString(), 140, i);
        i = i + 10;
   });
   doc.text('Total HT: ' + this.total, 110, i + 10);
   doc.text('Total TVA: ' + this.totaltva, 110, i + 20);
   doc.text('Total TTC: ' + this.totalTTC, 110, i + 30);
    doc.save('devis.pdf');
}
}
