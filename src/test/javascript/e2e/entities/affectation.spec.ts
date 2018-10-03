import { browser, element, by } from 'protractor';
import { NavBarPage } from './../page-objects/jhi-page-objects';

describe('Affectation e2e test', () => {

    let navBarPage: NavBarPage;
    let affectationDialogPage: AffectationDialogPage;
    let affectationComponentsPage: AffectationComponentsPage;

    beforeAll(() => {
        browser.get('/');
        browser.waitForAngular();
        navBarPage = new NavBarPage();
        navBarPage.getSignInPage().autoSignInUsing('admin', 'admin');
        browser.waitForAngular();
    });

    it('should load Affectations', () => {
        navBarPage.goToEntity('affectation');
        affectationComponentsPage = new AffectationComponentsPage();
        expect(affectationComponentsPage.getTitle())
            .toMatch(/gesBtpApp.affectation.home.title/);

    });

    it('should load create Affectation dialog', () => {
        affectationComponentsPage.clickOnCreateButton();
        affectationDialogPage = new AffectationDialogPage();
        expect(affectationDialogPage.getModalTitle())
            .toMatch(/gesBtpApp.affectation.home.createOrEditLabel/);
        affectationDialogPage.close();
    });

    it('should create and save Affectations', () => {
        affectationComponentsPage.clickOnCreateButton();
        affectationDialogPage.setDateDebutInput('2000-12-31');
        expect(affectationDialogPage.getDateDebutInput()).toMatch('2000-12-31');
        affectationDialogPage.setDateFinInput('2000-12-31');
        expect(affectationDialogPage.getDateFinInput()).toMatch('2000-12-31');
        affectationDialogPage.etatSelectLastOption();
        affectationDialogPage.travauxSelectLastOption();
        affectationDialogPage.chantierSelectLastOption();
        // affectationDialogPage.employeSelectLastOption();
        affectationDialogPage.save();
        expect(affectationDialogPage.getSaveButton().isPresent()).toBeFalsy();
    });

    afterAll(() => {
        navBarPage.autoSignOut();
    });
});

export class AffectationComponentsPage {
    createButton = element(by.css('.jh-create-entity'));
    title = element.all(by.css('jhi-affectation div h2 span')).first();

    clickOnCreateButton() {
        return this.createButton.click();
    }

    getTitle() {
        return this.title.getAttribute('jhiTranslate');
    }
}

export class AffectationDialogPage {
    modalTitle = element(by.css('h4#myAffectationLabel'));
    saveButton = element(by.css('.modal-footer .btn.btn-primary'));
    closeButton = element(by.css('button.close'));
    dateDebutInput = element(by.css('input#field_dateDebut'));
    dateFinInput = element(by.css('input#field_dateFin'));
    etatSelect = element(by.css('select#field_etat'));
    travauxSelect = element(by.css('select#field_travaux'));
    chantierSelect = element(by.css('select#field_chantier'));
    employeSelect = element(by.css('select#field_employe'));

    getModalTitle() {
        return this.modalTitle.getAttribute('jhiTranslate');
    }

    setDateDebutInput = function(dateDebut) {
        this.dateDebutInput.sendKeys(dateDebut);
    };

    getDateDebutInput = function() {
        return this.dateDebutInput.getAttribute('value');
    };

    setDateFinInput = function(dateFin) {
        this.dateFinInput.sendKeys(dateFin);
    };

    getDateFinInput = function() {
        return this.dateFinInput.getAttribute('value');
    };

    setEtatSelect = function(etat) {
        this.etatSelect.sendKeys(etat);
    };

    getEtatSelect = function() {
        return this.etatSelect.element(by.css('option:checked')).getText();
    };

    etatSelectLastOption = function() {
        this.etatSelect.all(by.tagName('option')).last().click();
    };
    travauxSelectLastOption = function() {
        this.travauxSelect.all(by.tagName('option')).last().click();
    };

    travauxSelectOption = function(option) {
        this.travauxSelect.sendKeys(option);
    };

    getTravauxSelect = function() {
        return this.travauxSelect;
    };

    getTravauxSelectedOption = function() {
        return this.travauxSelect.element(by.css('option:checked')).getText();
    };

    chantierSelectLastOption = function() {
        this.chantierSelect.all(by.tagName('option')).last().click();
    };

    chantierSelectOption = function(option) {
        this.chantierSelect.sendKeys(option);
    };

    getChantierSelect = function() {
        return this.chantierSelect;
    };

    getChantierSelectedOption = function() {
        return this.chantierSelect.element(by.css('option:checked')).getText();
    };

    employeSelectLastOption = function() {
        this.employeSelect.all(by.tagName('option')).last().click();
    };

    employeSelectOption = function(option) {
        this.employeSelect.sendKeys(option);
    };

    getEmployeSelect = function() {
        return this.employeSelect;
    };

    getEmployeSelectedOption = function() {
        return this.employeSelect.element(by.css('option:checked')).getText();
    };

    save() {
        this.saveButton.click();
    }

    close() {
        this.closeButton.click();
    }

    getSaveButton() {
        return this.saveButton;
    }
}
