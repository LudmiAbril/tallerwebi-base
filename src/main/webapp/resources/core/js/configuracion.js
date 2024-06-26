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

   
    document.getElementById('error-cant-numeros').innerText = '';

   
    localStorage.removeItem('cantidadPelotas');
    localStorage.removeItem('dimensionTablero');

    
    return true;
}


function guardarValorInput() {
    var cantidadPelotas = document.getElementById('cant-numeros').value;
    localStorage.setItem('cantidadPelotas', cantidadPelotas);

    var dimensionTablero = document.querySelector('input[name="dimensionTablero"]:checked').value;
    localStorage.setItem('dimensionTablero', dimensionTablero);
}

// Función para cargar el valor del input desde localStorage
function cargarValorInput() {
    var cantidadPelotas = localStorage.getItem('cantidadPelotas');
    if (cantidadPelotas !== null) {
        document.getElementById('cant-numeros').value = cantidadPelotas;
    }
    var dimensionTablero = localStorage.getItem('dimensionTablero');
    if (dimensionTablero !== null) {
        document.querySelector('input[name="dimensionTablero"][value="' + dimensionTablero + '"]').checked = true;
    }
}

// Cargar el valor del input cuando se abre el modal
document.getElementById('configuracionModal').addEventListener('show', function() {
    cargarValorInput();
});

document.querySelectorAll('input[name="dimensionTablero"]').forEach(function(element) {
    element.addEventListener('change', guardarValorInput);
});

