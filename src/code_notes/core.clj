(ns code-notes.core
  (:require [clojure.java.io :as io]
            [clojure.string :as str]))

(defn extract-comment-todo-from-line [line]
  (let [line (str/trim line)]
    (if (and (str/includes? line "TODO") (> (count (str/split line #"\s")) 1))
      (subs line (+ 5 (.indexOf line "TODO")) (count line))
      nil)))

(defn extract-from-file [file-path]
  (let [lines (str/split-lines (slurp file-path))]
    (loop [current-line 0 acc []]
      (if (>= current-line (count lines))
        acc
        (recur (inc current-line)
               (let [comment (extract-comment-todo-from-line (nth lines current-line) )]
                 (if comment
                   (conj acc {:line (inc current-line) :note comment})
                   acc)))))))

(defn regex-file-seq
  [re dir]
  (map (fn [file] (.getPath file)) (filter #(re-find re (.getPath %)) (file-seq (io/file dir)))))

(defn all-notes
  [dir-project ]
  (reduce
   (fn [acc file]
     (let [notes (extract-from-file file)]
       (if-not (empty? notes)
         (conj acc {file notes})
         acc))) [] (regex-file-seq #".*\.rb" dir-project)))
