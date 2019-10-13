(ns vertx-lang-clojure-example.http-server
  (:require ;; [clojure.tools.logging :as log]
            [io.vertx.lang.clojure.json :as json]
            [io.vertx.lang.clojure.logging :as logging]
            [io.vertx.clojure.core.vertx :as vertx]
            [io.vertx.clojure.core.http.http-server :as server]
            [io.vertx.clojure.core.http.http-server-request :as request]
            [io.vertx.clojure.core.http.http-server-response :as response]
            [io.vertx.clojure.config.config-store-options :as config-store-options]
            [io.vertx.clojure.config.config-retriever :as config-retriever]
            [io.vertx.clojure.config.config-retriever-options :as config-retriever-options]))

(def logger (logging/create-logger "http-server"))

(defn handle-request [req]
  (let [response (request/response req)]
    (-> response
        (response/put-header "content-type" "text/plain")
        (response/end "Hello from Vert.x!"))))

(defn get-options [vertx]
  (let [store-options (config-store-options/new-instance)
        sys-options (config-store-options/new-instance)
        retriever-options (config-retriever-options/new-instance)]
    (-> store-options
        (config-store-options/set-type "file")
        (config-store-options/set-config (-> (json/new-json-object)
                                             (json/put "path" "config.json"))))
    (-> sys-options
        (config-store-options/set-type "sys"))
    (-> retriever-options
        (config-retriever-options/add-store store-options)
        (config-retriever-options/add-store sys-options))
    (config-retriever/create vertx retriever-options)))

(defn start [vertx]
  (let [http-server (vertx/create-http-server vertx)
        options-retriever (get-options vertx)]
    (logging/info logger "Start called...")
    (config-retriever/get-config
     options-retriever
     (vertx/handler
      #((if (.failed %1)
          (logging/error logger "Error")
          (let [config (.result %1)]
            (-> http-server
                (server/request-handler (vertx/handler handle-request))
                (server/listen (.getInteger config "port"))))))))))
