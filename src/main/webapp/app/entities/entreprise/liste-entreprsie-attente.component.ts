import { Component, OnInit, ViewChild, Inject, OnDestroy } from '@angular/core';
import { MatPaginator, MatSort, MatTableDataSource, PageEvent } from '@angular/material';
import { Entreprise } from '../../entities/entreprise/entreprise.model';
import { EntrepriseService } from '../../entities/entreprise/entreprise.service';
import {MatDialog, MatDialogRef, MAT_DIALOG_DATA} from '@angular/material';
import { JhiParseLinks, JhiAlertService, JhiEventManager, JhiLanguageService } from 'ng-jhipster';
import { Router, ActivatedRoute } from '@angular/router';
import { Principal, ITEMS_PER_PAGE, UserService, User } from '../../shared';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs/Subscription';
// import { Register } from '../account';
// import {Md5} from 'ts-md5/dist/md5';
import { Register } from '../../account';
import { JsonPipe } from '../../../../../../node_modules/@angular/common';
import PNotify from 'pnotify/dist/es/PNotify';
import PNotifyButtons from 'pnotify/dist/es/PNotifyButtons';

@Component({
  selector: 'jhi-liste-entreprsie-attente',
  templateUrl: './liste-entreprsie-attente.component.html',
  styles: []
})
export class ListeEntreprsieAttenteComponent implements OnInit, OnDestroy {

  // constructor() { }
  displayedColumns = ['id', 'nomEntreprise', 'nomCommercial', 'emailAdministrateur', 'bouton'];
  dataSource: MatTableDataSource<Entreprise>;
  entreprises: any;
  entrepriseByRow: any;
  entrepriseRowID: any;
  newUserEmail: any;
  newUserLogin: any;
  eventSubscriber: Subscription;
  // generator: any = require('generate-password');
  // generatedPassword: any;
  // md5: any = new Md5();
  encryptedPassword: any;
  utilisateurs: any;
  entreprisesSansUsers: any;
  length = 100;
  pageSize = 10;
  pageSizeOptions = [5, 10, 25, 100];
  pageEvent: PageEvent;
  success: boolean;
  user: User = new User();
  entreprise: Entreprise;

  @ViewChild(MatPaginator) paginator: MatPaginator;
  @ViewChild(MatSort) sort: MatSort;

  setPageSizeOptions(setPageSizeOptionsInput: string) {
    this.pageSizeOptions = setPageSizeOptionsInput.split(',').map((str) => +str);
  }

  constructor(
      private entrepriseService: EntrepriseService,
      private registerService: Register,
      private userService: UserService,
      private router: Router,
      private eventManager: JhiEventManager,
      private languageService: JhiLanguageService
  ) {
    PNotify.defaults.styling = 'bootstrap4';
    PNotify.defaults.width = '400px';
    PNotify.defaults.heigth = '400px';
    PNotify.defaults.icons = 'fontawesome4';
    PNotify.defaults.delay = 3000;
  }

  loadAll() {
    this.entrepriseService.getlisteAttente().subscribe((data: any) => {
      this.entreprises = data.body;
      this.dataSource = new MatTableDataSource(this.entreprises);
        this.dataSource.paginator = this.paginator;
        this.dataSource.sort = this.sort;
    });
  }
validerCompte(id) {
  this.entrepriseService.find(id)
            .subscribe((entrepriseResponse: HttpResponse<Entreprise>) => {
                this.entreprise = entrepriseResponse.body;
                this.user.password = this.entreprise.name + '' + this.entreprise.id;
                this.user.login = this.entreprise.name;
                this.user.firstName = this.entreprise.name;
                this.user.lastName = this.entreprise.name;
                this.user.email = this.entreprise.entrepriseAdminEmail;
                this.user.entreprise = this.entreprise;
                this.user.entreprise.id = this.entreprise.id;
                this.user.entreprise.name = this.entreprise.name;
                this.languageService.getCurrent().then((key) => {
                  this.user.langKey = key;
              });
              console.log(this.user);
              this.entrepriseService.validerCompte(this.user).subscribe(() => {
                this.success = true;
                this.loadAll();
                PNotify.error({
                  text: '\n\n Le compte a bien été activé\n\n',
                  stack: {
                    dir1: 'down', dir2: 'right', // Position from the top left corner.
                    firstpos1: 60, firstpos2: 400 // 90px from the top, 90px from the left.
                  }
                });
                alert('compte bien activé');
              }, (reponse) => this.processError(reponse));
            });
}

  ngOnInit() {
    this.loadAll();
    this.registerChangeInEntreprises();
  }
  registerChangeInEntreprises() {
    this.eventSubscriber = this.eventManager.subscribe('userListModification', (response) => this.loadAll());
}
  applyFilter(filterValue: string) {
    filterValue = filterValue.trim(); // Remove whitespace
    filterValue = filterValue.toLowerCase(); // MatTableDataSource defaults to lowercase matches
    this.dataSource.filter = filterValue;
    if (this.dataSource.paginator) {
        this.dataSource.paginator.firstPage();
      }
  }
  processError(response: HttpErrorResponse) {
   PNotify.error({
    text: response.error.title,
    stack: {
      dir1: 'down', dir2: 'right', // Position from the top left corner.
      firstpos1: 60, firstpos2: 400 // 90px from the top, 90px from the left.
    }
  });
  }
  ngOnDestroy() {
    this.eventManager.destroy(this.eventSubscriber);
}
  // ngOnInit() {
  // }

}
