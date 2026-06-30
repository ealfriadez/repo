import { HttpInterceptorFn, HttpErrorResponse } from '@angular/common/http';
import { inject } from '@angular/core';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Router } from '@angular/router';
import { catchError, throwError } from 'rxjs';
import { StorageService } from '../services/storage.service';

export const authInterceptor: HttpInterceptorFn = (req, next) => {
  const storage = inject(StorageService);
  const snackBar = inject(MatSnackBar);
  const router = inject(Router);

  const token = storage.getToken();
  const authReq = token
    ? req.clone({ setHeaders: { Authorization: `Bearer ${token}` } })
    : req;

  return next(authReq).pipe(
    catchError((error: HttpErrorResponse) => {

      console.log("Error status:", error.status);
      console.log("Error completo:", error);

      let message = 'Ha ocurrido un error inesperado';

      const backendMessage = error.error?.detail || error.error?.message;

      switch (error.status) {
        case 400:
          message = backendMessage || 'Datos invalidos';
          break;
        case 401:
          message = backendMessage || 'No autorizado';
          if (storage.getToken()) {
            storage.clearSession();
            router.navigate(['/login']);
          }
          break;
        case 403:
          message = backendMessage || 'No tienes permisos para realizar esta accion';
          break;
        case 404:
          message = backendMessage || 'Recurso no encontrado';
          break;
        case 409:
          message = backendMessage || 'Conflicto con los datos enviados';
          break;
        case 413:
          message = 'El archivo es demasiado pesado, intenta con uno más pequeño. (máx. 5MB)';
          break;
        case 500:
          message = 'Error del servidor. Intenta mas tarde';
          break;
      }

      snackBar.open(message, 'Cerrar');
      return throwError(() => error);
    })
  );
};
