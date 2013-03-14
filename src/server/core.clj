; Simple HTTP-server with routing
; Johan Astborg 2013

(ns server.core)
(import '[java.io BufferedReader InputStreamReader OutputStreamWriter PrintWriter])
(use 'server.gen)
(use 'clojure.contrib.server-socket)
(use '[clojure.contrib.str-utils2 :only (split)])

; main
(defn -main
  [& args]
  (println "Server started..."))

; pages

(def indexpage (str 
    (html 
      (head 
        (title "HTML-gen using Clojure")
      ) 
    (body 
      (h1 "Welcome to HTML-gen using Clojure"))
      (p "Very nice page")
      (ahref "/my" "My page")
      (footer
        (hr)
        (small "Start page")
      )
    )
  )
)

(def mypage (str 
    (html 
      (head 
        (title "HTML-gen using Clojure")
      ) 
    (body 
      (h1 "My page"))
      (ahref "/" "Back to start")
      (p "This is some very exhaustive description of this page.")
      (p "More info will certainly come later...")
      (footer
        (hr)
        (small "My page")
      )
    )
  )
)

; routes
(def routes {"/" indexpage "/my" mypage})

; parses req

(defn parse-req [line]
  (let [preq (zipmap
    [:method :path :protocol]
    (split line #"\s"))]
    (routes (str (preq :path))))
  )

(defn rl []
  (read-line))

(defn process-request [] (parse-req (apply str (interpose "" (seq (rl))))))  

; starts server on port 8080
(create-server 8080 
  (fn [in out]
    (binding
      [*in* (BufferedReader. (InputStreamReader. in))
       *out* (PrintWriter. out)]
      (println "HTTP/1.0 200 OK")
      (println "Content-Type: text/html")
      (println "")
      (println (process-request))
      (flush))))