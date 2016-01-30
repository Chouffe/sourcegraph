(ns webserver.dom-utils
  (:require [clojure.string :as s]
            [net.cgrand.enlive-html :as html]))

;; HTML parser
(def ^:private parse html/html-snippet)

;; DOM -> HTML String
(defn- render [& nodes]
  (s/join "" (apply html/emit* nodes)))

;; The following property holds (comp render parse) = identity

(defn- wrap-in-hyperlink [node hyperlink]
  ((html/wrap :a {:href hyperlink}) node))

(defn- annotate-string-with [lookup-table]
  (fn [string]
    (assert (string? string))
    (->> string
         (re-seq #"\w+|[^\w+]")
         (map (fn [word] (if-let [url (lookup-table word)] (wrap-in-hyperlink word url) word)))
         flatten)))

(defn- annotate-dom
  "Given a lookup table and a DOM (tree like structure), it performs the annotation on   the nodes that are not children of any <a> tags."
  [lookup-table dom]
  (cond
    (nil? dom)        nil
    (string? dom)     ((annotate-string-with lookup-table) dom)
    ;; recursively applies annotate dom on the sequence
    (sequential? dom) (->> dom
                           (remove nil?)
                           (map (partial annotate-dom lookup-table))
                           flatten)
    ;; Filtering out <a> tags
    :else (let [{:keys [tag content]} dom]
            (if (= tag :a)
              dom
              (update dom :content #(annotate-dom lookup-table %))))))

(defn annotate
  "Main function that annotates an html string with a lookup-table.

   Main steps:
   - Parses the html string
   - Annotates the DOM
   - Renders as a string"
  [lookup-table string]
  (->> string
       parse
       (annotate-dom lookup-table)
       render))
