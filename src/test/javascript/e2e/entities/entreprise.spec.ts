import { browser, element, by } from 'protractor';
import { NavBarPage } from './../page-objects/jhi-page-objects';

describe('Entreprise e2e test', () => {

    let navBarPage: NavBarPage;
    let entrepriseDialogPage: EntrepriseDialogPage;
    let entrepriseComponentsPage: EntrepriseComponentsPage;

    beforeAll(() => {
        browser.get('/');
        browser.waitForAngular();
        navBarPage = new NavBarPage();
        navBarPage.getSignInPage().autoSignInUsing('admin', 'admin');
        browser.waitForAngular();
    });

    it('should load Entreprises', () => {
        navBarPage.goToEntity('entreprise');
        entrepriseComponentsPage = new EntrepriseComponentsPage();
        expect(entrepriseComponentsPage.getTitle())
            .toMatch(/gesBtpApp.entreprise.home.title/);

    });

    it('should load create Entreprise dialog', () => {
        entrepriseComponentsPage.clickOnCreateButton();
        entrepriseDialogPage = new EntrepriseDialogPage();
        expect(entrepriseDialogPage.getModalTitle())
            .toMatch(/gesBtpApp.entreprise.home.createOrEditLabel/);
        entrepriseDialogPage.close();
    });

    it('should create and save Entreprises', () => {
        entrepriseComponentsPage.clickOnCreateButton();
        entrepriseDialogPage.setNameInput('name');
        expect(entrepriseDialogPage.getNameInput()).toMatch('name');
        entrepriseDialogPage.setCommercialNameInput('commercialName');
        expect(entrepriseDialogPage.getCommercialNameInput()).toMatch('commercialName');
        entrepriseDialogPage.setSirenNumberInput('sirenNumber');
        expect(entrepriseDialogPage.getSirenNumberInput()).toMatch('sirenNumber');
        entrepriseDialogPage.setEntrepriseAdminEmailInput('entrepriseAdminEmail');
        expect(entrepriseDialogPage.getEntrepriseAdminEmailInput()).toMatch('entrepriseAdminEmail');
        entrepriseDialogPage.setTelephoneInput('5');
        expect(entrepriseDialogPage.getTelephoneInput()).toMatch('5');
        entrepriseDialogPage.setPackInput('5');
        expect(entrepriseDialogPage.getPackInput()).toMatch('5');
        entrepriseDialogPage.save();
        expect(entrepriseDialogPage.getSaveButton().isPresent()).toBeFalsy();
    });

    afterAll(() => {
        navBarPage.autoSignOut();
    });
});

export class EntrepriseComponentsPage {
    createButton = element(by.css('.jh-create-entity'));
    title = element.all(by.css('jhi-entreprise div h2 span')).first();

    clickOnCreateButton() {
        return this.createButton.click();
    }

    getTitle() {
        return this.title.getAttribute('jhiTranslate');
    }
}

export class EntrepriseDialogPage {
    modalTitle = element(by.css('h4#myEntrepriseLabel'));
    saveButton = element(by.css('.modal-footer .btn.btn-primary'));
    closeButton = element(by.css('button.close'));
    nameInput = element(by.css('input#field_name'));
    commercialNameInput = element(by.css('input#field_commercialName'));
    sirenNumberInput = element(by.css('input#field_sirenNumber'));
    entrepriseAdminEmailInput = element(by.css('input#field_entrepriseAdminEmail'));
    telephoneInput = element(by.css('input#field_telephone'));
    packInput = element(by.css('input#field_pack'));

    getModalTitle() {
        return this.modalTitle.getAttribute('jhiTranslate');
    }

    setNameInput = function(name) {
        this.nameInput.sendKeys(name);
    };

    getNameInput = function() {
        return this.nameInput.getAttribute('value');
    };

    setCommercialNameInput = function(commercialName) {
        this.commercialNameInput.sendKeys(commercialName);
    };

    getCommercialNameInput = function() {
        return this.commercialNameInput.getAttribute('value');
    };

    setSirenNumberInput = function(sirenNumber) {
        this.sirenNumberInput.sendKeys(sirenNumber);
    };

    getSirenNumberInput = function() {
        return this.sirenNumberInput.getAttribute('value');
    };

    setEntrepriseAdminEmailInput = function(entrepriseAdminEmail) {
        this.entrepriseAdminEmailInput.sendKeys(entrepriseAdminEmail);
    };

    getEntrepriseAdminEmailInput = function() {
        return this.entrepriseAdminEmailInput.getAttribute('value');
    };

    setTelephoneInput = function(telephone) {
        this.telephoneInput.sendKeys(telephone);
    };

    getTelephoneInput = function() {
        return this.telephoneInput.getAttribute('value');
    };

    setPackInput = function(pack) {
        this.packInput.sendKeys(pack);
    };

    getPackInput = function() {
        return this.packInput.getAttribute('value');
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
