(defproject havaswwdigital "1.2"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :main yamazaki.core          
  :dependencies [
  	[org.clojure/clojure "1.4.0"]
    
  	[ontodev/excel "0.2.0"]

  	; install locally first
	  [com.dropbox.sdk/dropbox-client "1.5.3"]

    ; command line progress bar
    [org.skife.terminal/java-progress-bar "0.0.4"]

    ; logging
    [com.taoensso/timbre "1.2.0"]
    [com.postspectacular/rotor "0.1.0"]
  ])