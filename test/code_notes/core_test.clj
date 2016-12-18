(ns code-notes.core-test
  (:require [clojure.test :refer :all]
            [code-notes.core :refer :all]
            [clojure.java.io :as io]))

(deftest extract-comment-todo-from-line-test
  (testing "just a comment"
    (is (= "fix me" (extract-comment-todo-from-line "#TODO fix me")))
    (is (= "fix me" (extract-comment-todo-from-line "# TODO fix me")))
    (is (= "fix me" (extract-comment-todo-from-line "#      TODO fix me")))
    (is (= "#fix me" (extract-comment-todo-from-line "#TODO #fix me")))
    (is (= "#fix me TODO this" (extract-comment-todo-from-line "#TODO #fix me TODO this")))
    (is (= "remove this" (extract-comment-todo-from-line "x = 10 #TODO remove this")))
    (is (= nil (extract-comment-todo-from-line "end")))
    (is (= nil (extract-comment-todo-from-line "if x = 10 && >= 5")))))


 (deftest extract-from-file-test
   (let [file-path (str (io/resource "resources/hello.rb"))]
     (is (= [{:line 1, :note "a comment"}
             {:line 2, :note "remove magic number"}
             {:line 3, :note "introduce logic here"}
             {:line 5, :note "end of program"}] (extract-from-file file-path)))))
