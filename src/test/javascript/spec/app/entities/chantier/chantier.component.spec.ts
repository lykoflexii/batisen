/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { Observable } from 'rxjs/Observable';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { GesBtpTestModule } from '../../../test.module';
import { ChantierComponent } from '../../../../../../main/webapp/app/entities/chantier/chantier.component';
import { ChantierService } from '../../../../../../main/webapp/app/entities/chantier/chantier.service';
import { Chantier } from '../../../../../../main/webapp/app/entities/chantier/chantier.model';

describe('Component Tests', () => {

    describe('Chantier Management Component', () => {
        let comp: ChantierComponent;
        let fixture: ComponentFixture<ChantierComponent>;
        let service: ChantierService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [GesBtpTestModule],
                declarations: [ChantierComponent],
                providers: [
                    ChantierService
                ]
            })
            .overrideTemplate(ChantierComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(ChantierComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(ChantierService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN
                const headers = new HttpHeaders().append('link', 'link;link');
                spyOn(service, 'query').and.returnValue(Observable.of(new HttpResponse({
                    body: [new Chantier(123)],
                    headers
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.query).toHaveBeenCalled();
                expect(comp.chantiers[0]).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
