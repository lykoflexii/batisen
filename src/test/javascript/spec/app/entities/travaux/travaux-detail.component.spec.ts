/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';

import { GesBtpTestModule } from '../../../test.module';
import { TravauxDetailComponent } from '../../../../../../main/webapp/app/entities/travaux/travaux-detail.component';
import { TravauxService } from '../../../../../../main/webapp/app/entities/travaux/travaux.service';
import { Travaux } from '../../../../../../main/webapp/app/entities/travaux/travaux.model';

describe('Component Tests', () => {

    describe('Travaux Management Detail Component', () => {
        let comp: TravauxDetailComponent;
        let fixture: ComponentFixture<TravauxDetailComponent>;
        let service: TravauxService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [GesBtpTestModule],
                declarations: [TravauxDetailComponent],
                providers: [
                    TravauxService
                ]
            })
            .overrideTemplate(TravauxDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(TravauxDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(TravauxService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                spyOn(service, 'find').and.returnValue(Observable.of(new HttpResponse({
                    body: new Travaux(123)
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.find).toHaveBeenCalledWith(123);
                expect(comp.travaux).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
