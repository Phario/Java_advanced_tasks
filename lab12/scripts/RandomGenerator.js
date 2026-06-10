function generateMap(width, height) {
    const map = Array.from({length: height}, () => Array(width).fill(0));

    let random = Math.random;

    const wallAmount = width * height * 0.2;

    for (let i = 0; i < wallAmount; i++) {
        const x = Math.floor(random() * width);
        const y = Math.floor(random() * height);

        map[y][x] = 1;
    }

    return map;
}