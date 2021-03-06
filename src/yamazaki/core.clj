(ns yamazaki.core
	(:gen-class :main true)
	(:use clojure.java.io)
	(:require 
		[yamazaki.box :as box]
		[yamazaki.log :as log]
		[yamazaki.bar :as bar]
		[ontodev.excel :as xls]))

(def OUTPUT "output")

(defn parse-lines[workbook]
	(iterator-seq (.rowIterator (.getSheetAt (xls/load-workbook workbook) 0))))

(defn debug-lines[line]
	(doseq [i (range 16)] (println i (.getCell line i))))

(defn get-file-pair[line ind]
	(let [
		org-v (.getStringCellValue (.getCell line ind))
		new-v (.getStringCellValue (.getCell line (+ 1 ind)))
	]
	{:org org-v
	 :new (if (empty? new-v) org-v new-v)
	}))

; refactor this to use a rename mode, and also a start column
; make this method as a parameter
(defn process-line[line]
	{ :directory  			(.getStringCellValue (.getCell line 1))
	  :files [
	  (get-file-pair line 3)
	  (get-file-pair line 5)
	  (get-file-pair line 7)
	  (get-file-pair line 9)
	  (get-file-pair line 11)
	  (get-file-pair line 13)
	  ]
	})

(defn download-file[api file directory-name]
	(if (not (empty? (file :org)))
		(box/get-one-file 
		 api 
		 (str (file :org))
		 (str directory-name "/" (file :new)))))

(def bar2 (bar/make-progress-bar "" 20 2))

(defn export-line[api line]
	(let [ 
		metadata (process-line line)
		directory-name (str OUTPUT "/" (metadata :directory))
		directory (as-file directory-name)
		files (metadata :files)
		total (count files)
		]

		(bar/update-label bar2 directory-name 20)
		(bar/update-progress bar2 (/ 0 total))

		(.mkdir directory)

		(loop [files files i 0]
			(when (seq files)
			  (download-file api (first files) directory-name)
			  (bar/update-progress bar2 (/ i total))
			  (recur (rest files) (inc i))))
			))

(defn -main[& args]
	(println "Exporting:" (first args))
	; make sure output directory exist
	(.mkdir (as-file OUTPUT))

	(def lines (parse-lines (first args)))
	(def total-lines (inc (count lines)) )
	(def api (box/init-api))
	(def bar1 (bar/make-progress-bar "Total: " 20 3))
	(doseq [row (range 1 total-lines)]
		(bar/update-progress bar1 (/ row total-lines))
		(export-line api (nth lines row))))