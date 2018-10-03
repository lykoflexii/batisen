import { Component, OnInit } from '@angular/core';
import { ChantierService, Chantier } from '../chantier';
import { HttpResponse } from '../../../../../../node_modules/@angular/common/http';
import { AffectationService } from './affectation.service';
import { AmChartsService, AmChart } from '@amcharts/amcharts3-angular';
import { Data } from './affectation.model';
import { ClientService, Client } from '../client';
import { Chantiers } from '../chantier/chantiers.model';

@Component({
  selector: 'jhi-dashboard',
  templateUrl: './dashboard.component.html',
  styles: [
    'dashboard.css',
    'dashboard.scss'
  ]
})
export class DashboardComponent implements OnInit {
  public pieChartLabels2: string[] = ['Travaux \nTerminer\n', 'Travaux \nen cours\n', 'Travaux \nen retard\n'];
  public pieChartLabels: string[] = ['Chantier \nTerminer\n', 'Chantier \nen cours\n', 'Chantier \nen retard\n'];
  public color: string[] = ['#FCD202', '#FF9E01', '#FF6600', '#DDDDDD', '#999999', '#754DEB', '#000000', '#333333',
                              '#FCD202', '#FF9E01', '#FF6600', '#DDDDDD', '#999999', '#754DEB', '#000000', '#333333']
  selected = 'pie';
  selected2: number;
  selected3: number;
  selectedClient: number;
  selectedChantier: number;
  terminer: number;
  enCours: number;
  retard: number;
  chantiers: Chantiers[];
  clients: Client[];
  etatTravaux: any;
  etatChantierGlob: any;
  etatChantierClient: any;
  etat: any;
  donnee: any;
  data: Data[] = [new Data(), new Data(), new Data()];
  data2: Data[] = [new Data(), new Data(), new Data()];
  data3: Data[] = [new Data(), new Data(), new Data()];
  data4: Data[] = [new Data(), new Data(), new Data()];
  dataCoutChantier: Data[] = [new Data(), new Data()];
  chart: AmChart;
  databar: Data[] = [new Data(), new Data(), new Data()];
  titre: string;
  coutChantier: any;
  constructor(
    private chantierService: ChantierService,
    private affectationService: AffectationService,
    private AmCharts: AmChartsService,
    private clientService: ClientService
  ) { }

  ngOnInit() {
            this.clientService.findClient(+sessionStorage.getItem('entreprise_id'))
            .subscribe((chantierResponse: HttpResponse<Client[]>) => {
                this.clients = chantierResponse.body;
            });
            this.chantierService.findEtatChantierParClient(2351).subscribe((res: HttpResponse<any>) => {
              this.etat = res.body;
            });
            this.affectationService.coutChantier(3101).subscribe((res: HttpResponse<any>) => {
              this.coutChantier = res.body;
            });
          this.loadEtatChantier();
         // this.diagrammeLigne();
  }

