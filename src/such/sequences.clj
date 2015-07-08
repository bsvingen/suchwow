(ns such.sequences
  (require [such.wrongness :as !]))

(defn vertical-slices
  "Given N sequences, return one sequence whose first element
   is a sequence of all the first elements, etc."
  [& sequences]
  (apply (partial map (fn [& args] args)) sequences))


(defn only
  "Gives the sole element of a sequence"
  [coll]
  (if (seq (rest coll))
    (!/boom! "`%s` should have only one element." coll)
    (if (seq coll)
      (first coll)
      (!/boom! "`%s` should have only one element." coll))))

(defn +into [coll & colls]
  "The result collection is formed by `conj`ing all element of the other
   `colls` onto `coll` (in order). 
    
         (+into [] (map inc [1 2]) (map dec [-1 -2]))  => [2 3 -2 -3]

   `into` is a convenient way to coerce a number of collections into a vector.

   Note: the 1.7 version of `into` has a three argument version that takes a 
   transducer as its second argument. For that version, this is more a 
   change of domain than a widening."

  (reduce into coll colls))

(defn bifurcate
  "Apply `pred` to all elements of `coll` and return two sequences. 
   Those elements for which `pred` returns a truthy value go in the
   first sequence. `pred` is only evaluated once per element. 
   Sequences are created lazily.
    
        (bifurcate even? [1 2 3 4]) => [ [2 4] [1 3] ]
        (take 5 (first (bifurcate even? (range)))) => [0 2 4 6 8]
"
  [pred coll]
  (let [tagged (map #(vector (pred %) %) coll)
        choice (fn [in-or-out] (map second (in-or-out first tagged)))]
    (vector (choice filter) (choice remove))))
        
