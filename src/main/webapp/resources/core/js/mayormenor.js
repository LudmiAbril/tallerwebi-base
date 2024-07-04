$(document).ready(function () {
    let cartaActual = null;
    let aciertos = 0;

    function agregarCarta(contenedor, nombreCarta, jugador) {
        //contenedor.append(
        contenedor.html(
            "<img src='./imgStatic/cartas/" +
            nombreCarta +
            ".png' width='140px' class='carta nueva-carta-" +
            jugador +
            "'>"
        );
    }
    function agregarDosCartas(contenedor, cartaAnterior, cartaNueva, jugador) {
            contenedor.html(
                "<img src='./imgStatic/cartas/" +
                cartaAnterior +
                ".png' width='140px' class='carta carta-anterior-" +
                jugador +
                "'>" +
                "<img src='./imgStatic/cartas/" +
                cartaNueva +
                ".png' width='140px' class='carta nueva-carta-" +
                jugador +
                "'>"
            );
        }
        //seagrego
function iniciarPartida() {
    $.get("ComenzarMayorMenor", function (data) {
        cartaActual = data.cartaInicial;
        aciertos = 0;
        actualizarAciertos(aciertos);
        $("#nombre").text(data.jugadorActual);
        agregarCarta(
            $("#cartasJugador"),
            cartaActual.simbolo + "_" + cartaActual.palo,
            "jugador"
        );
    });
}
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

            /*agregarCarta(
                $("#cartasJugador"),
                cartaNueva.simbolo + "_" + cartaNueva.palo,
                "jugador"
            );*/
            agregarDosCartas(
                            $("#cartasJugador"),
                            cartaActual.simbolo + "_" + cartaActual.palo,
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
        $("#resultadoPartida").text("Aciertos:" + aciertos);
    }

    $("#btnReiniciar").click(function () {
        iniciarPartida();
    });
    iniciarPartida();
});
