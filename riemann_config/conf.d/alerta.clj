; -*- mode: clojure; -*-
; vim: filetype=clojure
(ns alerta
  (:require [clj-http.client :as client]
            [riemann.streams :refer :all]
            [riemann.config :as c]
            [cheshire.core :as json]))


(def version "3.1.0")

(def alerta-endpoints
    {:alert "http://10.20.30.40:8081/api/alert"
     :heartbeat "http://10.20.30.40:8081/api/heartbeat"
     :apikey "nskKwHlzEIuTejKqcYKT7-IB_owdw7Dd8sGhXceA"})


(defn post-to-alerta
  "POST to the Alerta REST API."
  [url request]
  (let [event-url url
    event-json (json/generate-string request)]
    (client/post event-url
               {:body event-json
        ;       :headers {"Authorization" (str "Key " (get alerta-endpoints :apikey))}
                :body-encoding "UTF-8"
                :socket-timeout 5000
                :conn-timeout 5000
                :content-type :json
                :accept :json
        ; :debug true
        ; :debug-body true
                :throw-entire-message? true})))

(def hostname (.getHostName (java.net.InetAddress/getLocalHost)))

(defn hostgroup2contact
  [e]
  (:tags (riemann.index/lookup (:index @c/core) (first (:hostgroup e)) "CONTACT_INFO")))

(defn add-contact
  [e]
  (if-let [contact (hostgroup2contact e)]
    (assoc e :contact contact)
    e))

(defn format-alerta-event
  "Formats an event for Alerta."
  [event]
  {
   :origin (str "riemann/" hostname)
   :resource
    (if (.contains (:service event) " ")
      (let [[_ instance] (clojure.string/split (:service event) #" " 2)]
        (str (:host event) ":" instance))
        (:host event))
   :event (get event :event (:service event))
   :group (get event :group "Performance")
   :value (get event :metric)
   :severity (:state event)
   :environment (get event :environment "Production")
   :service [(get event :grid "Platform")]
   :tags (if (empty? (:tags event)) [] (:tags event))
   :text (:description event)
   :rawData event})

(defn alerta
  "Creates an alerta adapter.
    (changed-state (alerta))"
  [e]
  (post-to-alerta (:alert alerta-endpoints) (format-alerta-event (add-contact e))))

(defn heartbeat [e] (post-to-alerta
    (:heartbeat alerta-endpoints)
    {:origin (str "riemann/" hostname)
       :tags [version]
       :type "Heartbeat"}))

(defn severity
    [severity message & children]
    (fn [e] ((apply with {:state severity :description message} children) e)))

