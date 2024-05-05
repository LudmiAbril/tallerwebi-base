$(document).ready(function () {
  // TRAER LOS DATOS INICIALES
  $.get("comenzar", function (data) {
    // Mostrar las cartas iniciales del crupier
    data.cartasCasa.forEach(function (carta) {
      $("#cartasCasa").append("carta: " + carta.valor + carta.palo + "<br>");
    });

    // Mostrar las cartas iniciales del jugador
    data.cartasJugador.forEach(function (carta) {
      let nombreCarta = carta.simbolo + "_" + carta.palo;
      $("#cartasJugador").append("<div class='carta'>l</div>");
    });

    // si llegara a haber un blackjack inicial, finalizar
    if (data.estadoPartida === "FINALIZADA") {
      setTimeout(function () {
        finalizar(data.ganador, data.jugadorActual);
      }, 1000);
    }
  });

  // PEDIR UNA CARTA NUEVA Y ACTUALIZAR LAS CARTAS (ESTAR ATENTO AL ESTADO DE LA PARTIDA)
  $("#pedirCarta").click(function () {
    $.get("pedir-carta", function (data) {
      // Limpiar las cartas anteriores
      $("#cartasJugador").empty();
      $("#cartasCasa").empty();

      // Mostrar las nuevas cartas del jugador
      data.cartasJugador.forEach(function (carta) {
        let nombreCarta = carta.simbolo + "_" + carta.palo;
        $("#cartasJugador").append(
          "<img th:src='@{/img/cartas/" + nombreCarta + "}' width='140px'>"
        );
      });

      // Mostrar las nuevas cartas del crupier
      data.cartasCasa.forEach(function (carta) {
        $("#cartasCasa").append("carta: " + carta.valor + carta.palo + "<br>");
      });

      if (data.estadoPartida === "FINALIZADA") {
        setTimeout(function () {
          finalizar(data.ganador, data.jugadorActual);
        }, 1000);
      }
    });
  });

  // MOSTRAR MODAL CON EL RESULTADO
  $("#plantarse").click(function () {
    $.get("plantarse", function (data) {
      // Limpiar las cartas anteriores
      $("#cartasCasa").empty();

      // Mostrar las nuevas cartas del crupier
      data.cartasCasa.forEach(function (carta) {
        $("#cartasCasa").append("carta: " + carta.valor + carta.palo + "<br>");
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
