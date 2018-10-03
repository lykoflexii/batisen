import { browser, element, by } from 'protractor';
import { NavBarPage } from './../page-objects/jhi-page-objects';

describe('Devis e2e test', () => {

    let navBarPage: NavBarPage;
    let devisDialogPage: DevisDialogPage;
    let devisComponentsPage: DevisComponentsPage;

    beforeAll(() => {
        browser.get('/');
        browser.waitForAngular();
        navBarPage = new NavBarPage();
        navBarPage.getSignInPage().autoSignInUsing('admin', 'admin');
        browser.waitForAngular();
    });

    it('should load Devis', () => {
        navBarPage.goToEntity('devis');
        devisComponentsPage = new DevisComponentsPage();
        expect(devisComponentsPage.getTitle())
            .toMatch(/gesBtpApp.devis.home.title/);

    });

    it('should load create Devis dialog', () => {
        devisComponentsPage.clickOnCreateButton();
        devisDialogPage = new DevisDialogPage();
        expect(devisDialogPage.getModalTitle())
            .toMatch(/gesBtpApp.devis.home.createOrEditLabel/);
        devisDialogPage.close();
    });

    it('should create and save Devis', () => {
        devisComponentsPage.clickOnCreateButton();
        devisDialogPage.setTitreInput('titre');
        expect(devisDialogPage.getTitreInput()).toMatch('titre');
        devisDialogPage.setDateDeCreationInput('2000-12-31');
        expect(devisDialogPage.getDateDeCreationInput()).toMatch('2000-12-31');
        devisDialogPage.setValiditeInput('2000-12-31');
        expect(devisDialogPage.getValiditeInput()).toMatch('2000-12-31');
        devisDialogPage.setTvaInput('5');
        expect(devisDialogPage.getTvaInput()).toMatch('5');
        devisDialogPage.setCoutMainDoeuvreInput('5');
        expect(devisDialogPage.getCoutMainDoeuvreInput()).toMatch('5');
        devisDialogPage.clientSelectLastOption();
        devisDialogPage.chantierSelectLastOption();
        devisDialogPage.save();
        expect(devisDialogPage.getSaveButton().isPresent()).toBeFalsy();
    });

    afterAll(() => {
        navBarPage.autoSignOut();
    });
});

export class DevisComponentsPage {
    createButton = element(by.css('.jh-create-entity'));
    title = element.all(by.css('jhi-devis div h2 span')).first();

    clickOnCreateButton() {
        return this.createButton.click();
    }

    getTitle() {
        return this.title.getAttribute('jhiTranslate');
    }
}

export class DevisDialogPage {
    modalTitle = element(by.css('h4#myDevisLabel'));
    saveButton = element(by.css('.modal-footer .btn.btn-primary'));
    closeButton = element(by.css('button.close'));
    titreInput = element(by.css('input#field_titre'));
    dateDeCreationInput = element(by.css('input#field_dateDeCreation'));
    validiteInput = element(by.css('input#field_validite'));
    tvaInput = element(by.css('input#field_tva'));
    coutMainDoeuvreInput = element(by.css('input#field_coutMainDoeuvre'));
    clientSelect = element(by.css('select#field_client'));
    chantierSelect = element(by.css('select#field_chantier'));

    getModalTitle() {
        return this.modalTitle.getAttribute('jhiTranslate');
    }

    setTitreInput = function(titre) {
        this.titreInput.sendKeys(titre);
    };

    getTitreInput = function() {
        return this.titreInput.getAttribute('value');
    };

    setDateDeCreationInput = function(dateDeCreation) {
        this.dateDeCreationInput.sendKeys(dateDeCreation);
    };

    getDateDeCreationInput = function() {
        return this.dateDeCreationInput.getAttribute('value');
    };

    setValiditeInput = function(validite) {
        this.validiteInput.sendKeys(validite);
    };

    getValiditeInput = function() {
        return this.validiteInput.getAttribute('value');
    };

    setTvaInput = function(tva) {
        this.tvaInput.sendKeys(tva);
    };

    getTvaInput = function() {
        return this.tvaInput.getAttribute('value');
    };

    setCoutMainDoeuvreInput = function(coutMainDoeuvre) {
        this.coutMainDoeuvreInput.sendKeys(coutMainDoeuvre);
    };

    getCoutMainDoeuvreInput = function() {
        return this.coutMainDoeuvreInput.getAttribute('value');
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
