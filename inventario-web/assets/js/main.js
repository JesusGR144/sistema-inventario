import { login, guardarUsuario } from './login.js';

document.getElementById('loginForm').addEventListener('submit', async (e) => {
    e.preventDefault();
    const correo = document.getElementById('correo').value;
    const contra = document.getElementById('contrasena').value;
    const mensaje = document.getElementById('mensaje');
    mensaje.textContent = '';

    try {
        const data = await login(correo, contra);
        guardarUsuario(data.id, data.rol);
        mensaje.textContent = 'Login exitoso';
        mensaje.classList.add('text-green-500');
        setTimeout(() => {
            window.location.href = 'home.html';
            mensaje.classList.remove('text-green-500');
        }, 2000);
    }catch(error) {
        mensaje.textContent = `Error al iniciar sesión ${error.message}`;
        mensaje.classList.add('text-red-500');
        setTimeout(() => {
            mensaje.textContent = '';
            mensaje.classList.remove('text-red-500');
        }, 2000);
    }
});