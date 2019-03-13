import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';

import { AppComponent } from './app.component';
import { CreateAccountComponent } from './component/create-account/create-account.component';
import {BrowserAnimationsModule} from "@angular/platform-browser/animations";
import {
  MAT_DATE_LOCALE, MatButtonModule, MatDatepickerModule, MatIconModule, MatInputModule, MatNativeDateModule,
  MatRadioModule,
  MatSelectModule
} from "@angular/material";
import {FlexLayoutModule} from "@angular/flex-layout";
import { ContainerLeftAlignedComponent } from './shared/container-left-aligned/container-left-aligned.component';
import {ReactiveFormsModule} from "@angular/forms";
import {HttpClientModule} from "@angular/common/http";

@NgModule({
  declarations: [
    AppComponent,
    CreateAccountComponent,
    ContainerLeftAlignedComponent
  ],
  imports: [
    BrowserModule,
    BrowserAnimationsModule,
    MatButtonModule,
    MatInputModule,
    FlexLayoutModule,
    MatSelectModule,
    MatDatepickerModule,
    MatNativeDateModule,
    MatRadioModule,
    MatIconModule,
    ReactiveFormsModule,
    HttpClientModule
  ],
  providers: [{provide: MAT_DATE_LOCALE, useValue: 'fr-FR'}],
  bootstrap: [AppComponent]
})
export class AppModule { }
