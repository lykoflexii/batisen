/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { Observable } from 'rxjs/Observable';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { GesBtpTestModule } from '../../../test.module';
import { FactureComponent } from '../../../../../../main/webapp/app/entities/facture/facture.component';
import { FactureService } from '../../../../../../main/webapp/app/entities/facture/facture.service';
import { Facture } from '../../../../../../main/webapp/app/entities/facture/facture.model';

describe('Component Tests', () => {

    describe('Facture Management Component', () => {
        let comp: FactureComponent;
        let fixture: ComponentFixture<FactureComponent>;
        let service: FactureService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [GesBtpTestModule],
                declarations: [FactureComponent],
                providers: [
                    FactureService
                ]
            })
            .overrideTemplate(FactureComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(FactureComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(FactureService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN
                const headers = new HttpHeaders().append('link', 'link;link');
                spyOn(service, 'query').and.returnValue(Observable.of(new HttpResponse({
                    body: [new Facture(123)],
                    headers
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.query).toHaveBeenCalled();
                expect(comp.factures[0]).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
