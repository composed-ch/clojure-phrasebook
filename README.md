# Clojure Phrasebook

1. Read one chapter of _Getting Clojure_ by Russ Olsen and take notes on paper.
2. Transfer the notes into a markdown file.
3. Add 3-5 exercises per chapter.
4. Provide solutions to the exercises.

## Setup

Initialize Module:

    hugo mod init clojure-phrasebook.composed.ch

extend `hugo.toml`:

```toml
baseURL = 'https://clojure-phrasebook.composed.ch'
languageCode = 'en-us'
title = 'Clojure Phrasebook'

[module]
[[module.imports]]
path = 'github.com/McShelby/hugo-theme-relearn'
```

Install Dependencies:

    hugo mod tidy
