document.addEventListener('DOMContentLoaded', () => {
    const musicaFondo = document.getElementById('musicaFondo');
    const toggleMusica = document.getElementById('toggleMusica');

    toggleMusica.addEventListener('click', () => {
        if (musicaFondo.paused) {
            musicaFondo.play();
            toggleMusica.classList.remove('fa-music');
            toggleMusica.classList.add('fa-pause');
        } else {
            musicaFondo.pause();
            toggleMusica.classList.remove('fa-pause');
            toggleMusica.classList.add('fa-music');
        }
    });
});