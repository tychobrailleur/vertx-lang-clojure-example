(ns vertx-lang-clojure-example.core
  (:require [io.vertx.clojure.core.vertx :as vertx]
            [io.vertx.clojure.core.vertx-options :as vertx-options])
   (:gen-class))

(defn -main [& args]
  (let [v (vertx/vertx)]
    ;; Need to figure out how to handle overloaded `deployVerticle`
    (.deployVerticle v "vertx-lang-clojure-example.websocket.clj")
    (.deployVerticle v "vertx-lang-clojure-example.http_server.clj")
    (.deployVerticle v "vertx-lang-clojure-example.net-server.clj")
    (.deployVerticle v "vertx-lang-clojure-example.sql.clj")))
