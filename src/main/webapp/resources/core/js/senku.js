$(document).ready(function () {
    //TABLERO
    function actualizarTablero() {
        $.get("obtenerTablero", function (data) {
            var tableroHtml = '';
            for (var i = 0; i < data.tablero.casilleros.length; i++) {
                tableroHtml += '<div class="fila">';
                for (var j = 0; j < data.tablero.casilleros[i].length; j++) {
                    var color = data.tablero.casilleros[i][j].ocupado ? 'ficha' : 'vacio';
                    tableroHtml += '<div class="casillero ' + color + '" data-x="' + i + '" data-y="' + j + '"></div>';
                }
                tableroHtml += '</div>';
            }
            $('.tablero').html(tableroHtml);

            //CLICKS CASILLEROS
            $('.casillero').click(function () {
                var x = $(this).data('x');
                var y = $(this).data('y');
                moverOSeleccionar(x, y);
            });
        });
    }

    function moverOSeleccionar(x, y) {
        $.post("moverOSeleccionar/" + x + "/" + y, function (data) {
            if (data.success) {
                actualizarTablero();
                $(".mov").text(data.contadorMovimientos);
            }
            $(".mensaje").text(data.mensaje);
        });
    }

    function comprobarSiSeGano() {
        fetch('/senku/gano', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            credentials: 'include'
        })
            .then(response => response.json())
            .then(data => {
                if (data.seGano) {
                    document.getElementById('modalSenkuFinish').style.display = 'block';
                    document.querySelector('#modalSenkuFinish span').innerText = data.nombreJugador;
                    mostrarMensajeMovimientos(data); 
                }
            })
            .catch(error => console.error('Error:', error));
    }
    

    actualizarTablero();
    setInterval(comprobarSiSeGano, 5000);
});

function mostrarMensajeMovimientos(respuesta) {
    if (respuesta.movimientosDisponibles === false) {
        document.getElementById("mensajeMovimientos").textContent = "No hay movimientos válidos disponibles.";
        document.getElementById("modalSenkuFinish").style.display = "block";
    }
}