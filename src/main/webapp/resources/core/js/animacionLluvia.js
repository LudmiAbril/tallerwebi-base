function createChip() {
    const chip = document.createElement('div');
    chip.className = 'rain-chip';
    chip.style.left = Math.random() * 100 + 'vw';
    chip.style.animationDuration = Math.random() * 4 + 2 + 's';

    // Randomly select between two chip images
    const chipImage = Math.random() < 0.5 ? 'imgStatic/chip2.png' : 'imgStatic/chip1.png';
    chip.style.backgroundImage = `url(${chipImage})`;

    document.getElementById('rainContainer').appendChild(chip);

    // Remove chip after animation ends
    chip.addEventListener('animationend', () => {
        chip.remove();
    });
}

// Generate casino chips continuously
setInterval(createChip, 800);
