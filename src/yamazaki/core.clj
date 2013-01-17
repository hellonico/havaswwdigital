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

; execute
(defn export-line[line func]

	)
; (doseq [i (range 1 17)] (println i))

; -> D&E, F&G, H&I, J&K, L&M, N&O, P&Q 
; 1 line = 1 directory 

(defn debug-lines[line]
	(doseq [i (range 16)] (println i (.getCell line i))))

(defn get-file-pair[line ind]
	{:org (.getStringCellValue (.getCell line ind))
	 :new (.getStringCellValue (.getCell line (+ 1 ind)))
	})

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
	(box/get-one-file 
		api 
		(str (file :org))
		(str directory-name "/" (file :new))))

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
	(.mkdir (as-file OUTPUT))
	(def lines (parse-lines (first args)))
	(def api (box/init-api))
	(def bar1 (bar/make-progress-bar "Total: " 20 3))
	(doseq [row (range 1 36)]
		(bar/update-progress bar1 (/ row 36))
		(export-line api (nth lines row))))