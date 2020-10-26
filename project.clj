(defproject shine "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url  "https://www.eclipse.org/legal/epl-2.0/"}
  :source-paths ["src/clojure"]
  :java-source-paths ["src/java"]
  :dependencies [[org.clojure/clojure "1.10.0"]
                 [io.netty/netty-all "4.1.42.Final"]
                 [cheshire "5.9.0"]
                 ["io.forward/yaml" "1.0.9"]
                 ["mysql/mysql-connector-java" "8.0.19"]
                 [ring "1.8.1"]
                 [ring/ring-defaults "0.3.2"]
                 [compojure "1.6.1"]
                 [com.qcloud/cos_api "5.6.24"]
                 [liberator "0.15.1"]
                 [com.google.code.gson/gson "2.8.6"]
                 ]
  ;:main ^:skip-aot shine.core
  :target-path "target/%s"
  :plugins [[lein-ring "0.7.1"]]
  :ring {:handler cos.http/app}
  :profiles {:uberjar {:aot :all}})
