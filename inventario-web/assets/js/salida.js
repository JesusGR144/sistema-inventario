const usuarioActual = {
    id: localStorage.getItem("usuarioId"),
    rol: localStorage.getItem("usuarioRol")
};

const apiUrl = "http://localhost:8080/inventario-app";
const tablaBody = document.querySelector("#tablaProductos tbody");
const mensaje = document.getElementById("mensaje");

// Mostrar el rol en el frontend
document.getElementById("rolUsuario").textContent = `${usuarioActual.rol || "No definido"}`;

let productosDisponibles = [];

const mostrarMensaje = msg => {
    mensaje.textContent = msg;
    mensaje.style.display = "block";
    setTimeout(() => { mensaje.style.display = "none"; mensaje.textContent = ""; }, 3000);
};

const cargarProductosParaSalida = async () => {
    try {
        const res = await fetch(`${apiUrl}/productos/salidas?usuarioId=${usuarioActual.id}`);
        if (res.ok) {
            productosDisponibles = await res.json();
            renderTabla();
            llenarSelectProductos();
        } else {
            const error = await res.json();
            mostrarMensaje(error.error || "Error al cargar productos");
        }
    } catch (error) {
        mostrarMensaje("Error de conexión");
    }
};

const renderTabla = () => {
    tablaBody.innerHTML = productosDisponibles.map(({ idProducto, nombre, cantidadActual, precio }) => `
        <tr>
            <td>${idProducto}</td>
            <td>${nombre || "Sin descripción"}</td>
            <td>${cantidadActual}</td>
            <td>$${precio}</td>
        </tr>
    `).join("");
};

const llenarSelectProductos = () => {
    const select = document.getElementById("productoSelect");
    select.innerHTML = '<option value="">Selecciona un producto</option>';

    productosDisponibles
        .filter(p => p.cantidadActual > 0)
        .forEach(producto => {
            const option = document.createElement("option");
            option.value = producto.idProducto;
            option.textContent = `${producto.nombre || "Sin descripción"} (Disponible: ${producto.cantidadActual})`;
            select.appendChild(option);
        });
};

document.getElementById("salidaForm").onsubmit = async e => {
    e.preventDefault();

    const productoId = document.getElementById("productoSelect").value;
    const cantidad = parseInt(document.getElementById("cantidadSalida").value);

    if (!productoId) {
        mostrarMensaje("Selecciona un producto");
        return;
    }

    try {
        const res = await fetch(`${apiUrl}/productos/${productoId}/salida?usuarioId=${usuarioActual.id}&cantidad=${cantidad}`, {
            method: "PUT"
        });

        const result = await res.json();

        if (res.ok) {
            mostrarMensaje(result.mensaje || "Salida registrada exitosamente");
            document.getElementById("salidaForm").reset();
            cargarProductosParaSalida(); // Recargar datos
        } else {
            mostrarMensaje(result.error || "Error al registrar salida");
        }
    } catch (error) {
        mostrarMensaje("Error de conexión");
    }
};

// Cargar productos al iniciar
cargarProductosParaSalida();