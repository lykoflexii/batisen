/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';

import { GesBtpTestModule } from '../../../test.module';
import { LigneFactureDetailComponent } from '../../../../../../main/webapp/app/entities/ligne-facture/ligne-facture-detail.component';
import { LigneFactureService } from '../../../../../../main/webapp/app/entities/ligne-facture/ligne-facture.service';
import { LigneFacture } from '../../../../../../main/webapp/app/entities/ligne-facture/ligne-facture.model';

describe('Component Tests', () => {

    describe('LigneFacture Management Detail Component', () => {
        let comp: LigneFactureDetailComponent;
        let fixture: ComponentFixture<LigneFactureDetailComponent>;
        let service: LigneFactureService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [GesBtpTestModule],
                declarations: [LigneFactureDetailComponent],
                providers: [
                    LigneFactureService
                ]
            })
            .overrideTemplate(LigneFactureDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(LigneFactureDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(LigneFactureService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                spyOn(service, 'find').and.returnValue(Observable.of(new HttpResponse({
                    body: new LigneFacture(123)
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.find).toHaveBeenCalledWith(123);
                expect(comp.ligneFacture).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
