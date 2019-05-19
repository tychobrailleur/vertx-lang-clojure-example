(ns vertx-lang-clojure-example.http-server
  (:require [io.vertx.clojure.core.vertx :as vertx]
            [io.vertx.clojure.core.http.http-server :as server]
            [io.vertx.clojure.core.http.http-server-request :as request]
            [io.vertx.clojure.core.http.http-server-response :as response]))

(defn handle-request [req]
  (let [response (request/response req)]
    (-> response
        (response/put-header "content-type" "text/plain")
        (response/end "Hello from Vert.x!"))))

(defn start [vertx]
  (let [http-server (vertx/create-http-server vertx)]
    (println "Start called...")
    (-> http-server
        (server/request-handler (vertx/handler handle-request))
        (server/listen 8080))))
