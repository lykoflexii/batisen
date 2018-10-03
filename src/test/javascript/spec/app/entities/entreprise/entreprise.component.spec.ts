/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { Observable } from 'rxjs/Observable';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { GesBtpTestModule } from '../../../test.module';
import { EntrepriseComponent } from '../../../../../../main/webapp/app/entities/entreprise/entreprise.component';
import { EntrepriseService } from '../../../../../../main/webapp/app/entities/entreprise/entreprise.service';
import { Entreprise } from '../../../../../../main/webapp/app/entities/entreprise/entreprise.model';

describe('Component Tests', () => {

    describe('Entreprise Management Component', () => {
        let comp: EntrepriseComponent;
        let fixture: ComponentFixture<EntrepriseComponent>;
        let service: EntrepriseService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [GesBtpTestModule],
                declarations: [EntrepriseComponent],
                providers: [
                    EntrepriseService
                ]
            })
            .overrideTemplate(EntrepriseComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(EntrepriseComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(EntrepriseService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN
                const headers = new HttpHeaders().append('link', 'link;link');
                spyOn(service, 'query').and.returnValue(Observable.of(new HttpResponse({
                    body: [new Entreprise(123)],
                    headers
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.query).toHaveBeenCalled();
                expect(comp.entreprises[0]).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
