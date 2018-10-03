/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { Observable } from 'rxjs/Observable';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { GesBtpTestModule } from '../../../test.module';
import { EmployeComponent } from '../../../../../../main/webapp/app/entities/employe/employe.component';
import { EmployeService } from '../../../../../../main/webapp/app/entities/employe/employe.service';
import { Employe } from '../../../../../../main/webapp/app/entities/employe/employe.model';

describe('Component Tests', () => {

    describe('Employe Management Component', () => {
        let comp: EmployeComponent;
        let fixture: ComponentFixture<EmployeComponent>;
        let service: EmployeService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [GesBtpTestModule],
                declarations: [EmployeComponent],
                providers: [
                    EmployeService
                ]
            })
            .overrideTemplate(EmployeComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(EmployeComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(EmployeService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN
                const headers = new HttpHeaders().append('link', 'link;link');
                spyOn(service, 'query').and.returnValue(Observable.of(new HttpResponse({
                    body: [new Employe(123)],
                    headers
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.query).toHaveBeenCalled();
                expect(comp.employes[0]).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
