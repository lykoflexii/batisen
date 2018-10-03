import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'jhi-redirect',
  templateUrl: './redirect.component.html',
  styles: []
})
export class RedirectComponent implements OnInit {

  constructor(
    private router: Router
  ) { }

  ngOnInit() {
    this.router.navigate(['/']);
  }

}
