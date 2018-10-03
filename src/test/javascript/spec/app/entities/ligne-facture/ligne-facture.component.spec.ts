/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { Observable } from 'rxjs/Observable';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { GesBtpTestModule } from '../../../test.module';
import { LigneFactureComponent } from '../../../../../../main/webapp/app/entities/ligne-facture/ligne-facture.component';
import { LigneFactureService } from '../../../../../../main/webapp/app/entities/ligne-facture/ligne-facture.service';
import { LigneFacture } from '../../../../../../main/webapp/app/entities/ligne-facture/ligne-facture.model';

describe('Component Tests', () => {

    describe('LigneFacture Management Component', () => {
        let comp: LigneFactureComponent;
        let fixture: ComponentFixture<LigneFactureComponent>;
        let service: LigneFactureService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [GesBtpTestModule],
                declarations: [LigneFactureComponent],
                providers: [
                    LigneFactureService
                ]
            })
            .overrideTemplate(LigneFactureComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(LigneFactureComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(LigneFactureService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN
                const headers = new HttpHeaders().append('link', 'link;link');
                spyOn(service, 'query').and.returnValue(Observable.of(new HttpResponse({
                    body: [new LigneFacture(123)],
                    headers
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.query).toHaveBeenCalled();
                expect(comp.ligneFactures[0]).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
