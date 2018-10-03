/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';

import { GesBtpTestModule } from '../../../test.module';
import { LigneDevisDetailComponent } from '../../../../../../main/webapp/app/entities/ligne-devis/ligne-devis-detail.component';
import { LigneDevisService } from '../../../../../../main/webapp/app/entities/ligne-devis/ligne-devis.service';
import { LigneDevis } from '../../../../../../main/webapp/app/entities/ligne-devis/ligne-devis.model';

describe('Component Tests', () => {

    describe('LigneDevis Management Detail Component', () => {
        let comp: LigneDevisDetailComponent;
        let fixture: ComponentFixture<LigneDevisDetailComponent>;
        let service: LigneDevisService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [GesBtpTestModule],
                declarations: [LigneDevisDetailComponent],
                providers: [
                    LigneDevisService
                ]
            })
            .overrideTemplate(LigneDevisDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(LigneDevisDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(LigneDevisService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                spyOn(service, 'find').and.returnValue(Observable.of(new HttpResponse({
                    body: new LigneDevis(123)
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.find).toHaveBeenCalledWith(123);
                expect(comp.ligneDevis).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
