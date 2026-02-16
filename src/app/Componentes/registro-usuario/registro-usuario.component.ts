import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router'; // 👈 AÑADIDO RouterLink
import { UsuarioService } from '../../servicios/usuario.service';
import { Usuario } from '../../models/usuario';

@Component({
  selector: 'app-registro-usuario',
  standalone: true,
  imports: [FormsModule, RouterLink], // 👈 AÑADIDO AQUÍ
  templateUrl: './registro-usuario.component.html',
  styleUrls: ['./registro-usuario.component.css']
})
export class RegistroUsuarioComponent {

  nombre = '';
  apellido1 = '';
  apellido2 = '';
  fechaNac = '';
  movil = '';
  email = '';
  rol = '';
  password = '';
  password2 = '';

  constructor(
    private usuarioService: UsuarioService,
    private router: Router
  ) {}

  registrar() {
    if (this.password !== this.password2) {
      alert('Las contraseñas no coinciden');
      return;
    }

    const usuario: Usuario = {
      nombreUsuario: this.nombre,
      apellidosUsuario: this.apellido1 + ' ' + this.apellido2,
      fchNacUsu: this.fechaNac,
      movil: this.movil,
      correoElectronico: this.email,
      tipoUsuario: this.rol,
      contrasena: this.password
    };

    this.usuarioService.registrar(usuario).subscribe({
      next: () => {
        alert('Usuario registrado correctamente');
        this.router.navigate(['/login']);
      },
      error: (err) => {
        console.error(err);
        alert('Error al registrar usuario');
      }
    });
  }
}
