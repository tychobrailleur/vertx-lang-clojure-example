(ns vertx-lang-clojure-example.websocket
  (:require [io.vertx.clojure.core.vertx :as vertx]
            [io.vertx.clojure.core.http.http-server :as server]
            [io.vertx.clojure.ext.web.router :as vertx-router]
            [io.vertx.clojure.core.http.web-socket :as ws]
            [io.vertx.clojure.ext.web.handler.static-handler :as static-handler]))

(defn define-router [vertx]
  (let [router (vertx-router/router vertx)
        static-handler (static-handler/create)]
    (doto (.route router "/static/*")
      (.handler static-handler))
    router))

(defn create-message []
  (str (System/currentTimeMillis) " – " (rand-int 10000000)))

(defn socket-closed-handler [vertx timer-id]
  (fn [arg]
    (vertx/cancel-timer vertx timer-id)))

(defn create-request-handler [vertx]
  (fn [websocket]
    (let [timer-id (vertx/set-periodic vertx 1000
                                      (vertx/handler
                                       (fn [_id] (ws/write-text-message websocket (create-message)))))]
      (ws/end-handler websocket (vertx/handler (socket-closed-handler vertx timer-id)))
      (ws/close-handler websocket (vertx/handler (socket-closed-handler vertx timer-id))))))

(defn start [vertx]
  (let [http-server (vertx/create-http-server vertx)
        router (define-router vertx)
        fileSystem (vertx/file-system vertx)]
    (println "Start called...")
    (-> http-server
        (server/request-handler router)
        (server/web-socket-handler (vertx/handler (create-request-handler vertx)))
        (server/listen 9000))))
