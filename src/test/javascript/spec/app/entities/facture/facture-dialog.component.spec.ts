/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable } from 'rxjs/Observable';
import { JhiEventManager } from 'ng-jhipster';

import { GesBtpTestModule } from '../../../test.module';
import { FactureDialogComponent } from '../../../../../../main/webapp/app/entities/facture/facture-dialog.component';
import { FactureService } from '../../../../../../main/webapp/app/entities/facture/facture.service';
import { Facture } from '../../../../../../main/webapp/app/entities/facture/facture.model';
import { ChantierService } from '../../../../../../main/webapp/app/entities/chantier';
import { TravauxService } from '../../../../../../main/webapp/app/entities/travaux';
import { EntrepriseService } from '../../../../../../main/webapp/app/entities/entreprise';

describe('Component Tests', () => {

    describe('Facture Management Dialog Component', () => {
        let comp: FactureDialogComponent;
        let fixture: ComponentFixture<FactureDialogComponent>;
        let service: FactureService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [GesBtpTestModule],
                declarations: [FactureDialogComponent],
                providers: [
                    ChantierService,
                    TravauxService,
                    EntrepriseService,
                    FactureService
                ]
            })
            .overrideTemplate(FactureDialogComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(FactureDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(FactureService);
            mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
            mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
        });

        describe('save', () => {
            it('Should call update service on save for existing entity',
                inject([],
                    fakeAsync(() => {
                        // GIVEN
                        const entity = new Facture(123);
                        spyOn(service, 'update').and.returnValue(Observable.of(new HttpResponse({body: entity})));
                        comp.facture = entity;
                        // WHEN
                        comp.save();
                        tick(); // simulate async

                        // THEN
                        expect(service.update).toHaveBeenCalledWith(entity);
                        expect(comp.isSaving).toEqual(false);
                        expect(mockEventManager.broadcastSpy).toHaveBeenCalledWith({ name: 'factureListModification', content: 'OK'});
                        expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                    })
                )
            );

            it('Should call create service on save for new entity',
                inject([],
                    fakeAsync(() => {
                        // GIVEN
                        const entity = new Facture();
                        spyOn(service, 'create').and.returnValue(Observable.of(new HttpResponse({body: entity})));
                        comp.facture = entity;
                        // WHEN
                        comp.save();
                        tick(); // simulate async

                        // THEN
                        expect(service.create).toHaveBeenCalledWith(entity);
                        expect(comp.isSaving).toEqual(false);
                        expect(mockEventManager.broadcastSpy).toHaveBeenCalledWith({ name: 'factureListModification', content: 'OK'});
                        expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                    })
                )
            );
        });
    });

});
