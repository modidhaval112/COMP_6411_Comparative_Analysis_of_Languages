(require '[clojure.string :as str])

 (defn treasure1 []
   (with-open [rdr (clojure.java.io/reader "map.txt")]
   (reduce conj [] (line-seq rdr))))
(def vec1 (treasure1))

(defn isSafe [inputArray x y length height]

  (def value (aget inputArray x y))
  (if ( and (>= x 0) (< x length) (>= y 0) (< y height) (or (= value "-") (= value "@")) )
   true
   false)
)

(defn solveMazeUtil [inputArray length height x y solArray visitedArray]
     

  (def flag1 false)
  (def flag2 false)
  (def flag3 false)
  (def flag4 false)
  (def returnValue (isSafe inputArray x y length height))
  (def outputArray (aclone inputArray))
  (def mazeValue (aget inputArray x y))
  (if(and (= returnValue true) (= mazeValue "@"))
    (do

      (aset solArray x y 5)

      (def x length)
      (def y height)

      (def b (aclone solArray))
      
      (doseq [[value row] (map vector b (range))]
      (doseq [[value1 column] (map vector value (range))]
        (def tempValue (aget b row column))
        (def outputArrayValue (aget outputArray row column))
        (if(= outputArrayValue "-")
          (do
            (if (= tempValue 1)
              (aset outputArray row column "+")
            )
            (if (= tempValue 2)
              (aset outputArray row column "!")
            )
          )
        )

      )
      )
      (println)
      (println "Woo hoo, I found the treasure :-)")
      (println)
    
      (doseq [[value row] (map vector outputArray (range))]
      (doseq [[value1 column] (map vector value (range))]
        (print (aget outputArray row column))
      )
      (println)
      )
      (println)

      (java.lang.System/exit 0)


    )
    (do
      (if(and (= returnValue true))
        (do
          (aset solArray x y 1)
          (aset visitedArray x y 1)

          (def plusx (inc x))
          (if( and (< plusx length) (not= (aget visitedArray plusx y) 1) (= (solveMazeUtil inputArray length height plusx y solArray visitedArray) true))
          (do
            (def flag2 true)
            true
          )
          )



          (def plusy (inc y))
          (if( and (< plusy height) (not= (aget visitedArray x plusy) 1) (= (solveMazeUtil inputArray length height x plusy solArray visitedArray) true))
          (do
            (def flag1 true)
            true
          )
          )

          

          (def decx (dec x))
          (if( and (>= decx 0) (not= (aget visitedArray decx y) 1) (= (solveMazeUtil inputArray length height decx y solArray visitedArray) true))
          (do
            (def flag3 true)
            true
          )
          )

          (def decy (dec y))
          (if( and (>= decy 0) (not= (aget visitedArray x decy) 1) (= (solveMazeUtil inputArray length height x decy solArray visitedArray) true))
          (do
            (def flag4 true)
            true
          )
          )        

          (if(and (= flag1 false) (= flag2 false) (= flag3 false) (= flag4 false))
          (do
            (aset solArray x y 2)
            false
          ))

        )
        false
      )
    )
  )
)


(defn solveMaze [inputArray]
  (def length (alength inputArray))
  (def height (alength (aget inputArray 0)))
  (def solArray (make-array Long/TYPE length height))
  (def visitedArray (make-array Long/TYPE length height))
  (def value (solveMazeUtil inputArray length height 0 0 solArray visitedArray))

  (if (= value false)
  		(do
  				(println)

  				(def outputArray (aclone inputArray))
						(doseq [[value row] (map vector solArray (range))]
      (doseq [[value1 column] (map vector value (range))]
        (def tempValue (aget solArray row column))
        (def outputArrayValue (aget outputArray row column))
        (if(= outputArrayValue "-")
          (do
            (if (= tempValue 1)
              (aset outputArray row column "+")
            )
            (if (= tempValue 2)
              (aset outputArray row column "!")
            )
          )
        )

      )
      )

      (println "Uh oh, I could not find the treasure :-(")
      (println)

      (doseq [[value row] (map vector outputArray (range))]
      (doseq [[value1 column] (map vector value (range))]
        (print (aget outputArray row column))
      )
      (println)
      )
						
						(println)

  		)
  )

 )


 (defn Treasure []
 	(def mapVector 
  (mapv #(str/split % #"") vec1))

  (def inputArray (to-array-2d mapVector))
  
		(doseq [[value row] (map vector inputArray (range))]
      (if (not= row 0)
      		(do
      				(def length (alength (aget inputArray row)))
      				(def preLength (alength (aget inputArray (dec row))))
      				(if (not= length preLength)
      								(do 
      											(println)
      											(println "Please provide valid map.txt file")
      											(println)
      											(java.lang.System/exit 0)
      								)
      				)
      			)
      )
  )

  (println)
  (println "This is my challange ")
  (println)

  (doseq [[value row] (map vector inputArray (range))]
  (doseq [[value1 column] (map vector value (range))]
     (print (aget inputArray row column))
  )
  (println)
  )
  (println)

  (def length (alength inputArray))
  (def height (alength (aget inputArray 0)))

  (solveMaze inputArray)
)
(Treasure)