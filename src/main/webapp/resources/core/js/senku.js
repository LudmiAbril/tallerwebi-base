// Importar funciones de cronometro.js
import { start, stop } from './cronometro.js';

$(document).ready(function () {

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

            // Manejo de clics en los casilleros
            $('.casillero').click(function () {
                var x = $(this).data('x');
                var y = $(this).data('y');
                moverOSeleccionar(x, y);
            });
        }).fail(function () {
            $(".mensaje").text("Error al obtener el tablero.");
        });
    }

    function moverOSeleccionar(x, y) {
        $.post("moverOSeleccionar/" + x + "/" + y, function (data) {
            if (data.success) {
                actualizarTablero();
                $(".mov").text(data.contadorMovimientos);
            }
            $(".mensaje").text(data.mensaje);
            start();
        }).fail(function () {
            $(".mensaje").text("Error al mover o seleccionar.");
        });
    }

    function comprobarSiSeGano() {
        fetch("http://localhost:8080/spring/senkuGano", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            }
        })
            .then(response => {
                if (!response.ok) {
                    throw new Error('Error en la respuesta del servidor');
                }
                return response.json();
            })
            .then(data => {
                if (data.error) {
                    console.error('Error del servidor:', data.error);
                    return;
                }
                var MovimientosMaxEnLaSesion = data.maxMovimientos;
                if (data.seGano || data.movimientosRealizados >= MovimientosMaxEnLaSesion) {
                    document.getElementById('modalSenkuFinish').style.display = 'block';
                    document.querySelector('#modalSenkuFinish span').textContent = data.nombreJugador;
                    mostrarMensajeMovimientos(data);
                    stop();
                } else if (data.movimientosDisponibles === false) {
                    mostrarMensajeMovimientos(data);
                }
            })
            .catch(error => {
                console.error('Error en la solicitud:', error);
                $(".mensaje").text("Error en la solicitud: " + error.message);
            });
    }

    function mostrarMensajeMovimientos(respuesta) {
        if (respuesta.movimientosDisponibles === false) {
            document.getElementById("mensajeMovimientos").textContent = "No hay movimientos válidos disponibles.";
            document.getElementById("modalSenkuFinish").style.display = "block";
        } else if (respuesta.seGano) {
            document.getElementById("mensajeMovimientos").textContent = "¡Felicidades! Has ganado la partida.";
            document.getElementById("modalSenkuFinish").style.display = "block";
        }
    }

    actualizarTablero();
    setInterval(comprobarSiSeGano, 5000);
});
