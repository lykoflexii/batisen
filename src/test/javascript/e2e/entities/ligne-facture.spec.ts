import { browser, element, by } from 'protractor';
import { NavBarPage } from './../page-objects/jhi-page-objects';

describe('LigneFacture e2e test', () => {

    let navBarPage: NavBarPage;
    let ligneFactureDialogPage: LigneFactureDialogPage;
    let ligneFactureComponentsPage: LigneFactureComponentsPage;

    beforeAll(() => {
        browser.get('/');
        browser.waitForAngular();
        navBarPage = new NavBarPage();
        navBarPage.getSignInPage().autoSignInUsing('admin', 'admin');
        browser.waitForAngular();
    });

    it('should load LigneFactures', () => {
        navBarPage.goToEntity('ligne-facture');
        ligneFactureComponentsPage = new LigneFactureComponentsPage();
        expect(ligneFactureComponentsPage.getTitle())
            .toMatch(/gesBtpApp.ligneFacture.home.title/);

    });

    it('should load create LigneFacture dialog', () => {
        ligneFactureComponentsPage.clickOnCreateButton();
        ligneFactureDialogPage = new LigneFactureDialogPage();
        expect(ligneFactureDialogPage.getModalTitle())
            .toMatch(/gesBtpApp.ligneFacture.home.createOrEditLabel/);
        ligneFactureDialogPage.close();
    });

    it('should create and save LigneFactures', () => {
        ligneFactureComponentsPage.clickOnCreateButton();
        ligneFactureDialogPage.setDesignationInput('designation');
        expect(ligneFactureDialogPage.getDesignationInput()).toMatch('designation');
        ligneFactureDialogPage.setPrixUnitaireInput('5');
        expect(ligneFactureDialogPage.getPrixUnitaireInput()).toMatch('5');
        ligneFactureDialogPage.setQuantiteInput('5');
        expect(ligneFactureDialogPage.getQuantiteInput()).toMatch('5');
        ligneFactureDialogPage.setQuantiteRetenueInput('5');
        expect(ligneFactureDialogPage.getQuantiteRetenueInput()).toMatch('5');
        ligneFactureDialogPage.factureSelectLastOption();
        ligneFactureDialogPage.save();
        expect(ligneFactureDialogPage.getSaveButton().isPresent()).toBeFalsy();
    });

    afterAll(() => {
        navBarPage.autoSignOut();
    });
});

export class LigneFactureComponentsPage {
    createButton = element(by.css('.jh-create-entity'));
    title = element.all(by.css('jhi-ligne-facture div h2 span')).first();

    clickOnCreateButton() {
        return this.createButton.click();
    }

    getTitle() {
        return this.title.getAttribute('jhiTranslate');
    }
}

export class LigneFactureDialogPage {
    modalTitle = element(by.css('h4#myLigneFactureLabel'));
    saveButton = element(by.css('.modal-footer .btn.btn-primary'));
    closeButton = element(by.css('button.close'));
    designationInput = element(by.css('input#field_designation'));
    prixUnitaireInput = element(by.css('input#field_prixUnitaire'));
    quantiteInput = element(by.css('input#field_quantite'));
    quantiteRetenueInput = element(by.css('input#field_quantiteRetenue'));
    factureSelect = element(by.css('select#field_facture'));

    getModalTitle() {
        return this.modalTitle.getAttribute('jhiTranslate');
    }

    setDesignationInput = function(designation) {
        this.designationInput.sendKeys(designation);
    };

    getDesignationInput = function() {
        return this.designationInput.getAttribute('value');
    };

    setPrixUnitaireInput = function(prixUnitaire) {
        this.prixUnitaireInput.sendKeys(prixUnitaire);
    };

    getPrixUnitaireInput = function() {
        return this.prixUnitaireInput.getAttribute('value');
    };

    setQuantiteInput = function(quantite) {
        this.quantiteInput.sendKeys(quantite);
    };

    getQuantiteInput = function() {
        return this.quantiteInput.getAttribute('value');
    };

    setQuantiteRetenueInput = function(quantiteRetenue) {
        this.quantiteRetenueInput.sendKeys(quantiteRetenue);
    };

    getQuantiteRetenueInput = function() {
        return this.quantiteRetenueInput.getAttribute('value');
    };

    factureSelectLastOption = function() {
        this.factureSelect.all(by.tagName('option')).last().click();
    };

    factureSelectOption = function(option) {
        this.factureSelect.sendKeys(option);
    };

    getFactureSelect = function() {
        return this.factureSelect;
    };

    getFactureSelectedOption = function() {
        return this.factureSelect.element(by.css('option:checked')).getText();
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
