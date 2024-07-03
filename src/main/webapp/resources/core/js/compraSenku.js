let seccionActual = 0;
const secciones = document.querySelectorAll('.seccion');

function abrirModalCompra() {
    document.getElementById('modalCompra').style.display = 'flex';
    resetSecciones(); 
}

function cerrarModalCompra() {
    document.getElementById('modalCompra').style.display = 'none';
    resetSecciones();
    resetFormularioCompra();
    resetResumenCompra();
}


function abrirSeccionCompra(tirada, precio) {
    const datosCompra = document.getElementById('datosCompra');
    datosCompra.innerHTML = `<p>Tirada: ${tirada}</p><p>Precio: ${precio}</p>`;
    irASeccion(1);
}

function irAtras() {
    if (seccionActual > 0) {
        irASeccion(seccionActual - 1);
    }
}

function irASeccion(index) {
    secciones[seccionActual].style.display = 'none';
    seccionActual = index;
    secciones[seccionActual].style.display = 'flex';

    if (seccionActual > 0) {
        document.querySelector('.flecha-atras').style.display = 'flex';
    } else {
        document.querySelector('.flecha-atras').style.display = 'none';
    }
}

function mostrarError(mensaje) {
    const errorDiv = document.getElementById('error');
    const errorP = errorDiv.querySelector('.error');
    errorP.textContent = mensaje;
}

function cerrarModalDeLimiteAlcanzado() {
    document.getElementById("modalLimite").style.display = "none";
}

function resetFormularioCompra() {
    document.getElementById('nombreTitular').value = '';
    document.getElementById('numeroTarjeta').value = '';
    document.getElementById('dni').value = '';
    document.getElementById('fechaCaducidad').value = '';
    document.getElementById('codigoSeguridad').value = '';
    mostrarError('');
}

function resetResumenCompra() {
    const resumenCompra = document.getElementById('resumenCompra');
    resumenCompra.innerHTML = '';
}
function procesarCompra(event) {
    event.preventDefault();
    const nombreTitular = document.getElementById('nombreTitular').value;
    const numeroTarjeta = document.getElementById('numeroTarjeta').value;
    const dni = document.getElementById('dni').value;
    const fechaCaducidad = document.getElementById('fechaCaducidad').value;
    const codigoSeguridad = document.getElementById('codigoSeguridad').value;

    // Validaciones
    if (!nombreTitular || !numeroTarjeta || !dni || !fechaCaducidad || !codigoSeguridad) {
        mostrarError('Completa todos los campos, por favor.');
        return;
    }

    if (numeroTarjeta.length !== 16) {
        mostrarError('El número de tarjeta debe tener 16 dígitos.');
        return;
    }

    if (dni.length !== 8) {
        mostrarError('El DNI debe tener 8 dígitos.');
        return;
    }

    if (codigoSeguridad.length !== 3) {
        mostrarError('El código de seguridad debe tener 3 dígitos.');
        return;
    }

    const [year, month] = fechaCaducidad.split('-');
    const fechaActual = new Date();
    const fechaExpiracion = new Date(year, month);

    if (fechaExpiracion <= fechaActual) {
        mostrarError('La fecha de caducidad no puede estar vencida.');
        return;
    }

    const resumenCompra = document.getElementById('resumenCompra');
    resumenCompra.innerHTML = `
        <p><strong>Movimientos:</strong> ${document.getElementById('datosCompra').children[0].innerText.split(': ')[1]}</p>
        <p><strong>Precio:</strong> ${document.getElementById('datosCompra').children[1].innerText.split(': ')[1]}</p>
        <p><strong>Nombre del titular:</strong> ${nombreTitular}</p>
        <p><strong>Número de tarjeta:</strong> ${numeroTarjeta}</p>
        <p><strong>DNI del titular:</strong> ${dni}</p>
        <p><strong>Fecha de caducidad:</strong> ${fechaCaducidad}</p>
        <p><strong>Código de seguridad:</strong> XXX</p>
    `;

    irASeccion(2);
    mostrarError(''); // Limpiar errores al cerrar el modal

}
function cerrarModalCompra() {
    document.getElementById('modalCompra').style.display = 'none';
    resetSecciones();
    resetFormularioCompra();
    resetResumenCompra();
}

function aceptarCompra() {
    /*alert('Compra aceptada. ¡Gracias por tu compra!');*/
    cerrarModalCompra();
    cerrarModalDeLimiteAlcanzado();
    resetFormularioCompra();
    resetResumenCompra();
}

function resetSecciones() {
    seccionActual = 0;
    secciones.forEach((seccion, index) => {
        seccion.style.display = index === 0 ? 'flex' : 'none';
    });
    document.querySelector('.flecha-atras').style.display = 'none';
    mostrarError(''); // Limpiar errores al cerrar el modal
}
