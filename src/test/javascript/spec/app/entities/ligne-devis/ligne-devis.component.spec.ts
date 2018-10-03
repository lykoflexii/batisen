/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { Observable } from 'rxjs/Observable';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { GesBtpTestModule } from '../../../test.module';
import { LigneDevisComponent } from '../../../../../../main/webapp/app/entities/ligne-devis/ligne-devis.component';
import { LigneDevisService } from '../../../../../../main/webapp/app/entities/ligne-devis/ligne-devis.service';
import { LigneDevis } from '../../../../../../main/webapp/app/entities/ligne-devis/ligne-devis.model';

describe('Component Tests', () => {

    describe('LigneDevis Management Component', () => {
        let comp: LigneDevisComponent;
        let fixture: ComponentFixture<LigneDevisComponent>;
        let service: LigneDevisService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [GesBtpTestModule],
                declarations: [LigneDevisComponent],
                providers: [
                    LigneDevisService
                ]
            })
            .overrideTemplate(LigneDevisComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(LigneDevisComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(LigneDevisService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN
                const headers = new HttpHeaders().append('link', 'link;link');
                spyOn(service, 'query').and.returnValue(Observable.of(new HttpResponse({
                    body: [new LigneDevis(123)],
                    headers
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.query).toHaveBeenCalled();
                expect(comp.ligneDevis[0]).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
