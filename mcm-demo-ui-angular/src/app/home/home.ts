import { Component } from '@angular/core';
import { AuthService } from '../auth.service';
import { NgIf } from '@angular/common';

@Component({
  selector: 'app-home',
  imports: [NgIf],
  templateUrl: './home.html',
  styleUrl: './home.scss'
})
export class Home {

  constructor(public auth: AuthService) { }

  login() {
    this.auth.login();
  }

  logout() {
    this.auth.logout();
  }
}
