$(document).ready(function () {
    let cartaActual = null;
    let aciertos = 0;

    function agregarCarta(contenedor, nombreCarta, jugador) {
        contenedor.append(
            "<img src='./imgStatic/cartas/" +
            nombreCarta +
            ".png' width='140px' class='carta nueva-carta-" +
            jugador +
            "'>"
        );
    }
    $.get("ComenzarMayorMenor", function (data) {
        cartaActual = data.cartaInicial;
        $("#nombre").text(data.jugadorActual);
        agregarCarta(
            $("#cartasJugador"),
            cartaActual.simbolo + "_" + cartaActual.palo,
            "jugador"
        );
    });
    $("#btnMayor").click(function () {
        evaluarCarta("MAYOR");
    });
    $("#btnMenor").click(function () {
        evaluarCarta("MENOR");
    });
    function evaluarCarta(eleccion) {
        $.get("comparar-carta", { eleccion: eleccion }, function (data) {
            let cartaNueva = data.cartaNueva;
            let acierto = data.acierto;
            aciertos = data.aciertos;

            agregarCarta(
                $("#cartasJugador"),
                cartaNueva.simbolo + "_" + cartaNueva.palo,
                "jugador"
            );

            if (acierto) {
                cartaActual = cartaNueva;
                actualizarAciertos(aciertos);
            } else {
                finalizarPartida(aciertos);
            }
        });
    }

    function actualizarAciertos(aciertos) {
        $("#aciertos").text(aciertos);
    }
    function finalizarPartida(aciertos) {
        $("#modalFinPartida").show();
        $("#resultadoPartida").text("Partida finalizada. Aciertos: " + aciertos);
    }

    $("#btnReiniciar").click(function () {
        location.reload();
    });
});
