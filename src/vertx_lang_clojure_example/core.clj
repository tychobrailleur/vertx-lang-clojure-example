(ns vertx-lang-clojure-example.core
  (:require [io.vertx.clojure.core.vertx :as vertx]
            [io.vertx.clojure.core.future :as future]
            [io.vertx.lang.clojure.verticle :as verticle]
            [io.vertx.clojure.core.vertx-options :as vertx-options])
  (:gen-class))

(def my-handler
  (verticle/completion-handler println))


(defn -main [& args]
  (let [v (vertx/vertx)]
    (vertx/deploy-verticle v "vertx-lang-clojure-example.websocket.clj" my-handler)
    (vertx/deploy-verticle v "vertx-lang-clojure-example.http_server.clj" my-handler)
    (vertx/deploy-verticle v "vertx-lang-clojure-example.net_server.clj" my-handler)
    (vertx/deploy-verticle v "vertx-lang-clojure-example.db_store.clj" my-handler)))
