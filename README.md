# Contacts App

A lightweight Android contacts app with built-in sample data, real-time search, and deep system integration for calling and messaging. Works out of the box — no account or permission required to browse contacts.

---

## Architecture

```
MainActivity
    ├── ContactAdapter          ← Filterable RecyclerView adapter
    │   ├── allContacts         ← Full list (never mutated)
    │   └── filteredList        ← What the RecyclerView actually renders
    └── DetailActivity          ← Receives Contact via Parcel
            ├── ACTION_DIAL     ← Opens phone dialler
            └── ACTION_VIEW     ← Opens SMS app
```

---

## Core Files

### `Contact.kt` — Data Model

Annotated with `@Parcelize`, which auto-generates the `Parcelable` implementation at compile time. This means the entire `Contact` object can be dropped into an `Intent` and recovered in the next activity without manually writing `putExtra` for every field.

| Field | Type | Notes |
|---|---|---|
| `id` | `Int` | Unique identifier |
| `name` | `String` | Full name |
| `phone` | `String` | Used for dial and SMS intents |
| `email` | `String` | — |
| `role` | `String` | Job title or relationship |
| `avatarRes` | `Int` | Drawable resource ID — points to the contact's avatar |

---

### `SampleData.kt` — Seed Data

20 pre-loaded contacts, each mapped to their own `R.drawable.avatar_*` resource. Names are drawn from Rwanda to reflect the developer's background — the app works and feels local from the first launch.

---

### `ContactAdapter.kt` — RecyclerView + Search

The adapter holds two lists internally:

- **`allContacts`** — the full dataset, set once, never mutated.
- **`filteredList`** — the subset the `RecyclerView` renders, rebuilt on every search.

When the `SearchView` fires a query, `Filterable` splits the work correctly:

1. **`performFiltering`** runs the filter logic on a **background thread** — no jank even on slow devices.
2. **`publishResults`** swaps in the new list and calls `notifyDataSetChanged()` back on the **main thread**.

Doing the filtering directly on the main thread causes UI freezes; this two-step pattern avoids that entirely.

---

### `MainActivity.kt` — Entry Point

- **`checkContactPermission()`** requests the `READ_CONTACTS` runtime permission following the modern Android pattern.
- If the user **denies** the permission, the app continues running with sample data and shows a toast — it never crashes or blocks the user.
- A **lambda** `{ contact -> ... }` is passed into the adapter. When a row is tapped, it packages the full `Contact` as a Parcel and opens `DetailActivity` via `Intent`.

---

### `DetailActivity.kt` — Contact Detail Screen

Receives the `Contact` object with `getParcelableExtra` (one call, no field-by-field unpacking), then populates all UI fields. Two action buttons wire directly to Android system intents:

| Button | Intent | Action |
|---|---|---|
| **Call** | `ACTION_DIAL` | Opens the phone dialler pre-filled with the number |
| **Message** | `ACTION_VIEW` with `sms:` URI | Opens the default SMS app |

No third-party libraries needed — the OS handles both.

---

## Avatars

Each avatar is a colored circle with the contact's initial letter, generated at five density buckets:

| Density | Folder |
|---|---|
| 1× | `mdpi` |
| 1.5× | `hdpi` |
| 2× | `xhdpi` |
| 3× | `xxhdpi` |
| 4× | `xxxhdpi` |

Android selects the appropriate size automatically based on the device's screen density — no runtime scaling, no blur.

---

## Permissions

| Permission | Required | Fallback |
|---|---|---|
| `READ_CONTACTS` | No | App loads sample data if denied |
| `CALL_PHONE` | No | `ACTION_DIAL` opens the dialler without placing the call directly |

---

## Requirements

- Android Studio Hedgehog or later
- Min SDK: 21
- Kotlin 1.9+
- `kotlin-parcelize` plugin enabled in `build.gradle`

---

## Getting Started

1. Clone the repository.
2. Open in Android Studio.
3. Run on a device or emulator — sample contacts load immediately, no setup needed.
4. Grant `READ_CONTACTS` if you want the app to surface contacts from the device (optional).
