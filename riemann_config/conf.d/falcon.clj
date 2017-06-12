(ns falcon
  (:require [riemann.config :refer :all]
            [riemann.streams :refer :all]
            [riemann.folds :as folds]))

(defn average
  "falcon avg(#n)"
  [n & children]
  (moving-event-window n
    (apply smap folds/mean children)))


(defn maximum
  "falcon max(#n)"
  [n & children]
  (moving-event-window n
    (apply smap folds/maximum children)))


(defn- all-fold
  "return an fn which do f for all events"
  [f]
  (fn [events]
    (when-let [e (some identity events)]
      (let [metrics (folds/non-nil-metrics events)]
        (when (seq metrics)
          (when (every? true? (map f metrics))
            (last events)))))))
           

(defn all
  "falcon all(#n)"
  [n f & children]
  (moving-event-window n
    (apply smap (all-fold f) children)))
