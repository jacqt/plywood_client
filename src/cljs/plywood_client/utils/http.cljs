(ns plywood-client.utils.http
  (:require [plywood-client.utils.auth :as auth]
            [goog.uri.utils :as uri-utils]
            [goog.net.XhrIo :as net-xhrio]))

(enable-console-print!)

(def BASE_URL "http://api.notifsta.com/v1")
(def LOGIN_URL (str BASE_URL "/auth/login"))

; parses goog.net.XhrIo response to a json
(defn parse-xhrio-response [success-callback fail-callback]
  (fn [response]
    (let [target (aget response "target")]
      (if (.isSuccess target) 
        (let [json (.getResponseJson target)]
          (success-callback (js->clj json :keywordize-keys true)))
        (let [error (.getLastError target)]
          (fail-callback (js->clj error :keywordize-keys true)))))))

; wraps goog.net.XhrIo library in a simpler function xhr
(defn xhr [http-method base-url url-params success-callback fail-callback]
  (.send
    goog.net.XhrIo
    (reduce 
      (fn [partial-url param-key]
        (.appendParams
          goog.uri.utils
          partial-url
          param-key
          (url-params param-key)))
      base-url
      (keys url-params))
    (parse-xhrio-response success-callback fail-callback)
    http-method))

(defn login [facebook-id facebook-token callback]
  (xhr
    "GET"
    LOGIN_URL
    {"facebook_id" facebook-id, "facebook_token" facebook-token}
    (fn [response]
      (let [data (response :data)]
        (auth/set-credentials
          {:facebook-id facebook-id,
           :auth-token facebook-token})
        (callback true)))
    (fn [error]
      (println "[LOG] Failed to login or signup"))))
