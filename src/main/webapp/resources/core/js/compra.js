let seccionActual = 0;
const secciones = document.querySelectorAll('.seccion');

function abrirModalCompra() {
    document.getElementById('modalCompra').style.display = 'flex';
    document.querySelector('.flecha-atras').style.display = 'none';
}

function cerrarModalCompra() {
    document.getElementById('modalCompra').style.display = 'none';
    resetSecciones();
}
function resetSecciones() {
    seccionActual = 0;
    secciones.forEach((seccion, index) => {
        seccion.style.display = index === 0 ? 'flex' : 'none';
    });
    document.querySelector('.flecha-atras').style.display = 'none';
    mostrarError(''); // Limpiar errores al cerrar el modal
}
function abrirSeccionCompra(texto, precio, tirada) {
    const datosCompra = document.getElementById('datosCompra');
    datosCompra.innerHTML = `<p>Tirada: ${texto}</p><p>Precio: ${precio}</p>`;
    document.getElementById("tiradaComprada").value = tirada;
    document.getElementById("precioTirada").value = precio;
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

function procesarCompra(event) {
    event.preventDefault();
    let numeroTarjetaValida = false;
    let nombreTitularValido = false;
    let dniValido = false;
    let fechaCaducidadValida = false;
    let codigoSeguridadValido = false;

    const nombreTitular = document.getElementById('nombreTitular').value;
    const numeroTarjeta = document.getElementById('numeroTarjeta').value;
    const dni = document.getElementById('dni').value;
    const fechaCaducidad = document.getElementById('fechaCaducidad').value;
    const codigoSeguridad = document.getElementById('codigoSeguridad').value;

    if (nombreTitular === "") {
        mostrarError("El titular no puede estar vacío");
        return;
    } else {
        nombreTitularValido = true;
    }

    if (numeroTarjeta.length !== 16) {
        mostrarError('El número de tarjeta debe tener 16 dígitos.');
        return;
    } else {
        numeroTarjetaValida = true;
    }

    if (dni.length !== 8) {
        mostrarError('El DNI debe tener 8 dígitos.');
        return;
    } else {
        dniValido = true;
    }

    if (codigoSeguridad.length !== 3) {
        mostrarError('El código de seguridad debe tener 3 dígitos.');
        return;
    } else {
        codigoSeguridadValido = true;
    }

    const [year, month] = fechaCaducidad.split('-');
    const fechaActual = new Date();
    const fechaExpiracion = new Date(year, month);

    if (fechaExpiracion <= fechaActual) {
        mostrarError('La fecha de caducidad no puede estar vencida.');
        return;
    } else {
        fechaCaducidadValida = true;
    }

    if (numeroTarjetaValida && nombreTitularValido && dniValido && fechaCaducidadValida && codigoSeguridadValido) {
        const tirada = document.getElementById("tiradaComprada").value;
        const precio = document.getElementById("precioTirada").value;

        fetch(`http://localhost:8080/spring/reiniciarTirada/${tirada}/${precio}`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({})
        })
        .then(response => response.json())
        .then(data => {
            if (data.seGuardo) {
                console.log("Compra realizada con éxito", data);
                const resumenCompra = document.getElementById('resumenCompra');
    resumenCompra.innerHTML = `
        <p><strong>Tirada:</strong> ${document.getElementById('datosCompra').children[0].innerText.split(': ')[1]}</p>
        <p><strong>Precio:</strong> ${document.getElementById('datosCompra').children[1].innerText.split(': ')[1]}</p>
        <p><strong>Nombre del titular:</strong> ${nombreTitular}</p>
        <p><strong>Número de tarjeta:</strong> ${numeroTarjeta}</p>
        <p><strong>DNI del titular:</strong> ${dni}</p>
        <p><strong>Fecha de caducidad:</strong> ${fechaCaducidad}</p>
        <p><strong>Código de seguridad:</strong> XXX</p>
    `;
    irASeccion(2);
               
               
            } else {
                mostrarError('Hubo un problema al realizar la compra.');
            }
        })
        .catch(error => {
            console.error("Error al realizar la compra:", error);
            mostrarError('Hubo un problema al realizar la compra.');
        });
    }
}

 
 
function aceptarCompra() {
    cerrarModalLimiteTirada();
    cerrarModalCompra();
    obtenerNuevoNumero();
}
 
function cerrarModalLimiteTirada(){
    document.getElementById('modalLimite').style.display = 'none';
}
 
function obtenerNuevoNumero() {
    fetch(`http://localhost:8080/spring/obtenerNuevoNumero`)
    .then(response => response.json())
    .then(data => {
        if (!data.limiteAlcanzado) {
            console.log("Nuevo número entregado:", data.nuevoNumero);
           
            document.getElementById('numeroActual').textContent = data.nuevoNumero;
        } else {
            console.log("Límite de tiradas alcanzado.");
            alert('Límite de tiradas alcanzado.');
        }
    })
    .catch(error => {
        console.error("Error al obtener nuevo número:", error);
    });
}
