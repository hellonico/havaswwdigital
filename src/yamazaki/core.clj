(ns yamazaki.core
	(:require [ontodev.excel :as xls]))

(def workbook (xls/load-workbook "test/yamazaki.xlsx"))
(def sheet0 (.getSheetAt workbook 0))
(def lines (iterator-seq (.rowIterator sheet0)))
(count lines)

; base folder 
(def dropbox-url "https://www.dropbox.com/sh/o3vi0b9kikq3we6/4uHNal-oh2")

; sample url
; https://www.dropbox.com/sh/o3vi0b9kikq3we6/X95YNRbPgM/JP/Saiko_Osara/12-03-MON#f:2012-12-02%2019.22.31.jpg

; http://poi.apache.org/apidocs/org/apache/poi/xssf/usermodel/XSSFRow.html

; execute
(defn export-line[line func]

	)
(doseq [i (range 1 17)] (println i))

; -> D&E, F&G, H&I, J&K, L&M, N&O, P&Q 
; 1 line = 1 directory 

(defn debug-lines[line]
	(doseq [i (range 16)] (println i (.getCell line i))))

(defn process-line[line]
	{ :directory  			(.getStringCellValue (.getCell line 1))
	  

	})

;  (.getStringCellValue (.getCell (nth lines 3) 4))
;  (doseq [i (range 1 17)] (println i))
;  (nth lines 10)

(defn -main[& args]
	(println "Exporting:" (first args)))