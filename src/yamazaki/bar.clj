(ns yamazaki.bar
	(:import [org.skife.terminal ProgressBar Label Percentage Height]))

; https://github.com/brianm/java-progress-bar/blob/master/src/main/java/org/skife/terminal/ProgressBar.java

(defn make-progress-bar[label size fromBottom]
	(ProgressBar. 
		(Label/create label size)
		(Height/fromBottom fromBottom)
	 	(Percentage/show)))

(defn update-progress[bar percentage]
	(.get (.render (.progress bar (double percentage)))))

(defn update-label[bar label size]
	(.setLabel bar (Label/create label size)))