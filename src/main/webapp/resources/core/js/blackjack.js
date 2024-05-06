$(document).ready(function () {
  // TRAER LOS DATOS INICIALES
  $.get("comenzar", function (data) {
    data.cartasCasa.forEach(function (carta) {
      let nombreCarta = carta.simbolo + "_" + carta.palo;
      $("#cartasCasa").append(
        "<img src='./img/cartas/" +
          nombreCarta +
          ".png' width='140px' class='carta nueva-carta-casa'>"
      );
    });

    data.cartasJugador.forEach(function (carta) {
      let nombreCarta = carta.simbolo + "_" + carta.palo;
      $("#cartasJugador").append(
        "<img src='./img/cartas/" +
          nombreCarta +
          ".png' width='140px' class='carta nueva-carta-jugador' >"
      );
    });
    // si llegara a haber un blackjack inicial, finalizar separar esta funcion
    if (data.estadoPartida === "FINALIZADA") {
      setTimeout(function () {
        finalizar(data.ganador, data.jugadorActual);
      }, 1000);
    }
  });

  // PEDIR UNA CARTA NUEVA Y METERLA AL DIV(ESTAR ATENTO AL ESTADO DE LA PARTIDA)
  $("#pedirCarta").click(function () {
    $.get("pedir-carta", function (data) {
      // Agrego la carta nueva al div del jugador
      let nombreCarta = data.cartaNueva.simbolo + "_" + data.cartaNueva.palo;
      $("#cartasJugador").append(
        "<img src='./img/cartas/" +
          nombreCarta +
          ".png' width='140px' class='carta nueva-carta-jugador' >"
      );

      if (data.estadoPartida === "FINALIZADA") {
        setTimeout(function () {
          finalizar(data.ganador, data.jugadorActual);
        }, 1000);
      }
    });
  });

  //  PLANTARSE, AGREGAR CARTAS CRUPIER Y MOSTRAR MODAL CON EL RESULTADO
  $("#plantarse").click(function () {
    $.get("plantarse", function (data) {
      // Mostrar las nuevas cartas del crupier
      data.manoFinalCrupier.forEach(function (carta) {
        let nombreCarta = carta.simbolo + "_" + carta.palo;
        $("#cartasCasa").append(
          "<img src='./img/cartas/" +
            nombreCarta +
            ".png' width='140px' class='carta nueva-carta-casa'>"
        );
      });

      setTimeout(function () {
        finalizar(data.ganador, data.jugadorActual);
      }, 1000);
    });
  });
});

function finalizar(ganador, jugador) {
  document.getElementById("modalFinPartida").style.display = "block";
  let mensaje = "x";
  switch (ganador) {
    case "casa":
      mensaje = "Ha Ganado la casa!";
      break;
    case jugador:
      mensaje = "Ganaste!";
      break;
    case "empate":
      mensaje = "Empate!";
      break;
  }
  $("#resultadoPartida").text(mensaje);
}
