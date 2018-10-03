/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject } from '@angular/core/testing';
import { OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { GesBtpTestModule } from '../../test.module';
import { MockActivatedRoute } from '../../helpers/mock-route.service';
import { EntrepriseMgmtDetailComponent } from '../../../../../main/webapp/app/admin/entreprise-management/entreprise-management-detail.component';
import { EntrepriseService } from '../../../../../main/webapp/app/admin/entreprise-management/entreprise.service';
import { Entreprise } from '../../../../../main/webapp/app/admin/entreprise-management/entreprise.model';

describe('Component Tests', () => {

    describe('Entreprise Management Detail Component', () => {
        let comp: EntrepriseMgmtDetailComponent;
        let fixture: ComponentFixture<EntrepriseMgmtDetailComponent>;
        let service: EntrepriseService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [GesBtpTestModule],
                declarations: [EntrepriseMgmtDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    EntrepriseService,
                    JhiEventManager
                ]
            }).overrideTemplate(EntrepriseMgmtDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(EntrepriseMgmtDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(EntrepriseService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new Entreprise(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.entreprise).toEqual(jasmine.objectContaining({id: 10}));
            });
        });
    });

});
