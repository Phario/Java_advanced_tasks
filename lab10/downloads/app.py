from flask import Flask, jsonify, request
from flasgger import Swagger
from GameEngine.gameEngine import GameEngine
import random

app = Flask(__name__)

app.config["SWAGGER"] = {
    "title": "Game API",
    "uiversion": 3
}

Swagger(app)

engine = GameEngine()


def ok(data=None, status=200):
    return jsonify({
        "ok": True,
        "data": data
    }), status


def err(message, status=400):
    return jsonify({
        "ok": False,
        "error": str(message)
    }), status


@app.route("/games/new", methods=["POST"])
def new_game():
    """
    Create a new game
    ---
    tags:
      - Games

    parameters:
      - name: body
        in: body
        required: false
        schema:
          type: object
          properties:
            players:
              type: integer
              default: 2
              example: 2
            board_size:
              type: integer
              default: 4
              example: 4
            memory_mode:
              type: string
              enum: [ram, disk]
              default: ram
              example: ram
            path:
              type: string
              example: "game.txt"

    responses:
      201:
        description: Game created

      400:
        description: Invalid input
    """

    body = request.get_json(silent=True) or {}

    try:
        game_id = engine.new_game(
            players=int(body.get("players", 2)),
            board_size=int(body.get("board_size", 4))
        )

        memory_mode = body.get("memory_mode", "ram")
        memory_path = f"games/{random.randint(1000, 99999999)}.txt"

        engine.set_memory_mode(game_id, memory_mode, memory_path)

        return ok({
            "game_id": game_id,
            **engine.get_game_state(game_id)
        }, 201)

    except (ValueError, FileNotFoundError) as e:
        return err(e, 400)


@app.route("/games/list", methods=["GET"])
def list_games():
    """
    List all games
    ---
    tags:
      - Games

    responses:
      200:
        description: List of game IDs
    """

    return ok({
        "game_ids": engine.get_game_ids()
    })


@app.route("/games/<game_id>", methods=["GET"])
def get_state(game_id):
    """
    Get game state
    ---
    tags:
      - Games

    parameters:
      - name: game_id
        in: path
        type: string
        required: true

    responses:
      200:
        description: Game state

      404:
        description: Game not found

      400:
        description: Invalid disk state
    """

    try:
        return ok(engine.get_game_state(game_id))

    except KeyError as e:
        return err(e, 404)

    except (ValueError, FileNotFoundError) as e:
        return err(e, 400)


@app.route("/games/<game_id>/move", methods=["POST"])
def make_move(game_id):
    """
    Make a move
    ---
    tags:
      - Games

    parameters:
      - name: game_id
        in: path
        type: string
        required: true

      - name: body
        in: body
        required: true
        schema:
          type: object
          required:
            - x
            - y
          properties:
            x:
              type: integer
              example: 1
            y:
              type: integer
              example: 2

    responses:
      200:
        description: Updated game state

      400:
        description: Invalid move

      404:
        description: Game not found
    """

    body = request.get_json() or {}

    try:
        x = int(body["x"])
        y = int(body["y"])

        engine.make_move(game_id, x, y)

        return ok(engine.get_game_state(game_id))

    except KeyError as e:
        return err(e, 404)

    except (ValueError, FileNotFoundError) as e:
        return err(e, 400)


@app.route("/games/<game_id>/memory", methods=["POST"])
def set_memory_mode(game_id):
    """
    Set game memory mode
    ---
    tags:
      - Games

    parameters:
      - name: game_id
        in: path
        type: string
        required: true

      - name: body
        in: body
        required: true
        schema:
          type: object
          required:
            - memory_mode
          properties:
            memory_mode:
              type: string
              enum: [ram, disk]
              example: disk
            path:
              type: string
              example: "game.txt"

    responses:
      200:
        description: Memory mode changed

      400:
        description: Invalid memory mode

      404:
        description: Game not found
    """

    body = request.get_json() or {}

    try:
        memory_mode = body["memory_mode"]
        memory_path = body.get("path")

        engine.set_memory_mode(game_id, memory_mode, memory_path)

        return ok(engine.get_game_state(game_id))

    except KeyError as e:
        return err(e, 404)

    except (ValueError, FileNotFoundError) as e:
        return err(e, 400)


@app.route("/games/<game_id>/reset", methods=["POST"])
def reset_game(game_id):
    """
    Reset a game
    ---
    tags:
      - Games

    parameters:
      - name: game_id
        in: path
        type: string
        required: true

    responses:
      200:
        description: Game reset

      404:
        description: Game not found

      400:
        description: Invalid disk state
    """

    try:
        engine.reset_game(game_id)

        return ok(engine.get_game_state(game_id))

    except KeyError as e:
        return err(e, 404)

    except (ValueError, FileNotFoundError) as e:
        return err(e, 400)


@app.route("/games/<game_id>/delete", methods=["DELETE"])
def end_game(game_id):
    """
    Delete a game
    ---
    tags:
      - Games

    parameters:
      - name: game_id
        in: path
        type: string
        required: true

    responses:
      200:
        description: Game deleted

      404:
        description: Game not found
    """

    try:
        engine.end_game(game_id)

        return ok({
            "deleted": game_id
        })

    except KeyError as e:
        return err(e, 404)


@app.route("/games/<game_id>/save", methods=["POST"])
def save_game(game_id):
    """
    Save a game
    ---
    tags:
      - Games

    parameters:
      - name: game_id
        in: path
        type: string
        required: true

      - name: body
        in: body
        required: true
        schema:
          type: object
          required:
            - path
          properties:
            path:
              type: string
              example: "game.txt"

    responses:
      200:
        description: Game saved

      400:
        description: Invalid save path

      404:
        description: Game not found
    """

    body = request.get_json() or {}

    path = body.get("path")

    if not path:
        return err("'path' is required", 400)

    try:
        engine.save_game(game_id, path)

        return ok({
            "saved_to": path
        })

    except KeyError as e:
        return err(e, 404)

    except (ValueError, FileNotFoundError) as e:
        return err(e, 400)


@app.route("/games/load", methods=["POST"])
def load_game():
    """
    Load a game
    ---
    tags:
      - Games

    parameters:
      - name: body
        in: body
        required: true
        schema:
          type: object
          required:
            - path
          properties:
            path:
              type: string
              example: "game.txt"
            memory_mode:
              type: string
              enum: [ram, disk]
              default: ram
              example: ram

    responses:
      201:
        description: Game loaded

      400:
        description: Invalid file
    """

    body = request.get_json() or {}

    path = body.get("path")

    if not path:
        return err("'path' is required", 400)

    try:
        game_id = engine.load_game(path)

        memory_mode = body.get("memory_mode", "ram")
        if memory_mode == "disk":
            engine.set_memory_mode(game_id, "disk", path)

        return ok({
            "game_id": game_id,
            **engine.get_game_state(game_id)
        }, 201)

    except (ValueError, FileNotFoundError) as e:
        return err(e, 400)


if __name__ == "__main__":
    app.run(debug=True)