# Chat Demo

A small desktop group-chat application. Conversations are held in memory and
messages can carry an optional image attachment alongside their text.

## Stack

- **Backend** — Kotlin (`src/main/kotlin/chat/backend`): a `ChatService` orchestration
  layer over `Conversation` / `Message` repositories (in-memory implementations).
- **UI** — Java Swing (`src/main/java/chat/ui`): conversation list, message view, and
  a composer with text input and image attachment.

The layers are decoupled through repository interfaces, and the UI observes new
messages via a listener registered on `ChatService`.

## Features

- Group conversations with multiple participants.
- Text messages (truncated to a maximum length).
- Image attachments — send an image with or without a caption; image-only
  messages are supported.

## Requirements

- JDK 17

## Build & run

```bash
./gradlew run      # launch the desktop UI
./gradlew test     # run the test suite
./gradlew classes  # compile only
```

## Tests

Backend behaviour is covered by JUnit tests in `src/test/kotlin`, including text and
image attachments, image-only messages, text truncation, message persistence, and
participant handling.
