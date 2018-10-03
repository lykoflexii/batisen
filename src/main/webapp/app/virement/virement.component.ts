import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Register } from '../account';
import { HttpClient, HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { LOGIN_ALREADY_USED_TYPE, EMAIL_ALREADY_USED_TYPE } from '../shared';
import { Entreprise, EntrepriseService } from '../entities/entreprise';
import { Observable } from '../../../../../node_modules/rxjs';
import { JhiEventManager } from '../../../../../node_modules/ng-jhipster';

@Component({
  selector: 'jhi-virement',
  templateUrl: './virement.component.html',
  styleUrls: [
    'virement.scss'
  ]
})
export class VirementComponent implements OnInit {
  typeVirement = '';
  donnees: any;
  success: boolean;
  error: string;
  errorEmailExists: string;
  errorUserExists: string;
  entreprise: Entreprise;
  isSaving: boolean;

  constructor(private route: ActivatedRoute,
    private registerService: Register,
    private http: HttpClient,
    private entrepriseService: EntrepriseService,
    private eventManager: JhiEventManager
  ) { }

  ngOnInit() {
    this.route.queryParams.subscribe((p) => {
      this.typeVirement = p['typevirement'];
      this.donnees = JSON.parse(p['data']);
      this.entreprise = JSON.parse(p['data']);
      // this.entreprise.name = this.donnees.name;
      // this.entreprise.commercialName = this.donnees.commercialName;
      // this.entreprise.sirenNumber = this.donnees.sirenNumber;
      // this.entreprise.entrepriseAdminEmail = this.donnees.entrepriseAdminEmail;
      // this.entreprise.telephone = this.donnees.telephone;
      // this.entreprise.pack = this.donnees.pack;
    });
  }

  payement() {
    this.error = null;
    this.errorUserExists = null;
    this.errorEmailExists = null;
      this.registerService.save(this.donnees).subscribe(() => {
        this.success = true;
        alert('Patientez le temps que l\'on vous recontacte avec les informations de votre compte.');
      },
        (response) => this.processError(response));

  }
//   save() {
//     this.isSaving = true;
//     this.subscribeToSaveResponse( this.entrepriseService.create(this.entreprise));
// }

// private subscribeToSaveResponse(result: Observable<HttpResponse<Entreprise>>) {
//     result.subscribe((res: HttpResponse<Entreprise>) =>
//         this.onSaveSuccess(res.body), (res: HttpErrorResponse) => this.onSaveError());
// }

// private onSaveSuccess(result: Entreprise) {
//     this.eventManager.broadcast({ name: 'entrepriseListModification', content: 'OK'});
//     this.isSaving = false;
// }

// private onSaveError() {
//     this.isSaving = false;
// }

  private processError(response: HttpErrorResponse) {
    this.success = null;
    if (response.status === 400 && response.error.type === LOGIN_ALREADY_USED_TYPE) {
      this.errorUserExists = 'ERROR';
    } else if (response.status === 400 && response.error.type === EMAIL_ALREADY_USED_TYPE) {
      this.errorEmailExists = 'ERROR';
    } else {
      this.error = 'ERROR';
    }
  }

}
