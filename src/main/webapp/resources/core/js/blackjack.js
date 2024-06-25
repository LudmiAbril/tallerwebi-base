import { start, stop } from "./cronometro.js";

$(document).ready(function () {
  // Variable para almacenar la última carta del crupier con el dorso
  let modoDificil;
  let cartaDorsoMostrar = null;
  let hayContrareloj = false;
  let tiempoLimite = "";
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

  // MOSTRAR DATOS INICIALES
  $.get("comenzar", function (data) {
    // nombre y valor de la mano

    modoDificil = data.modoDificil;
    console.log(modoDificil);

    if (data.contrareloj) {
      hayContrareloj = true;
      tiempoLimite = data.tiempoLimite;
      $("#limite").text(tiempoLimite);
    } else {
      hayContrareloj = false;
    }

    $("#nombre").text(data.jugadorActual);

    actualizarPuntaje(data.puntaje);
    // partidas anteriores
    if (data.partidas) {
      data.partidas.forEach(function (partida) {
        $(".c-partidas").append(
          partida.fechaYhora + " puntaje alcanzado: " + partida.puntaje + "<br>"
        );
      });
    }

    data.cartasCasa.forEach(function (carta, index) {
      // Mostrar el dorso de la carta inicialmente
      if (index === data.cartasCasa.length - 1) {
        if (modoDificil === true) {
          agregarCarta(
            $("#cartasCasa"),
            carta.simbolo + "_" + carta.palo,
            "casa"
          );
        } else {
          agregarCarta($("#cartasCasa"), "DORSO", "casa");
          // Guardar la referencia a la carta con dorso
          cartaDorsoMostrar = carta.simbolo + "_" + carta.palo;
        }
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

    start();
    // si llegara a haber un blackjack inicial, finalizar separar esta funcion
    if (data.estadoPartida === "FINALIZADA") {
      setTimeout(function () {
        mostrarModalfinalizar(data.ganador, data.jugadorActual);
      }, 1000);
    }
  });

  // PEDIR CARTA
  $("#pedirCarta").click(function () {
    $.get("pedir-carta", function (data) {
      // Agrego la carta nueva al div del jugador
      agregarCarta(
        $("#cartasJugador"),
        data.cartaNueva.simbolo + "_" + data.cartaNueva.palo,
        "jugador"
      );
      // actualizar puntaje de mano
      actualizarPuntaje(data.puntaje);

      if (data.estadoPartida === "FINALIZADA") {
        setTimeout(function () {
          mostrarModalfinalizar(data.ganador, data.jugadorActual);
        }, 1000);
      } else if (modoDificil === true) {
        $("#plantarse").click();
      }
    });
  });

  // PLANTARSE
  $("#plantarse").click(function () {
    $.get("plantarse", function (data) {
      if (modoDificil === false) {
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
      }

      setTimeout(function () {
        mostrarModalfinalizar(data.ganador, data.jugadorActual);
      }, 2000); // Esperar 3 segundos antes de finalizar
    });
  });

  setInterval(function () {
    if (hayContrareloj) {
      verificarTiempo(tiempoLimite);
      console.log(
        "paso un min" +
          "tiempo lim:" +
          tiempoLimite +
          "hora actual:" +
          obtenerHoraActual()
      );
    }
  }, 60000);
});

function obtenerHoraActual() {
  var tiempoActual = new Date();
  var horas = tiempoActual.getHours();
  var minutos = tiempoActual.getMinutes();
  var segundos = tiempoActual.getSeconds();

  // Formatear los componentes de tiempo
  horas = horas < 10 ? "0" + horas : horas;
  minutos = minutos < 10 ? "0" + minutos : minutos;
  segundos = segundos < 10 ? "0" + segundos : segundos;

  // Crear y devolver la cadena de tiempo formateada
  return horas + ":" + minutos;
}

function verificarTiempo(tiempoLimite) {
  let horaActual = obtenerHoraActual();
  if (horaActual === tiempoLimite) {
    stop();
    $(".reloj").addClass("puntaje-limite");
    $("#plantarse").click();
  }
}

function actualizarPuntaje(puntaje) {
  $("#puntaje").text(puntaje);

  if (puntaje > 21) {
    $("#puntaje").addClass("puntaje-limite");
  }
}

function mostrarModalfinalizar(ganador, jugador) {
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
  stop();
}
