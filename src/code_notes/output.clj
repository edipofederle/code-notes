(ns code-notes.output
  (:require [code-notes.core :as core])
  (use hiccup.core
       clojure.java.io))

(defn line-code
  [note]
  (-> note
      vals
      first
      :line))

(defn note-code
  [note]
  (-> note
      vals
      first
      :note))

(defn html-notes
  [notes]
  (for [note notes]
    [:div
     [:b (str "File: " (first (keys note)))]
     (for [comment (first (vals note))]
       [:pre (str (:line comment) " : " (:note comment))])
     [:hr]]))

(def header
  [:head
   [:link {:href "https://cdnjs.cloudflare.com/ajax/libs/bulma/0.2.3/css/bulma.min.css" :type "text/css" :rel "stylesheet"}]])

(defn generate-html
  [notes]
  (html
   header
   (html-notes notes)))

(defn output-html
  [notes path-to-save-output]
  (with-open [wrtr (writer (str path-to-save-output "/output.html") :append false)]
    (.write wrtr (generate-html notes))))
