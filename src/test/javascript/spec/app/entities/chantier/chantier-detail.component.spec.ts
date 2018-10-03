/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';

import { GesBtpTestModule } from '../../../test.module';
import { ChantierDetailComponent } from '../../../../../../main/webapp/app/entities/chantier/chantier-detail.component';
import { ChantierService } from '../../../../../../main/webapp/app/entities/chantier/chantier.service';
import { Chantier } from '../../../../../../main/webapp/app/entities/chantier/chantier.model';

describe('Component Tests', () => {

    describe('Chantier Management Detail Component', () => {
        let comp: ChantierDetailComponent;
        let fixture: ComponentFixture<ChantierDetailComponent>;
        let service: ChantierService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [GesBtpTestModule],
                declarations: [ChantierDetailComponent],
                providers: [
                    ChantierService
                ]
            })
            .overrideTemplate(ChantierDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(ChantierDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(ChantierService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                spyOn(service, 'find').and.returnValue(Observable.of(new HttpResponse({
                    body: new Chantier(123)
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.find).toHaveBeenCalledWith(123);
                expect(comp.chantier).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
