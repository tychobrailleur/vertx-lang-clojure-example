(defproject vertx-lang-clojure-example "0.1.0-SNAPSHOT"
  :description "Vert.x Clojure Example"
  :url "https://github.com/tychobrailleur/vertx-lang-clojure-example"
  :license {:name "Apache License 2.0"
            :url "https://www.apache.org/licenses/LICENSE-2.0.txt"}
  :dependencies [[org.clojure/clojure "1.10.0"]
                 [io.vertx/vertx-web "4.0.0-SNAPSHOT"]
                 [io.vertx/vertx-config "4.0.0-SNAPSHOT"]
                 [io.vertx/vertx-jdbc-client "4.0.0-SNAPSHOT"]
                 [io.vertx/vertx-sql-common "4.0.0-SNAPSHOT"]
                 [org.slf4j/slf4j-api "1.7.28"]
                 [com.h2database/h2 "1.4.200"]
                 [ch.qos.logback/logback-classic "1.2.3"]
                 [com.w2v4/vertx-lang-clojure "4.0.0-SNAPSHOT"]]
  :aot [vertx-lang-clojure-example.core]
  :env-vars [".env-vars"]
  :plugins [[lein-with-env-vars "0.2.0"]]
  :main vertx-lang-clojure-example.core)
