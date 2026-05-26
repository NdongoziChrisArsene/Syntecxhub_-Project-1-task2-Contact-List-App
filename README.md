# 📱 Contacts App — Android

A native Android contacts application built in Kotlin, featuring runtime permissions, live search filtering, and full contact detail view with call and message actions.

---

## Features

- **Contact list** with avatar thumbnails, names, and phone numbers
- **Live search** via `SearchView` with background-thread filtering
- **Runtime permission** request for `READ_CONTACTS` — falls back gracefully to sample data if denied
- **Detail screen** with Call (`ACTION_DIAL`) and Message (`ACTION_VIEW sms:`) system intents
- **Parcelable contact model** for safe, boilerplate-free data passing between activities
- **Density-aware avatars** — colored circles with initial letters, generated at `mdpi` through `xxxhdpi`

---

## Project Structure

```
app/src/main/
├── java/.../
│   ├── Contact.kt          # Data model (Parcelable)
│   ├── SampleData.kt       # 20 hardcoded contacts with drawable references
│   ├── ContactAdapter.kt   # RecyclerView adapter with Filterable support
│   ├── MainActivity.kt     # Entry point — permission check, list, search
│   └── DetailActivity.kt   # Contact detail — call & message actions
└── res/
    └── drawable-*/         # avatar_* resources at mdpi → xxxhdpi
```

---

## File Breakdown

### `Contact.kt`
Data class annotated with `@Parcelize`, enabling the entire object to be passed between activities via `Intent` without manually calling `putExtra` for every field. The `avatarRes` field is an `Int` holding the drawable resource ID.

### `SampleData.kt`
Provides 20 sample contacts, each mapped to its own `R.drawable.avatar_*` resource. Names are drawn from a Rwandan context.

### `ContactAdapter.kt`
Maintains two internal lists:
- `allContacts` — the original, never-mutated dataset
- `filteredList` — what the `RecyclerView` actually reads

When the `SearchView` fires, `performFiltering` runs on a background thread. `publishResults` then updates `filteredList` and calls `notifyDataSetChanged()` on the main thread — the correct pattern to avoid UI freezes on lower-end devices.

### `MainActivity.kt`
- `checkContactPermission()` requests `READ_CONTACTS` at runtime
- If the user denies the permission, the app continues with sample data and shows a `Toast`
- A lambda `{ contact -> ... }` passed to the adapter opens `DetailActivity` with the full `Contact` parcel

### `DetailActivity.kt`
- Reads the incoming parcel via `getParcelableExtra`
- Populates all UI fields
- Wires the **Call** button to `ACTION_DIAL` and the **Message** button to `ACTION_VIEW` with an `sms:` URI

### Avatars
Each avatar is a colored circle containing the contact's initial letter, generated at five density buckets (`mdpi`, `hdpi`, `xhdpi`, `xxhdpi`, `xxxhdpi`). Android automatically selects the appropriate size based on the device's screen density — no manual selection needed.

---

## Permissions

```xml
<uses-permission android:name="android.permission.READ_CONTACTS" />
<uses-permission android:name="android.permission.CALL_PHONE" />
```

`READ_CONTACTS` is requested at runtime (Android 6.0+). The app remains fully functional on a refusal, using the bundled sample data instead.

---

## Getting Started

1. Clone the repository
2. Open in **Android Studio**
3. Sync Gradle and let dependencies resolve
4. Run on an emulator or physical device (API 21+)

> On first launch, grant the Contacts permission to load from your device's real contacts. Tap any contact to open the detail screen, then use the Call or Message buttons to trigger system actions.

---

## Tech Stack

| Layer | Choice |
|---|---|
| Language | Kotlin |
| UI | XML layouts + RecyclerView |
| Data passing | `@Parcelize` / `Intent` extras |
| Search | `Filterable` + background thread |
| System intents | `ACTION_DIAL`, `ACTION_VIEW (sms:)` |
| Min SDK | 21 (Android 5.0) |
