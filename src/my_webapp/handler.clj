(ns my-webapp.handler
  (:require [my-webapp.views :as views]
            [compojure.core :refer :all]
            [compojure.route :as route]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]]))

(defroutes app-routes
  (GET "/"
       []
       (views/home-page))
  (GET "/add-location"
       {params :params}
       (views/add-location-results-page params))
  (POST "/add-location"
        {params :params}
        (views/add-location-results-page params))
  (GET "/location/:loc-id"
       [loc-id]
       (views/location-page loc-id))
  (GET "/all-locations"
       []
       (views/all-location-page))
  (route/resources "/")
  (route/not-found "Not Found"))


(def app
  (wrap-defaults app-routes site-defaults))
