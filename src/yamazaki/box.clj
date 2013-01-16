(ns yamazaki.box
	(:import [com.dropbox.client2.session WebAuthSession AccessTokenPair AppKeyPair Session])
	)

(def appkey ["imo5iffx3z2sxoa" "mxx8d0htzviayvi"])
(def TIMEOUT 5000)

(defn authenticate[]
	(let 
	[
	 keypair (AppKeyPair. (first appkey) (second appkey))
	 session (WebAuthSession. keypair com.dropbox.client2.session.Session$AccessType/DROPBOX)
	 info (.getAuthInfo session)
	 url (.url info)
	]
	; user visit URL
	(.browse (java.awt.Desktop/getDesktop) (java.net.URI. (.url info)))
	(Thread/sleep TIMEOUT)
	; ask for auth
	(.retrieveWebAccessToken session (.requestTokenPair info))	
	(.getAccessTokenPair session)
	(com.dropbox.client2.DropboxAPI. session)
	
	))

; WebAuthSession session = new WebAuthSession(state.appKey, WebAuthSession.AccessType.APP_FOLDER);
; session.setAccessTokenPair(state.accessToken);
; DropboxAPI<?> client = new DropboxAPI<WebAuthSession>(session);

; -> "57022796"
; get tokens


(def tokens ["m3nb6ig9u2yajof" "nf908rsg156hfz6"])
; "ym76ducq2seyht5", secret="7zm7oqijw163qqx"}>
; (spit "state.json" tokens)

(def api (com.dropbox.client2.DropboxAPI. session))

(.search (com.dropbox.client2.DropboxAPI. session) "/" "*.jpg" 0 false)

; file:///Users/niko/Downloads/dropbox-java-sdk-1.5.3/docs/index.html