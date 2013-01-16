(defproject yamazaki "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  ; :repositories [
  ;   ["dropbox" "http://getdropbox.com/developers/"]
  ; ]
  :main yamazaki.core          
  :dependencies [
  	[org.clojure/clojure "1.4.0"]
  	[ontodev/excel "0.2.0"]

  	; install locally first
	[com.dropbox.sdk/dropbox-client "1.5.3"]
  ])