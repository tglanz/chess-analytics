# Chess.com Public API

Base URL: `https://api.chess.com/pub`

All endpoints are read-only (GET). No authentication required. Responses are JSON.

---

## Player

### Get Player Profile

```
GET /player/{username}
```

| Field | Type | Description |
|-------|------|-------------|
| `@id` | string | API resource URL |
| `player_id` | number | Numeric ID |
| `username` | string | Username |
| `name` | string | Display name |
| `title` | string | Chess title (GM, IM, FM, etc.) |
| `url` | string | Profile page URL |
| `avatar` | string | Avatar image URL |
| `country` | string | Country API URL |
| `location` | string | Location text |
| `followers` | number | Follower count |
| `joined` | number | Unix timestamp |
| `last_online` | number | Unix timestamp |
| `status` | string | `premium`, `basic`, `staff`, etc. |
| `is_streamer` | boolean | Whether the player streams |
| `verified` | boolean | Verified account |
| `league` | string | Competitive league tier |

### Get Player Stats

```
GET /player/{username}/stats
```

Returns ratings and records per game format. Top-level keys:

- `chess_daily` - Daily chess
- `chess_rapid` - Rapid
- `chess_blitz` - Blitz
- `chess_bullet` - Bullet
- `chess960_daily` - Chess960 daily
- `fide` - FIDE rating (number)
- `tactics` - Puzzle rating
- `puzzle_rush` - Puzzle rush scores

Each format contains:

| Field | Type | Description |
|-------|------|-------------|
| `last.rating` | number | Current rating |
| `last.date` | number | Unix timestamp |
| `last.rd` | number | Rating deviation |
| `best.rating` | number | Peak rating |
| `best.game` | string | Game URL of best rating |
| `record.win` | number | Total wins |
| `record.loss` | number | Total losses |
| `record.draw` | number | Total draws |

### Get Player Clubs

```
GET /player/{username}/clubs
```

Returns `{ "clubs": [...] }`. Each club entry:

| Field | Type | Description |
|-------|------|-------------|
| `@id` | string | Club API URL |
| `name` | string | Club name |
| `url` | string | Club page URL |
| `icon` | string | Club icon URL |
| `joined` | number | Unix timestamp |
| `last_activity` | number | Unix timestamp |

---

## Games

### List Game Archives

```
GET /player/{username}/games/archives
```

Returns `{ "archives": [...] }` - an array of monthly archive URLs.

Each URL follows the pattern:
```
https://api.chess.com/pub/player/{username}/games/{YYYY}/{MM}
```

### List Games by Month

```
GET /player/{username}/games/{YYYY}/{MM}
```

Returns `{ "games": [...] }`. Each game:

| Field | Type | Description |
|-------|------|-------------|
| `url` | string | Game page URL |
| `uuid` | string | Unique game ID |
| `pgn` | string | Full PGN including moves and clock times |
| `fen` | string | Final position in FEN |
| `time_control` | string | Time control (e.g. `"180"`, `"600+5"`) |
| `time_class` | string | `daily`, `rapid`, `blitz`, `bullet` |
| `rules` | string | `chess`, `chess960`, etc. |
| `rated` | boolean | Whether the game was rated |
| `initial_setup` | string | Starting FEN (if non-standard) |
| `eco` | string | ECO opening code |
| `accuracies` | object | `{ "white": number, "black": number }` (if analyzed) |
| `white` | object | White player info (see below) |
| `black` | object | Black player info (see below) |

Player object (`white` / `black`):

| Field | Type | Description |
|-------|------|-------------|
| `username` | string | Username |
| `rating` | number | Rating at time of game |
| `result` | string | `win`, `resigned`, `timeout`, `checkmated`, `stalemate`, `repetition`, `agreed`, `insufficient`, `50move`, `abandoned`, `timevsinsufficient` |
| `@id` | string | Player API URL |
| `uuid` | string | Player UUID |

---

## Club

### Get Club Profile

```
GET /club/{club-url-id}
```

| Field | Type | Description |
|-------|------|-------------|
| `@id` | string | API resource URL |
| `name` | string | Club name |
| `club_id` | number | Numeric ID |
| `url` | string | Club page URL |
| `icon` | string | Club icon URL |
| `country` | string | Country API URL |
| `description` | string | HTML description |
| `members_count` | number | Total members |
| `average_daily_rating` | number | Average member rating |
| `visibility` | string | `public` or `private` |
| `created` | number | Unix timestamp |
| `last_activity` | number | Unix timestamp |
| `admin` | array | Admin profile API URLs |
| `join_request` | string | Join request URL |

---

## Country

### Get Country

```
GET /country/{iso-code}
```

| Field | Type | Description |
|-------|------|-------------|
| `@id` | string | API resource URL |
| `code` | string | ISO country code |
| `name` | string | Country name |

---

## Leaderboards

### Get Leaderboards

```
GET /leaderboards
```

Returns top 50 players per category. Top-level keys:

- `daily`
- `daily960`
- `live_rapid`
- `live_blitz`
- `live_bullet`

Each entry:

| Field | Type | Description |
|-------|------|-------------|
| `player_id` | number | Numeric ID |
| `username` | string | Username |
| `name` | string | Display name |
| `title` | string | Chess title |
| `url` | string | Profile page URL |
| `country` | string | Country API URL |
| `status` | string | Account status |
| `avatar` | string | Avatar URL |
| `score` | number | Rating |
| `rank` | number | Leaderboard position |
| `win_count` | number | Total wins |
| `loss_count` | number | Total losses |
| `draw_count` | number | Total draws |
| `trend_score` | object | `{ "direction": number, "delta": number }` |
| `trend_rank` | object | `{ "direction": number, "delta": number }` |
