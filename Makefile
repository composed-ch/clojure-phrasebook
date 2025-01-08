clojure-phrasebook.html: 00-meta.md 01-hello-clojure.md 02-vectors-and-lists.md
	pandoc -t html5 --toc -s -N $^ -o $@
