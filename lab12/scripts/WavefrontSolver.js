function findPath(map, startX, startY, endX, endY) {
    const rows = map.length;
    const cols = map[0].length;

    let dist = Array.from({ length: rows }, () => Array(cols).fill(-1));

    let queue = [{ x: startX, y: startY }];
    dist[startY][startX] = 0;

    let head = 0;
    while (head < queue.length) {
        let { x, y } = queue[head++];

        if (x === endX && y === endY) break;

        const neighbors = [
            { x: x + 1, y: y }, { x: x - 1, y: y },
            { x: x, y: y + 1 }, { x: x, y: y - 1 }
        ];

        for (let { x: nx, y: ny } of neighbors) {
            if (nx >= 0 && nx < cols && ny >= 0 && ny < rows &&
                map[ny][nx] === 0 && dist[ny][nx] === -1) {

                dist[ny][nx] = dist[y][x] + 1;
                queue.push({ x: nx, y: ny });
            }
        }
    }

    if (dist[endY][endX] === -1) return null;

    let path = [];
    let currX = endX;
    let currY = endY;

    while (currX !== startX || currY !== startY) {
        path.unshift([currX, currY]);

        const neighbors = [
            { x: currX + 1, y: currY }, { x: currX - 1, y: currY },
            { x: currX, y: currY + 1 }, { x: currX, y: currY - 1 }
        ];

        for (let { x: nx, y: ny } of neighbors) {
            if (nx >= 0 && nx < cols && ny >= 0 && ny < rows &&
                dist[ny][nx] === dist[currY][currX] - 1) {
                currX = nx;
                currY = ny;
                break;
            }
        }
    }
    path.unshift([startX, startY]);
    return path;
}