import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { UsuarioService } from '../../servicios/usuario.service';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router'; // 👈 AÑADIDO


@Component({
  selector: 'app-admin',
  standalone: true,
  imports: [CommonModule, FormsModule],  
  templateUrl: './admin.component.html',
  styleUrls: ['./admin.component.css']
})
export class AdminComponent implements OnInit {

  usuarios: any[] = [];
  usuarioSeleccionado: any = null;
  modoEdicion = false;

  constructor(
    private usuarioService: UsuarioService,
    private router: Router // 👈 AÑADIDO
  ) {}

  ngOnInit(): void {
    this.cargarUsuarios();
  }

  // 👇 AÑADIDO (para volver al inicio correctamente)
  irInicio() {
    this.router.navigate(['/']);
  }

  cargarUsuarios() {
    this.usuarioService.listarUsuarios().subscribe({
      next: (data) => {
        this.usuarios = data;
      },
      error: (err) => console.error(err)
    });
  }

  eliminar(id: number) {
    if (confirm('¿Seguro que quieres eliminar este usuario?')) {
      this.usuarioService.eliminarUsuario(id).subscribe(() => {
        this.cargarUsuarios();
      });
    }
  }

  editarUsuario(usuario: any) {
    this.usuarioSeleccionado = { ...usuario };
    this.modoEdicion = true;
  }

  guardarCambios() {
    console.log(this.usuarioSeleccionado);

    this.usuarioService.actualizarUsuario(this.usuarioSeleccionado)
      .subscribe({
        next: () => {
          alert("Usuario actualizado correctamente");
          this.modoEdicion = false;
          this.cargarUsuarios();
        },
        error: (err) => {
          console.error("Error al actualizar:", err);
          alert("Error al actualizar usuario");
        }
      });
  }

}
