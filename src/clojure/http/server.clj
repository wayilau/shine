(ns http.server
  (:use ring.adapter.jetty)
  (:require [compojure.core :refer :all]
            [compojure.route :as route]))

(defn handler
  [request]
  {:status  200
   :headers {"Content-Type" "text/html"}
   :body    "Hello World"})


(defroutes app

           (GET "/" [] "<h1>HelloWorld</h1>")
           (route/not-found "<h1>Page not found</h1>"))

(defn -main
  [& args]
  (run-jetty handler {:port 8080}))
