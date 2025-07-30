async function cargarMovimientos() {
    const usuarioActual = {
        id: localStorage.getItem("usuarioId"),
        rol: localStorage.getItem("usuarioRol")
    };
    document.getElementById("rolUsuario").textContent = `${usuarioActual.rol || "No definido"}`;
    const tipo = document.getElementById('tipoFiltro').value;
    let url = `http://localhost:8080/inventario-app/movimientos-inventario?usuarioId=${usuarioActual.id}`;
    if (tipo) {
        url = `http://localhost:8080/inventario-app/movimientos-inventario/tipo/${tipo}?usuarioId=${usuarioActual.id}`;
    }
    const res = await fetch(url);
    const movimientos = await res.json();

    function formatearFecha(fechaStr) {
        const fecha = new Date(fechaStr.replace(' ', 'T'));
        return fecha.toLocaleString('es-MX', {
            year: 'numeric',
            month: '2-digit',
            day: '2-digit',
            hour: '2-digit',
            minute: '2-digit',
            second: '2-digit'
        });
    }

    const tbody = document.getElementById('tablaMovimientos');
    tbody.innerHTML = '';
    movimientos.forEach(m => {
        const fila = `<tr>
            <td>${m.producto.nombre}</td>
            <td>${m.cantidad}</td>
            <td>${m.tipoMovimiento}</td>
            <td>${m.usuario.nombre}</td>
            <td>${formatearFecha(m.fechaHora.replace('T', ' '))}</td>
        </tr>`;
        tbody.innerHTML += fila;
    });
}

// Cargar al inicio
cargarMovimientos();

document.getElementById('tipoFiltro').addEventListener('change', cargarMovimientos);