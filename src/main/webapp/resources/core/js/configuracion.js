// Función para cerrar el modal
function closeModal(modalId) {
    var modal = document.getElementById(modalId);
    modal.style.display = "none";
}

// Valida el formulario
function validarFormulario() {
    var dimensionCarton = parseInt(document.querySelector('input[name="dimensionCarton"]:checked').value);
    var cantidadPelotas = parseInt(document.getElementById('cant-numeros').value);

    if (isNaN(cantidadPelotas) || cantidadPelotas < dimensionCarton * dimensionCarton) {
        var errorMensaje = 'Ingrese al menos ' + dimensionCarton * dimensionCarton + ' números.';
        document.getElementById('mensajeError').innerText = errorMensaje;
        return false;
    }

    // Limpiar mensaje de error
    document.getElementById('mensajeError').innerText = '';

    // Limpiar localStorage del formulario
    localStorage.removeItem('cantidadPelotas');
    localStorage.removeItem('dimensionTablero');
    localStorage.removeItem('maxMovimientos');

    return true;
}

// Guardar el valor de los inputs
function guardarValorInput() {
    var cantidadPelotas = document.getElementById('cant-numeros').value;
    localStorage.setItem('cantidadPelotas', cantidadPelotas);
}
function guardarDimensionTablero() {
    var dimensionTablero = document.querySelector('input[name="dimensionTablero"]:checked').value;
    localStorage.setItem('dimensionTablero', dimensionTablero);
}
function guardarMaxMovimientos() {
    var maxMovimientos = document.getElementById('max-movimientos').value;
    localStorage.setItem('maxMovimientos', maxMovimientos);
}

// Funciones para cargar datos
function cargarValorInput() {
    var cantidadPelotas = localStorage.getItem('cantidadPelotas');
    if (cantidadPelotas !== null) {
        document.getElementById('cant-numeros').value = cantidadPelotas;
    }
}
function cargarDimensionTablero() {
    var dimensionTablero = localStorage.getItem('dimensionTablero');
    if (dimensionTablero !== null) {
        document.querySelector('input[name="dimensionTablero"][value="' + dimensionTablero + '"]').checked = true;
    }
}
function cargarMaxMovimientos() {
    var maxMovimientos = localStorage.getItem('maxMovimientos');
    if (maxMovimientos !== null) {
        document.getElementById('max-movimientos').value = maxMovimientos;
    }
}

// Cargar datos del localStorage cuando se muestra el modal
document.getElementById('configuracionModal').addEventListener('show', function() {
    cargarValorInput();
    cargarDimensionTablero();
    cargarMaxMovimientos();
});


// Guardar los valores si cambian
document.getElementById('cant-numeros').addEventListener('input', guardarValorInput);
document.querySelectorAll('input[name="dimensionTablero"]').forEach(function(input) {
    input.addEventListener('change', guardarDimensionTablero);
});
document.getElementById('max-movimientos').addEventListener('input', guardarMaxMovimientos);

