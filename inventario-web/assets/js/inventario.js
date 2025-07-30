const usuarioActual = {
    id: localStorage.getItem("usuarioId"),
    rol: localStorage.getItem("usuarioRol")
};

const apiUrl = "http://localhost:8080/inventario-app";
const tablaBody = document.querySelector("#tablaProductos tbody");
const mensaje = document.getElementById("mensaje");

// Mostrar el rol en el frontend
document.getElementById("rolUsuario").textContent = ` ${usuarioActual.rol || "No definido"}`;

let productos = [];
let productoEntradaId = null;

const mostrar = (id, show = true) => document.getElementById(id).style.display = show ? "block" : "none";
const getValue = id => document.getElementById(id).value;

const mostrarMensaje = msg => {
    mensaje.textContent = msg;
    mensaje.style.display = "block";
    setTimeout(() => {mensaje.textContent = ""; mensaje.style.display = "none"}, 3000);
};

const cargarProductos = async (filtro = "") => {
    const urls = {
        "activos": `${apiUrl}/productos/activos?usuarioId=${usuarioActual.id}`,
        "inactivos": `${apiUrl}/productos/inactivos?usuarioId=${usuarioActual.id}`,
        "": `${apiUrl}/productos?usuarioId=${usuarioActual.id}`
    };
    const res = await fetch(urls[filtro]);
    productos = await res.json();
    renderTabla();
};

const renderTabla = () => {
    tablaBody.innerHTML = productos.map(({ id, idProducto, talla, color, tipo, precio, cantidadActual, activo }) => `
        <tr>
            <td>${idProducto || id}</td>                               
            <td>${talla || ""}</td>
            <td>${color || ""}</td>
            <td class="truncate">${tipo || ""}</td>
            <td>$ ${precio}</td>
            <td>${cantidadActual}</td>
            <td>
                <input type="checkbox" ${activo ? "checked" : ""} 
                       onchange="toggleProductoEstado(${idProducto || id}, this)" 
                       class="checkbox checkbox-info" />
            </td>
            <td>
                <button id="btn-entrada-${idProducto || id}" 
                        onclick="mostrarEntradaForm(${idProducto || id})" 
                        ${!activo ? "disabled" : ""}>
                    <img width="30" height="30" src="https://img.icons8.com/color/48/add--v1.png" alt="add--v1"/>
                </button>
            </td>
        </tr>
    `).join("");
};

// Filtro desplegable
document.getElementById("filtroProductos").addEventListener("change", e => {
    cargarProductos(e.target.value);
});

document.getElementById("cancelarEntrada").addEventListener("click", () => {
    mostrar("formEntrada", false);
    productoEntradaId = null;
});

document.getElementById("agregarProductoForm").onsubmit = async e => {
    e.preventDefault();
    const data = {
        // nombre: getValue("nombre"),
        talla: getValue("talla"),
        color: getValue("color"),
        tipo: getValue("tipo"),
        precio: getValue("precio"),
        cantidadActual: 0
    };
    const res = await fetch(`${apiUrl}/productos?usuarioId=${usuarioActual.id}`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(data)
    });
    const result = await res.json();
    mostrarMensaje(result.mensaje || result.error);
    document.getElementById("agregarProductoForm").reset();
    cargarProductos(document.getElementById("filtroProductos").value);    
};

window.mostrarEntradaForm = id => {
    productoEntradaId = id;
    mostrar("formEntrada");
};

document.getElementById("entradaForm").onsubmit = async e => {
    e.preventDefault();
    const cantidad = getValue("cantidadEntrada");
    const res = await fetch(`${apiUrl}/productos/${productoEntradaId}/entrada?usuarioId=${usuarioActual.id}&cantidad=${cantidad}`, {
        method: "PUT"
    });
    const result = await res.json();
    mostrarMensaje(result.mensaje || result.error);
    mostrar("formEntrada", false);
    document.getElementById("entradaForm").reset();
    cargarProductos(document.getElementById("filtroProductos").value);
};


window.toggleProductoEstado = async (id, checkbox) => {
    const botonEntrada = document.getElementById(`btn-entrada-${id}`);
    
    if (checkbox.checked) {
        // Reactivar producto
        const res = await fetch(`${apiUrl}/productos/${id}/reactivar?usuarioId=${usuarioActual.id}`, { method: "PUT" });
        const result = await res.json();
        
        if (res.ok) {
            botonEntrada.disabled = false;
            mostrarMensaje(result.mensaje || "Producto reactivado");
        } else {
            checkbox.checked = false; // Revertir checkbox si falla
            mostrarMensaje(result.error || "Error al reactivar");
        }
    } else {
        // Dar de baja producto
        const res = await fetch(`${apiUrl}/productos/${id}/baja?usuarioId=${usuarioActual.id}`, { method: "PUT" });
        const result = await res.json();
        
        if (res.ok) {
            botonEntrada.disabled = true;
            mostrarMensaje(result.mensaje || "Producto dado de baja");
        } else {
            checkbox.checked = true; // Revertir checkbox si falla
            mostrarMensaje(result.error || "Error al dar de baja");
        }
    }
};

cargarProductos();