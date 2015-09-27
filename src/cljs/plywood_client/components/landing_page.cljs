(ns plywood-client.components.landing-page
  (:require [om.core :as om :include-macros true]
            [om.dom :as dom :include-macros true]
            [cljs.core.async :refer [put! chan <!]]
            [plywood-client.components.login-signup :as login-signup]))

(defn page-content-view [{:keys [auth-changed-channel]} this]
  (reify om/IRenderState
    (render-state [this _]
      (dom/div
        #js {:className "pusher"}
        (dom/div
          #js {:className "ui inverted vertical masthead center aligned segment"}
          (dom/div
            #js {:className "ui container"}
            (dom/div
              #js {:className "ui large secondary inverted pointing menu"}
              (dom/a
                #js {:className "active item"}
                "Home")
              (dom/a
                #js {:className "item"}
                "Work")
              (dom/a
                #js {:className "item"}
                "Company")
              (dom/div
                #js {:className "right item"}
                (dom/a
                  #js {:className "ui inverted button"}
                  "Log In")
                (dom/a
                  #js {:className "ui inverted button"}
                  "Sign Up"))))

          (dom/div
            #js {:className "ui text container"}
            (dom/h1
              #js {:className "ui inverted header"}
              "Your Title!")
            (dom/h2
              #js {}
              "Your wonderful amazing message goes here!")
            (om/build
              login-signup/login-signup-view
              {:auth-changed-channel auth-changed-channel}))
          )
        )
      )
    )
  )

(defn landing-page-view [{:keys [auth-changed-channel]} owner]
  (reify
    om/IRenderState
    (render-state [this _]
      (dom/div
        #js {}
        (om/build
          page-content-view
          {:auth-changed-channel auth-changed-channel}))
      )
    )
  )
