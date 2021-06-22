(ns vertx-lang-clojure-example.net-server
  (:require [clojure.string :as str]
            [io.vertx.clojure.core.vertx :as vertx]
            [io.vertx.clojure.core.net.net-server :as net-server]
            [io.vertx.clojure.core.net.net-socket :as net-socket]
            [io.vertx.clojure.core.net.net-server-options :as net-server-options]))


(defn create-server-options [port]
  (let [server-options  (net-server-options/new-instance)]
    (-> server-options
        (net-server-options/set-port port))
    server-options))

(defn start [vertx]
  (let [server-options (create-server-options 10000)
        server (vertx/create-net-server vertx server-options)]

    (-> server
        (net-server/connect-handler
         (vertx/handler (fn [socket]
                          (.handler socket
                                    (vertx/handler
                                     (fn [buf] (println (str/trim (.toString buf)))))))))
        (net-server/listen (vertx/handler (fn [s] (println s)))))))
