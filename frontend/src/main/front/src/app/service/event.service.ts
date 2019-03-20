import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {Event} from '../model/event';

@Injectable({
  providedIn: 'root'
})
export class EventService {

  private eventUrl = 'api/events?userId=';

  constructor(private httpClient: HttpClient) { }

  public getHomeEvents(userId: number): Observable<Event[]> {
    return this.httpClient.get<Event[]>(this.eventUrl + userId);
  }
}