  loadEtatChantier() {
    this.chantierService.findEtatChantier(+sessionStorage.getItem('entreprise_id')).subscribe((res: HttpResponse<any>) => {
      this.etatChantierGlob = res.body;
      this.data4[0].value = this.etatChantierGlob['terminer'];
      this.data4[1].value = this.etatChantierGlob['enCours'];
      this.data4[2].value = this.etatChantierGlob['enRetard'];
      this.pieChartLabels.forEach((el, i) => {
        this.data4[i].etat = el;
        this.data4[i].color = this.color[i];
        this.titre = 'ETAT DES CHANTIERS';
      });
      if (this.selected === 'line') {
        this.AmCharts.makeChart('chartdiv', {
          'theme': 'light',
        'type': 'serial',
        'titles': [ {
          'text': this.titre,
          'size': 16
        } ],
      'startDuration': 2,
        'valueAxes': [{
            'position': 'left',
            'title': 'Visitors'
        }],
        'graphs': [{
            'balloonText': '[[category]]: <b>[[value]]</b>',
            'fillColorsField': 'color',
            'fillAlphas': 1,
            'lineAlpha': 0.1,
            'type': 'column',
            'valueField': 'value'
        }],
        'depth3D': 20,
      'angle': 30,
        'chartCursor': {
            'categoryBalloonEnabled': false,
            'cursorAlpha': 0,
            'zoomable': false
        },
        'categoryField': 'etat',
        'categoryAxis': {
            'gridPosition': 'start',
            'labelRotation': 45
        },
        'export': {
          'enabled': true
         },
         'dataProvider': this.data4
      });
      }else {
       this.AmCharts.makeChart( 'chartdiv', {
          'type': 'pie',
          'theme': 'light',
          'titles': [ {
            'text': this.titre,
            'size': 16
          } ],
          'valueField': 'value',
          'titleField': 'etat',
          // 'startEffect': 'elastic',
          'startDuration': 2,
          'labelRadius': 15,
          // 'innerRadius': '50%',
          // 'depth3D': 10,
          // 'balloonText': '[[title]]<br><span style='font-size:14px'><b>[[value]]</b> ([[percents]]%)</span>',
          // 'angle': 15,
          'balloon': {
            'fixedPosition': true
           },
          'export': {
            'enabled': true
          },
          'dataProvider': this.data4
        } );
      }
    });
        }

