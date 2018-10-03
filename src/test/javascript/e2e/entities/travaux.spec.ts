import { browser, element, by } from 'protractor';
import { NavBarPage } from './../page-objects/jhi-page-objects';

describe('Travaux e2e test', () => {

    let navBarPage: NavBarPage;
    let travauxDialogPage: TravauxDialogPage;
    let travauxComponentsPage: TravauxComponentsPage;

    beforeAll(() => {
        browser.get('/');
        browser.waitForAngular();
        navBarPage = new NavBarPage();
        navBarPage.getSignInPage().autoSignInUsing('admin', 'admin');
        browser.waitForAngular();
    });

    it('should load Travauxes', () => {
        navBarPage.goToEntity('travaux');
        travauxComponentsPage = new TravauxComponentsPage();
        expect(travauxComponentsPage.getTitle())
            .toMatch(/gesBtpApp.travaux.home.title/);

    });

    it('should load create Travaux dialog', () => {
        travauxComponentsPage.clickOnCreateButton();
        travauxDialogPage = new TravauxDialogPage();
        expect(travauxDialogPage.getModalTitle())
            .toMatch(/gesBtpApp.travaux.home.createOrEditLabel/);
        travauxDialogPage.close();
    });

    it('should create and save Travauxes', () => {
        travauxComponentsPage.clickOnCreateButton();
        travauxDialogPage.setNomTravInput('nomTrav');
        expect(travauxDialogPage.getNomTravInput()).toMatch('nomTrav');
        travauxDialogPage.setDescriptionTravInput('descriptionTrav');
        expect(travauxDialogPage.getDescriptionTravInput()).toMatch('descriptionTrav');
        travauxDialogPage.save();
        expect(travauxDialogPage.getSaveButton().isPresent()).toBeFalsy();
    });

    afterAll(() => {
        navBarPage.autoSignOut();
    });
});

export class TravauxComponentsPage {
    createButton = element(by.css('.jh-create-entity'));
    title = element.all(by.css('jhi-travaux div h2 span')).first();

    clickOnCreateButton() {
        return this.createButton.click();
    }

    getTitle() {
        return this.title.getAttribute('jhiTranslate');
    }
}

export class TravauxDialogPage {
    modalTitle = element(by.css('h4#myTravauxLabel'));
    saveButton = element(by.css('.modal-footer .btn.btn-primary'));
    closeButton = element(by.css('button.close'));
    nomTravInput = element(by.css('input#field_nomTrav'));
    descriptionTravInput = element(by.css('input#field_descriptionTrav'));

    getModalTitle() {
        return this.modalTitle.getAttribute('jhiTranslate');
    }

    setNomTravInput = function(nomTrav) {
        this.nomTravInput.sendKeys(nomTrav);
    };

    getNomTravInput = function() {
        return this.nomTravInput.getAttribute('value');
    };

    setDescriptionTravInput = function(descriptionTrav) {
        this.descriptionTravInput.sendKeys(descriptionTrav);
    };

    getDescriptionTravInput = function() {
        return this.descriptionTravInput.getAttribute('value');
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
