import {Component, OnInit} from '@angular/core';
import {EventService} from '../../service/event.service';
import {Event} from '../../model/event';
import {User} from '../../model/user';
import {Gender} from '../../model/gender.enum';
import {Activity} from '../../model/activity.enum';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss']
})
export class HomeComponent implements OnInit {

  public events: Event[];
  private user: User = {
    id: 1,
    firstName: '',
    lastName: '',
    birthDate: new Date(),
    gender: Gender.MAN,
    city: 'Paris',
    activities: [Activity.FOOTBALL, Activity.GUITAR, Activity.JAVA],
    picture: ''
  };

  constructor(private eventService: EventService) {
  }

  ngOnInit() {
    this.eventService.getHomeEvents(this.user.id).subscribe((events: Event[]) => {
      this.events = events;
    });
  }

}
