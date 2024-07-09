let partidasHistorial = []; // Almacenar todas las partidas obtenidas

function abrirModalHistorial() {
  const modal = document.getElementById("modal-history");
  if (modal) {
    modal.style.display = "block";

    fetch("getHistorialPartidas")
      .then((response) => response.json())
      .then((data) => {
        partidasHistorial = data.partidas; // Guardar las partidas en la variable global
        mostrarPartidas(partidasHistorial); // Mostrar todas las partidas inicialmente
      })
      .catch((error) => console.error("Error al obtener las partidas:", error));
  } else {
    console.error("El elemento modal no se encuentra en el DOM.");
  }
}

function mostrarPartidas(partidas) {
  const tbody = document.getElementsByClassName("tabla-historial-cuerpo")[0];
  tbody.innerHTML = "";
  if (partidas.length === 0) {
    const emptyrow = document.createElement("tr");
    emptyrow.innerHTML = `<td>-</td><td>-</td><td>-</td>`;
    tbody.appendChild(emptyrow);

    document.getElementById("mensaje-partidas").innerText =
      "Todavia no jugaste a este juego, que esperas para probar unas partidas?";
    return;
  } else {
    document.getElementById("mensaje-partidas").textContent = "";
  }

  partidas.forEach((partida) => {
    const row = document.createElement("tr");
    row.innerHTML = `
            <td>${partida.puntaje}</td>
            <td>${partida.juego}</td>
            <td>${partida.fechaYhora}</td>
        `;
    tbody.appendChild(row);
  });
}

function filtrarPorJuego() {
  const radioButtons = document.getElementsByName("juego-op");
  let filtro = "todos";
  for (const radioButton of radioButtons) {
    if (radioButton.checked) {
      filtro = radioButton.value;
      break;
    }
  }

  const partidasFiltradas =
    filtro === "todos"
      ? partidasHistorial
      : partidasHistorial.filter(
          (partida) => partida.juego.toLowerCase() === filtro.toLowerCase()
        );

  mostrarPartidas(partidasFiltradas);
}
