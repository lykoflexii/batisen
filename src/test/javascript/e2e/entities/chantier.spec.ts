import { browser, element, by } from 'protractor';
import { NavBarPage } from './../page-objects/jhi-page-objects';

describe('Chantier e2e test', () => {

    let navBarPage: NavBarPage;
    let chantierDialogPage: ChantierDialogPage;
    let chantierComponentsPage: ChantierComponentsPage;

    beforeAll(() => {
        browser.get('/');
        browser.waitForAngular();
        navBarPage = new NavBarPage();
        navBarPage.getSignInPage().autoSignInUsing('admin', 'admin');
        browser.waitForAngular();
    });

    it('should load Chantiers', () => {
        navBarPage.goToEntity('chantier');
        chantierComponentsPage = new ChantierComponentsPage();
        expect(chantierComponentsPage.getTitle())
            .toMatch(/gesBtpApp.chantier.home.title/);

    });

    it('should load create Chantier dialog', () => {
        chantierComponentsPage.clickOnCreateButton();
        chantierDialogPage = new ChantierDialogPage();
        expect(chantierDialogPage.getModalTitle())
            .toMatch(/gesBtpApp.chantier.home.createOrEditLabel/);
        chantierDialogPage.close();
    });

    it('should create and save Chantiers', () => {
        chantierComponentsPage.clickOnCreateButton();
        chantierDialogPage.setNomChantierInput('nomChantier');
        expect(chantierDialogPage.getNomChantierInput()).toMatch('nomChantier');
        chantierDialogPage.setDescriptionChantierInput('descriptionChantier');
        expect(chantierDialogPage.getDescriptionChantierInput()).toMatch('descriptionChantier');
        chantierDialogPage.setVilleInput('ville');
        expect(chantierDialogPage.getVilleInput()).toMatch('ville');
        chantierDialogPage.setAdresseInput('adresse');
        expect(chantierDialogPage.getAdresseInput()).toMatch('adresse');
        chantierDialogPage.etatChantierSelectLastOption();
        chantierDialogPage.setDateDebutReelleInput('2000-12-31');
        expect(chantierDialogPage.getDateDebutReelleInput()).toMatch('2000-12-31');
        chantierDialogPage.setDateFinReelleInput('2000-12-31');
        expect(chantierDialogPage.getDateFinReelleInput()).toMatch('2000-12-31');
        chantierDialogPage.setDateDebutPrevuInput('2000-12-31');
        expect(chantierDialogPage.getDateDebutPrevuInput()).toMatch('2000-12-31');
        chantierDialogPage.setDateFinPrevuInput('2000-12-31');
        expect(chantierDialogPage.getDateFinPrevuInput()).toMatch('2000-12-31');
        chantierDialogPage.clientSelectLastOption();
        chantierDialogPage.userSelectLastOption();
        chantierDialogPage.save();
        expect(chantierDialogPage.getSaveButton().isPresent()).toBeFalsy();
    });

    afterAll(() => {
        navBarPage.autoSignOut();
    });
});

export class ChantierComponentsPage {
    createButton = element(by.css('.jh-create-entity'));
    title = element.all(by.css('jhi-chantier div h2 span')).first();

    clickOnCreateButton() {
        return this.createButton.click();
    }

    getTitle() {
        return this.title.getAttribute('jhiTranslate');
    }
}

export class ChantierDialogPage {
    modalTitle = element(by.css('h4#myChantierLabel'));
    saveButton = element(by.css('.modal-footer .btn.btn-primary'));
    closeButton = element(by.css('button.close'));
    nomChantierInput = element(by.css('input#field_nomChantier'));
    descriptionChantierInput = element(by.css('input#field_descriptionChantier'));
    villeInput = element(by.css('input#field_ville'));
    adresseInput = element(by.css('input#field_adresse'));
    etatChantierSelect = element(by.css('select#field_etatChantier'));
    dateDebutReelleInput = element(by.css('input#field_dateDebutReelle'));
    dateFinReelleInput = element(by.css('input#field_dateFinReelle'));
    dateDebutPrevuInput = element(by.css('input#field_dateDebutPrevu'));
    dateFinPrevuInput = element(by.css('input#field_dateFinPrevu'));
    clientSelect = element(by.css('select#field_client'));
    userSelect = element(by.css('select#field_user'));

    getModalTitle() {
        return this.modalTitle.getAttribute('jhiTranslate');
    }

    setNomChantierInput = function(nomChantier) {
        this.nomChantierInput.sendKeys(nomChantier);
    };

    getNomChantierInput = function() {
        return this.nomChantierInput.getAttribute('value');
    };

    setDescriptionChantierInput = function(descriptionChantier) {
        this.descriptionChantierInput.sendKeys(descriptionChantier);
    };

    getDescriptionChantierInput = function() {
        return this.descriptionChantierInput.getAttribute('value');
    };

    setVilleInput = function(ville) {
        this.villeInput.sendKeys(ville);
    };

    getVilleInput = function() {
        return this.villeInput.getAttribute('value');
    };

    setAdresseInput = function(adresse) {
        this.adresseInput.sendKeys(adresse);
    };

    getAdresseInput = function() {
        return this.adresseInput.getAttribute('value');
    };

    setEtatChantierSelect = function(etatChantier) {
        this.etatChantierSelect.sendKeys(etatChantier);
    };

    getEtatChantierSelect = function() {
        return this.etatChantierSelect.element(by.css('option:checked')).getText();
    };

    etatChantierSelectLastOption = function() {
        this.etatChantierSelect.all(by.tagName('option')).last().click();
    };
    setDateDebutReelleInput = function(dateDebutReelle) {
        this.dateDebutReelleInput.sendKeys(dateDebutReelle);
    };

    getDateDebutReelleInput = function() {
        return this.dateDebutReelleInput.getAttribute('value');
    };

    setDateFinReelleInput = function(dateFinReelle) {
        this.dateFinReelleInput.sendKeys(dateFinReelle);
    };

    getDateFinReelleInput = function() {
        return this.dateFinReelleInput.getAttribute('value');
    };

    setDateDebutPrevuInput = function(dateDebutPrevu) {
        this.dateDebutPrevuInput.sendKeys(dateDebutPrevu);
    };

    getDateDebutPrevuInput = function() {
        return this.dateDebutPrevuInput.getAttribute('value');
    };

    setDateFinPrevuInput = function(dateFinPrevu) {
        this.dateFinPrevuInput.sendKeys(dateFinPrevu);
    };

    getDateFinPrevuInput = function() {
        return this.dateFinPrevuInput.getAttribute('value');
    };

    clientSelectLastOption = function() {
        this.clientSelect.all(by.tagName('option')).last().click();
    };

    clientSelectOption = function(option) {
        this.clientSelect.sendKeys(option);
    };

    getClientSelect = function() {
        return this.clientSelect;
    };

    getClientSelectedOption = function() {
        return this.clientSelect.element(by.css('option:checked')).getText();
    };

    userSelectLastOption = function() {
        this.userSelect.all(by.tagName('option')).last().click();
    };

    userSelectOption = function(option) {
        this.userSelect.sendKeys(option);
    };

    getUserSelect = function() {
        return this.userSelect;
    };

    getUserSelectedOption = function() {
        return this.userSelect.element(by.css('option:checked')).getText();
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
