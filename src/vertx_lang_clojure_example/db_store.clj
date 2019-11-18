(ns vertx-lang-clojure-example.db-store
  (:require [io.vertx.lang.clojure.json :as json]
            [io.vertx.clojure.core.vertx :as vertx]
            [io.vertx.clojure.ext.jdbc.jdbc-client :as jdbc-client]
            [io.vertx.clojure.ext.sql.sql-client :as sql-client]
            [io.vertx.clojure.ext.sql.sql-connection :as sql-conn]))


(defn init-database [client vertx]
  (sql-client/get-connection ; JDBCClient is a subclass of SQLClient
   client
   (sql-client/handler
    (fn [conn]
      (let [c (.result conn)]
        (.execute
         c
         "create table test(id int primary key, name varchar(255))"
         (sql-conn/handler
          (fn [rs]
            (if (.failed rs)
              (println "Create failed " (.cause rs))
              (.execute
               c
               "insert into test values (1, 'Hello')"
               (sql-conn/handler
                (fn [i]
                  (if (.failed rs)
                    (println "Insert failed " (.cause rs))
                    (.query
                     c
                     "select * from test"
                     (sql-conn/handler
                      (fn [s]
                        (let [results (.result s)]
                          (doseq [r (.getResults results)]
                            (println (.encode r)))
                          (.close c))))))))))))))))))

(defn start [vertx]
  (let [config (-> (json/new-json-object)
                   (json/put "url" "jdbc:h2:mem:demo;DB_CLOSE_DELAY=-1")
                   (json/put "driver_class" "org.h2.Driver")
                   (json/put "username" "sa")
                   (json/put "password" "")
                   (json/put "max_pool_size" 30))
        client (jdbc-client/create-shared vertx config)]
    (init-database client vertx)))
