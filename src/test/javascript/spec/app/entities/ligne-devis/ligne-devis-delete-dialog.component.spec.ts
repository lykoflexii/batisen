/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable } from 'rxjs/Observable';
import { JhiEventManager } from 'ng-jhipster';

import { GesBtpTestModule } from '../../../test.module';
import { LigneDevisDeleteDialogComponent } from '../../../../../../main/webapp/app/entities/ligne-devis/ligne-devis-delete-dialog.component';
import { LigneDevisService } from '../../../../../../main/webapp/app/entities/ligne-devis/ligne-devis.service';

describe('Component Tests', () => {

    describe('LigneDevis Management Delete Component', () => {
        let comp: LigneDevisDeleteDialogComponent;
        let fixture: ComponentFixture<LigneDevisDeleteDialogComponent>;
        let service: LigneDevisService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [GesBtpTestModule],
                declarations: [LigneDevisDeleteDialogComponent],
                providers: [
                    LigneDevisService
                ]
            })
            .overrideTemplate(LigneDevisDeleteDialogComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(LigneDevisDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(LigneDevisService);
            mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
            mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
        });

        describe('confirmDelete', () => {
            it('Should call delete service on confirmDelete',
                inject([],
                    fakeAsync(() => {
                        // GIVEN
                        spyOn(service, 'delete').and.returnValue(Observable.of({}));

                        // WHEN
                        comp.confirmDelete(123);
                        tick();

                        // THEN
                        expect(service.delete).toHaveBeenCalledWith(123);
                        expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                        expect(mockEventManager.broadcastSpy).toHaveBeenCalled();
                    })
                )
            );
        });
    });

});
