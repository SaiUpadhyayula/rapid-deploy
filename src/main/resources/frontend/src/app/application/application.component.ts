import {Component, OnInit} from '@angular/core';
import {Router} from '@angular/router';
import {FormControl, FormGroup, Validators} from '@angular/forms';
import {ApplicationService} from './application.service';
import {ApplicationPayload} from './application-payload';

@Component({
  selector: 'app-application',
  templateUrl: './application.component.html',
  styleUrls: ['./application.component.css']
})
export class ApplicationComponent implements OnInit {

  applicationForm: FormGroup;
  applicationPayload: ApplicationPayload;

  constructor(private router: Router, private applicationService: ApplicationService) {
  }

  ngOnInit(): void {
    this.applicationForm = new FormGroup({
      applicationName: new FormControl('', Validators.required)
    });
    this.applicationPayload = {
      applicationName: ''
    };
  }

  createApp(): void {
    this.applicationService.create(this.applicationPayload).subscribe(data => {
      console.log(data.guid);
    });
    this.router.navigateByUrl('/create-app');
  }
}
