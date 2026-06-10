function generateMap(width, height) {
    const map = Array.from({length: height}, () => Array(width).fill(0));

    const shapes = [
        [[1, 1], [1, 1]],           // O
        [[1, 1, 1, 1]],             // I
        [[1, 1, 1], [0, 1, 0]],     // T
        [[1, 1, 0], [0, 1, 1]],     // Z
        [[0, 1, 1], [1, 1, 0]],     // S
        [[1, 0, 0], [1, 1, 1]],     // L
        [[0, 0, 1], [1, 1, 1]]      // J
    ];

    function canPlace(shape, x, y) {
        for (let r = 0; r < shape.length; r++) {
            for (let c = 0; c < shape[r].length; c++) {
                if (shape[r][c] === 1) {
                    let newY = y + r;
                    let newX = x + c;
                    if (newY >= height || newX >= width || map[newY][newX] === 1) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    const tileCount = Math.floor((width * height) * 0.25 / 4);
    for (let i = 0; i < tileCount; i++) {
        const shape = shapes[Math.floor(Math.random() * shapes.length)];
        const x = Math.floor(Math.random() * width);
        const y = Math.floor(Math.random() * height);

        if (canPlace(shape, x, y)) {
            for (let r = 0; r < shape.length; r++) {
                for (let c = 0; c < shape[r].length; c++) {
                    if (shape[r][c] === 1) {
                        map[y + r][x + c] = 1;
                    }
                }
            }
        }
    }

    return map;
}