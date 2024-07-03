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

function abrirSeccionCompra(texto, precio, tirada) {
    const datosCompra = document.getElementById('datosCompra');
    datosCompra.innerHTML = `<p>Tirada: ${texto}</p><p>Precio: ${precio}</p>`;
    document.getElementById("tiradaComprada").value = tirada;
    document.getElementById("precioTirada").value=precio;
    tiradaHidden = document.getElementById("tiradaComprada").value;
    precioHidden = document.getElementById("precioTirada").value;
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
    6
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

    if (nombreTitular == null) {
        mostrarError("El titular no puede estar vacío");
        return;
    } else {
        nombreTitularValido = true;
    }

    // Validaciones
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

        num = 15;
        fetch(`http://localhost:8080/spring/reiniciarTirada/${tirada}/${precio}`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({ num: num,
                tirada: tirada,
                precio: precio
             })//num
        })
            .then(response => response.json())
            .then(data => {
                // Manejo de la respuesta del servidor
                console.log("Solicitud POST enviada correctamente", data);
                console.log(data.message)
                console.log(data.seGuardo)
                //  const resumenCompra = document.getElementById('resumenCompra');
                //  resumenCompra.innerHTML = `
                //      <p><strong>Tirada:</strong> ${document.getElementById('datosCompra').children[0].innerText.split(': ')[1]}</p>
                //      <p><strong>Precio:</strong> ${document.getElementById('datosCompra').children[1].innerText.split(': ')[1]}</p>
                //      <p><strong>Nombre del titular:</strong> ${nombreTitular}</p>
                //      <p><strong>Número de tarjeta:</strong> ${numeroTarjeta}</p>
                //      <p><strong>DNI del titular:</strong> ${dni}</p>
                //      <p><strong>Fecha de caducidad:</strong> ${fechaCaducidad}</p>
                //      <p><strong>Código de seguridad:</strong> XXX</p>
                //  `;

                //  irASeccion(2);
            });
    }




}



function aceptarCompra() {
    // alert('Compra aceptada. ¡Gracias por tu compra!');
    cerrarModalCompra();
}

function resetSecciones() {
    seccionActual = 0;
    secciones.forEach((seccion, index) => {
        seccion.style.display = index === 0 ? 'flex' : 'none';
    });
    document.querySelector('.flecha-atras').style.display = 'none';
    mostrarError(''); // Limpiar errores al cerrar el modal
}
