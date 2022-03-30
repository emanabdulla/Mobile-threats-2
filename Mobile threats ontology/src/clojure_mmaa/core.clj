(ns Clojure-mmaa
  (:use [tawny owl pattern util])
  (:require [tawny.owl :refer :all]
            [tawny.english]
            [tawny.reasoner :as r]))

  (require '[clojure.xml :as xml]
           '[clojure.java.io :as io]
           '[clojure.zip :as zip]
           '[clojure.data.zip.xml :as zip-xml]
           '[clojure.data.zip :as zf]
           '[clojure.string :as str])


;; define the ontology
(defontology threats
:comment "An ontology for mobile threat Catalogue (MTC), which describes, identifies, and structures the threats posed to mobile information systems.")

;;OWL CLASSES

(defclass Threat
  :comment "The threat is a potential negative action or event facilitated by a vulnerability that results in an unwanted impact on a computer system, application and mobile devices.")
(defclass ThreatCategory)

;; define Categories
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

;;; annotation properties

(defaproperty Id)
(def id (annotator id))
(defaproperty Description)
(def Description  (annotator Description))
(defaproperty CVEExamples)
(def CVEExamples (annotator CVEExamples))

;; function to parse Xml
(def xmlData (-> "mtc-datt.xml" io/file xml/parse zip/xml-zip))

;; another functions to parse XML by extract different content

;; (def xf (zip-xml/xml-> xmlData :row :Threat zip-xml/text ))
 ;; xf
; (def x)
 ;x
; (defclass symbol (first xf))

;(def xmlzipper (clojure.zip/xml-zip (clojure.xml/parse "mtc-datt.xml")))
;(clojure.zip/children xmlzipper)
;; (defn get-names [doc]
 
  ;; (->> (zip-xml/xml-> doc :row)
      
       ;; (map (juxt #(zip-xml/xml-> % :row zf/children  zip-xml/text)))))
 
;; (def xmld1
;; (get-names xmlData))
;; (get-names xmlData)

 ;; (->> (get-in xmlData [""])  
 ;; (map #(str "demo-data.xml" (get % "ThreatID") "/" (get % "Threat"))))

;; (def xmld  (for [m (zip-xml/xml-> xmlData :row zf/children)]
;; [(keyword :content) (zip-xml/text m)]))

;;different function to parse
(defn zip-str [s]  ((xml/parse (io/as-file s)) ))
(defn zip-str [s]
  (zip/xml-zip
   (xml/parse (io/as-file s))))
(def x  (zip-str "mtc-data.xml")  )

;; => #'Clojure-mmaa/x(x)

; function to filter the provided tag in the argument of = function
(defn threat-product? [node]
  (some-> node :tag (= :Threat)))

(defn row-product? [node]
  (some-> node :tag (= :row)))
(def x

(->>"mtc-dat.xml" ; using thread last macro to pass the result of each function to next function
     io/resource
     io/file ;input xml will be read
     xml/parse ; xml wil be parse
     xml-seq ;result of xml/parse will be parse to xml-seq to arrange it in sequence
     (filter row-product?) ;the result of xml-seq will be passed to our defined function which will filter the tag
     (mapcat :content))) ;store the content of the filtered tag and display it

(def claz-names

(->>"mtc-dat.xml" ; using thread last macro to pass the result of each function to next function
     io/resource
     io/file ;input xml will be read
     xml/parse ; xml wil be parse
     xml-seq ;result of xml/parse will be parse to xml-seq to arrange it in sequence
     (filter threat-product?) ;the result of xml-seq will be passed to our defined function which will filter the tag
     (mapcat :content))) ;store the content of the filtered tag and display it

;; define function to create classes 
(defn createclass [cls]
(intern-owl-string cls 
(owl-class cls
:comment " to creat classes for threats")))
(createclass  "eman" )
(createclass (first claz-names))
 (map createclass claz-names)

; function to filter the provided tag in the argument of = function
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

(def construct-class?
0)
(loop [node] in x 


(if node.tag== "threatID"
id=node.content
(if construct-class == 1 
(creatclass [id category threat])
construct-class = 0
)


construct-class =1)
(elseif node.tag== "threatCategory"
category=node.content)
((elseif node.tag== "threat"
threat=node.content)))

(if construct-class == 1 
(creatclass [id category threat])
construct-class = 0)

 
