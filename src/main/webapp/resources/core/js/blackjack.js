$(document).ready(function () {
  // Variable para almacenar la última carta del crupier con el dorso
  let cartaDorsoMostrar = null;

  // Función para agregar una nueva carta a un contenedor dado
  function agregarCarta(contenedor, nombreCarta, jugador) {
    contenedor.append(
      "<img src='./img/cartas/" +
        nombreCarta +
        ".png' width='140px' class='carta nueva-carta-" +
        jugador +
        "'>"
    );
  }

  // TRAER LOS DATOS INICIALES
  $.get("comenzar", function (data) {
    data.cartasCasa.forEach(function (carta, index) {
      // Mostrar el dorso de la carta inicialmente
      if (index === data.cartasCasa.length - 1) {
        agregarCarta($("#cartasCasa"), "DORSO", "casa");
        // Guardar la referencia a la carta con dorso
        cartaDorsoMostrar = carta.simbolo + "_" + carta.palo;
      } else {
        agregarCarta(
          $("#cartasCasa"),
          carta.simbolo + "_" + carta.palo,
          "casa"
        );
      }
    });

    data.cartasJugador.forEach(function (carta) {
      agregarCarta(
        $("#cartasJugador"),
        carta.simbolo + "_" + carta.palo,
        "jugador"
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
      agregarCarta(
        $("#cartasJugador"),
        data.cartaNueva.simbolo + "_" + data.cartaNueva.palo,
        "jugador"
      );

      if (data.estadoPartida === "FINALIZADA") {
        setTimeout(function () {
          finalizar(data.ganador, data.jugadorActual);
        }, 1000);
      }
    });
  });

  // PLANTARSE, AGREGAR CARTAS CRUPIER Y MOSTRAR MODAL CON EL RESULTADO
  $("#plantarse").click(function () {
    $.get("plantarse", function (data) {
      let cartaDorsoCrupier = $("#cartasCasa img:last-child");
      cartaDorsoCrupier.addClass("flip-animation");

      // Después de 0.25 segundos (mitad de la duración de la animación), cambiar la imagen
      setTimeout(function () {
        cartaDorsoCrupier.attr(
          "src",
          "./img/cartas/" + cartaDorsoMostrar + ".png"
        );
      }, 120); // 250 milisegundos = 0.25 segundos

      // Después de 0.5 segundos (duración completa de la animación), quitar la clase de animación
      setTimeout(function () {
        cartaDorsoCrupier.removeClass("nueva-carta-casa");
        cartaDorsoCrupier.removeClass("flip-animation");
      }, 500); // 500 milisegundos = 0.5 segundos
      // Mostrar las nuevas cartas del crupier
      setTimeout(function () {
        data.manoFinalCrupier.forEach(function (carta, index) {
          let nombreCarta = carta.simbolo + "_" + carta.palo;
          agregarCarta($("#cartasCasa"), nombreCarta, "casa");
        });
      }, 800);

      setTimeout(function () {
        finalizar(data.ganador, data.jugadorActual);
      }, 3000); // Esperar 3 segundos antes de finalizar
    });
  });
});

function finalizar(ganador, jugador) {
  $("#modalFinPartida").show();
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
