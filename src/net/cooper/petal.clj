(ns net.cooper.petal
  (:require [clojure.spec.alpha :as s]))

(s/def :petal/type #{:petal/hidden :petal/visible})
(s/def :petal/size (s/and int? #(>= % 0) #(<= % 100)))
(s/def :petal/rate (s/and int? pos?))
(s/def :petal/countdown (s/and int? #(>= % 0)))

(defmulti petal-type :petal/type)
(defmethod petal-type :petal/hidden [_]
  (s/keys :req [:petal/type :petal/countdown]))
(defmethod petal-type :petal/visible [_]
  (s/keys :req [:petal/type :petal/size :petal/rate]))

(s/def :petal/petal (s/multi-spec petal-type :petal/type))
(s/def :petal/petal-hidden (s/and :petal/petal #(= (:petal/type %) :petal/hidden)))
(s/def :petal/petal-visible (s/and :petal/petal #(= (:petal/type %) :petal/visible)))

(s/fdef advance-petal-visible
  :args (s/cat :petal :petal/petal-visible)
  :ret :petal/petal
  :fn #(if (zero? (get-in (:args %) [:petal :petal/size]))
         (= (:petal/type (:ret %)) :petal/hidden)
         (= (:petal/type (:ret %)) :petal/visible)))

(s/fdef advance-petal-hidden
  :args (s/cat :petal :petal/petal-hidden)
  :ret :petal/petal
  :fn #(if (zero? (get-in (:args %) [:petal :petal/countdown]))
         (= (:petal/type (:ret %)) :petal/visible)
         (= (:petal/type (:ret %)) :petal/hidden)))

(defmulti advance-petal
  "Progress the petal forward in time.

    If visible, shrink the petal by its shrinking rate. If invisible,
    tick the countdown timer by one.  If the relevant visible/hidden
    petals values reach a zero value or lower (size/countdown
    respectively) then flip them into the alternating state with
    randomized new starting conditions.

    Arguments:
    - petal to advance
    Return:
    - petal having moved forward in time, including flipping state."
  :petal/type)

(defn advance-petal-hidden
  [{:keys [petal/countdown] :as petal}]
  (if (zero? countdown)
    #:petal{:type :petal/visible :size 100 :rate 10}
    (assoc petal :petal/countdown (dec countdown))))

(defmethod advance-petal :petal/hidden
  [petal]
  (advance-petal-hidden petal))

(defn advance-petal-visible
  [{:keys [petal/size petal/rate] :as petal}]
  (if (zero? size)
    #:petal{:type :petal/hidden :countdown 100}
    (assoc petal :petal/size (max 0 (- size rate)))))

(defmethod advance-petal :petal/visible
  [petal]
  (advance-petal-visible petal))
