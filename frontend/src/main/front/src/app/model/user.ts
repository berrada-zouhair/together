import {Gender} from './gender.enum';
import {Activity} from './activity.enum';

export class User {
  id: number;
  firstName: string;
  lastName: string;
  gender: Gender;
  birthDate: any;
  city: string;
  picture: string;
  activities: Activity[];
}
