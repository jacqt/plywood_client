(ns plywood-client.components.login-signup
  (:require [om.core :as om :include-macros true]
            [om.dom :as dom :include-macros true]
            [cljs.core.async :refer [put! chan <!]]
            [plywood-client.utils.http :as http]))

(defn login-to-facebook [auth-changed-channel]
  (js/FB.login
    (fn [facebook-response]
      (js/FB.api
        "/me"
        (fn [facebook-profile]
          (http/login
            (.. facebook-response -authResponse -userID)
            (.. facebook-response -authResponse -accessToken)
            (fn [status]
              (put! auth-changed-channel status))))))
    #js{"scope" "email"}))

(defn login-signup-view [{:keys [auth-changed-channel]} owner]
  (reify
    om/IRenderState
    (render-state [this _]
      (dom/div
        #js {:className "login-panel"}
        (dom/div
          #js {:className "login-component"}
          (dom/button
            #js {:onClick (fn [e] (login-to-facebook auth-changed-channel))
                 :className "ui primary button" }
            (dom/i
              #js {:className "facebook icon"})
            "Login or signup with Facebook"))))))
