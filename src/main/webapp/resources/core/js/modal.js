function openModal(modalId) {
    document.getElementById(modalId).style.display = "block";
}

function closeModal(modalId) {
    document.getElementById(modalId).style.display = "none";
}

window.onclick = function(event) {
    const homeModal = document.getElementById('homeModal');
    const configuracionModal = document.getElementById('configuracionModal');

    if (event.target == homeModal) {
        homeModal.style.display = "none";
    } else if (event.target == configuracionModal) {
        configuracionModal.style.display = "none";
    }
}
