(ns add-classpath.core
  (:import (java.net URLClassLoader
                     URL)
           (java.nio.file FileSystems
                          Files)
           (java.io File))
  (:refer-clojure :exclude [add-classpath]))

(let [add-url (.getDeclaredMethod URLClassLoader
                                  "addURL"
                                  (into-array [URL]))]
  (.setAccessible add-url true)
  (defn add-classpath [& globs]
    ;; Can we also put file-system outside of the defn?
    (let [file-system (FileSystems/getDefault)]
      (doseq [glob globs]
        (let [file (.getAbsoluteFile (File. glob))]
          (let [jars (Files/newDirectoryStream
                      (.getPath file-system
                                (.getParent file)
                                (make-array String 0))
                      (.getName file))]
            (doseq [jar jars]
              (.invoke add-url
                       (ClassLoader/getSystemClassLoader)
                       (into-array [(.toURL (.toUri jar))])))))))))
