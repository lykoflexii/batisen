import { browser, element, by, $ } from 'protractor';
import { NavBarPage } from './../page-objects/jhi-page-objects';

describe('Entreprise-management e2e test', () => {

    let navBarPage: NavBarPage;
    let entrepriseMgmtDialogPage: EntrepriseMgmtDialogPage;
    let entrepriseMgmtComponentsPage: EntrepriseMgmtComponentsPage;
    
    beforeAll(() => {
        browser.get('/');
        browser.waitForAngular();
        navBarPage = new NavBarPage(true);
        entrepriseMgmtComponentsPage = new EntrepriseMgmtComponentsPage();
        entrepriseMgmtDialogPage = new EntrepriseMgmtDialogPage();
        navBarPage.getSignInPage().autoSignInUsing('admin', 'admin');
        browser.waitForAngular();
        navBarPage.clickOnAdminMenu();
        navBarPage.clickOnAdmin("entreprise-management");
        browser.waitForAngular();
    });

    it('should load create entreprise dialog', () => {
        entrepriseMgmtComponentsPage.clickOnCreateButton();
        expect(entrepriseMgmtDialogPage.getModalTitle()).toMatch(/entrepriseManagement.home.createLabel/);
        entrepriseMgmtDialogPage.close();
    });

   it('should create and save entreprises', () => {
        entrepriseMgmtComponentsPage.clickOnCreateButton();
        entrepriseMgmtDialogPage.setNameInput('new entreprise');
        expect(entrepriseMgmtDialogPage.getNameInput()).toMatch('new entreprise');
        entrepriseMgmtDialogPage.save();
        expect(entrepriseMgmtDialogPage.getSaveButton().isPresent()).toBeFalsy();        
    });

    afterAll(() => {
        navBarPage.autoSignOut();
    });
});

export class EntrepriseMgmtComponentsPage {
    createButton = element(by.css('.jh-create-entity'));
    title = element.all(by.css('div h2 span')).first();

    clickOnCreateButton() {
        return this.createButton.click();
    }

    getTitle() {
        return this.title.getAttribute('jhiTranslate');
    }
}

export class EntrepriseMgmtDialogPage {
    modalTitle = element(by.css('.modal-title'));
    saveButton = element(by.css('.modal-footer .btn.btn-primary'));
    closeButton = element(by.css('button.close'));
    nameInput = element(by.css('input#field_name'));

    getModalTitle() {
        return this.modalTitle.getAttribute('jhiTranslate');
    }

    setNameInput = function (name) {
        this.nameInput.sendKeys(name);
    }

    getNameInput = function () {
        return this.nameInput.getAttribute('value');
    }
    
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
