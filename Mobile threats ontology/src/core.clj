(ns Clojure-mmaa)
(require '[clojure.xml :as xml]
         '[clojure.zip :as zip]
         '[clojure.java.io :as io])
(defn zip-str [s]  (zip/xml-zip(xml/parse (io/as-file s)) ))

;;parse from file to internal xml representation
(zip-str "mtc.xml")

(+ 2 2)
