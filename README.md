`-*- word-wrap: t; -*-`

# wmrd-becoming

Video browing and selection front end for [Wayne McGregor|Random Dance](http://www.randomdance.org)'s _Becoming_ project.

The code here draws on a demonstration [ClojureScript and Backbone project](https://github.com/konrad-garus/hello-clj-backbone) by Konrad Garus, but implements a  complete Backbone MVC stack, rather than doing the event capture by hand, and also links into some HTML5 video control machinery.

## Technologies

The front end is written in [ClojureScript](https://github.com/clojure/clojurescript) and uses [Backbone.js](http://backbonejs.org) as its control framework (sitting on [jQuery](http://jquery.com)). Page layout is assisted by [Bootstrap](http://twitter.github.io/bootstrap). The actual pages are written in [Clojure](http://clojure.org) and rendered out using [Hiccup](https://github.com/weavejester/hiccup), [Compojure](https://github.com/weavejester/compojure) and [Ring](https://github.com/ring-clojure/ring).

## Usage

Install [Leiningen](https://github.com/technomancy/leiningen). Then, in the project directory:

- `lein once` to build the Javascript

- `lein go` to run the server

The test site will then be running on `http://localhost:3000`. Use `lein auto` to run an auto-compiler on the ClojureScript if you want to modify it. (Changes to the Hiccup page declarations will be picked up automatically on reload.)

_Note_: the site needs access to a tree of video assets (with thumbnails) in order to work. We're working on this, and an abstraction layer to allow the system to access different kinds of assets.

## License

Copyright © 2013 Nick Rothwell

Distributed under the Eclipse Public License, the same as Clojure.
