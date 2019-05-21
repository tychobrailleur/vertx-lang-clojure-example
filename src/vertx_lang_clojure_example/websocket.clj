(ns vertx-lang-clojure-example.websocket
  (:require [io.vertx.clojure.core.vertx :as vertx]
            [io.vertx.clojure.core.http.http-server :as server]
            [io.vertx.clojure.core.http.http-server-request :as request]
            [io.vertx.clojure.core.http.http-server-response :as response]))

(defn define-router [vertx]
  (let [router (io.vertx.ext.web.Router/router vertx)
        static-handler (io.vertx.ext.web.handler.StaticHandler/create)]
    (doto
        (.route router "/static/*")
      (.handler static-handler))
    router))

(defn create-message []
  (str (System/currentTimeMillis) " – " (rand-int 10000000)))


(defn handle-request [websocket]
  ;; FIXME pass existing Vert.x context instead of creating a new one.
  ;; FIXME handle case when websocket is closed.
  (let [vertx (vertx/vertx)]
    (.setPeriodic vertx 1000
                  (vertx/handler
                   (fn [id] (.writeTextMessage websocket (create-message)))))))

(defn start [vertx]
  (let [http-server (vertx/create-http-server vertx)
        router (define-router vertx)
        fileSystem (.fileSystem vertx)]
    (println "Start called...")
    (-> http-server
        (server/request-handler router)
        (server/websocket-handler (vertx/handler handle-request))
        (server/listen 9000))))
