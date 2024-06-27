/*
// Función para cerrar el modal
function closeModal(modalId) {
    var modal = document.getElementById(modalId);
    modal.style.display = "none";
}

// Función para validar el formulario
function validarFormulario() {
    var dimensionCarton = parseInt(document.querySelector('input[name="dimensionCarton"]:checked').value);
    var cantidadPelotas = parseInt(document.getElementById('cant-numeros').value);

    if (isNaN(cantidadPelotas) || cantidadPelotas < dimensionCarton * dimensionCarton) {
        var errorMensaje = 'Ingrese al menos ' + dimensionCarton * dimensionCarton + ' números.';
        document.getElementById('error-cant-numeros').innerText = errorMensaje;
        return false;
    }

    // Limpiar mensaje de error si pasa la validación
    document.getElementById('error-cant-numeros').innerText = '';

    // Limpiar localStorage al enviar el formulario
    localStorage.removeItem('cantidadPelotas');

    // Si la validación pasa, se envía el formulario
    return true;
}

// Función para guardar el valor del input en localStorage
function guardarValorInput() {
    var cantidadPelotas = document.getElementById('cant-numeros').value;
    localStorage.setItem('cantidadPelotas', cantidadPelotas);
}

// Función para cargar el valor del input desde localStorage
function cargarValorInput() {
    var cantidadPelotas = localStorage.getItem('cantidadPelotas');
    if (cantidadPelotas !== null) {
        document.getElementById('cant-numeros').value = cantidadPelotas;
    }
}

// Cargar el valor del input cuando se abre el modal
document.getElementById('configuracionModal').addEventListener('show', function() {
    cargarValorInput();
});
*/

// Función para cerrar el modal
function closeModal(modalId) {
    var modal = document.getElementById(modalId);
    modal.style.display = "none";
}

// Función para validar el formulario
function validarFormulario() {
    var dimensionCarton = parseInt(document.querySelector('input[name="dimensionCarton"]:checked').value);
    var cantidadPelotas = parseInt(document.getElementById('cant-numeros').value);

    if (isNaN(cantidadPelotas) || cantidadPelotas < dimensionCarton * dimensionCarton) {
        var errorMensaje = 'Ingrese al menos ' + dimensionCarton * dimensionCarton + ' numeros.';
        document.getElementById('mensajeError').innerText = errorMensaje;
        return false;
    }

    // Limpiar mensaje de error si pasa la validación
    document.getElementById('mensajeError').innerText = '';

    // Limpiar localStorage al enviar el formulario
    localStorage.removeItem('cantidadPelotas');

    // Si la validación pasa, se envía el formulario
    return true;
}

// Función para guardar el valor del input en localStorage
function guardarValorInput() {
    var cantidadPelotas = document.getElementById('cant-numeros').value;
    localStorage.setItem('cantidadPelotas', cantidadPelotas);
}

// Función para cargar el valor del input desde localStorage
function cargarValorInput() {
    var cantidadPelotas = localStorage.getItem('cantidadPelotas');
    if (cantidadPelotas !== null) {
        document.getElementById('cant-numeros').value = cantidadPelotas;
    }
}

// Cargar el valor del input cuando se abre el modal
document.getElementById('configuracionModal').addEventListener('show', function() {
    cargarValorInput();
});

