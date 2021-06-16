import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {HomeComponent} from './home/home.component';
import {ApplicationComponent} from './application/application.component';
import {CreateApplicationComponent} from './application/create-application/create-application.component';

const routes: Routes = [
  {path: '', component: HomeComponent},
  {path: 'apps', component: ApplicationComponent},
  {path: 'create-app', component: CreateApplicationComponent},
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {
}
