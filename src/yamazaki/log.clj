(ns yamazaki.log
	(:use [taoensso.timbre :as timbre])
	(:require [com.postspectacular.rotor :as rotor]))

(timbre/set-config! [:appenders] {})
(timbre/set-config!
  [:appenders :rotor]
  {:doc "Writes to to (:path (:rotor :shared-appender-config)) file
         and creates optional backups."
   :min-level :info
   :enabled? true
   :async? false ; should be always false for rotor
   :max-message-per-msecs nil
   :fn rotor/append})

(timbre/set-config!
  [:shared-appender-config :rotor]
  {:path "download.log" :max-size (* 512 1024) :backlog 5})