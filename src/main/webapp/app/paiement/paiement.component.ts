import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { JhiLanguageService } from 'ng-jhipster';
import { Register } from '../account/register/register.service';
import { HttpErrorResponse, HttpClient } from '@angular/common/http';
import { LOGIN_ALREADY_USED_TYPE, EMAIL_ALREADY_USED_TYPE, UserService } from '../shared';

@Component({
  selector: 'jhi-paiement',
  templateUrl: './paiement.component.html',
  styleUrls: [
    'paiement.scss'
  ]
})
export class PaiementComponent implements OnInit {
  typePaiement = '';
  typevirement: string;

  confirmPassword: string;
  doNotMatch: string;
  error: string;
  errorEmailExists: string;
  errorUserExists: string;
  registerAccount: any;
  success: boolean;
  // passer = true;
  // name:string;
  // commercialName: string;
  // sirenNumber: string;
  // telephone: string;
  // modalRef: NgbModalRef;

  constructor(
    private route: ActivatedRoute,
    private languageService: JhiLanguageService,
    private registerService: Register,
    private http: HttpClient,
    private userService: UserService,
    private router: Router,
  ) { }

  ngOnInit() {
    this.route.queryParams.subscribe((p) => {
        this.typePaiement = p['typepaiement'];
        this.typevirement = this.typePaiement;
      });
      this.success = false;
      this.registerAccount = {};
  }

  register(data) {
    alert('okokokokok');
    if (data.password !== data.confirmPassword) {
      this.doNotMatch = 'ERROR';
      alert('Le mot de passe et la confirmation de celui-ci ne correspondent pas!');
    } else {
      this.doNotMatch = null;
      this.error = null;
      this.errorUserExists = null;
      this.errorEmailExists = null;
      this.languageService.getCurrent().then((key) => {
        // this.passer = false;
        // data.name = this.name;
        // data.commercialName = this.commercialName;
        // data.sirenNumber = this.sirenNumber;
        // data.telephone = this.telephone;
        if (this.typevirement === 'simple') {
          data.pack = 5;
        } else if (this.typevirement === 'pro') {
          data.pack = 10;
        } else {
          data.pack = 100;
        }
        data.langKey = key;
        this.router.navigate(['/virement'], { queryParams: { typevirement: this.typevirement, data: JSON.stringify(data) } });
      });
    }
  }
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
