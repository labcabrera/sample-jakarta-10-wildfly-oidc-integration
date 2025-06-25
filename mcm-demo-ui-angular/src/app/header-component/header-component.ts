import { AsyncPipe, NgIf } from '@angular/common';
import { Component, Input } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatMenuModule } from '@angular/material/menu';
import { MatToolbarModule } from '@angular/material/toolbar';
import { AuthService } from '../auth.service';

@Component({
  selector: 'app-header-component',
  standalone: true,
  imports: [
    NgIf,
    AsyncPipe,
    MatToolbarModule,
    MatButtonModule,
    MatMenuModule,
    MatIconModule
  ],
  templateUrl: './header-component.html',
  styleUrl: './header-component.scss'
})
export class HeaderComponent {
  @Input() title = '';
  constructor(public auth: AuthService) { }
  login() { this.auth.login(); }
  logout() { this.auth.logout(); }
}
