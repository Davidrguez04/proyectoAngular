import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { AuthService } from '../../servicios/auth.service';

@Component({
  selector: 'app-usuario',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './usuario.component.html',
  styleUrls: ['./usuario.component.css']
})
export class UsuarioComponent implements OnInit {

  usuario: any;

  constructor(
    private authService: AuthService,
    private router: Router
  ) {}

  ngOnInit(): void {
    const token = this.authService.obtenerToken();

    if (token) {
      const payload = JSON.parse(atob(token.split('.')[1]));
      const email = payload.sub;

      this.authService.obtenerDetalles(email).subscribe(data => {
        this.usuario = data;
      });
    }
  }

  cerrarSesion() {
    this.authService.logout();
    this.router.navigate(['/']);
  }

}
