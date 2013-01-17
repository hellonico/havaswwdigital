(ns yamazaki.box
	(:use clojure.java.io)
	(:use [taoensso.timbre :as timbre])
	(:require [yamazaki.bar :as bar])
	(:import [com.dropbox.client2.session 
				WebAuthSession 
				AccessTokenPair 
				AppKeyPair 
				Session]))

; static app key
(def appkey ["mwtv6r6p11oblfj" "ahh9fvhgnuz3xnb"])
(def STATE_FILE "keypair.clj")
(def TIMEOUT 8000)

; basic keypair
(defn get-keypair[]
	(AppKeyPair. (first appkey) (second appkey)))

; init dropbox session
(defn init-session[]
	(WebAuthSession. (get-keypair) com.dropbox.client2.session.Session$AccessType/DROPBOX))

; save keypair
(defn save-state[pair]
	(spit STATE_FILE {:key (.key pair) :secret (.secret pair)}))

; load keypair
(defn load-state[]
	(let [keypair (read-string (slurp STATE_FILE))]
	 (AccessTokenPair. (keypair :key) (keypair :secret))))

; authenticate
(defn authenticate[]
	(let [	session (init-session)
	 		info (.getAuthInfo session)
	 		url (.url info)]
	; user visit URL
	(.browse (java.awt.Desktop/getDesktop) (java.net.URI. (.url info)))
	; wait some time, hoping the user can click
	(Thread/sleep TIMEOUT)
	; ask for auth
	(.retrieveWebAccessToken session (.requestTokenPair info))
	; store session
	(save-state (.getAccessTokenPair session))
	; return session
	(com.dropbox.client2.DropboxAPI. session)))

; reuse tokens
(defn re-authenticate[]
	(let [session (init-session)]
	(.setAccessTokenPair session (load-state))
	(com.dropbox.client2.DropboxAPI. session)))

; init dropbox api
(defn init-api[]
	(if (.exists (as-file STATE_FILE))
		(re-authenticate)
		(authenticate)))

; search for a file
; use blank characters for regexp
(defn search-file[api query]
	(.search api "/" query 0 false))

(def bar3 (bar/make-progress-bar "" 20 1))

; 
(defn get-file[api entry localfilename]

	(let 
		[
		total (.bytes entry)
		counter (atom 0)
		pl (proxy [com.dropbox.client2.ProgressListener] [] 
			 (progresInterval [] 100)
             (onProgress [bytes total] 
             	(swap! counter (partial + bytes))
             	(bar/update-progress bar3 (/ bytes total))
             	))
		]
	(bar/update-label bar3 (.getName (as-file localfilename)) 20)
	(bar/update-progress bar3 (/ 0 total))

	(with-open [w (clojure.java.io/output-stream localfilename)] 
		(.getFile api 
			(.path entry)
			(.rev entry)
			w 
			pl))))

(defn get-one-file[api query localname]
	(if (.exists (as-file localname))
		(timbre/info "Skip: " localname)
		(try 
			(get-file api (first (search-file api query)) localname)
		(catch Exception e
	 		(timbre/error "Cannot download:" query " to " localname)))))