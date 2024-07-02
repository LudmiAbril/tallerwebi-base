import { start, stop } from './cronometro.js';

$(document).ready(function () {
    $('.carrusel').slick({
        infinite: true,
        slidesToShow: 3,
        slidesToScroll: 1,
        autoplay: true,
        autoplaySpeed: 2000,
    });
    var tiempo = false;
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
                    
                    // CLICKS CASILLEROS
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
                                 start();
    }

    setInterval(function () {
        let tiempoLimite = "01:00"; // Ejemplo de tiempo límite
        verificarTiempo(tiempoLimite);
        console.log("Verificando tiempo: hora actual=" + obtenerHoraActual() + ", tiempo límite=" + tiempoLimite);
    }, 60000);

    function obtenerHoraActual() {
        var tiempoActual = new Date();
        var horas = tiempoActual.getHours();
        var minutos = tiempoActual.getMinutes();
        horas = horas < 10 ? "0" + horas : horas;
        minutos = minutos < 10 ? "0" + minutos : minutos;
        return horas + ":" + minutos;
    }

    function verificarTiempo(tiempoLimite) {
        let horaActual = obtenerHoraActual();
        if (horaActual === tiempoLimite) {
            tiempo = true;
            stop();
            $(".reloj").addClass("puntaje-limite");
            comprobarSiSeGano();
        }
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
            if (data.seGano || data.movimientosRealizados >= MovimientosMaxEnLaSesion || tiempo === true) {
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
    
    function addMinutes(time, minutes) {
        var timeParts = time.split(":");
        var hours = parseInt(timeParts[0]);
        var mins = parseInt(timeParts[1]);
    
        mins += minutes;
    
        if (mins >= 60) {
            hours += Math.floor(mins / 60);
            mins = mins % 60;
        }
    
        hours = hours % 24; 
    
        var newTime = ("0" + hours).slice(-2) + ":" + ("0" + mins).slice(-2);
        return newTime;
    }
    
    function mostrarMensajeMovimientos(respuesta) {
        if (respuesta.movimientosDisponibles === false) {
            document.getElementById("mensajeMovimientos").textContent = "No hay movimientos válidos disponibles.";
            document.getElementById("modalSenkuFinish").style.display = "block";
        } else if(tiempo==true) {
            document.getElementById("mensajeMovimientos").textContent = "No hay más tiempo.";
            document.getElementById("modalSenkuFinish").style.display = "block";
        }
    }
    verificarTiempo("01:00"); 
    actualizarTablero();
    setInterval(comprobarSiSeGano, 5000);
});
