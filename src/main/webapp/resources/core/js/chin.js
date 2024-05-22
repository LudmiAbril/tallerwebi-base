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
  }