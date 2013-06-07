`-*- word-wrap: t; -*-`

# cljs-video-control

This project is a collection of test pages for a video selection and control system currently under construction for [Wayne McGregor|Random Dance](http://www.randomdance.org). While the final system is going to be quite complex, I've built a collection of demo pages to test various isolated components; these should be independent and reasonably easy to understand. The demos include on-tage sliders, drag-and-drop, and assorted random element rewriting.

The code here draws on a [ClojureScript and Backbone project](https://github.com/konrad-garus/hello-clj-backbone) by Konrad Garus, but implements a series of complete Backbone MVC stacks, rather than doing the event capture by hand.

## Technologies

The front end is written in [ClojureScript](https://github.com/clojure/clojurescript) and uses [Backbone.js](http://backbonejs.org) as its control framework (sitting on [jQuery](http://jquery.com). Page layout is assisted by [Bootstrap](http://twitter.github.io/bootstrap). The actual pages are written in [Clojure](http://clojure.org) and rendered out using [Hiccup](https://github.com/weavejester/hiccup), [Compojure](https://github.com/weavejester/compojure) and [Ring](https://github.com/ring-clojure/ring).

## Usage

Install [Leiningen](https://github.com/technomancy/leiningen). Then, in the project directory:

- `lein once` to build the Javascript

- `lein go` to run the server

The test site will then be running on `http://localhost:3000`. Use `lein auto` to run an auto-compiler on the ClojureScript if you want to modify it. (Changes to the Hiccup page declarations will be picked up automatically on reload.)

## License

Copyright © 2013 Nick Rothwell

Distributed under the Eclipse Public License, the same as Clojure.
