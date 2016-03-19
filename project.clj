(defproject webserver "0.1.0"
  :description "SourceGraph Challenge"
  :min-lein-version "2.0.0"
  :dependencies [[org.clojure/clojure "1.7.0"]
                 [compojure "1.4.0"]
                 [com.stuartsierra/component "0.3.1"]
                 [cheshire "5.5.0"]
                 [enlive "1.1.6"]
                 [ring "1.4.0"]
                 [ring-server "0.4.0"]
                 [ring/ring-json "0.3.1"]
                 [ring/ring-jetty-adapter "1.2.1"]
                 [ring/ring-defaults "0.1.5"]
                 [clj-time "0.11.0"]]
  :plugins [[lein-ring "0.9.7"]]
  :ring {:handler webserver.handler/app}
  :main webserver.handler
  :profiles
  {:dev {:dependencies [[javax.servlet/servlet-api "2.5"]
                        [ring/ring-mock "0.3.0"]]}})
