/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable } from 'rxjs/Observable';
import { JhiEventManager } from 'ng-jhipster';

import { GesBtpTestModule } from '../../../test.module';
import { LigneFactureDialogComponent } from '../../../../../../main/webapp/app/entities/ligne-facture/ligne-facture-dialog.component';
import { LigneFactureService } from '../../../../../../main/webapp/app/entities/ligne-facture/ligne-facture.service';
import { LigneFacture } from '../../../../../../main/webapp/app/entities/ligne-facture/ligne-facture.model';
import { FactureService } from '../../../../../../main/webapp/app/entities/facture';

describe('Component Tests', () => {

    describe('LigneFacture Management Dialog Component', () => {
        let comp: LigneFactureDialogComponent;
        let fixture: ComponentFixture<LigneFactureDialogComponent>;
        let service: LigneFactureService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [GesBtpTestModule],
                declarations: [LigneFactureDialogComponent],
                providers: [
                    FactureService,
                    LigneFactureService
                ]
            })
            .overrideTemplate(LigneFactureDialogComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(LigneFactureDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(LigneFactureService);
            mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
            mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
        });

        describe('save', () => {
            it('Should call update service on save for existing entity',
                inject([],
                    fakeAsync(() => {
                        // GIVEN
                        const entity = new LigneFacture(123);
                        spyOn(service, 'update').and.returnValue(Observable.of(new HttpResponse({body: entity})));
                        comp.ligneFacture = entity;
                        // WHEN
                        comp.save();
                        tick(); // simulate async

                        // THEN
                        expect(service.update).toHaveBeenCalledWith(entity);
                        expect(comp.isSaving).toEqual(false);
                        expect(mockEventManager.broadcastSpy).toHaveBeenCalledWith({ name: 'ligneFactureListModification', content: 'OK'});
                        expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                    })
                )
            );

            it('Should call create service on save for new entity',
                inject([],
                    fakeAsync(() => {
                        // GIVEN
                        const entity = new LigneFacture();
                        spyOn(service, 'create').and.returnValue(Observable.of(new HttpResponse({body: entity})));
                        comp.ligneFacture = entity;
                        // WHEN
                        comp.save();
                        tick(); // simulate async

                        // THEN
                        expect(service.create).toHaveBeenCalledWith(entity);
                        expect(comp.isSaving).toEqual(false);
                        expect(mockEventManager.broadcastSpy).toHaveBeenCalledWith({ name: 'ligneFactureListModification', content: 'OK'});
                        expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                    })
                )
            );
        });
    });

});
