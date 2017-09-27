(ns my-webapp.views
  (:require [my-webapp.db :as db]
            [clojure.string :as str]
            [hiccup.page :as page]
            [hiccup.core :as core]
            [ring.util.anti-forgery :as util]))

(defn gen-page-head
  [title]
  [:head
   [:title (str "Locations: " title)]
   (page/include-css "/lib/bootstrap-4.0.0-beta-dist/css/bootstrap.min.css")
   (page/include-js "/lib/jquery-3.2.1/jquery-3.2.1.min.js") 
   (page/include-js "/lib/popper/popper.min.js") 
   (page/include-js "/lib/bootstrap-4.0.0-beta-dist/js/bootstrap.min.js") 
   (page/include-css "/css/styles.css")])


(def header-links
  [:div#header-links
   "[ "
   [:a {:href "/"} "Home"]
   " | "
   [:a {:href "/add-location"} "Add a Location"]
   " | "
   [:a {:href "/all-locations"} "View All Locations"]
   " ]"])

(defn main-navbar
  []
  [:nav {:class "navbar navbar-expand-lg navbar-light bg-light"}
   [:a.navbar-brand {:href "#"}]
   [:button.navbar-toggler {:type "button" :data-toggle "collapse" :data-target "#navbarSupportedContent"}
    [:span.navbar-toggler-icon]]
   [:div#navbarSupportedContent {:class "collapse navbar-collapse"}
    [:ul {:class "navbar-nav mr-auto"}
     [:li {:class "nav-item active"}
      [:a.nav-link {:href "/"} "Home" [:span.sr-only "(current)"]]]
     [:li {:class "nav-item"}
      [:a.nav-link {:href "/add-location"} "Add location"]]
     [:li {:class "nav-item"}
      [:a {:class "nav-link" :href "/all-locations"} "All locations"]]
     ]
    [:form {:class "form-inline my-2 my-lg-0"}
     [:input {:class "form-control mr-sm-2" :type "text" :placeholder "Search"}]
     [:button {:class "btn btn-outline-succes my-2 my-sm-0" :type "submit"} "Submit"]]]])

(defn home-page
  []
  (page/html5
   (gen-page-head "Home")
   (main-navbar)
   [:div.container
    [:h1 "Home"]
    [:p "Webapp to store and display some 2D (x,y) locations."]]))

(defn add-location-page
  []
  (page/html5
   (gen-page-head "Add a Location")
   (main-navbar)
   [:h1 "Add a Location"]
   [:form {:action "/add-location" :method "POST"}
    (util/anti-forgery-field)
    [:p "x value: " [:input {:type "text" :name "x"}]]
    [:p "y value: " [:input {:type "text" :name "y"}]]
    [:p [:input {:type "submit" :value "submit location"}]]]))

(defn add-location-results-page
  [{:keys [x y]}]
  (let [id (db/add-location-to-db x y)]
    (page/html5
     (gen-page-head "Added a Location")
     (main-navbar)
     [:h1 "Added a Location"]
     [:p "Added [" x ", " y "] (id: " id ") to the db. "
      [:a {:href (str "/location/" id)} "See for yourself"]
      "."])))

(defn location-page
  [loc-id]
  (let [
        {x :x y :y} (db/get-xy loc-id)
        row (first (db/get-location-from-id loc-id))
        ]
    (page/html5
     (gen-page-head (str "Location " loc-id))
     (main-navbar)
     [:table {:class "table"}
      [:thead
       [:tr
        [:th "id"]
        [:th "x"]
        [:th "y"]]]
      [:tbody
       [:tr
        [:th loc-id]
        [:th "a"]
        [:th "y"]]]]
     [:h1 "A Single Location"]
     
     [:p "id: "(:id row)]
     [:p "x: " (:x row)]
     [:p "y: " (:y row)])))

(defn all-locations-page
  []
  (let [all-locs (db/get-all-locations)]
    (page/html5
     (gen-page-head "All Locations in the db")
     (main-navbar)
     [:div {:class "container"}
      [:h1 "All Locations"]
      [:table {:class "table table-bordered table-striped"}
       [:thead {:class "thead-inverse"} [:tr [:th "id"] [:th "x"] [:th "y"]]]
       (for [loc all-locs]
         [:tr [:td (:id loc)] [:td (:x loc)] [:td (:y loc)]])]])))


