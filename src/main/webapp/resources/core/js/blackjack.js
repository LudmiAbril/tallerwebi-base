$(document).ready(function () {
  $.get("comenzar", function (data) {
    $("#jugadorNombre").text(data.jugadorActual);
    $("#estado").text(data.estadoPartida);
    $("#ganador").text(data.ganador);

    // Mostrar las cartas iniciales del crupier
    data.cartasCasa.forEach(function (carta) {
      $("#cartasCasa").append("carta: " + carta.valor + carta.palo + "<br>");
    });

    // Mostrar las cartas iniciales del jugador
    data.cartasJugador.forEach(function (carta) {
      $("#cartasJugador").append(
        "carta: " + carta.valor + " de " + carta.palo + "<br>"
      );
    });
  });

  $("#pedirCarta").click(function () {
    $.get("pedir-carta", function (data) {
      // // Limpiar las cartas anteriores
      $("#cartasJugador").empty();
      $("#cartasCasa").empty();

      $("#estado").text(data.estadoPartida);
      $("#ganador").text(data.ganador);
      // Mostrar las nuevas cartas del jugador
      data.cartasJugador.forEach(function (carta) {
        $("#cartasJugador").append(
          "carta" + carta.valor + " de " + carta.palo + "<br>"
        );
      });

      // Mostrar las nuevas cartas del crupier
      data.cartasCasa.forEach(function (carta) {
        $("#cartasCasa").append("carta: " + carta.valor + carta.palo + "<br>");
      });
    });
  });

  $("#plantarse").click(function () {
    $.get("plantarse", function () {
       // // Limpiar las cartas anteriores
       $("#cartasJugador").empty();
       $("#cartasCasa").empty();
 
       $("#estado").text(data.estadoPartida);
       $("#ganador").text(data.ganador);
       // Mostrar las nuevas cartas del jugador
       data.cartasJugador.forEach(function (carta) {
         $("#cartasJugador").append(
           "carta" + carta.valor + " de " + carta.palo + "<br>"
         );
       });
 
       // Mostrar las nuevas cartas del crupier
       data.cartasCasa.forEach(function (carta) {
         $("#cartasCasa").append("carta: " + carta.valor + carta.palo + "<br>");
       });
    });
  });

  $("#reiniciar").click(function () {
    $.get("/reiniciar", function () {
      console.log("funca");
    });
  });
});
