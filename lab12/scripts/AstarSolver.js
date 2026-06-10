function findPath(map, startX, startY, endX, endY) {
    const rows = map.length;
    const cols = map[0].length;

    const heuristic = (x1, y1, x2, y2) => Math.abs(x1 - x2) + Math.abs(y1 - y2);

    let openSet = [{ x: startX, y: startY, f: 0, g: 0, parent: null }];
    let closedSet = new Set();

    while (openSet.length > 0) {
        openSet.sort((a, b) => a.f - b.f);
        let current = openSet.shift();

        if (current.x === endX && current.y === endY) {
            let path = [];
            while (current) {
                path.unshift([current.x, current.y]);
                current = current.parent;
            }
            return path;
        }

        closedSet.add(`${current.x},${current.y}`);

        const neighbors = [
            { x: current.x + 1, y: current.y }, { x: current.x - 1, y: current.y },
            { x: current.x, y: current.y + 1 }, { x: current.x, y: current.y - 1 }
        ];

        for (let neighbor of neighbors) {
            if (neighbor.x >= 0 && neighbor.x < cols &&
                neighbor.y >= 0 && neighbor.y < rows &&
                map[neighbor.y][neighbor.x] === 0 &&
                !closedSet.has(`${neighbor.x},${neighbor.y}`)) {

                let g = current.g + 1;
                let h = heuristic(neighbor.x, neighbor.y, endX, endY);
                let f = g + h;

                let existing = openSet.find(n => n.x === neighbor.x && n.y === neighbor.y);
                if (!existing || g < existing.g) {
                    openSet.push({ x: neighbor.x, y: neighbor.y, f, g, parent: current });
                }
            }
        }
    }
    return null;
}