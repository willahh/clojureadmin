(ns my-webapp.handler
  (:require [my-webapp.views :as views]
            [compojure.core :refer :all]
            [compojure.route :as route]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]]
            ;; [ring.adapter.jetty :as jetty]
            )
  ;; (:gen-class)
  )

(defroutes app-routes
  (GET "/"
       []
       (views/home-page))
  (GET "/add-location"
       {params :params}
       (views/add-location-results-page params))
  (GET "/location/:loc-id"
       [loc-id]
       (views/location-page loc-id))
  (GET "/all-locations"
       []
       (views/all-locations-page))
    (GET "/add-location/save"
       {params :params}
       (views/add-location-save params))
  (route/resources "/")
  (route/not-found "Not Found"))


(def app
  ;; (wrap-defaults app-routes site-defaults)
  app-routes)

;; (defn -main
;;   [& [port]]
;;   (let [port (Integer. (or port
;;                            (System/getenv "PORT")
;;                            5000))]
;;     (jetty/run-jetty #'app {:port port
;;                             :join? false})))
