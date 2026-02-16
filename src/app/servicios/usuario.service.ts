import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Usuario } from '../models/usuario';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class UsuarioService {

  private apiUrl = 'http://localhost:8083/api/usuarios';

  constructor(private http: HttpClient) {}

  registrar(usuario: Usuario): Observable<Usuario> {
    return this.http.post<Usuario>(
      `${this.apiUrl}/registrarUsuario`,
      usuario
    );
  }  

listarUsuarios() {
  return this.http.get<any[]>(`${this.apiUrl}/listar`);
}

eliminarUsuario(id: number) {
  return this.http.delete(`${this.apiUrl}/eliminar/${id}`, {
    responseType: 'text'
  });
}
actualizarUsuario(usuario: any) {
  return this.http.put(
    'http://localhost:8083/api/usuarios/actualizarUsuario',
    usuario,
    { responseType: 'text' }   // 👈 ESTA ES LA CLAVE
  );
}
}


