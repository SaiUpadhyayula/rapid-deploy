import {Component, OnInit} from '@angular/core';
import {FormControl, FormGroup, Validators} from '@angular/forms';
import {CreateAppPayload} from './create-app-payload';
import {ApplicationService} from '../application/application.service';
import {Router} from '@angular/router';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {
  createAppForm!: FormGroup;
  createServiceForm!: FormGroup;
  createAppPayload!: CreateAppPayload;

  constructor(private appService: ApplicationService, private router: Router) {
  }

  ngOnInit(): void {
    this.createAppForm = new FormGroup({
      applicationName: new FormControl('', Validators.required)
    });
    this.createAppPayload = {
      applicationName: ''
    };
  }

  createApp(): void {
    this.router.navigateByUrl('/create-app');
    // this.createAppPayload.applicationName = this.createAppForm.get('applicationName').value;
    // this.appService.create(this.createAppPayload).subscribe(data => console.log(data.guid));
  }

  createService(): void {

  }
}