        loadEtatTravaux(id: number) {
          this.affectationService.findEtatTravaux(id).subscribe((res: HttpResponse<any>) => {
            this.etatTravaux = res.body;
            this.data[0].value = this.etatTravaux['terminer'];
            this.data[1].value = this.etatTravaux['enCours'];
            this.data[2].value = this.etatTravaux['enRetard'];
            this.pieChartLabels2.forEach((el, i) => {
              this.data[i].etat = el;
              this.data[i].color = this.color[i];
            });
            if (this.selected === 'line') {
              this.chart = this.AmCharts.makeChart('chartdiv4', {
                'theme': 'light',
              'type': 'serial',
              'titles': [ {
                'text': 'ETAT D"AVANCEMENT \nDES TRAVAUX DU CHANTIER N°' + id,
                'size': 16
              } ],
            'startDuration': 2,
              'valueAxes': [{
                  'position': 'left',
                  'title': 'Visitors'
              }],
              'graphs': [{
                  'balloonText': '[[category]]: <b>[[value]]</b>',
                  'fillColorsField': 'color',
                  'fillAlphas': 1,
                  'lineAlpha': 0.1,
                  'type': 'column',
                  'valueField': 'value'
              }],
              'depth3D': 20,
            'angle': 30,
              'chartCursor': {
                  'categoryBalloonEnabled': false,
                  'cursorAlpha': 0,
                  'zoomable': false
              },
              'categoryField': 'etat',
              'categoryAxis': {
                  'gridPosition': 'start',
                  'labelRotation': 45
              },
              'export': {
                'enabled': true
               },
               'dataProvider': this.data
            });
            }else {
              this.chart = this.AmCharts.makeChart( 'chartdiv4', {
                'type': 'pie',
                'theme': 'light',
                'titles': [ {
                  'text': 'ETAT D"AVANCEMENT \nDES TRAVAUX DU CHANTIER N°' + id,
                  'size': 16
                } ],
                'valueField': 'value',
                'titleField': 'etat',
                // 'startEffect': 'elastic',
                'startDuration': 2,
                'labelRadius': 15,
                // 'innerRadius': '50%',
                // 'depth3D': 10,
                // 'balloonText': '[[title]]<br><span style='font-size:14px'><b>[[value]]</b> ([[percents]]%)</span>',
                // 'angle': 15,
                'balloon': {
                  'fixedPosition': true
                 },
                'export': {
                  'enabled': true
                },
                'dataProvider': this.data
              } );
            }
          });
        }
        loadEtatChantierParClient(id) {
          this.chantierService.findEtatChantierParClient(id).subscribe((res: HttpResponse<any>) => {
            this.etatChantierClient = res.body;
            this.data3[0].value = this.etatChantierClient['terminer'];
            this.data3[1].value = this.etatChantierClient['enCours'];
            this.data3[2].value = this.etatChantierClient['enRetard'];
            this.pieChartLabels.forEach((el, i) => {
              this.data3[i].etat = el;
              this.data3[i].color = this.color[i];
              this.titre = 'ETAT DES CHANTIERS';
            });
            if (this.selected === 'line') {
              this.AmCharts.makeChart('chartdiv3', {
                'theme': 'light',
              'type': 'serial',
              'titles': [ {
                'text': 'ETAT D"AVANCEMENT \nDES CHANTIER DU ClIENT N°' + id,
                'size': 16
              } ],
            'startDuration': 2,
              'valueAxes': [{
                  'position': 'left',
                  'title': 'Visitors'
              }],
              'graphs': [{
                  'balloonText': '[[category]]: <b>[[value]]</b>',
                  'fillColorsField': 'color',
                  'fillAlphas': 1,
                  'lineAlpha': 0.1,
                  'type': 'column',
                  'valueField': 'value'
              }],
              'depth3D': 20,
            'angle': 30,
              'chartCursor': {
                  'categoryBalloonEnabled': false,
                  'cursorAlpha': 0,
                  'zoomable': false
              },
              'categoryField': 'etat',
              'categoryAxis': {
                  'gridPosition': 'start',
                  'labelRotation': 45
              },
              'export': {
                'enabled': true
               },
               'dataProvider': this.data3
            });
            }else {
             this.AmCharts.makeChart( 'chartdiv3', {
                'type': 'pie',
                'theme': 'light',
                'titles': [ {
                  'text': 'ETAT D"AVANCEMENT \nDES CHANTIER DU ClIENT N°' + id,
                  'size': 16
                } ],
                'valueField': 'value',
                'titleField': 'etat',
                // 'startEffect': 'elastic',
                'startDuration': 2,
                'labelRadius': 15,
                // 'innerRadius': '50%',
                // 'depth3D': 10,
                // 'balloonText': '[[title]]<br><span style='font-size:14px'><b>[[value]]</b> ([[percents]]%)</span>',
                // 'angle': 15,
                'balloon': {
                  'fixedPosition': true
                 },
                'export': {
                  'enabled': true
                },
                'dataProvider': this.data3
              } );
            }
          });
        }
      
