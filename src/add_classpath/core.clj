(ns add-classpath.core
  (:import (java.net URLClassLoader
                     URL)
           (java.io File
                    FileNotFoundException))
  (:refer-clojure :exclude [add-classpath]))

(let [add-url (.getDeclaredMethod URLClassLoader
                                  "addURL"
                                  (into-array [URL]))]
  (.setAccessible add-url true)
  (defn add-classpath [file]
    (.invoke add-url
             (ClassLoader/getSystemClassLoader)
             (into-array [(.toURL (.toURI (new File file)))]))))
