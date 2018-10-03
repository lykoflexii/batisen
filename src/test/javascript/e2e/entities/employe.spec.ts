import { browser, element, by } from 'protractor';
import { NavBarPage } from './../page-objects/jhi-page-objects';

describe('Employe e2e test', () => {

    let navBarPage: NavBarPage;
    let employeDialogPage: EmployeDialogPage;
    let employeComponentsPage: EmployeComponentsPage;

    beforeAll(() => {
        browser.get('/');
        browser.waitForAngular();
        navBarPage = new NavBarPage();
        navBarPage.getSignInPage().autoSignInUsing('admin', 'admin');
        browser.waitForAngular();
    });

    it('should load Employes', () => {
        navBarPage.goToEntity('employe');
        employeComponentsPage = new EmployeComponentsPage();
        expect(employeComponentsPage.getTitle())
            .toMatch(/gesBtpApp.employe.home.title/);

    });

    it('should load create Employe dialog', () => {
        employeComponentsPage.clickOnCreateButton();
        employeDialogPage = new EmployeDialogPage();
        expect(employeDialogPage.getModalTitle())
            .toMatch(/gesBtpApp.employe.home.createOrEditLabel/);
        employeDialogPage.close();
    });

    it('should create and save Employes', () => {
        employeComponentsPage.clickOnCreateButton();
        employeDialogPage.setNomEmployeInput('nomEmploye');
        expect(employeDialogPage.getNomEmployeInput()).toMatch('nomEmploye');
        employeDialogPage.setPrenomEmployeInput('prenomEmploye');
        expect(employeDialogPage.getPrenomEmployeInput()).toMatch('prenomEmploye');
        employeDialogPage.setMatriculeInput('matricule');
        expect(employeDialogPage.getMatriculeInput()).toMatch('matricule');
        employeDialogPage.setFonctionInput('fonction');
        expect(employeDialogPage.getFonctionInput()).toMatch('fonction');
        employeDialogPage.setDateNaissanceInput('2000-12-31');
        expect(employeDialogPage.getDateNaissanceInput()).toMatch('2000-12-31');
        employeDialogPage.setTelephoneEmployeInput('telephoneEmploye');
        expect(employeDialogPage.getTelephoneEmployeInput()).toMatch('telephoneEmploye');
        employeDialogPage.setSalaireInput('5');
        expect(employeDialogPage.getSalaireInput()).toMatch('5');
        employeDialogPage.sexeSelectLastOption();
        employeDialogPage.entrepriseSelectLastOption();
        employeDialogPage.save();
        expect(employeDialogPage.getSaveButton().isPresent()).toBeFalsy();
    });

    afterAll(() => {
        navBarPage.autoSignOut();
    });
});

export class EmployeComponentsPage {
    createButton = element(by.css('.jh-create-entity'));
    title = element.all(by.css('jhi-employe div h2 span')).first();

    clickOnCreateButton() {
        return this.createButton.click();
    }

    getTitle() {
        return this.title.getAttribute('jhiTranslate');
    }
}

export class EmployeDialogPage {
    modalTitle = element(by.css('h4#myEmployeLabel'));
    saveButton = element(by.css('.modal-footer .btn.btn-primary'));
    closeButton = element(by.css('button.close'));
    nomEmployeInput = element(by.css('input#field_nomEmploye'));
    prenomEmployeInput = element(by.css('input#field_prenomEmploye'));
    matriculeInput = element(by.css('input#field_matricule'));
    fonctionInput = element(by.css('input#field_fonction'));
    dateNaissanceInput = element(by.css('input#field_dateNaissance'));
    telephoneEmployeInput = element(by.css('input#field_telephoneEmploye'));
    salaireInput = element(by.css('input#field_salaire'));
    sexeSelect = element(by.css('select#field_sexe'));
    entrepriseSelect = element(by.css('select#field_entreprise'));

    getModalTitle() {
        return this.modalTitle.getAttribute('jhiTranslate');
    }

    setNomEmployeInput = function(nomEmploye) {
        this.nomEmployeInput.sendKeys(nomEmploye);
    };

    getNomEmployeInput = function() {
        return this.nomEmployeInput.getAttribute('value');
    };

    setPrenomEmployeInput = function(prenomEmploye) {
        this.prenomEmployeInput.sendKeys(prenomEmploye);
    };

    getPrenomEmployeInput = function() {
        return this.prenomEmployeInput.getAttribute('value');
    };

    setMatriculeInput = function(matricule) {
        this.matriculeInput.sendKeys(matricule);
    };

    getMatriculeInput = function() {
        return this.matriculeInput.getAttribute('value');
    };

    setFonctionInput = function(fonction) {
        this.fonctionInput.sendKeys(fonction);
    };

    getFonctionInput = function() {
        return this.fonctionInput.getAttribute('value');
    };

    setDateNaissanceInput = function(dateNaissance) {
        this.dateNaissanceInput.sendKeys(dateNaissance);
    };

    getDateNaissanceInput = function() {
        return this.dateNaissanceInput.getAttribute('value');
    };

    setTelephoneEmployeInput = function(telephoneEmploye) {
        this.telephoneEmployeInput.sendKeys(telephoneEmploye);
    };

    getTelephoneEmployeInput = function() {
        return this.telephoneEmployeInput.getAttribute('value');
    };

    setSalaireInput = function(salaire) {
        this.salaireInput.sendKeys(salaire);
    };

    getSalaireInput = function() {
        return this.salaireInput.getAttribute('value');
    };

    setSexeSelect = function(sexe) {
        this.sexeSelect.sendKeys(sexe);
    };

    getSexeSelect = function() {
        return this.sexeSelect.element(by.css('option:checked')).getText();
    };

    sexeSelectLastOption = function() {
        this.sexeSelect.all(by.tagName('option')).last().click();
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
