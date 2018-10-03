/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';

import { GesBtpTestModule } from '../../../test.module';
import { FactureDetailComponent } from '../../../../../../main/webapp/app/entities/facture/facture-detail.component';
import { FactureService } from '../../../../../../main/webapp/app/entities/facture/facture.service';
import { Facture } from '../../../../../../main/webapp/app/entities/facture/facture.model';

describe('Component Tests', () => {

    describe('Facture Management Detail Component', () => {
        let comp: FactureDetailComponent;
        let fixture: ComponentFixture<FactureDetailComponent>;
        let service: FactureService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [GesBtpTestModule],
                declarations: [FactureDetailComponent],
                providers: [
                    FactureService
                ]
            })
            .overrideTemplate(FactureDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(FactureDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(FactureService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                spyOn(service, 'find').and.returnValue(Observable.of(new HttpResponse({
                    body: new Facture(123)
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.find).toHaveBeenCalledWith(123);
                expect(comp.facture).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
