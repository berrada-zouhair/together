import {Component, ElementRef, OnInit, ViewChild} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {User} from '../../model/user';
import * as moment from 'moment';
import {UserService} from '../../service/user.service';

@Component({
  selector: 'app-create-account',
  templateUrl: './create-account.component.html',
  styleUrls: ['./create-account.component.scss']
})
export class CreateAccountComponent implements OnInit {

  @ViewChild('fileButton')
  private fileButton: ElementRef;
  private user: User = new User();
  public fileName: string;
  public inscriptionForm: FormGroup;

  constructor(private formBuilder: FormBuilder, private userService: UserService) {
  }


  ngOnInit() {
    this.inscriptionForm = this.formBuilder.group({
      firstName: [this.user.firstName, Validators.required],
      lastName: [this.user.lastName, Validators.required],
      gender: [this.user.gender, Validators.required],
      birthDate: [this.user.birthDate, Validators.required],
      city: [this.user.city, Validators.required],
    });
  }

  selectFile() {
    this.fileButton.nativeElement.click();
  }

  clearFile() {
    this.fileName = null;
    this.user.picture = null
    this.fileButton.nativeElement.value = null;
  }

  onFileChange(event) {
    if (event.target.files && event.target.files.length) {
      this.fileName = event.target.files[0].name;
      let reader = new FileReader();
      let that = this;
      reader.onload = function (e) {
        that.user.picture = reader.result;
      }
      reader.readAsText(event.target.files[0]);
    }
  }

  isFormValid(): boolean {
    return !this.inscriptionForm.invalid && this.user.picture && !!this.user.picture.length;
  }

  submitUser() {
    const picture = this.user.picture
    this.user = Object.assign({}, this.inscriptionForm.value)
    this.user.picture = picture;
    this.userService.createUser(this.user).subscribe(() => {
    });
  }
}
