(ns plywood-client.core
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require [om.core :as om :include-macros true]
            
            [om.dom :as dom :include-macros true]
            [om.dom :as dom :include-macros true]
            [cljsjs.jquery]
            [secretary.core :as secretary :include-macros true :refer-macros [defroute]]
            [cljs.core.async :refer [put! chan <!]]
            [plywood-client.router :as router] 
            [plywood-client.index :as index] 
            [plywood-client.utils.auth :as auth]))

(enable-console-print!)

; the auth-changed-channel which allows components to notify the model when
; authentication status of the app has changed
(defonce auth-changed-channel (chan))

; the app-state
(defonce app-state (atom {:auth-changed-channel auth-changed-channel
                          :credentials (auth/get-credentials)
                          :route nil }))

; function that syncs the app-state's credentials with the browser cookie state
(defn update-login-info []
  (let [credentials (auth/get-credentials)]
    (if (or (= credentials nil))
      (swap! app-state assoc :credentials nil)
      (swap! app-state assoc :credentials credentials))))

; function loop to listen on th auth-changed-channel
(defn listen-for-changes []
  (go
    (loop []
      (<! auth-changed-channel )
      (update-login-info)
      (recur))))

(defn main []
  (listen-for-changes)
  (router/route-app app-state)
  (secretary/dispatch!
    (.substring (.. js/window -location -hash) 1)) 
  (om/root
    index/index-view
    app-state
    {:target (. js/document (getElementById "app"))}))
