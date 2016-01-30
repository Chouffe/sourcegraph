# webserver

SourceGraph Challenge.

## Endpoints

The main rationale for using a hashmap from names to urls was to be able to have fast lookups on names. A hashmap is an optimal DataStructure forl (O(1) time) lookups (given the load factor is not too high < 75%).

- GET /names/:name
    - Lookup in a hashmap
        - O(1) time
        - O(n) space (to store the hashmap), where n is the number of names stored so far

- PUT /names/:name
    - Add an entry into the hashmap
        - O(1) time
        - O(n) space (to store the hashmap)

- DELETE /names
    - Deletes the hashmap and creates a fresh new empty one
        - O(1) time
        - O(1) space

- POST /annotate
    - Space: hashmap is stored O(n) + parsed html O(number of html tags)
    - Time: parsing O(number of html tags) + data lookup O(words in html)
    - Parses the html string into a tree structure (dom).
        - time: O(number of html tags)
        - space: O(number of html tags)
    - Performs a tree traversal O(number of html tags)
        - On each textual node (but the children of an <a> tag) performs a lookup (O(1) time) on each word to see if it is in the hashmap, and if it is wraps it with the associated url
            - time: O(number of words in the html)
            - space: O(n) hashmap

## Prerequisites

You will need [Leiningen][] 2.0.0 or above installed.

[leiningen]: https://github.com/technomancy/leiningen

## Running

To start a web server for the application, run:

> lein repl

Leiningen will fetch the dependencies and install them in the project directory. It can take some time :)

When the clojure repl is started, run:

> (-main)

That will start a web server on port 3001.

## License

Copyright © 2016 Arthur Caillau
