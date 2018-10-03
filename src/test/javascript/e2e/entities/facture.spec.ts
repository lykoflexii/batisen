import { browser, element, by } from 'protractor';
import { NavBarPage } from './../page-objects/jhi-page-objects';

describe('Facture e2e test', () => {

    let navBarPage: NavBarPage;
    let factureDialogPage: FactureDialogPage;
    let factureComponentsPage: FactureComponentsPage;

    beforeAll(() => {
        browser.get('/');
        browser.waitForAngular();
        navBarPage = new NavBarPage();
        navBarPage.getSignInPage().autoSignInUsing('admin', 'admin');
        browser.waitForAngular();
    });

    it('should load Factures', () => {
        navBarPage.goToEntity('facture');
        factureComponentsPage = new FactureComponentsPage();
        expect(factureComponentsPage.getTitle())
            .toMatch(/gesBtpApp.facture.home.title/);

    });

    it('should load create Facture dialog', () => {
        factureComponentsPage.clickOnCreateButton();
        factureDialogPage = new FactureDialogPage();
        expect(factureDialogPage.getModalTitle())
            .toMatch(/gesBtpApp.facture.home.createOrEditLabel/);
        factureDialogPage.close();
    });

    it('should create and save Factures', () => {
        factureComponentsPage.clickOnCreateButton();
        factureDialogPage.setDateCreationInput('2000-12-31');
        expect(factureDialogPage.getDateCreationInput()).toMatch('2000-12-31');
        factureDialogPage.setRemiseInput('5');
        expect(factureDialogPage.getRemiseInput()).toMatch('5');
        factureDialogPage.getValiderInput().isSelected().then((selected) => {
            if (selected) {
                factureDialogPage.getValiderInput().click();
                expect(factureDialogPage.getValiderInput().isSelected()).toBeFalsy();
            } else {
                factureDialogPage.getValiderInput().click();
                expect(factureDialogPage.getValiderInput().isSelected()).toBeTruthy();
            }
        });
        factureDialogPage.chantierSelectLastOption();
        factureDialogPage.travauxSelectLastOption();
        factureDialogPage.entrepriseSelectLastOption();
        factureDialogPage.save();
        expect(factureDialogPage.getSaveButton().isPresent()).toBeFalsy();
    });

    afterAll(() => {
        navBarPage.autoSignOut();
    });
});

export class FactureComponentsPage {
    createButton = element(by.css('.jh-create-entity'));
    title = element.all(by.css('jhi-facture div h2 span')).first();

    clickOnCreateButton() {
        return this.createButton.click();
    }

    getTitle() {
        return this.title.getAttribute('jhiTranslate');
    }
}

export class FactureDialogPage {
    modalTitle = element(by.css('h4#myFactureLabel'));
    saveButton = element(by.css('.modal-footer .btn.btn-primary'));
    closeButton = element(by.css('button.close'));
    dateCreationInput = element(by.css('input#field_dateCreation'));
    remiseInput = element(by.css('input#field_remise'));
    validerInput = element(by.css('input#field_valider'));
    chantierSelect = element(by.css('select#field_chantier'));
    travauxSelect = element(by.css('select#field_travaux'));
    entrepriseSelect = element(by.css('select#field_entreprise'));

    getModalTitle() {
        return this.modalTitle.getAttribute('jhiTranslate');
    }

    setDateCreationInput = function(dateCreation) {
        this.dateCreationInput.sendKeys(dateCreation);
    };

    getDateCreationInput = function() {
        return this.dateCreationInput.getAttribute('value');
    };

    setRemiseInput = function(remise) {
        this.remiseInput.sendKeys(remise);
    };

    getRemiseInput = function() {
        return this.remiseInput.getAttribute('value');
    };

    getValiderInput = function() {
        return this.validerInput;
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

    entrepriseSelectLastOption = function() {
        this.entrepriseSelect.all(by.tagName('option')).last().click();
    };

    entrepriseSelectOption = function(option) {
        this.entrepriseSelect.sendKeys(option);
    };

    getEntrepriseSelect = function() {
        return this.entrepriseSelect;
    };

    getEntrepriseSelectedOption = function() {
        return this.entrepriseSelect.element(by.css('option:checked')).getText();
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
