import {Activity} from './activity.enum';
import {User} from './user';

export class Event {
  id: number;
  name: string;
  description: string;
  date: Date;
  location: Location;
  activity: Activity;
  owner: User;
}
