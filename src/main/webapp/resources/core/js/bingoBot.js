var intervaloRefresco;
//se utilizará para almacenar el intervalo de actualización del número cantado.
var numeroColorMap = {};
let nombreJugador = "";
$(document).ready(function () {
  $.get("obtenerDatosInicialesBot", function (data) {
    console.log("entre a la solicitud de obtener datos iniciales bot");
    $("#numeroCantado").text(data.numeroAleatorioCantado);
    var cartonhtmlbot = "";
    for (var i = 0; i < data.cartonBot.numeros.length; i++) {
      cartonhtmlbot += "<tr>";
      for (var j = 0; j < data.cartonBot.numeros[i].length; j++) {
        //Obtiene el número de casillero en la posición (i, j) del cartón.
        var numeroCasillero = data.cartonBot.numeros[i][j];
        cartonhtmlbot +=
          "<td><button id='botonCasilleroBot" +
          numeroCasillero +
          "' >" +
          numeroCasillero +
          "</button></td>";
      }
      cartonhtmlbot += "</tr>";
    }
    $(".cartonBot").html(cartonhtmlbot);
    $(".cartonBot").addClass("w3-animate-bottom");

    var cartonhtmluser = "";
    for (var i = 0; i < data.carton.numeros.length; i++) {
      cartonhtmluser += "<tr>";
      for (var j = 0; j < data.carton.numeros[i].length; j++) {
        //Obtiene el número de casillero en la posición (i, j) del cartón.
        var numeroCasillero = data.carton.numeros[i][j];
        cartonhtmluser +=
          "<td><button id='botonCasillero" +
          numeroCasillero +
          "' onclick='marcarCasillero(" +
          numeroCasillero +
          ")'>" +
          numeroCasillero +
          "</button></td>";
      }
      cartonhtmluser += "</tr>";
    }
    $(".cartonUser").html(cartonhtmluser);
    $(".cartonUser").addClass("w3-animate-bottom");


    $(".numeroCantadoContenedor").addClass("w3-animate-top");

    $("#numerosRestantesParaCompletarLaTirada").text(
      "Numeros restantes para completar la tirada: " +
        data.numerosRestantesParaCompletarLaTirada
    );
    console.log(numerosRestantesParaCompletarLaTirada);

    if (data.error) {
      alert(data.error);
    } else {
      tipoPartidaBingo = data.tipoPartidaBingo;
      if (tipoPartidaBingo === "LINEA") {
        document.getElementById("botonLinea").style.display = "block";
        document.getElementById("botonBingo").style.display = "none";
      } else if (tipoPartidaBingo === "BINGO") {
        document.getElementById("botonLinea").style.display = "none";
        document.getElementById("botonBingo").style.display = "block";
      }
    }
  });
  console.log("no entre a la solicitud");
  intervaloRefresco = setInterval(refrescarNumero, 7000);
});

function refrescarNumero() {
  obtenerLosNumerosEntregados();
  $(".numeroCantadoContenedor").removeClass("w3-animate-top");
  setTimeout(function () {
    $.get("obtenerNuevoNumeroBot", function (data) {
      console.log("realice la peticion de obtener nuevo numero bot");
      if (data.limiteAlcanzado) {
        abrirModalDeLimiteAlcanzado();
      } else {
        $("#numerosRestantesParaCompletarLaTirada").text(
          "Numeros restantes para completar la tirada: " +
            data.numerosRestantesParaCompletarLaTirada
        );
        $("#numeroCantado").text(data.nuevoNumero);
        $(".numeroCantadoContenedor").addClass("w3-animate-top");
      }
      // ver si se marco en el bot
      if (data.seMarcoBot) {
        $("#botonCasilleroBot" + data.nuevoNumero).css(
          "background-color",
          "#28e4d4"
        );
      }
      if (data.seHizoBingoBot) {
        abrirModalBot();
        clearInterval(intervaloRefresco); // Detener la actualización del número
        intervaloRefresco = null;
      }
    });
    console.log("no pude procesar la solicitud de obtener nuevo numero bot");
  }, 100);
}

function marcarCasillero(numeroCasillero) {
  $.get("obtenerNumeroActual", function (data) {
    numeroActual = data.numeroActual;
    casilleroEsIgualANumeroEntregado(numeroCasillero, function (result) {
      if (numeroCasillero == numeroActual || result) {
        $.post("marcarCasillero/" + numeroCasillero, function () {
          $("#botonCasillero" + numeroCasillero).css(
            "background-color",
            "purple"
          );
        });
      }
    });
  });
}

