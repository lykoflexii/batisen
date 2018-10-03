import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiLanguageService } from 'ng-jhipster';

import { ProfileService } from '../profiles/profile.service';
import { JhiLanguageHelper, Principal, LoginModalService, LoginService } from '../../shared';

import { VERSION } from '../../app.constants';
import { EntrepriseService } from '../../entities/entreprise';
import { Entreprise } from '../../admin/entreprise-management/entreprise.model';
import { HttpResponse } from '@angular/common/http';

@Component({
    selector: 'jhi-navbar',
    templateUrl: './navbar.component.html',
    styleUrls: [
        'navbar.scss'
    ]
})
export class NavbarComponent implements OnInit {
    inProduction: boolean;
    isNavbarCollapsed: boolean;
    languages: any[];
    swaggerEnabled: boolean;
    modalRef: NgbModalRef;
    version: string;
    nomEntrepise: string;
    nomprenom: string;

    constructor(
        private loginService: LoginService,
        private languageService: JhiLanguageService,
        private languageHelper: JhiLanguageHelper,
        private principal: Principal,
        private loginModalService: LoginModalService,
        private profileService: ProfileService,
        private router: Router,
        private entrepriseService: EntrepriseService
    ) {
        this.version = VERSION ? 'v' + VERSION : '';
        this.isNavbarCollapsed = true;
    }

    ngOnInit() {
        this.nomEntrepise = 'BatiSen';
        this.languageHelper.getAll().then((languages) => {
            this.languages = languages;
        });
        this.profileService.getProfileInfo().then((profileInfo) => {
            this.inProduction = profileInfo.inProduction;
            this.swaggerEnabled = profileInfo.swaggerEnabled;
        });
       setInterval(() => { this.changerNom(); }, 5000);
    }

    changeLanguage(languageKey: string) {
      this.languageService.changeLanguage(languageKey);
    }

    collapseNavbar() {
        this.isNavbarCollapsed = true;
    }

    isAuthenticated() {
        return this.principal.isAuthenticated();
    }

    login() {
        this.modalRef = this.loginModalService.open();
    }

    logout() {
        this.collapseNavbar();
        this.loginService.logout();
        for (let i = 0; i < sessionStorage.length; i++) {
                const a = sessionStorage.key(i);
                sessionStorage.removeItem(a);
            }
        this.router.navigate(['']);
    }

    toggleNavbar() {
        this.isNavbarCollapsed = !this.isNavbarCollapsed;
    }

    hasEntreprise() {
        return this.principal.getEntreprise() ? true : false;
    }
    getImageUrl() {
        return this.isAuthenticated() ? this.principal.getImageUrl() : null;
    }
    changerNom() {
        if (this.isAuthenticated()) {
            this.nomEntrepise = sessionStorage.getItem('entreprise_name');
            this.nomprenom = sessionStorage.getItem('username');
        } else {
            this.nomEntrepise = 'BatiSen';
        }
    }
}
