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
  "Return an fn which first do (map f events)
   and will then return the newest event
   if every value in result vector is true.

   Ignore the nil event and nil metric."
  [f]
  (fn [events]
    (when-let [e (some identity events)]
      (let [metrics (folds/non-nil-metrics events)]
        (when (seq metrics)
          (when (every? true? (map f metrics))
            (last events)))))))

(defn- any-fold
  "similar to all-fold, but return the newest
   event if any value in result vector is true."
  [f]
  (fn [events]
    (when-let [e (some identity events)]
      (let [metrics (folds/non-nil-metrics events)]
        (when (seq metrics)
          (when (some true? (map f metrics))
            (last events)))))))

(defn all
  "falcon all(#n)"
  [n f & children]
  (moving-event-window n
    (apply smap (all-fold f) children)))

(defn non-nil-metric-events
  "Given a sequence of events, returns a compact sequence of the new events
   --that is, omits any events which are nil, or have nil metrics."
  [events]
  (keep (fn [event]
          (when-not (nil? event)
            (if (:metric event)
              event)))
        events))

(defn events-diff
  "接受事件的数组，变换成另外一个事件数组。
   新的事件数组中，每一个事件的 metric 是之前相邻两个事件 metric 的差。
   如果你的事件是一个一直增长的计数器，那么用这个流可以将它变成每次实际增长的值。"
  [evs]
  (let [events (non-nil-metric-events evs)]
    (map
      (fn [[ev' ev]] (assoc ev' :metric (- (:metric ev') (:metric ev)) :prev-metric (:metric ev')))
        (map vector (rest events) events))))

(defn events-pdiff
  "similar to events-diff, but divide to event metric to calculate
   the percentage of diff"
  [evs]
  (let [events (non-nil-metric-events evs)]
    (map
      (fn [[ev' ev]] (assoc ev' :metric (/ (- (:metric ev') (:metric ev)) (:metric ev')) :prev-metric (:metric ev')))
        (filter
          (fn [[ev' ev]] (not= (:metric ev') 0))
            (map vector (rest events) events)))))

(defn diff
  "similar to falcon diff(#n), but implemented as consecutive diff"
  [n f & children]
  (moving-event-window n
    (apply smap (comp (any-fold f) events-diff) children)))

(defn pdiff
  "similar to falcon pdiff(#n), but implemented as consecutive pdiff"
  [n f & children]
  (moving-event-window n
    (apply smap (comp (any-fold f) events-pdiff) children)))

(defn query-hostgroup
  "Use query to find if there are any hostgroups current event belongs to.

  HOSTGROUP_INFO event describes hostgroup/host as :service/:tags field
  => { :service \"hostgroup2\",
       :tags [\"owl-docker\", \"hosta\"]
       :description \"HOSTGROUP_INFO\" }"
  [e]
       ; Use host as argument to construct the expression of query
       ; Use syntax quoting and unquote tilde
  (->> `(and (= :description "HOSTGROUP_INFO")
                     (:tagged ~(:host e)))
       ; Search the current Riemann core's index for any matching events
       ; These events are HOSTGROUP_INFO events.
        (riemann.index/search (:index @core))
       ; Filter the events to retrieve the hostgroup info.
       ; hostgroup info stays in :service field of events.
       ; Return the vector of hostgroup
        (mapv (juxt :service :description))))
