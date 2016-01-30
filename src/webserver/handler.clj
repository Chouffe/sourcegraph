(ns webserver.handler
  (:require [clojure.string :as s]
            [compojure.core :refer [defroutes GET PUT POST DELETE ANY]]
            [compojure.route :as route]
            [com.stuartsierra.component :as component]
            [ring.adapter.jetty :refer [run-jetty]]
            [ring.util.response :refer [resource-response response]]
            [ring.middleware.json :as middleware]
            [ring.middleware.params :refer [wrap-params]]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults api-defaults]]
            [cheshire.core :as json]
            [webserver.dom-utils :as domu]))

;; hashmap: name -> url
(def data (atom {}))

;; ---------
;; API utils
;; ---------

(def jsonify (comp #(str % "\n") json/generate-string))

;; ---------
;; API Logic
;; ---------

(defn delete! []
  (reset! data {}))

(defn save! [name url]
  (swap! data assoc name url))

(defn lookup [name]
  (when-let [url (get @data name)]
    {:name name :url url}))

(defn annotate [string]
  (domu/annotate @data string))

;; ------
;; Routes
;; ------

(defroutes app-routes
  (GET "/names/:name" [name] (response (some-> (lookup name) jsonify)))
  (POST "/annotate" request (response (some->> request :body slurp annotate)))
  (PUT "/names/:name" request
       (save! (get-in request [:params :name])
              (get-in request [:body "url"]))
       (response nil))
  (DELETE "/names" [] (delete!) (response nil))
  (route/not-found "Not Found"))

;; -----------
;; Middlewares
;; -----------

(def app (-> app-routes
             (middleware/wrap-json-body)
             (wrap-defaults api-defaults)
             wrap-params))

(defn -main []
  (add-watch data :updates (fn [k r o n] (println "data=> " n)))
  (run-jetty app {:port 3001}))