function marcarCasillero(numeroCasillero) {
  $.get("obtenerNumeroActual", function (data) {
    numeroActual = data.numeroActual;
    casilleroEsIgualANumeroEntregado(numeroCasillero, function (result) {
      if (numeroCasillero == numeroActual || result) {
        $.post("marcarCasillero/" + numeroCasillero, function () {
          $("#botonCasillero" + numeroCasillero).css(
            "background-color",
            "purple"
          );
        });
      }
    });
  });
}

function casilleroEsIgualANumeroEntregado(numeroCasillero, callback) {
  $.get("obtenerLosNumerosEntregados", function (data) {
    numerosEntregados = new Set(data.numerosEntregadosDeLaSesion);
    if (numerosEntregados.has(numeroCasillero)) {
      console.log("el casillero es igual a un numero entregado antes");
      callback(true);
    } else {
      console.log("El casillero no es igual a un numero entregado antes");
      callback(false);
    }
  });
}

function obtenerLosNumerosEntregados() {
  var bolaAmarillo = "bolaAmarillo.png";
  var bolaCeleste = "bolaCeleste.png";
  var bolaNaranja = "bolaNaranja.png";
  var bolaRoja = "bolaRoja.png";
  var bolaVerde = "bolaVerde.png";
  var bolaVioleta = "bolaVioleta.png";
  var rutaDeLasImgDeLasBolas = "/spring/imgStatic/";

  var bolas = [
    bolaAmarillo,
    bolaCeleste,
    bolaNaranja,
    bolaRoja,
    bolaVerde,
    bolaVioleta,
  ];

  $.get("obtenerCincoUltimosNumerosEntregadosBot", function (data) {
    var ultimosNumeros = data.ultimosNumerosEntregados;
    ultimosNumeros.reverse();
    var numerosEntregadosDiv = $(".numerosEntregados");
    numerosEntregadosDiv.empty();

    ultimosNumeros.forEach(function (numero) {
      if (!numeroColorMap[numero]) {
        numeroColorMap[numero] =
          bolas[Object.keys(numeroColorMap).length % bolas.length];
      }

      var parrafo = $("<p>")
        .text(numero)
        .attr("id", "numeroCantadoColeccion")
        .addClass("numerosEntregadosContenedor");
      var backgroundImageUrl = rutaDeLasImgDeLasBolas + numeroColorMap[numero];
      parrafo.css("background-image", "url(" + backgroundImageUrl + ")");
      numerosEntregadosDiv.append(parrafo);
    });
  });
}

function bingo() {
  $.post("bingoUser", function (data) {
    if (data.seHizoBingo) {
      abrirModal();
      clearInterval(intervaloRefresco); // Detener la actualización del número
      intervaloRefresco = null;
    } else if (!data.seHizoBingo) {
      var botonBingo = document.querySelector("#botonBingo");
      botonBingo.style.color = "black";
      botonBingo.classList.add("animate_animated", "animate_shakeX");
      botonBingo.style.backgroundColor = "gray";
      setTimeout(function () {
        botonBingo.classList.remove("animate_animated", "animate_shakeX");
        botonBingo.style.backgroundColor = "#8a2be2";
      }, 1000);
    }
  });
}

// GANO USER
function abrirModal() {
  $(".tituloModal").text("Ganaste!");
  $(".textoModal").text("felicidades");
  document.getElementById("modalBingo").style.display = "block";
  lanzarConfetti();
}

// GANO BOT
function abrirModalBot() {
  $(".tituloModal").text("Perdiste!");
  $(".textoModal").text("Suerte para la proxima!");
  document.getElementById("modalBingo").style.display = "block";
}

function lanzarConfetti() {
  var duration = 5 * 1000;
  var animationEnd = Date.now() + duration;
  var defaults = { startVelocity: 30, spread: 360, ticks: 60, zIndex: 0 };

  function randomInRange(min, max) {
    return Math.random() * (max - min) + min;
  }

  var interval = setInterval(function () {
    var timeLeft = animationEnd - Date.now();

    if (timeLeft <= 0) {
      return clearInterval(interval);
    }

    var particleCount = 50 * (timeLeft / duration);
    confetti(
      Object.assign({}, defaults, {
        particleCount,
        origin: { x: randomInRange(0.1, 0.3), y: Math.random() - 0.2 },
      })
    );
    confetti(
      Object.assign({}, defaults, {
        particleCount,
        origin: { x: randomInRange(0.7, 0.9), y: Math.random() - 0.2 },
      })
    );
  }, 250);
}


function abrirModalDeLimiteAlcanzado() {
  document.getElementById("modalLimite").style.display = "block";
}

function abrirModalDeCompra(){
  document.getElementById("modalCompra").style.display="block";
}