function togglePlaylist() {
    const playlist = document.getElementById('playlist');
    if (playlist.style.display === 'none') {
        playlist.style.display = 'block';
    } else {
        playlist.style.display = 'none';
    }
}