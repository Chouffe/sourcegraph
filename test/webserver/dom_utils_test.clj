(ns webserver.dom-utils-test
  (:require [clojure.string :as s]
            [webserver.dom-utils :refer :all]
            [clojure.test :refer :all]))

(deftest annotation
  (let [lookup-table {"alex" "hello.com" "boo" "boo.fr"}]
    (is (= (annotate lookup-table "Hello you alex") "Hello you <a href=\"hello.com\">alex</a>"))
    (is (= (annotate lookup-table "My name is alex, what is yours?") "My name is <a href=\"hello.com\">alex</a>, what is yours?"))
    (is (= (annotate lookup-table "<p>My name is alex</p>") "<p>My name is <a href=\"hello.com\">alex</a></p>"))
    (is (= (annotate lookup-table "<p>My name is <a href=\"http://doo.io\">alex</a></p>") "<p>My name is <a href=\"http://doo.io\">alex</a></p>"))))
