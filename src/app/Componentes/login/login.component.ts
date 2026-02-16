import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { AuthService } from '../../servicios/auth.service';
import { Router, RouterLink } from '@angular/router';
import { RouterModule } from '@angular/router';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [FormsModule, RouterLink],
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {

  email = '';
  password = '';

  constructor(
    private authService: AuthService,
    private router: Router
  ) {}

  login() {

    this.authService.login(this.email, this.password).subscribe({
      next: (response) => {

        if (response.token) {

          this.authService.guardarToken(response.token);

          // Extraer email del token
          const payload = JSON.parse(atob(response.token.split('.')[1]));
          const emailToken = payload.sub;

          // Obtener detalles del usuario
          this.authService.obtenerDetalles(emailToken).subscribe(user => {

            if (user.tipoUsuario === 'ADMIN') {
              this.router.navigate(['/admin']);
            } else {
              this.router.navigate(['/usuario']);
            }

          });

        } else {
          alert('Credenciales incorrectas');
        }

      },
      error: () => {
        alert('Error al iniciar sesión');
      }
    });
  }
}
