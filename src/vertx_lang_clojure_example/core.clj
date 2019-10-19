(ns vertx-lang-clojure-example.core
  (:require [io.vertx.clojure.core.vertx :as vertx])
   (:gen-class))

(defn -main [& args]
  (let [v (vertx/vertx)]
    (vertx/deploy-verticle v "vertx-lang-clojure-example.websocket.clj")
    (vertx/deploy-verticle v "vertx-lang-clojure-example.http_server.clj")
    (vertx/deploy-verticle v "vertx-lang-clojure-example.net-server.clj")))
