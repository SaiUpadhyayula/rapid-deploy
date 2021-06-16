import {Injectable} from '@angular/core';
import {ApplicationPayload} from './application-payload';
import {Observable} from 'rxjs';
import {HttpClient} from '@angular/common/http';
import {ApplicationResponse} from './application-response';

@Injectable({
  providedIn: 'root'
})
export class ApplicationService {

  constructor(private httpClient: HttpClient) {
  }

  create(applicationPayload: ApplicationPayload): Observable<ApplicationResponse> {
    return this.httpClient.post<ApplicationResponse>('http://localhost:9000/api/application', applicationPayload);
  }
}
