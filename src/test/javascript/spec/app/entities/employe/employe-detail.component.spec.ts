/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';

import { GesBtpTestModule } from '../../../test.module';
import { EmployeDetailComponent } from '../../../../../../main/webapp/app/entities/employe/employe-detail.component';
import { EmployeService } from '../../../../../../main/webapp/app/entities/employe/employe.service';
import { Employe } from '../../../../../../main/webapp/app/entities/employe/employe.model';

describe('Component Tests', () => {

    describe('Employe Management Detail Component', () => {
        let comp: EmployeDetailComponent;
        let fixture: ComponentFixture<EmployeDetailComponent>;
        let service: EmployeService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [GesBtpTestModule],
                declarations: [EmployeDetailComponent],
                providers: [
                    EmployeService
                ]
            })
            .overrideTemplate(EmployeDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(EmployeDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(EmployeService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                spyOn(service, 'find').and.returnValue(Observable.of(new HttpResponse({
                    body: new Employe(123)
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.find).toHaveBeenCalledWith(123);
                expect(comp.employe).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
