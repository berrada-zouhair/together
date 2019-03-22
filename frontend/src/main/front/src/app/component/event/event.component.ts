import {Component, Input, OnInit} from '@angular/core';
import {Event} from '../../model/event';
import {Router} from '@angular/router';

@Component({
  selector: 'app-event',
  templateUrl: './event.component.html',
  styleUrls: ['./event.component.scss']
})
export class EventComponent implements OnInit {
  @Input('event') public event: Event;

  constructor(private router: Router) { }

  ngOnInit() {
  }

  redirectToInscription() {
    this.router.navigate(['/account']);
  }
}
