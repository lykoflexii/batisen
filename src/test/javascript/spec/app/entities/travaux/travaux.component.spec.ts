/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { Observable } from 'rxjs/Observable';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { GesBtpTestModule } from '../../../test.module';
import { TravauxComponent } from '../../../../../../main/webapp/app/entities/travaux/travaux.component';
import { TravauxService } from '../../../../../../main/webapp/app/entities/travaux/travaux.service';
import { Travaux } from '../../../../../../main/webapp/app/entities/travaux/travaux.model';

describe('Component Tests', () => {

    describe('Travaux Management Component', () => {
        let comp: TravauxComponent;
        let fixture: ComponentFixture<TravauxComponent>;
        let service: TravauxService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [GesBtpTestModule],
                declarations: [TravauxComponent],
                providers: [
                    TravauxService
                ]
            })
            .overrideTemplate(TravauxComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(TravauxComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(TravauxService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN
                const headers = new HttpHeaders().append('link', 'link;link');
                spyOn(service, 'query').and.returnValue(Observable.of(new HttpResponse({
                    body: [new Travaux(123)],
                    headers
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.query).toHaveBeenCalled();
                expect(comp.travauxes[0]).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
