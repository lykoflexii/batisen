/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable } from 'rxjs/Observable';
import { JhiEventManager } from 'ng-jhipster';

import { GesBtpTestModule } from '../../../test.module';
import { LigneDevisDialogComponent } from '../../../../../../main/webapp/app/entities/ligne-devis/ligne-devis-dialog.component';
import { LigneDevisService } from '../../../../../../main/webapp/app/entities/ligne-devis/ligne-devis.service';
import { LigneDevis } from '../../../../../../main/webapp/app/entities/ligne-devis/ligne-devis.model';
import { DevisService } from '../../../../../../main/webapp/app/entities/devis';

describe('Component Tests', () => {

    describe('LigneDevis Management Dialog Component', () => {
        let comp: LigneDevisDialogComponent;
        let fixture: ComponentFixture<LigneDevisDialogComponent>;
        let service: LigneDevisService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [GesBtpTestModule],
                declarations: [LigneDevisDialogComponent],
                providers: [
                    DevisService,
                    LigneDevisService
                ]
            })
            .overrideTemplate(LigneDevisDialogComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(LigneDevisDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(LigneDevisService);
            mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
            mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
        });

        describe('save', () => {
            it('Should call update service on save for existing entity',
                inject([],
                    fakeAsync(() => {
                        // GIVEN
                        const entity = new LigneDevis(123);
                        spyOn(service, 'update').and.returnValue(Observable.of(new HttpResponse({body: entity})));
                        comp.ligneDevis = entity;
                        // WHEN
                        comp.save();
                        tick(); // simulate async

                        // THEN
                        expect(service.update).toHaveBeenCalledWith(entity);
                        expect(comp.isSaving).toEqual(false);
                        expect(mockEventManager.broadcastSpy).toHaveBeenCalledWith({ name: 'ligneDevisListModification', content: 'OK'});
                        expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                    })
                )
            );

            it('Should call create service on save for new entity',
                inject([],
                    fakeAsync(() => {
                        // GIVEN
                        const entity = new LigneDevis();
                        spyOn(service, 'create').and.returnValue(Observable.of(new HttpResponse({body: entity})));
                        comp.ligneDevis = entity;
                        // WHEN
                        comp.save();
                        tick(); // simulate async

                        // THEN
                        expect(service.create).toHaveBeenCalledWith(entity);
                        expect(comp.isSaving).toEqual(false);
                        expect(mockEventManager.broadcastSpy).toHaveBeenCalledWith({ name: 'ligneDevisListModification', content: 'OK'});
                        expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                    })
                )
            );
        });
    });

});
