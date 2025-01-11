DOC_PARAMS=-N --toc -s
TEX_PARAMS=--pdf-engine=xelatex -V fontsize=11pt -V documentclass=scrartcl -V papersize=a4
FONTS=-V mainfont="Bitter" -V sansfont="Lato" -V monofont="Fantasque Sans Mono"
DATE=-V date="`date +'%Y-%m-%d'`"

.PHONY: all clean

all: clojure-phrasebook.pdf clojure-phrasebook.html clojure-phrasebook.epub

clojure-phrasebook.pdf: *.md
	pandoc $(DOC_PARAMS) $(TEX_PARAMS) $(FONTS) $(DATE) $^ -o $@

clojure-phrasebook.html: *.md
	pandoc $(DOC_PARAMS) $(DATE) $^ -o $@

clojure-phrasebook.epub: *.md
	pandoc $(DOC_PARAMS) $(DATE) $^ -o $@

clean:
	rm -f clojure-phrasebook.pdf clojure-phrasebook.html clojure-phrasebook.epub
