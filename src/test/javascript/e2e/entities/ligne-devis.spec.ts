import { browser, element, by } from 'protractor';
import { NavBarPage } from './../page-objects/jhi-page-objects';

describe('LigneDevis e2e test', () => {

    let navBarPage: NavBarPage;
    let ligneDevisDialogPage: LigneDevisDialogPage;
    let ligneDevisComponentsPage: LigneDevisComponentsPage;

    beforeAll(() => {
        browser.get('/');
        browser.waitForAngular();
        navBarPage = new NavBarPage();
        navBarPage.getSignInPage().autoSignInUsing('admin', 'admin');
        browser.waitForAngular();
    });

    it('should load LigneDevis', () => {
        navBarPage.goToEntity('ligne-devis');
        ligneDevisComponentsPage = new LigneDevisComponentsPage();
        expect(ligneDevisComponentsPage.getTitle())
            .toMatch(/gesBtpApp.ligneDevis.home.title/);

    });

    it('should load create LigneDevis dialog', () => {
        ligneDevisComponentsPage.clickOnCreateButton();
        ligneDevisDialogPage = new LigneDevisDialogPage();
        expect(ligneDevisDialogPage.getModalTitle())
            .toMatch(/gesBtpApp.ligneDevis.home.createOrEditLabel/);
        ligneDevisDialogPage.close();
    });

    it('should create and save LigneDevis', () => {
        ligneDevisComponentsPage.clickOnCreateButton();
        ligneDevisDialogPage.setDesignationInput('designation');
        expect(ligneDevisDialogPage.getDesignationInput()).toMatch('designation');
        ligneDevisDialogPage.setPrixUnitaireInput('5');
        expect(ligneDevisDialogPage.getPrixUnitaireInput()).toMatch('5');
        ligneDevisDialogPage.setQuantiteInput('5');
        expect(ligneDevisDialogPage.getQuantiteInput()).toMatch('5');
        ligneDevisDialogPage.devisSelectLastOption();
        ligneDevisDialogPage.save();
        expect(ligneDevisDialogPage.getSaveButton().isPresent()).toBeFalsy();
    });

    afterAll(() => {
        navBarPage.autoSignOut();
    });
});

export class LigneDevisComponentsPage {
    createButton = element(by.css('.jh-create-entity'));
    title = element.all(by.css('jhi-ligne-devis div h2 span')).first();

    clickOnCreateButton() {
        return this.createButton.click();
    }

    getTitle() {
        return this.title.getAttribute('jhiTranslate');
    }
}

export class LigneDevisDialogPage {
    modalTitle = element(by.css('h4#myLigneDevisLabel'));
    saveButton = element(by.css('.modal-footer .btn.btn-primary'));
    closeButton = element(by.css('button.close'));
    designationInput = element(by.css('input#field_designation'));
    prixUnitaireInput = element(by.css('input#field_prixUnitaire'));
    quantiteInput = element(by.css('input#field_quantite'));
    devisSelect = element(by.css('select#field_devis'));

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

    devisSelectLastOption = function() {
        this.devisSelect.all(by.tagName('option')).last().click();
    };

    devisSelectOption = function(option) {
        this.devisSelect.sendKeys(option);
    };

    getDevisSelect = function() {
        return this.devisSelect;
    };

    getDevisSelectedOption = function() {
        return this.devisSelect.element(by.css('option:checked')).getText();
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
