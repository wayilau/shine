(ns cos.tencentcos
  (:import (com.qcloud.cos.auth BasicCOSCredentials)
           (com.qcloud.cos COSClient ClientConfig)
           (com.qcloud.cos.region Region)
           (com.qcloud.cos.model PutObjectRequest Bucket ListObjectsRequest ObjectListing COSObjectSummary)
           (java.io File)
           (java.util HashMap ArrayList)))


(def secredId "AKIDve0Yr9g3dLqhgP4HNDn8DUVI4xb0s7E5")
(def secretKey "rUHpgJf8MMJuFKEseATYeVyDBI9nxucT")
(def region "ap-chengdu")
(def bucket "alex-1259220172")
(def key (.toString (System/nanoTime)))

(defn build-client []
  "build client for cos, return cos client"
  (let [cred (BasicCOSCredentials. secredId secretKey)
        region (Region. region)
        clientConfig (ClientConfig. region)
        cosClient (COSClient. cred clientConfig)]
    cosClient))


(def client (build-client))

;upload file to buckets
(defn upload [path]
  "upload file to cos."
  (let [file (File. path)
        putObjectRequest (PutObjectRequest. bucket key file)]
    (.putObject ^COSClient client putObjectRequest)))

;list buckets
(defn printBucket
  [^Bucket bucket]
  (println "name:" (.getName bucket) "location:" (.getLocation bucket))
  )


(defn listBucket []
  "query list from cos object."
  (let [buckets (.listBuckets client)]
    (doseq [bucket buckets]
      (printBucket bucket))
    ))

(defn listObjects
  "list the files in a bucket. return list"
  [bucket]
  (try
    (let [request (ListObjectsRequest.)]
      (doto request
        (.setBucketName bucket)
        ;(.setPrefix "pic")
        (.setDelimiter "/")
        (.setMaxKeys (.intValue 1000)))
      (.getObjectSummaries ^ObjectListing (.listObjects client request)))
    (catch Exception e
      (.printStackTrace e)))
  )

(def result (ArrayList.))
(defn listFiles
  "list objects files."
  [objects]
  (doseq [^COSObjectSummary objectFile objects]
    (let [path (.getKey objectFile)
          etag (.getETag objectFile)
          fileSize (.getSize objectFile)
          type (.getStorageClass objectFile)
          maps (HashMap.)]
      (println "file path:" path "etag: " etag "fileSize: " fileSize "storage type: " type)
      (.put maps "path" path)
      (.put maps "etag" etag)
      (.put maps "fileSize" fileSize)
      (.put maps "type" type)
      (println maps)
      (.add result maps))
    )
  result)

(defn list
  []
  (listFiles (listObjects bucket))
  )