import { browser, element, by } from 'protractor';
import { NavBarPage } from './../page-objects/jhi-page-objects';

describe('Client e2e test', () => {

    let navBarPage: NavBarPage;
    let clientDialogPage: ClientDialogPage;
    let clientComponentsPage: ClientComponentsPage;

    beforeAll(() => {
        browser.get('/');
        browser.waitForAngular();
        navBarPage = new NavBarPage();
        navBarPage.getSignInPage().autoSignInUsing('admin', 'admin');
        browser.waitForAngular();
    });

    it('should load Clients', () => {
        navBarPage.goToEntity('client');
        clientComponentsPage = new ClientComponentsPage();
        expect(clientComponentsPage.getTitle())
            .toMatch(/gesBtpApp.client.home.title/);

    });

    it('should load create Client dialog', () => {
        clientComponentsPage.clickOnCreateButton();
        clientDialogPage = new ClientDialogPage();
        expect(clientDialogPage.getModalTitle())
            .toMatch(/gesBtpApp.client.home.createOrEditLabel/);
        clientDialogPage.close();
    });

    it('should create and save Clients', () => {
        clientComponentsPage.clickOnCreateButton();
        clientDialogPage.setNomClientInput('nomClient');
        expect(clientDialogPage.getNomClientInput()).toMatch('nomClient');
        clientDialogPage.setVilleClientInput('villeClient');
        expect(clientDialogPage.getVilleClientInput()).toMatch('villeClient');
        clientDialogPage.setAdresseClientInput('adresseClient');
        expect(clientDialogPage.getAdresseClientInput()).toMatch('adresseClient');
        clientDialogPage.setTelephoneClientInput('telephoneClient');
        expect(clientDialogPage.getTelephoneClientInput()).toMatch('telephoneClient');
        clientDialogPage.setFaxInput('fax');
        expect(clientDialogPage.getFaxInput()).toMatch('fax');
        clientDialogPage.setEmailClientInput('emailClient');
        expect(clientDialogPage.getEmailClientInput()).toMatch('emailClient');
        clientDialogPage.typeClientSelectLastOption();
        clientDialogPage.setPrenomClientInput('prenomClient');
        expect(clientDialogPage.getPrenomClientInput()).toMatch('prenomClient');
        clientDialogPage.setNomCommercialInput('nomCommercial');
        expect(clientDialogPage.getNomCommercialInput()).toMatch('nomCommercial');
        clientDialogPage.entrepriseSelectLastOption();
        clientDialogPage.save();
        expect(clientDialogPage.getSaveButton().isPresent()).toBeFalsy();
    });

    afterAll(() => {
        navBarPage.autoSignOut();
    });
});

export class ClientComponentsPage {
    createButton = element(by.css('.jh-create-entity'));
    title = element.all(by.css('jhi-client div h2 span')).first();

    clickOnCreateButton() {
        return this.createButton.click();
    }

    getTitle() {
        return this.title.getAttribute('jhiTranslate');
    }
}

export class ClientDialogPage {
    modalTitle = element(by.css('h4#myClientLabel'));
    saveButton = element(by.css('.modal-footer .btn.btn-primary'));
    closeButton = element(by.css('button.close'));
    nomClientInput = element(by.css('input#field_nomClient'));
    villeClientInput = element(by.css('input#field_villeClient'));
    adresseClientInput = element(by.css('input#field_adresseClient'));
    telephoneClientInput = element(by.css('input#field_telephoneClient'));
    faxInput = element(by.css('input#field_fax'));
    emailClientInput = element(by.css('input#field_emailClient'));
    typeClientSelect = element(by.css('select#field_typeClient'));
    prenomClientInput = element(by.css('input#field_prenomClient'));
    nomCommercialInput = element(by.css('input#field_nomCommercial'));
    entrepriseSelect = element(by.css('select#field_entreprise'));

    getModalTitle() {
        return this.modalTitle.getAttribute('jhiTranslate');
    }

    setNomClientInput = function(nomClient) {
        this.nomClientInput.sendKeys(nomClient);
    };

    getNomClientInput = function() {
        return this.nomClientInput.getAttribute('value');
    };

    setVilleClientInput = function(villeClient) {
        this.villeClientInput.sendKeys(villeClient);
    };

    getVilleClientInput = function() {
        return this.villeClientInput.getAttribute('value');
    };

    setAdresseClientInput = function(adresseClient) {
        this.adresseClientInput.sendKeys(adresseClient);
    };

    getAdresseClientInput = function() {
        return this.adresseClientInput.getAttribute('value');
    };

    setTelephoneClientInput = function(telephoneClient) {
        this.telephoneClientInput.sendKeys(telephoneClient);
    };

    getTelephoneClientInput = function() {
        return this.telephoneClientInput.getAttribute('value');
    };

    setFaxInput = function(fax) {
        this.faxInput.sendKeys(fax);
    };

    getFaxInput = function() {
        return this.faxInput.getAttribute('value');
    };

    setEmailClientInput = function(emailClient) {
        this.emailClientInput.sendKeys(emailClient);
    };

    getEmailClientInput = function() {
        return this.emailClientInput.getAttribute('value');
    };

    setTypeClientSelect = function(typeClient) {
        this.typeClientSelect.sendKeys(typeClient);
    };

    getTypeClientSelect = function() {
        return this.typeClientSelect.element(by.css('option:checked')).getText();
    };

    typeClientSelectLastOption = function() {
        this.typeClientSelect.all(by.tagName('option')).last().click();
    };
    setPrenomClientInput = function(prenomClient) {
        this.prenomClientInput.sendKeys(prenomClient);
    };

    getPrenomClientInput = function() {
        return this.prenomClientInput.getAttribute('value');
    };

    setNomCommercialInput = function(nomCommercial) {
        this.nomCommercialInput.sendKeys(nomCommercial);
    };

    getNomCommercialInput = function() {
        return this.nomCommercialInput.getAttribute('value');
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
