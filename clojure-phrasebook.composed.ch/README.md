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
