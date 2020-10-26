(ns cos.server
  (:use ring.adapter.jetty)
  (:use cos.tencentcos)
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [ring.middleware.defaults :refer :all]
            [ring.middleware.params :refer [wrap-params]]
            )
  (:import (com.google.gson Gson)))

(defroutes app
           (GET "/" [] "<h1>HelloWorld</h1>")
           (POST "/upload" [] (upload "/Users/francis/Desktop/media/15552948359170/test.jpg"))
           (GET "/list" []
             (let [gson (Gson.)]
               (.toJson gson (cos.tencentcos/list))))
           (GET "/listObject" [] (cos.tencentcos/listBucket))
           (route/not-found "<h1>Page Not Found</h1>"))

(def handler
  (-> app
      wrap-params))

(defn -main
  [& args]
  (run-jetty app {:port 9993}))
