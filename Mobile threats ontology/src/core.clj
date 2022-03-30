(ns Clojure-mmaa
  (:use [tawny owl pattern util])
  (:require [tawny.owl :refer :all]
            [tawny.english]
            [tawny.reasoner :as r]))
  (require '[clojure.xml :as xml]
           '[clojure.java.io :as io]
           '[clojure.zip :as zip])

(defn zip-str [s]  ((xml/parse (io/as-file s)) ))
(defn zip-str [s]
  (zip/xml-zip
   (xml/parse (io/as-file s))))

(def x  (zip-str "mtc-data.xml")  )
;; => #'Clojure-mmaa/x(x)

; function to filter the provided tag in the argument of = function
(defn threat-product? [node]
  (some-> node :tag (= :Threat)))

(defontology threats
:comment "An ontology for mobile threat Catalogue (MTC), which describes, identifies, and structures the threats posed to mobile information systems.")

;;OWL CLASSES

(defclass Threat
  :comment "The threat is a potential negative action or event facilitated by a vulnerability that results in an unwanted impact on a computer system, application and mobile devices.")
(defclass ThreatCategory)

(deftier ThreatCategory 
   [
   Application 
   Authentication 
   Cellular 
   Ecosystem 
   EMM
   GPS 
   LAN&PAN 
   Payment 
   PhysicalAccess 
   Privacy 
   Stack 
   SupplyChain 
   ]
  :functional false
)

(def claz-names


(->>"mtc-dat.xml" ; using thread last macro to pass the result of each function to next function
     io/resource
     io/file ;input xml will be read
     xml/parse ; xml wil be parse
     xml-seq ;result of xml/parse will be parse to xml-seq to arrange it in sequence
     (filter threat-product?) ;the result of xml-seq will be passed to our defined function which will filter the tag
     (mapcat :content))) ;store the content of the filtered tag and display it

(defn createclass [cls]
(intern-owl-string cls 
(owl-class cls
:comment " to creat classes for threats")))

(createclass  "eman" )
(createclass (first claz-names))
 (map createclass claz-names)


(defn threatID-product? [node]
  (some-> node :tag (= :ThreatID)))

(->>"mtc-dat.xml"
     io/resource
     io/file
     xml/parse
     xml-seq
     (filter threatID-product?)
     (mapcat  :content) )

(defn threatC-product? [node]
  (some-> node :tag (= :ThreatCategory)))

(->>"mtc-dat.xml"
     io/resource
     io/file
     xml/parse
     xml-seq
     (filter threatC-product?)
     (mapcat  :content) )

(defn threatCVE-product? [node]
  (some-> node :tag (= :CVEExamples)))

(->>"mtc-dat.xml"
     io/resource
     io/file
     xml/parse
     xml-seq
     (filter threatCVE-product?)
     (mapcat  :content) )


(save-ontology "ontology.omn" :omn)
(save-ontology "ontology2.owl" :owl)

;;parse from file to internal xml representation
;; (def x  (zip-str "mtc-data.xml")  )

;; => #'Clojure-mmaa/x
;; (nth x)

;(count x)
;(print x)


;;; 
;; (-> x
    ;zip/down
    ;zip/down
    ;zip/down  
    ;; zip/node)

 ;; (-> x
      ;; zip/down
  ;    zip/down
      ;; zip/right 
 ;     zip/node
   ;
      ;; )

 ;
 ;; (-> x
;      zip/down
      ;; zip/down
      ;; zip/node)

 ;; (-> x
      ;; zip/down
      ;; zip/right     
      ;; zip/node)



;; (def class-name (-> x zip/down ))
;; (def class-id (-> class-name zip/down))







;
;; (defcalss Application
  ;; :annotation (id ))

;; (defn node-tag? [node]
  ;; (some-> node :tag (= :Threat)))

;; (x)
;; (->>  "mtc-data.xml"
      ;; io/file
      ;; xml/parse
      ;; xml-seq
      ;; (node-tag?)
      ;; (mapcat :content)

;; )
