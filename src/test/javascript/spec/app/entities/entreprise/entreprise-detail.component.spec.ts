/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';

import { GesBtpTestModule } from '../../../test.module';
import { EntrepriseDetailComponent } from '../../../../../../main/webapp/app/entities/entreprise/entreprise-detail.component';
import { EntrepriseService } from '../../../../../../main/webapp/app/entities/entreprise/entreprise.service';
import { Entreprise } from '../../../../../../main/webapp/app/entities/entreprise/entreprise.model';

describe('Component Tests', () => {

    describe('Entreprise Management Detail Component', () => {
        let comp: EntrepriseDetailComponent;
        let fixture: ComponentFixture<EntrepriseDetailComponent>;
        let service: EntrepriseService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [GesBtpTestModule],
                declarations: [EntrepriseDetailComponent],
                providers: [
                    EntrepriseService
                ]
            })
            .overrideTemplate(EntrepriseDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(EntrepriseDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(EntrepriseService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                spyOn(service, 'find').and.returnValue(Observable.of(new HttpResponse({
                    body: new Entreprise(123)
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.find).toHaveBeenCalledWith(123);
                expect(comp.entreprise).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
