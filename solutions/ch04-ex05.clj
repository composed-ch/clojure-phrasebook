(defn parse-number [input]
  (try
    (Integer/parseInt input)
    (catch NumberFormatException e
      (ex-info (str "cannot parse '" input "' as an integer")
               {:input input}))))

