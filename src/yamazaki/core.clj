(ns yamazaki.core
	(:use clojure.java.io)
	(:require [yamazaki.box :as box])
	(:require [ontodev.excel :as xls]))

(def WORKBOOK "test/yamazaki.xlsx")
(def OUTPUT "output")

(defn parse-lines[]
	(iterator-seq (.rowIterator (.getSheetAt (xls/load-workbook WORKBOOK) 0))))

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

(defn export-line[api line]
	(let [ 
		metadata (process-line line)
		directory-name (str OUTPUT "/" (metadata :directory))
		directory (as-file directory-name)
		files (metadata :files)
		]
		(println "-")
		(println "Downloading to Folder" directory-name)
		(.mkdir directory)
		(doseq [file files]
			(println "Downloading file:" file)
			(download-file api file directory-name)
			)))

(defn -main[& args]
	(println "Exporting:" (first args))
	(.mkdir (as-file OUTPUT))
	(def lines (parse-lines))
	(def api (box/init-api))
	(doseq [row (range 1 36)]
		(export-line api (nth lines row))))