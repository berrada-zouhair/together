import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs/index';
import {User} from '../model/user';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  private userUrl = 'api/user';

  constructor(private httpClient: HttpClient) { }

  public createUser(user: User): Observable<Object> {
    return this.httpClient.post(this.userUrl, user);
  }
}
