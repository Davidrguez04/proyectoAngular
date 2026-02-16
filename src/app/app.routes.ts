import { Routes } from '@angular/router';
import { HomeComponent } from './Componentes/home/home.component';
import { RegistroUsuarioComponent } from './Componentes/registro-usuario/registro-usuario.component';
import { LoginComponent } from './Componentes/login/login.component';
import { AdminComponent } from './Componentes/admin/admin.component';
import { AuthGuard } from './guards/auth.guard';
import { UsuarioComponent } from './Componentes/usuario/usuario.component';

export const routes: Routes = [

  { path: '', component: HomeComponent }, // ← Página principal

  { path: 'registro', component: RegistroUsuarioComponent },

  { path: 'login', component: LoginComponent },

  { path: 'admin', component: AdminComponent, canActivate: [AuthGuard] },
  { path: 'usuario', component: UsuarioComponent }

];