  previousState() {
    window.history.back();
}
 // events
 public chartClicked(e: any): void {
  console.log(e);
}
public chartHovered(e: any): void {
  console.log(e);
}

choisirClient(e) {
  if ( this.selectedClient !== undefined && this.selectedClient !== 0) {
    this.loadEtatChantierParClient(this.selectedClient);
    this.chantierService.findByClient(this.selectedClient)
          .subscribe((chantierResponse: HttpResponse<Chantiers[]>) => {
              this.chantiers = chantierResponse.body;
          });
        }
      }

choisirChantier(e) {
  if ( this.selectedChantier !== undefined && this.selectedChantier !== 0) {
   this.diagrammeLigne(this.selectedChantier);
   this.loadEtatTravaux(this.selectedChantier);
   this.loadCoutChantier(this.selectedChantier);
  } 
}

diagrammeLigne(id1) {
  this.affectationService.findChantierParTache(id1)
            .subscribe((chantierResponse: HttpResponse<Chantier[]>) => {
                this.donnee = chantierResponse.body;
                let i = 0;
                this.databar = [];
                for (const id in this.donnee) {
                  const da = new Data(id, this.donnee[id], this.color[i])
                  this.databar[i] = da;
                  i++;
                }
                this.AmCharts.makeChart('chartdiv2', {
                  'theme': 'light',
                  'type': 'serial',
                 'startDuration': 2,
                  'dataProvider': this.databar,
                  'valueAxes': [{
                      'position': 'left',
                      'title': 'DEPENSE PAR TACHE'
                  }],
                  'graphs': [{
                      'balloonText': '[[category]]: <b>[[value]]</b>',
                      'fillColorsField': 'color',
                      'fillAlphas': 1,
                      'lineAlpha': 0.1,
                      'type': 'column',
                      'valueField': 'value'
                  }],
                  'depth3D': 20,
                'angle': 30,
                  'chartCursor': {
                      'categoryBalloonEnabled': false,
                      'cursorAlpha': 0,
                      'zoomable': false
                  },
                  'categoryField': 'etat',
                  'categoryAxis': {
                      'gridPosition': 'start',
                      'labelRotation': 90
                  },
                  'export': {
                    'enabled': true
                   }
                });
            }); 
}

loadCoutChantier(id) {
  this.affectationService.coutChantier(id).subscribe((res: HttpResponse<any>) => {
    this.coutChantier = res.body;
    this.dataCoutChantier[0].value = this.coutChantier['cout main doeuvre'];
    this.dataCoutChantier[1].value = this.coutChantier['cout facture'];
      this.dataCoutChantier[0].etat = 'cout main \nd"oeuvre';
      this.dataCoutChantier[1].etat = 'cout facture';
      this.titre = 'ETAT DES CHANTIERS';
    if (this.selected === 'line') {
      this.AmCharts.makeChart('chartdivMap', {
        'theme': 'light',
      'type': 'serial',
      'titles': [ {
        'text': 'COUT CHANTIER N°' + id,
        'size': 16
      } ],
    'startDuration': 2,
      'valueAxes': [{
          'position': 'left',
          'title': 'Visitors'
      }],
      'graphs': [{
          'balloonText': '[[category]]: <b>[[value]]</b>',
          'fillColorsField': 'color',
          'fillAlphas': 1,
          'lineAlpha': 0.1,
          'type': 'column',
          'valueField': 'value'
      }],
      'depth3D': 20,
    'angle': 30,
      'chartCursor': {
          'categoryBalloonEnabled': false,
          'cursorAlpha': 0,
          'zoomable': false
      },
      'categoryField': 'etat',
      'categoryAxis': {
          'gridPosition': 'start',
          'labelRotation': 45
      },
      'export': {
        'enabled': true
       },
       'dataProvider': this.dataCoutChantier
    });
    }else {
     this.AmCharts.makeChart( 'chartdivMap', {
        'type': 'pie',
        'theme': 'light',
        'titles': [ {
          'text': 'COUT CHANTIER N°' + id,
          'size': 16
        } ],
        'valueField': 'value',
        'titleField': 'etat',
        // 'startEffect': 'elastic',
        'startDuration': 2,
        'labelRadius': 15,
        // 'innerRadius': '50%',
        // 'depth3D': 10,
        // 'balloonText': '[[title]]<br><span style='font-size:14px'><b>[[value]]</b> ([[percents]]%)</span>',
        // 'angle': 15,
        'balloon': {
          'fixedPosition': true
         },
        'export': {
          'enabled': true
        },
        'dataProvider': this.dataCoutChantier
      } );
    }
  });
}
// choixGraphe(e: any) {
//   const bol1 = this.selected3 === undefined ;
//   const bol = this.selected2 === undefined ;
//   if (!bol1) {
//     if(this.selected3 !== 0) {
//       if (!bol) {
//         if(this.selected2 !==0) {
//           alert(this.selected);
//           this.loadEtatTravaux(this.selected2);
//         }else {
//           alert('global pour le client'+ this.selected3);
//         }
//       }else {
//         alert('global pour le client aaa'+ this.selected3);
//       }
//     }else {
//       this.loadEtatChantier();
//     }
//   }else {
//     this.loadEtatChantier();
//   }
// }

// activiteParClient(e: any) {
//     if ( this.selected3 !== undefined && this.selected3 !== 0) {
//       this.loadEtatChantierParClient(this.selected3);
//       this.chantierService.findByClient(this.selected3)
//             .subscribe((chantierResponse: HttpResponse<Chantiers[]>) => {
//                 this.chantiers = chantierResponse.body;
//             });
//     } 
// }

// activiteParChantier(e: any) {
//   if ( this.selected2 !== undefined && this.selected2 !== 0) {
//     this.loadEtatTravaux(this.selected2);
//   }
// }

// loadChart()  {
//   if(this.selected === 'pie') {
//     this.diagrammeCirculaire();
//   }else {
//     this.diagrammeBaton();
//   }
// }

// diagrammeBaton() {
//     this.loadEtatChantier();
//   this.chart = this.AmCharts.makeChart("chartdiv", {
//     "theme": "light",
//   "type": "serial",
//   "titles": [ {
//     "text": "Visitors countries",
//     "size": 16
//   } ],
// "startDuration": 2,
//   "valueAxes": [{
//       "position": "left",
//       "title": "Visitors"
//   }],
//   "graphs": [{
//       "balloonText": "[[category]]: <b>[[value]]</b>",
//       "fillColorsField": "color",
//       "fillAlphas": 1,
//       "lineAlpha": 0.1,
//       "type": "column",
//       "valueField": "value"
//   }],
//   "depth3D": 20,
// "angle": 30,
//   "chartCursor": {
//       "categoryBalloonEnabled": false,
//       "cursorAlpha": 0,
//       "zoomable": false
//   },
//   "categoryField": "etat",
//   "categoryAxis": {
//       "gridPosition": "start",
//       "labelRotation": 45
//   },
//   "export": {
//     "enabled": true
//    },
//    "dataProvider": this.data

// });
// }

// diagrammeCirculaire() {
//     this.loadEtatChantier();
//   this.chart = this.AmCharts.makeChart( "chartdiv", {
//     "type": "pie",
//     "theme": "light",
//     "titles": [ {
//       "text": "Visitors countries",
//       "size": 16
//     } ],
//     "valueField": "value",
//     "titleField": "etat",
//     "startEffect": "elastic",
//     "startDuration": 2,
//     "labelRadius": 15,
//     "innerRadius": "50%",
//     "depth3D": 10,
//     "balloonText": "[[title]]<br><span style='font-size:14px'><b>[[value]]</b> ([[percents]]%)</span>",
//     "angle": 15,
//     "export": {
//       "enabled": true
//     },
//     "dataProvider": this.data
//   } );
// }

// chart.addListener("dataUpdated", zoomChart);
// when we apply theme, the dataUpdated event is fired even before we add listener, so
// we need to call zoomChart here
// zoomChart();
// this method is called when chart is first inited as we listen for "dataUpdated" event
// function zoomChart() {
    // different zoom methods can be used - zoomToIndexes, zoomToDates, zoomToCategoryValues
//     chart.zoomToIndexes(chartData.length - 250, chartData.length - 100);
// }

// generate some random data, quite different range
//  generateChartData() {
//     let chartData = [];
    // current date
    // let firstDate = new Date();
    // now set 500 minutes back
    // firstDate.setMinutes(firstDate.getDate() - 1000);

    // and generate 500 data items
    // let visits = 500;
    // for (let i = 0; i < 500; i++) {
    //     let newDate = new Date(firstDate);
        // each time we add one minute
        // newDate.setMinutes(newDate.getMinutes() + i);
        // some random number
        // visits += Math.round((Math.random()<0.5?1: -1)*Math.random()*10);
        // add data item to the array
//         chartData.push({
//             date: newDate,
//             visits: visits
//         });
//     }
//     return chartData;
// }

}
