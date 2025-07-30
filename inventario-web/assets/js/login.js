export async function login(correo, contrasena) { 
    const response = await fetch('http://localhost:8080/inventario-app/login', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({ correo, contrasena })
    });
    
    if(!response.ok) {
        throw new Error('credenciales inválidas');
    }
    return await response.json();
}

export function guardarUsuario(id, rol) {
    localStorage.setItem('usuarioId', id);
    localStorage.setItem('usuarioRol', rol);
}