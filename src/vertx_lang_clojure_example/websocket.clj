(ns vertx-lang-clojure-example.websocket
  (:require [io.vertx.clojure.core.vertx :as vertx]
            [io.vertx.clojure.core.http.http-server :as server]))

(defn define-router [vertx]
  (let [router (io.vertx.ext.web.Router/router vertx)
        static-handler (io.vertx.ext.web.handler.StaticHandler/create)]
    (doto
        (.route router "/static/*")
      (.handler static-handler))
    router))

(defn create-message []
  (str (System/currentTimeMillis) " – " (rand-int 10000000)))

(defn socket-closed-handler [vertx timerId]
  (fn [arg]
    (.cancelTimer vertx timerId)))

(defn create-request-handler [vertx]
  (fn [websocket]
    (let [timerId (.setPeriodic vertx 1000
                  (vertx/handler
                   (fn [id] (.writeTextMessage websocket (create-message)))))]
      (.endHandler websocket (vertx/handler (socket-closed-handler vertx timerId)))
      (.closeHandler websocket (vertx/handler (socket-closed-handler vertx timerId))))))

(defn start [vertx]
  (let [http-server (vertx/create-http-server vertx)
        router (define-router vertx)
        fileSystem (.fileSystem vertx)]
    (println "Start called...")
    (-> http-server
        (server/request-handler router)
        (server/websocket-handler (vertx/handler (create-request-handler vertx)))
        (server/listen 9000))))
